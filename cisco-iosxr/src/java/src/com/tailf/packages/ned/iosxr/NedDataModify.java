package com.tailf.packages.ned.iosxr;

import java.util.List;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.tailf.ned.NedWorker;
import com.tailf.ned.NedException;
import com.tailf.ned.NedExpectResult;

import com.tailf.conf.ConfPath;
import com.tailf.ned.CliSession;

/**
 * Utility class for restoring sets and interfaces addresses on XR device
 *
 * @author lbang
 * @version 20161111
 */

@SuppressWarnings("deprecation")
public class NedDataModify {

    /*
     * Local data
     */
    private NedWorker worker;
    boolean trace;
    private String device_id;
    private boolean showVerbose;
    private CliSession session;

    private final static String[] lists;
    private final static String privexec_prompt;
    static {

        // start of input, > 0 non-# and ' ', one #, >= 0 ' ', eol
        privexec_prompt = "\\A[^\\# ]+#[ ]?$";

        lists = new String[] {
            "rd-set ",
            "prefix-set ",
            "as-path-set ",
            "community-set ",
            "extcommunity-set rt ",
            "extcommunity-set soo "
        };
    }

    /**
     * Constructor
     */
    NedDataModify(CliSession session, NedWorker worker, boolean trace, String device_id, boolean showVerbose) {
        this.session     = session;
        this.worker      = worker;
        this.trace       = trace;
        this.device_id   = device_id;
        this.showVerbose = showVerbose;
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
     * getListSets
     */
    private ArrayList<String> getListSets(String list)
        throws NedException {
        String line = "show run " + list;
        String result;
        ArrayList<String> sets = new ArrayList<String>();

        // Send line and wait for echo and result
        try {
            session.print(line + "\n");
            session.expect(new String[] { Pattern.quote(line) }, worker);
            result = session.expect(privexec_prompt, worker);
        } catch (Exception e) {
            throw new NedException("NedDataModify(getlist) - ERROR : failed to show '"+list+"' : "+e.getMessage());
        }

        // Get start and end of set
        int start = result.indexOf("\n"+list);
        if (start < 0)
            return sets;
        int nl = result.indexOf("\n", start + 1);
        if (nl < 0)
            return sets;
        int end = result.indexOf("\nend-set");
        if (end < 0)
            return sets;

        // Trim list to keep only sets
        String trimmed = result.substring(nl, end).trim();
        if (trimmed.isEmpty())
            return sets;

        // Add sets and trim commas (on all except the last set)
        String lines[] = trimmed.split("\n");
        for (int n = 0; n < lines.length; n++) {
            String set = lines[n].trim();
            if (n + 1 < lines.length)
                set = set.substring(0, set.length() - 1);
            sets.add(set);
        }

        return sets;
    }


    /*
     * modifySets
     *
     * @param data - config data from applyConfig, before commit
     * @return config data modified after modifying sets
     */
    public String modifySets(String data)
        throws NedException {
        int j, s;

        String lines[] = data.split("\n");
        StringBuilder newdata = new StringBuilder();

        for (int n = 0; n < lines.length; n++) {
            if (lines[n].trim().isEmpty())
                continue;

            // Check if one of the lists
            for (int list = 0; list < lists.length; list++) {
                if (lines[n].startsWith(lists[list])) {
                    String setLine = lines[n++];

                    // Add current line, the set name
                    newdata.append(setLine+"\n");

                    // Get running sets
                    ArrayList<String> sets = getListSets(setLine);
                    //traceVerbose("SET "+setLine);
                    //for (s = 0; s < sets.size(); s++) traceVerbose("set["+s+"]=" + sets.get(s));

                    // Apply transaction to create new sets
                    for (; n < lines.length; n++) {
                        String trimmed = lines[n].trim();
                        if (trimmed.equals("end-set"))
                            break;
                        if (trimmed.startsWith("no ")) {
                            sets.remove(trimmed.substring(3));
                        } else {
                            sets.add(trimmed);
                        }
                    }

                    if (sets.size() > 0) {
                        traceInfo("PATCH: restoring " + setLine);
                    }

                    // Add (back) sets, strip "" around ios-regex lines
                    for (s = 0; s < sets.size(); s++) {
                        String value = sets.get(s);
                        if (value.indexOf("ios-regex \"") >= 0) {
                            value = value.replaceAll("ios-regex \\\"(.*)\\\"", "ios-regex $1");
                        }
                        if (s + 1 < sets.size())
                            newdata.append(value+",\n");
                        else
                            newdata.append(value+"\n");
                    }

                    // Note: end-set is added outside outer for-loop
                    break;
                }
            }

            newdata.append(lines[n]+"\n");
        }

        data = newdata.toString();
        return "\n" + data + "\n";
    }


    private boolean isTopExit(String line) {
        if (line.startsWith("exit"))
            return true;
        if (line.startsWith("!") && line.trim().equals("!"))
            return true;
        return false;
    }


    private ArrayList<String> getIfAddresses(String toptag)
        throws NedException {
        String line = "show run " + toptag + " | i address";
        String result;
        ArrayList<String> addresses = new ArrayList<String>();

        // Send line and wait for echo and result
        try {
            session.print(line + "\n");
            session.expect(new String[] { Pattern.quote(line) }, worker);
            result = session.expect(privexec_prompt, worker);
        } catch (Exception e) {
            throw new NedException("NedDataModify(getIfAddresses) - ERROR : failed to show interface : " + e.getMessage());
        }

        // Add ipv4 and ipv6 addresses
        String lines[] = result.trim().split("\n");
        for (int n = 0; n < lines.length; n++) {
            if (lines[n].startsWith(" ipv4 address "))
                addresses.add(lines[n].trim());
            if (lines[n].startsWith(" ipv6 address "))
                addresses.add(lines[n].trim());
        }

        return addresses;
    }


    /*
     * vrfForwardingRestore
     *
     * @param data - config data from applyConfig, before commit
     * @return config data modified after modifying sets
     */
    public String vrfForwardingRestore(String data)
        throws NedException {
        int j;

        String lines[] = data.split("\n");
        StringBuilder newdata = new StringBuilder();
        String toptag = "";

        for (int n = 0; n < lines.length; n++) {
            int exit = -1;
            String trimmed = lines[n].trim();
            if (trimmed.isEmpty())
                continue;
            if (isTopExit(lines[n])) {
                toptag = "";
            } else if (Character.isLetter(lines[n].charAt(0))) {
                toptag = lines[n].trim();
            }

            // VRF modified - re-inject addresses after modification
            if (toptag.startsWith("interface ")
                && (trimmed.startsWith("vrf ") || trimmed.startsWith("no vrf "))) {

                // Get running interface addresses
                ArrayList<String> addresses = getIfAddresses(toptag);
                //traceVerbose("ADDRESSES "+toptag+":");
                //for (j = 0; j < addresses.size(); j++) traceVerbose(" ["+j+"]="+addresses.get(j));

                // Apply transaction and remove deleted addresses
                for (j = n; j < lines.length; j++) {
                    if (isTopExit(lines[j]))
                        break;
                    String line = lines[j].trim();
                    if (line.startsWith("no ipv4 address") || line.startsWith("no ipv6 address")) {
                        addresses.remove(line.substring(3));
                    }
                }

                if (addresses.size() > 0) {
                    traceInfo("PATCH: vrf modified, restoring "+addresses.size()+" IP address(es) on: " + toptag);
                }

                // Re-inject addresses after the vrf line
                newdata.append(lines[n++]+"\n");
                int numV4 = 0;
                int numV6 = 0;
                for (j = 0; j < addresses.size(); j++) {
                    String address = addresses.get(j);
                    if (address.startsWith("ipv4 ") && numV4++ == 0)
                        newdata.append(" no ipv4 address\n");
                    if (address.startsWith("ipv6 ") && numV6++ == 0)
                        newdata.append(" no ipv6 address\n");
                    newdata.append(" " + address + "\n");
                }
            }

            // Add line (may be empty due to stripped deleted address)
            newdata.append(lines[n]+"\n");
        }

        data = newdata.toString();
        return "\n" + data + "\n";
    }
}
