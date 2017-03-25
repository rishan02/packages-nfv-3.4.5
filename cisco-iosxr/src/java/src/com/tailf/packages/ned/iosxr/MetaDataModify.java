package com.tailf.packages.ned.iosxr;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.tailf.ned.NedWorker;
import com.tailf.ned.NedException;

import com.tailf.conf.ConfPath;


/**
 * Utility class for modifying config data based on YANG model meta data provided by NCS.
 * WARNING: Code does not handle multiple meta-data tags other than diff-interface-move [TODO]
 *
 * @author lbang
 * @version 20161108
 */

public class MetaDataModify {

    /*
     * Local data
     */
    private NedWorker worker;
    private String device_id;
    private String model;
    private boolean isNetsim;
    private boolean trace;
    private boolean showVerbose;
    private boolean autoVrfForwardingRestore;

    /**
     * Constructor
     */
    MetaDataModify(NedWorker worker, String device_id, String model,
                   boolean trace,
                   boolean showVerbose, boolean autoVrfForwardingRestore ) {
        this.worker      = worker;
        this.device_id   = device_id;
        this.model       = model;
        this.trace       = trace;
        this.showVerbose = showVerbose;
        this.autoVrfForwardingRestore = autoVrfForwardingRestore;

        this.isNetsim = model.equals("NETSIM");
    }

    /*
     * Write info in NED trace
     *
     * @param info - log string
     */
    private void traceInfo(String info) {
        if (trace) {
            worker.trace("-- " + info + "\n", "out", device_id);
        }
    }

    /*
     * Write info in NED trace if verbose output
     *
     * @param info - log string
     */
    private void traceVerbose(String info) {
        if (showVerbose && trace) {
            worker.trace("-- " + info + "\n", "out", device_id);
        }
    }

    /*
     * Modify config data based on meta-data given by NCS.
     *
     * @param data - config data from applyConfig, before commit
     * @return Config data modified after parsing !meta-data tags
     */
    public String modifyData(String data)
        throws NedException {

        int i, j, n;
        int next;

        // Make sure first line also starts and ends with \n for easier parsing
        data = "\n" + data + "\n";

        // metas[0] = ! meta-data
        // metas[1] = path
        // metas[2] = annotation name
        // metas[3..N] = meta value(s)
        //
        // Supported data annotations:
        //    max-values
        //
        // Supported line annotations:
        //    add-keyword
        //    string-remove-quotes
        //    diff-interface-move-X
        //    range-list-syntax
        //    range-list-syntax-mode

        //
        // MODIFY DATA
        //
        for (i = data.indexOf("! meta-data :: /ncs:devices/device{");
             i >= 0;
             i = data.indexOf("! meta-data :: /ncs:devices/device{", next)) {
            // Note: if data buffer size is reduced, reset next to i
            next = i + 34;

            // Extract meta-data and meta-value(s), store in metas[] where:
            // metas[1] = path
            // metas[2] = annotation
            // metas[3] = first meta-value (each value separated by ' :: '
            // Extract config line, stored in line
            int nl  = data.indexOf("\n", i);
            int lineNL = data.indexOf("\n", nl+1);
            String meta = data.substring(i, nl);
            String metas[] = meta.split(" :: ");
            String line = data.substring(nl+1, lineNL);

            //
            // NETSIM
            // Strip meta-data tags not supported with NETSIM
            if (isNetsim) {
                if (metas[2].equals("add-keyword")) {
                    data = data.substring(0, i) + data.substring(nl+1);
                    next = i;
                    continue;
                }
            }

            //for (j = 0; j < metas.length; j++) traceVerbose("METAS["+j+"]='"+metas[j]+"'");
            //traceVerbose("LINE='"+line+"'");

            // max-values
            // ==========
            // Split config lines with multiple values into multiple lines with a maximum
            // number of values per line
            // metas[3] = config line
            // metas[4] = offset in values[] for first value
            // metas[5] = maximum number of values per line
            // Example:
            // tailf:meta-data "max-values" {
            //  tailf:meta-value "mls qos monitor dscp :: 4 :: 8";
            // }
            if (metas[2].equals("max-values")) {
                String values[] = line.trim().split(" +");
                //for (j = 0; j < values.length; j++) traceVerbose("VALUE["+j+"]="+values[j]);
                int offset = Integer.parseInt(metas[4]);
                if (values[0].equals("no"))
                    offset++;
                int maxValues = Integer.parseInt(metas[5]);
                if (values.length <= offset + maxValues)
                    continue;

                traceInfo("meta-data :: max-values :: PATCH: split '"+line+"'");
                String insert = "";
                for (n = offset; n < values.length; n = n + maxValues) {
                    line = "";
                    for (j = n; (j < n + maxValues) && (j < values.length); j++)
                        line += " " + values[j];
                    if (values[0].equals("no"))
                        insert += "no ";
                    insert += metas[3] + line + "\n";
                }

                data = data.substring(0, nl+1) + insert + data.substring(lineNL+1);
            }
        }


        //
        // MODIFY LINE
        //
        String lines[] = data.split("\n");
        int lastif = -1;
        for (i = 0; i < lines.length - 1; i++) {
            if (lines[i].startsWith("interface "))
                lastif = i;
            String meta = lines[i].trim();
            String line = lines[i+1];
            if (meta.startsWith("! meta-data :: /ncs:devices/device{") == false)
                continue;

            String metas[] = meta.split(" :: ");
            //for (j = 0; j < metas.length; j++) traceVerbose("LINE METAS["+j+"]='"+metas[j]+"'");

            // Strip duplicate meat-data tags
            if (meta.equals(line)) {
                lines[i] = ""; // Strip duplicate meta-data comment
                traceVerbose("meta-data :: stripped duplicate tag :: "+meta);
                continue;
            }

            // Warn for multiple meta-data tags other than diff deps
            if (line.startsWith("! meta-data :: /ncs:devices/device{")
                && (meta.indexOf("diff-interface-move") < 0 ||
                    line.indexOf("diff-interface-move") < 0)) {
                traceVerbose("meta-data :: WARNING :: double tag #1 :: "+ meta);
                traceVerbose("meta-data :: WARNING :: double tag #2 :: "+ line);
            }

            // Strip remaining MODIFY DATA tags
            if (metas[2].equals("max-values")) {
                lines[i] = ""; // Strip meta-data comment
                continue;
            }

            // add-keyword
            // ===========
            // Add 'insert' keyword if 'search' not in command line
            // metas[3] = add keyword
            // metas[4] = positive regexp
            // metas[5] = negative regexp
            // metas[6] = last word [OPTIONAL]
            // Example:
            // add 'log disable' if extended|webtype and not log set
            // tailf:meta-data "add-keyword" {
            //   tailf:meta-value "log disable :: access-list \\S+ \"(extended|webtype) .* :: .* log ::  inactive";
            // }
            else if (metas[2].startsWith("add-keyword")) {
                lines[i] = ""; // Strip meta-data comment
                if (!line.matches("^"+metas[4].trim()+"$"))
                    continue;
                if (line.matches("^"+metas[5].trim()+"$"))
                    continue;
                if (metas.length > 6 && line.endsWith(metas[6])) {
                    lines[i+1] = line.substring(0,line.length()-metas[6].length()) + " " + metas[3] + metas[6];
                } else {
                    lines[i+1] = line + " " + metas[3];
                }
                traceInfo("meta-data :: add-keyword :: PATCH: new line '"+lines[i+1]+"'");
            }

            // string-remove-quotes
            // ====================
            // metas[3] = regexp, where <STRING> is the string to look at.
            // example:
            // tailf:meta-data "string-remove-quotes" {
            //  tailf:meta-value "route-policy <STRING>";
            // }
            else if (metas[2].startsWith("string-remove-quotes")) {
                lines[i] = ""; // Strip meta-data comment
                String regexp = metas[3].replace("<STRING>", "\\\"(.*)\\\"");
                String replacement = metas[3].replace("<STRING>", "$1");
                String newline = lines[i+1].replaceFirst(regexp, replacement);
                if (lines[i+1].equals(newline) == false) {
                    lines[i+1] = newline;
                    traceInfo("meta-data :: string-remove-quotes :: PATCH: "+
                              "removed quotes on: '"+lines[i+1]+"'");
                }
            }

            // diff-interface-move
            // ===================
            //    A :: after|before :: B
            // Move line A before or after line B within interface boundaries
            // metas[3] = line A to move (regexp)
            // metas[4] = after|before
            // metas[5] = line B to stay
            // metas[6] = device regexp [OPTIONAL]
            // example:
            // tailf:meta-data "diff-interface-move-1" {
            //  tailf:meta-value "no ip route-cache :: before :: switchport";
            // }
            else if (metas[2].startsWith("diff-interface-move")) {
                int move = -1, stay = -1, exit = -1;
                boolean before = metas[4].equals("before");
                if (metas.length > 6 && !model.matches(".*"+metas[6]+".*")) {
                    traceVerbose("meta-data :: "+metas[2]+" :: model != "+metas[6]+" -> toggled move direction");
                    before = !before;
                }
                for (j = lastif; j < lines.length; j++) {
                    String cmd = lines[j].trim();
                    if (cmd.matches("^"+metas[3].trim()+"$")) {
                        move = j;
                    } else if (cmd.matches("^"+metas[5].trim()+"$")) {
                        stay = j;
                    } else if (lines[j].equals("exit")) {
                        exit = j;
                        break;
                    }
                }
                if (move == -1 || stay == -1 || exit == -1) {
                    lines[i] = ""; // Strip meta-data comment
                    continue;
                }
                if (before == true && move > stay) {
                    traceInfo("meta-data :: "+metas[2]+" :: PATCH: "+
                              "moved '"+lines[move]+"' before '"+lines[stay]+"'");
                    lines[i] = lines[move];
                    lines[move] = "";
                    continue;
                }
                if (before == false && move < stay) {
                    traceInfo("meta-data :: "+metas[2]+" :: PATCH: "+
                              "moved '"+lines[move]+"' after '"+lines[stay]+"'");
                    for (j = i; j < stay; j++)
                        lines[j] = lines[j+1];
                    lines[stay] = lines[move];
                    lines[move] = "";
                    i--; // we shifted metas up, continue on same index
                    continue;
                }
            }

            // range-list-syntax
            // range-list-syntax-mode
            // ======================
            // Compact individual entries to range syntax.
            // Also supports empty mode and list delete.
            // metas[3] = entry to look for, contains <ID> and optional $i tags
            // Example:
            // tailf:meta-data "range-list-syntax" {
            //  tailf:meta-value "spanning-tree vlan <ID> $3 $4";
            // }
            else if (metas[2].startsWith("range-list-syntax")) {
                String values[] = line.trim().split(" +");
                int delete = values[0].equals("no") ? 1 : 0; // let first line device if create/delete
                boolean modeSearch = metas[2].equals("range-list-syntax-mode") && delete == 0;

                // Create line regexp and simple first match (to minimize regexp searches)
                String regexp = metas[3];
                if (delete == 1)
                    regexp = "no " + regexp;
                String first = regexp.substring(0,regexp.indexOf(" <ID>"));
                regexp = regexp.replace("<ID>", "(\\d+)");
                if (regexp.indexOf(" $") > 0) {
                    // Replace $i with value from line
                    String tokens[] = regexp.trim().split(" +");
                    for (int x = 0; x < tokens.length; x++) {
                        if (tokens[x].startsWith("$")) {
                            int index = (int)(tokens[x].charAt(1) - '0');
                            if (index + delete < values.length)
                                regexp = regexp.replace(tokens[x],values[index+delete]);
                        }
                    }
                    if (regexp.indexOf(" $") > 0) {
                        traceInfo(metas[2] + " :: ignoring '"+line+"'");
                        continue; // unresolved values, ignore non-matching line
                    }
                }

                // Find all matching entries, including this one (to extract first low/high value)
                int low = -1, high = -1;
                traceVerbose(metas[2] + " :: searching : mode="+modeSearch+" delete="+delete+" first='"+first+"' regexp='"+regexp+"'");
                for (j = i; j < lines.length - 1; j++) {
                    if (lines[j].indexOf(metas[2]) < 0)
                        continue; // non-matching meta
                    if (modeSearch && delete == 0) {
                        if (j + 2 >= lines.length)
                            break;
                        if (!(lines[j+2].trim().equals("!") || lines[j+2].trim().equals("exit")))
                            continue;
                    }
                    if (lines[j+1].trim().startsWith(first) == false)
                        continue;
                    Pattern pattern = Pattern.compile("^\\s*"+regexp+"$");
                    Matcher matcher = pattern.matcher(lines[j+1]);
                    if (!matcher.find())
                        continue; // non-matching line (mismatching $i values)
                    int index = Integer.parseInt(matcher.group(1));
                    if (low == -1) {
                        // first entry (the command line which will be modified if range found)
                        high = index;
                        low  = index;
                    } else if (index - 1 == high) {
                        // interval increased by 1
                        high++;
                        lines[j+1] = ""; // Strip command and optional mode exit
                        if (modeSearch) lines[j+2] = "";
                    } else if (index + 1 == low) {
                        // interval decreased by 1
                        low--;
                        lines[j+1] = ""; // Strip command and optional mode exit
                        if (modeSearch) lines[j+2] = "";
                    } else {
                        break; // non linear range, end compression [NOTE: possibly a continue, to find scattered entries?]
                    }
                    lines[j] = ""; // First or expanded range match -> strip meta-data comment
                }

                // Compress single entries to span, minimum 2 entries
                if (high - low > 0) {
                    traceInfo("meta-data :: range-list-syntax :: PATCH: compressed '"+lines[i+1]+"' to range="+low+"-"+high);
                    lines[i+1] = regexp.replace("(\\d+)", low + "-" + high);
                }
            }
        }

        // Make single string again
        StringBuilder moddata = new StringBuilder();
        for (i = 0; i < lines.length; i++) {
            if (lines[i] != null
                && !lines[i].isEmpty()
                && !lines[i].trim().equals("!")) {
                moddata.append(lines[i]+"\n");
            }
        }
        data = "\n" + moddata.toString();
        traceVerbose("\nSHOW_AFTER_META:\n"+data);
        return data;
    }
}
