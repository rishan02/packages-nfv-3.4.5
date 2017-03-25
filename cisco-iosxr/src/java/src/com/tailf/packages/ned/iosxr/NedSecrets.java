/**
 * Utility class for caching cleartext secrets in oper data, in order
 * to avoid a compare-config diff when device encrypts the secret(s).
 *
 * @author lbang
 * @version 20161205
 */

package com.tailf.packages.ned.iosxr;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.tailf.ned.NedWorker;
import com.tailf.ned.NedException;

import com.tailf.conf.ConfBuf;
import com.tailf.conf.ConfPath;
import com.tailf.conf.ConfKey;

import com.tailf.maapi.Maapi;
import com.tailf.maapi.MaapiSchemas.CSNode;

import com.tailf.cdb.Cdb;
import com.tailf.cdb.CdbDBType;
import com.tailf.cdb.CdbSession;

import com.tailf.ncs.ns.Ncs;
import com.tailf.ncs.ResourceManager;
import com.tailf.ncs.annotations.Resource;
import com.tailf.ncs.annotations.ResourceType;
import com.tailf.ncs.annotations.Scope;

import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuContext;
import com.tailf.navu.NavuList;
import com.tailf.navu.NavuNode;

import org.apache.log4j.Logger;

//
// NedSecrets
//
@SuppressWarnings("deprecation")
public class NedSecrets {

    /*
     * Local data
     */
    private NedWorker worker;
    private String device_id;
    private boolean trace;
    private boolean showVerbose;
    private String SP = "^";

    @Resource(type=ResourceType.MAAPI, scope=Scope.INSTANCE)
    private  Maapi mmOper;
    @Resource(type=ResourceType.CDB, scope=Scope.INSTANCE)
    private Cdb cdb;
    private CdbSession cdbOper;

    private boolean newEntries = false;
    private static Logger LOGGER = Logger.getLogger(IosxrNedCli.class);

    /*
     * Constructor
     */
    NedSecrets(NedWorker worker, String device_id, boolean trace, boolean showVerbose)
        throws NedException {

        this.worker      = worker;
        this.device_id   = device_id;
        this.trace       = trace;
        this.showVerbose = showVerbose;

        // Register resources
        try {
            ResourceManager.registerResources(this);
        } catch (Exception e) {
            throw new NedException("SECRETS - ERROR registering resources : "+e.getMessage());
        }

        // Start CDB session
        try {
            cdbOper = cdb.startSession(CdbDBType.CDB_OPERATIONAL);
        } catch (Exception e) {
            throw new NedException("SECRETS - ERROR starting oper session : "+e.getMessage());
        }
    }

    private void traceVerbose(NedWorker worker, String info) {
        if (showVerbose && trace) {
            worker.trace("-- " + info + "\n", "out", device_id);
        }
    }

    private void traceInfo(NedWorker worker, String info) {
        if (trace)
            worker.trace("-- " + info + "\n", "out", device_id);
    }

    private void logVerbose(NedWorker worker, String info) {
        if (showVerbose) {
            logInfo(worker, info);
        }
    }

    private void logInfo (NedWorker worker, String info) {
        LOGGER.info(device_id + " " + info);
        if (trace)
            worker.trace("-- " + info + "\n", "out", device_id);
    }

    private String getMatch(String text, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find())
            return null;
        return matcher.group(1);
    }

    private boolean isClearText(String password) {
        // encrypted
        if (password.matches("[0-9a-f]{2}(:([0-9a-f]){2})+"))
            return false;   // aa:11 .. :22:bb
        if (password.trim().indexOf(" encrypted") > 0)
            return false;  // XXX encrypted
        if (password.startsWith("password "))
            return false;  // password XXX

        // cleartext
        if (password.trim().indexOf(" ") < 0)
            return true;   // XXX
        if (password.trim().charAt(0) == '0')
            return true;   // 0 XXX
        if (password.startsWith("clear "))
            return true;  // clear XXX

        // Default to encrypted
        return false;      // 5 XXX
    }


    //
    // operSetElem
    //
    private void operSetElem(NedWorker worker, String value, String path) {
        String root = path.substring(0,path.lastIndexOf("/"));
        String elem = path.substring(path.lastIndexOf("/"));
        try {
            ConfPath cp = new ConfPath("/ncs:devices/ncs:device{"+device_id+"}/ncs:ned-settings/iosxr-op:cisco-iosxr-oper/secrets{"+root+"}");
            if (!cdbOper.exists(cp))
                cdbOper.create(cp);
            cdbOper.setElem(new ConfBuf(value), cp.append(elem));
            //traceVerbose(worker, "          " + path + " = " + value);
        } catch (Exception e) {
            logInfo(worker, "SECRETS - ERROR : failed to set "+path+" :: " + e.getMessage());
        }
    }

    //
    // operDeleteList
    //
    private void operDeleteList(NedWorker worker, String path) {
        try {
            ConfPath cp = new ConfPath("/ncs:devices/ncs:device{"+device_id+"}/ncs:ned-settings/iosxr-op:cisco-iosxr-oper/secrets{"+path+"}");
            if (cdbOper.exists(cp)) {
                cdbOper.delete(cp);
            }
        } catch (Exception e) {
            logInfo(worker, "SECRETS - ERROR : failed to delete "+path+" :: "
                    + e.getMessage());
        }
    }


    //
    // isMetaDataSecret
    //
    private boolean isMetaDataSecret(String line) {
        if (!line.trim().startsWith("! meta-data :: "))
            return false;
        if (line.indexOf(" :: secret-password") < 0)
            return false;
        return true;
    }


    //
    // prepareLine
    //
    private void prepareLine(NedWorker worker, String line, String meta)
        throws NedException {
        int i;

        // Get root path for secret-password container
        String root = getMatch(meta, " meta-data :: (.*?) :: secret-password");
        if (root == null) {
            throw new NedException("Internal error : missing secrets meta-data");
        }

        // Root postfix, to be able to store multiple passwords from a single line
        String rootPostfix = getMatch(meta, " meta-data :: .*? :: secret-password-(\\S+) ");

        // Root path
        root = root.substring(0, root.lastIndexOf("/"));
        String orgroot = root;

        // Remember delete command
        boolean delete = false;
        if (line.indexOf("no ") == 0) {
            traceVerbose(worker, "SECRETS - Preparing delete : " + root);
            delete = true;
        } else {
            traceVerbose(worker, "SECRETS - Preparing create : " + root);
            traceVerbose(worker, "                      line : " + line);
        }

        // Scan path looking at meta options
        try {
            int tid = delete ? worker.getFromTransactionId() : worker.getToTransactionId();
            mmOper.attach(tid, 0);
            NavuContainer navuCtr = new NavuContainer(new NavuContext(mmOper, tid));
            for (NavuNode navu = navuCtr.getNavuNode(new ConfPath(root));
                 navu != null;
                 navu = navu.getParent()) {
                if (navu.getName().equals("config"))
                    break;
                CSNode node = navu.getInfo().getCSNode();
                String newroot = null;
                String metaValue = "unknown";

                traceVerbose(worker, "   NAVU '"+navu.getName()+"' type : " + node.printNodeType());

                if (!node.hasMetaData())
                    continue;

                // WARNING:
                // NavuNode.getName() is broken in all NCS/NSO releases.
                // {0/0} key name is returned as 0}
                // As a result, I can't use the traverse here to prepare keys

                // secret-add-mode [ANY]
                if (node.getNodeInfo().getMetaData().containsKey("secret-add-mode")) {
                    // SP is replaced with .*= in order to find sub-mode entries
                    if (root.indexOf(navu.getName()+"/") > 0)
                        newroot = root.replace(navu.getName()+"/", navu.getName()+SP);
                    else if (root.indexOf(navu.getName()+"{") > 0)
                        newroot = root.replace(navu.getName()+"{", navu.getName()+SP+"{");
                    metaValue = "secret-add-mode";
                }

                // secret-add-leaf-value [CONTAINER]
                else if (node.getNodeInfo().getMetaData().containsKey("secret-add-leaf-value")) {
                    String leaf = (String)node.getNodeInfo().getMetaData().get("secret-add-leaf-value");
                    if (node.isContainer()) {
                        String value = ((NavuContainer)navu).leaf(leaf).valueAsString();
                        // WARNING: Lazy code, replaces all matches, make sure unique path
                        newroot = root.replace("/"+navu.getName()+"/",
                                            "/"+navu.getName()+"/"+value+"/");
                        metaValue = "secret-add-leaf-value";
                    }
                }

                // secret-expose-key-name [LIST]
                else if (node.getNodeInfo().getMetaData().containsKey("secret-expose-key-name")
                         && navu.getName().indexOf("}") > 0) {
                    String key = getMatch(navu.getName(), "\\{(.*)\\}");
                    if (key == null)
                        continue;
                    String tokens[] = key.split(" +");
                    String leaf = (String)node.getNodeInfo().getMetaData().get("secret-expose-key-name");
                    int index = Integer.parseInt(leaf.substring(0,1));
                    // WARNING: Lazy code, replaces all matches of value with name value
                    if (index < tokens.length) {
                        newroot = root.replace(" "+tokens[index], " "+leaf.substring(2)+" "+tokens[index]);
                        metaValue = "secret-expose-key-name";
                    }
                }

                // secret-drop-node-name [CONTAINER | LIST]
                else if (node.getNodeInfo().getMetaData().containsKey("secret-drop-node-name")) {
                    if (node.isContainer()) {
                        newroot = root.replace("/"+navu.getName(), "");
                        metaValue = "secret-drop-node-name-container";
                    } else if (node.isList()) {
                        if (navu.getName().indexOf("{") < 0) {
                            newroot = root.replace("/"+navu.getName(), "");
                            metaValue = "secret-drop-node-name-list";
                        }
                    } else if (node.isLeaf()) {
                        logInfo(worker, "SECRETS - ERROR : secret-drop-node-name leaf : "+navu.getName());
                    } else {
                        logInfo(worker, "SECRETS - ERROR : secret-drop-node-name unknown : "+navu.getName());
                    }
                }

                if (newroot != null) {
                    traceVerbose(worker, "          Applied " + metaValue + " on '"+navu.getName()+"' type : " + node.printNodeType());
                    if (!root.equals(newroot)) {
                        traceVerbose(worker, "          root = '"+newroot+"'");
                        root = newroot;
                    } else {
                        traceVerbose(worker, "          root = UNCHANGED");
                    }
                }
            }
            mmOper.detach(tid);
        } catch (Exception e) {
            logInfo(worker, "SECRETS - ERROR : failed to get "+root+" meta-data :: " + e.getMessage());
            return;
        }

        // Trim root, stripping the absolute path
        root = root.replaceFirst("(.*)}/config/(.*)", "$2");
        //traceVerbose(worker, "          root0 = '"+root+"'");

        // Extract password from the command line. Passwords: "XXX" or "# XXX"
        String password;
        String regexp = getMatch(meta, "secret-password(?:-\\S+)? :: (.*)");
        if (regexp != null) {
            // Password has static regexp stored in meta-value sub-annotation
            for (i = regexp.indexOf("<"); i >= 0; i = regexp.indexOf("<", i+1)) {
                int end;
                if ((end = regexp.indexOf(">", i+1)) < 0)
                    continue;
                String name = regexp.substring(i+1,end);
                if (name.equals("PASSWORD"))
                    continue;
                String value = getMatch(root, ".*?"+name+"\\{(.*?)\\}");
                if (value == null) {
                    throw new NedException("Internal error : malformed secrets meta-value : regexp='"+regexp+"' root='"+root+"'");
                }
                regexp = regexp.replace("<"+name+">", value);
            }
            // Extract password from the command line using the regexp
            regexp = regexp.replace("<PASSWORD>", "((?:[0-9] )?\\S+(?: encrypted)?)");
    //IOS:: regexp = regexp.replace("<PASSWORD>", "((?:[0-9] \\S+)|(?:\\S+))");
            if ((password = getMatch(line, regexp)) == null) {
                logInfo(worker, "SECRETS - ERROR : failed to get "+root+" password");
                logInfo(worker, "            line = '"+line+"'");
                logInfo(worker, "          regexp = '"+regexp+"'");
                return;
            }
        } else {
            // Extract password from command line, using the last leaf name
            String leafname = orgroot.substring(orgroot.lastIndexOf("/")+1);
            // Search for leafname in command line, ignore the password string
            int offset = line.lastIndexOf(leafname,line.lastIndexOf(" "));
            password = line.substring(offset + leafname.length() + 1);
        }

        // Replace '/' within keys with SP to avoid being replaced with ' '
        int depth = 0;
        for (i = 0; i < root.length(); i++) {
            if (root.charAt(i) == '{')
                depth++;
            else if (root.charAt(i) == '}')
                depth--;
            else if  (root.charAt(i) == '/' && depth > 0) {
                root = root.substring(0,i)+SP+root.substring(i+1);
            }
        }

        traceVerbose(worker, "          root = '"+root+"'");

        // Create standard regexp
        if (regexp == null) {
            regexp = root.replaceAll("/[A-Za-z-]*?-list", "");
            regexp = regexp.replaceAll("[A-Za-z-]*?-conf/", "");
            regexp = regexp.replace("/", " ");
            regexp = regexp.replace("{", "[ ]?"); // optional blank due to support for join-key
            regexp = regexp.replace("} ", "\\s.*?"); // make sure not match abbrev
            regexp = regexp.replace("(", "\\(").replace(")", "\\)"); // actual brackets
            regexp = regexp.replace(SP, ".*?"); // secret-add-mode, ignore sub-mode entries
            regexp = regexp.replace("-plus", "\\+"); // + sign
            regexp = regexp+" ([ \\S]+)";
        }

        traceVerbose(worker, "          regexp = '"+regexp+"'");
        traceVerbose(worker, "          password = '" + password+"'");

        // Make root path a valid key
        root = root.replace(" ", SP); // multiple keys, can't have blank in keys
        root = root.replace("{", "(").replace("}", ")");
        if (rootPostfix != null)
            root += "-" + rootPostfix;

        // add/update or delete secrets cache
        if (delete || !isClearText(password)) {
            // Delete command OR password encrypted and no need to cache
            traceVerbose(worker, "          Deleting : " + root);
            operDeleteList(worker, root);
        }
        else
        {
            // Add/update secrets cache with cleartext pw. Clear encrypted
            traceVerbose(worker, "          Adding : "+root);
            operSetElem(worker, password, root+"/cleartext");
            operSetElem(worker, "", root+"/encrypted");
            if (regexp != null)
                operSetElem(worker, regexp, root+"/regexp");
            newEntries = true;
        }
    }


    //
    // prepare
    //
    public void prepare(NedWorker worker, String lines[])
        throws NedException {
        int i, c;

        logInfo(worker, "SECRETS PREPARING CONFIG");

        for (i = 0 ; i < lines.length - 1; i++) {
            if (!isMetaDataSecret(lines[i]))
                continue; // not a meta-data secret
            for (c = i + 1; c < lines.length; c++)
                if (!lines[c].trim().startsWith("! meta-data :: "))
                    break; // found command line
            prepareLine(worker, lines[c], lines[i]);
        }

        logInfo(worker, "DONE SECRETS PREPARING");
    }


    //
    // update
    //
    public String update(NedWorker worker, String res, boolean convert) {

        traceVerbose(worker, "SECRETS - update(convert="+convert+")");
        if (convert == false && newEntries == false) {
            traceVerbose(worker, "          ignored, checksum only");
            return res;
        }

        logInfo(worker, "SECRETS UPDATING CONFIG");

        try {
            NavuContext context = new NavuContext(cdbOper);
            NavuList secretsList = new NavuContainer(context)
                .container(Ncs.hash)
                .container(Ncs._devices_)
                .list(Ncs._device_)
                .elem(new ConfKey(new ConfBuf(device_id)))
                .container("ncs", "ned-settings")
                .container("iosxr-op", "cisco-iosxr-oper")
                .list("secrets");
            for (NavuContainer entry : secretsList.elements()) {
                String root = entry.leaf("path").valueAsString();
                String encrypted = entry.leaf("encrypted").valueAsString();
                if (convert == false && encrypted.isEmpty() == false) {
                    // Ignore updating active entries if transid checksum only
                    continue;
                }

                traceInfo(worker, "SECRETS - Checking : " + root);

                // Look for the entry in show running-config
                String regexp = entry.leaf("regexp").valueAsString();
                traceVerbose(worker, "          regexp = '"+regexp+"'");
                Pattern pattern;
                try {
                    pattern = Pattern.compile("\n"+regexp, Pattern.DOTALL);
                } catch (Exception e) {
                    traceInfo(worker, "          ERROR in '"+regexp+"' pattern :"+ e.getMessage());
                    continue;
                }
                Matcher matcher;
                try {
                    matcher = pattern.matcher(res);
                } catch (Exception e) {
                    traceInfo(worker, "          ERROR in pattern.matcher, res="+res);
                    continue;
                }

                if (matcher.find() == false) {
                    // Entry not found on device, delete cached secret
                    traceInfo(worker, "          No entry on device, deleting : "+root);
                    operDeleteList(worker, root);
                    continue;
                }

                String devicePw = matcher.group(1).trim();
                if (isClearText(devicePw)) {
                    // Device password is cleartext, no need to cache it anymore
                    traceInfo(worker, "          Device cleartext entry '"+devicePw+"', deleting : "+root);
                    operDeleteList(worker, root);
                    continue;
                }


                if (encrypted.isEmpty()) {
                    // NEW/UPDATED : Cache encrypted password for device comparison
                    traceInfo(worker, "          Device encrypted secret, caching '"+devicePw+"'");
                    operSetElem(worker, devicePw, root+"/encrypted");
                    continue;
                }

                // ACTIVE : Compare encrypted device password vs cached
                if (encrypted.equals(devicePw)) {
                    String cleartext = entry.leaf("cleartext").valueAsString();

                    // Device entry matches cached, replace with cleartext secret
                    traceInfo(worker, "          Device match, inserting cleartext secret");
                    res = res.substring(0,matcher.start(1))
                        + cleartext
                        + res.substring(matcher.start(1) + devicePw.length());

                    // DIRTY patch to strip dynamic 'encrypted' once we inserted cleartext secret
                    // snmp-server user * * v3 encrypted auth * <PASSWORD> priv * [128] <PASSWORD>
                    String line = matcher.group(0).trim().replace(devicePw, cleartext);
                    if (line.startsWith("snmp-server user ")) {
                        String stripped = line.replace(" encrypted auth ", " auth ");
                        if (!line.equals(stripped)) {
                            traceVerbose(worker, "          PATCH: stripping encrypted keyword");
                            res = res.replace(line, stripped);
                        }
                    }
                } else {
                    // Password changed on device, delete entry
                    traceInfo(worker, "          Device diff, deleting : "+root);
                    operDeleteList(worker, root);
                }
            } // for(;;)
        } catch (Exception e) {
            logInfo(worker, "SECRETS - update() ERROR :: " + e.getMessage());
        }

        logInfo(worker, "DONE SECRETS UPDATING");

        return res;
    }


    //
    // getNewEntries
    //
    public boolean getNewEntries() {
        return newEntries;
    }


    //
    // clrNewEntries
    //
    public void clrNewEntries() {
        newEntries = false;
    }

}
