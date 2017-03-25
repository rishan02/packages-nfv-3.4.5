package com.tailf.packages.ned.iosxr;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

import com.tailf.conf.Conf;
import com.tailf.conf.ConfBuf;
import com.tailf.conf.ConfPath;
import com.tailf.conf.ConfValue;
import com.tailf.conf.ConfUInt16;
import com.tailf.conf.ConfUInt8;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfXMLParam;
import com.tailf.conf.ConfXMLParamStart;
import com.tailf.conf.ConfXMLParamStop;
import com.tailf.conf.ConfXMLParamValue;
import com.tailf.maapi.Maapi;
import com.tailf.ncs.ResourceManager;
import com.tailf.ncs.annotations.Resource;
import com.tailf.ncs.annotations.ResourceType;
import com.tailf.ncs.annotations.Scope;
import com.tailf.ncs.ns.Ncs;
import com.tailf.ned.NedCapability;
import com.tailf.ned.NedCliBase;
import com.tailf.ned.NedCliBaseTemplate;
import com.tailf.ned.NedCmd;
import com.tailf.ned.NedException;
import com.tailf.ned.NedExpectResult;
//4.x: import com.tailf.ned.NedWorker;

import com.tailf.ned.NedMux;
import com.tailf.ned.NedTTL;
import com.tailf.ned.NedTracer;
import com.tailf.ned.NedWorker;
import com.tailf.ned.NedWorker.TransactionIdMode;
import com.tailf.ned.SSHSessionException;
import com.tailf.ned.TelnetSession;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuContext;
import com.tailf.navu.NavuList;
import com.tailf.navu.NavuNode;

/**
 * This class implements NED interface for cisco iosxr routers
 *
 */

@SuppressWarnings("deprecation")
public class IosxrNedCli extends NedCliBaseTemplate {
    private static Logger LOGGER  = Logger.getLogger(IosxrNedCli.class);

    @Resource(type=ResourceType.MAAPI, scope=Scope.INSTANCE)
    public  Maapi   mm;

    private MetaDataModify metaData;
    private NedDataModify nedData;
    private NedSecrets secrets;

    private boolean inConfig = false;
    private boolean showFixed = false;
    private boolean showRaw = false;
    private boolean showVerbose = false;
    private String date_string = "2016-12-16";
    private String version_string = "4.6.2";
    private String iosversion = "unknown";
    private String iosmodel   = "unknown";
    private String iosdevice = "base";
    private boolean waitForEcho = true;
    private boolean includeCachedShowVersion = true;
    private boolean useCommitListForTransId  = true;
    private boolean useDirectCommit  = false;
    private boolean useExclusiveConfig = true;
    private boolean directCommitFailed  = false;
    private boolean showRunningStrictMode = false;
    private int     chunkSize = 100;
    private int     retryCount = 0;
    private int     waitTime = 1;
    private boolean autoVrfForwardingRestore = true;
    private boolean autoCSCtk60033Patch = true;
    private ArrayList<String> dynamicWarning = new ArrayList<String>();
    private ArrayList<String[]> autoPrompts = new ArrayList<String[]>();

    // start of input, 1 character, > 0 non-# and ' ', one #, >= 0 ' ', eol
    private static String prompt = "\\A[a-zA-Z0-9][^\\# ]+#[ ]?$";
    private static String admin_prompt = "\\A[^\\# ]+\\(admin\\)#[ ]?$";
    private static String config_prompt = "\\A.*\\(.*\\)#[ ]?$"; // may also match admin_prompt

    private final static Pattern[]
        move_to_top_pattern,
        noprint_line_wait_pattern,
        enter_config_pattern,
        exit_config_pattern;

    static {
        move_to_top_pattern = new Pattern[] {
            Pattern.compile("\\A.*\\((admin-)?config\\)#"),
            Pattern.compile("Invalid input detected at"),
            Pattern.compile("\\A.*\\(.*\\)#")
        };

        noprint_line_wait_pattern = new Pattern[] {
            Pattern.compile("\\A.*\\((admin-)?config\\)#"),
            Pattern.compile("\\A.*\\(cfg\\)#"),
            Pattern.compile("\\A.*\\((admin-)?config[^\\(\\)# ]+\\)#"),
            Pattern.compile("\\A.*\\(cfg[^\\(\\)# ]+\\)#"),
            Pattern.compile("\\A.*Uncommitted changes found, commit them"),
            Pattern.compile(prompt)
        };

        enter_config_pattern = new Pattern[] {
            Pattern.compile("\\A\\S*\\((admin-)?config.*\\)#"),
            Pattern.compile("\\A\\S*#"),
        };

        exit_config_pattern = new Pattern[] {
            Pattern.compile("\\A.*\\((admin-)?config\\)#"),
            Pattern.compile("\\A.*\\(cfg\\)#"),
            Pattern.compile("\\A.*\\((admin-)?config.*\\)#"),
            Pattern.compile("\\A.*\\(cfg.*\\)#"),
            Pattern.compile(admin_prompt),
            Pattern.compile(prompt),
            Pattern.compile("You are exiting after a 'commit confirm'"),
            Pattern.compile("\\A.*Uncommitted changes found, commit them"),
            Pattern.compile("\\A% Invalid input detected at ")
        };
    }

    public IosxrNedCli() {
        super();
        try {
            ResourceManager.registerResources(this);
        } catch (Exception e) {
            LOGGER.error("Error injecting Resources", e);
        }
    }


    /**
     * Constructor
     */
    public IosxrNedCli(String device_id,
               InetAddress ip,
               int port,
               String proto,  // ssh or telnet
               String ruser,
               String pass,
               String secpass,
               boolean trace,
               int connectTimeout, // msec
               int readTimeout,    // msec
               int writeTimeout,   // msec
               NedMux mux,
               NedWorker worker) {

        super(device_id, ip, port, proto, ruser, pass, secpass,
              trace, connectTimeout, readTimeout, writeTimeout, mux,
              worker);

        NedTracer tracer;
        if (trace)
            tracer = worker;
        else
            tracer = null;

        worker.setTimeout(connectTimeout);

        // LOG NCS and NED version & date
        logInfo(worker, "NCS VERSION: "+String.format("%x", Conf.LIBVSN));
        logInfo(worker, "NED VERSION: cisco-iosxr "+version_string+" "+
                date_string);

        try {
            ResourceManager.registerResources(this);
        } catch (Exception e) {
            LOGGER.error("Error injecting Resources", e);
        }

        try {
            NedExpectResult res;

            // Get NED configuration
            traceInfo(worker, "NED-SETTINGS:");

            mm.setUserSession(1);
            int th = mm.startTrans(Conf.DB_RUNNING, Conf.MODE_READ);
            String info;
            String[] paths =
                {
                    "/ncs:devices/ncs:global-settings/ncs:ned-settings",
                    "/ncs:devices/ncs:profiles/profile{cisco-iosxr}/ncs:ned-settings",
                    "/ncs:devices/device{" + device_id + "}/ncs:ned-settings",
                };

            // Get transaction ID method
            for (String p: paths) {
                p += "/cisco-iosxr-transaction-id-method";
                if (mm.exists(th, p)) {
                    ConfValue v = mm.getElem(th, p);
                    if (ConfValue.getStringByValue(p, v).equals("commit-list"))
                        this.useCommitListForTransId = true;
                    else
                        this.useCommitListForTransId = false;
                }
            }
            info = this.useCommitListForTransId ? "commit-list" : "config-hash";
            traceInfo(worker, "Transaction ID method : " + info);

            // Get commit method
            for (String p: paths) {
                p += "/cisco-iosxr-commit-method";
                if (mm.exists(th, p)) {
                    ConfValue v = mm.getElem(th, p);
                    this.useDirectCommit = ConfValue.getStringByValue(p, v).equals("direct");
                }
            }
            info = this.useDirectCommit ? "direct" : "confirmed";
            traceInfo(worker, "Commit method : " + info);

            // Get config method
            for (String p: paths) {
                p += "/cisco-iosxr-config-method";
                if (mm.exists(th, p)) {
                    ConfValue v = mm.getElem(th, p);
                    this.useExclusiveConfig = ConfValue.getStringByValue(p, v).equals("exclusive");
                }
            }
            info = this.useExclusiveConfig ? "exclusive" : "terminal";
            traceInfo(worker, "Config method : " + info);

            // Get show-running-strict-mode
            for (String p: paths) {
                p += "/cisco-iosxr-show-running-strict-mode";
                if (mm.exists(th, p)) {
                    ConfValue v = mm.getElem(th, p);
                    this.showRunningStrictMode = ConfValue.getStringByValue(p, v).equals("true");
                }
            }
            traceInfo(worker, "Using showRunningStrictMode : " + showRunningStrictMode);

            // Get chunkSize
            for (String p: paths) {
                p += "/cisco-iosxr-number-of-lines-to-send-in-chunk";
                if (mm.exists(th, p)) {
                    ConfUInt16 u = (ConfUInt16)mm.getElem(th, p);
                    chunkSize = (int)u.longValue();
                }
            }
            traceInfo(worker, "Lines to send per chunk : " + chunkSize);

            // Get retryCount
            for (String p: paths) {
                p += "/cisco-iosxr-connection-settings/number-of-retries";
                if (mm.exists(th, p)) {
                    ConfUInt8 u = (ConfUInt8)mm.getElem(th, p);
                    retryCount = (int)u.longValue();
                }
            }
            traceInfo(worker, "Connect retry count : " + retryCount);

            // Get waitTime
            for (String p: paths) {
                p += "/cisco-iosxr-connection-settings/time-between-retry";
                if (mm.exists(th, p)) {
                    ConfUInt8 u = (ConfUInt8)mm.getElem(th, p);
                    waitTime = (int)u.longValue();
                }
            }
            traceInfo(worker, "Time to wait between each retry : " + waitTime);

            // Fetch verbose
            for (String p: paths) {
                p += "/cisco-iosxr-log-verbose";
                if (mm.exists(th, p)) {
                    ConfValue v = mm.getElem(th, p);
                    this.showVerbose = ConfValue.getStringByValue(p, v).equals("true");
                }
            }
            traceInfo(worker, "Verbose logging : " + showVerbose);

            NavuContext context = new NavuContext(mm, th);

            // Base roots
            NavuContainer deviceSettings= new NavuContainer(context)
                .container(Ncs.hash)
                .container(Ncs._devices_)
                .list(Ncs._device_)
                .elem(new ConfKey(new ConfBuf(device_id)));

            NavuContainer globalSettings = new NavuContainer(context)
                .container(Ncs.hash)
                .container(Ncs._devices_)
                .container("ncs", "global-settings");

            NavuContainer profileSettings = new NavuContainer(context)
                .container(Ncs.hash)
                .container(Ncs._devices_)
                .container("ncs", "profiles")
                .list(Ncs._profile_)
                .elem(new ConfKey(new ConfBuf("cisco-ios")));

            NavuContainer[] settings = {globalSettings,
                                        profileSettings,
                                        deviceSettings };

            // Get cached-show-enable (true or false)
            for (NavuContainer s : settings ) {
                if (s == null)
                    continue;
                String val = s.container("ncs", "ned-settings")
                    .container("cisco-ios-xr-meta", "cisco-iosxr-cached-show-enable")
                    .leaf("cisco-ios-xr-meta", "version")
                    .valueAsString();
                if (val != null)
                    this.includeCachedShowVersion = val.equals("true") ? true : false;
            }
            traceInfo(worker, "includeCachedShowVersion configured to : "
                    + includeCachedShowVersion);

            // Get auto (true or false)
            for (NavuContainer s : settings ) {
                if (s == null)
                    continue;
                String val = s.container("ncs", "ned-settings")
                    .container("cisco-ios-xr-meta", "cisco-iosxr-auto")
                    .leaf("cisco-ios-xr-meta", "vrf-forwarding-restore")
                    .valueAsString();
                if (val != null)
                    this.autoVrfForwardingRestore = val.equals("true") ? true : false;
            }
            traceInfo(worker, "autoVrfForwardingRestore configured to : " + autoVrfForwardingRestore);

            for (NavuContainer s : settings ) {
                if (s == null)
                    continue;
                String val = s.container("ncs", "ned-settings")
                    .container("cisco-ios-xr-meta", "cisco-iosxr-auto")
                    .leaf("cisco-ios-xr-meta", "CSCtk60033-patch")
                    .valueAsString();
                if (val != null)
                    this.autoCSCtk60033Patch = val.equals("true") ? true : false;
            }
            traceInfo(worker, "autoCSCtk60033Patch configured to : " + autoCSCtk60033Patch);

            //
            // Get config warnings
            //
            for (NavuContainer s : settings ) {
                if (s == null)
                    continue;
                NavuList newWarnings = s.container("ncs", "ned-settings")
                    .list("cisco-ios-xr-meta", "cisco-iosxr-config-warning");
                for (NavuContainer entry : newWarnings.elements()) {
                    traceInfo(worker, "Adding config-warning: "
                            +entry.leaf("warning").valueAsString());
                    dynamicWarning.add(entry.leaf("warning").valueAsString());
                }
            }

            //
            // Get auto-prompts
            //
            for (NavuContainer s : settings ) {
                if (s == null)
                    continue;
                NavuList prompts = s.container("ncs", "ned-settings")
                    .list("cisco-ios-xr-meta", "cisco-iosxr-auto-prompts");
                for (NavuContainer entry : prompts.elements()) {
                    String[] newEntry  = new String[3];
                    newEntry[0] = entry.leaf("id").valueAsString();
                    newEntry[1] = entry.leaf("question").valueAsString();
                    newEntry[2] = entry.leaf("answer").valueAsString();
                    traceInfo(worker, "Adding auto-prompt["+newEntry[0]+"]:"
                            + " Q=" +newEntry[1]
                            + " A=" +newEntry[2]);
                    autoPrompts.add(newEntry);
                }
            }

            // Done reading standard config
            traceInfo(worker, "");

            // Connect to device
            int retries = retryCount;
            while(retries >= 0) {
                try {
                    if (proto.equals("ssh")) {
                        setupSSH(worker);
                        trace(worker, "SSH new session", "out");
                    }
                    else {
                        setupTelnet2(worker);
                        trace(worker, "TELNET new session", "out");
                    }
                    break;
                }
                catch (Exception e) {
                    if (retries == 0) {
                        logError(worker, "connect failed",  e);
                        worker.connectError(NedWorker.CONNECT_CONNECTION_REFUSED,
                                            e.getMessage());
                        return;
                    }
                    else {
                        retries--;
                        traceInfo(worker, "Connection timeout, sleeping " + waitTime + " second(s)");
                        try {Thread.sleep(waitTime*1000); } catch (InterruptedException ei) {}
                        traceInfo(worker, "Woke up, retrying "+retries+"/"+retryCount);
                        continue;
                    }
                }
            }

            // Get proxy settings and connect to inner device
            String devpath = "/ncs:devices/device{"
                + device_id
                + "}/ned-settings/cisco-iosxr-proxy-settings";
            if (mm.exists(th, devpath)) {
                ConfValue v;
                String p = devpath + "/remote-connection";
                v = mm.getElem(th, p);
                String remoteConnection = ConfValue.getStringByValue(p, v);

                logInfo(worker, "PROXY remote-connection configured to : "+remoteConnection);

                if (remoteConnection.equals("exec")) {
                    /*
                     * EXEC (terminal server)
                    */
                    p = devpath + "/remote-command";
                    v = mm.getElem(th, p);
                    String remoteCommand = ConfValue.getStringByValue(p, v);

                    p = devpath + "/remote-prompt";
                    v = mm.getElem(th, p);
                    String remotePrompt = ConfValue.getStringByValue(p, v);

                    p = devpath + "/remote-name";
                    v = mm.getElem(th, p);
                    String remoteName = ConfValue.getStringByValue(p, v);

                    p = devpath + "/remote-password";
                    v = mm.getElem(th, p);
                    String remotePassword = ConfValue.getStringByValue(p, v);

                    // Send connect string, wait for prompt
                    try {
                        logInfo(worker, "Connecting to device using EXEC");

                        session.println(remoteCommand);
                        res = session.expect(new String[] { "timed out", remotePrompt });
                        if (res.getHit() <= 0)
                            throw new NedException(NedWorker.CONNECT_TIMEOUT,
                                                   "PROXY connect failed, connection timed out");

                        logInfo(worker, "PROXY connected");

                        // Send newline, wait for username prompt
                        session.println("\r");
                        res = session.expect(new String[] { config_prompt, prompt, "Username:"});
                        if (res.getHit() < 2) {
                            logInfo(worker, "PROXY logged in without username and password");
                            if (res.getHit() == 0) {
                                traceInfo(worker, "PROXY exiting config mode");
                                exitConfig2(worker, "PROXY exiting config mode");
                            }
                        } else {
                            // Send username, wait for password prompt
                            session.println(remoteName);
                            res = session.expect(new String[] { "timed out", "Password:" });
                            if (res.getHit() <= 0)
                                throw new NedException(NedWorker.CONNECT_TIMEOUT,
                                                       "PROXY connect failed, timeout expired");

                            // Send password (mask from log), wait for exec prompt
                            if (trace)
                                session.setTracer(null);
                            traceInfo(worker, "Sending password (NOT LOGGED)");
                            session.println(remotePassword);
                            if (trace)
                                session.setTracer(worker);
                            res = session.expect(new String[] {
                                    "Login invalid",
                                    "timed out",
                                    prompt},
                                worker);
                            if (res.getHit() < 2)
                                throw new NedException(NedWorker.CONNECT_BADAUTH,
                                                       "PROXY connect failed, bad name or password?");
                        }
                    }
                    catch (Exception e) {
                        LOGGER.error("connect from proxy failed ",  e);
                        mm.finishTrans(th);
                        worker.connectError(NedWorker.CONNECT_CONNECTION_REFUSED,
                                            e.getMessage());
                        return;
                    }
                }
                else
                {
                    /*
                     * TELNET or SSH
                    */
                    p = devpath + "/remote-address";
                    v = mm.getElem(th, p);
                    String remoteAddress = ConfValue.getStringByValue(p, v);

                    p = devpath + "/remote-port";
                    v = mm.getElem(th, p);
                    String remotePort = ConfValue.getStringByValue(p, v);

                    p = devpath + "/proxy-prompt";
                    v = mm.getElem(th, p);
                    String proxyPrompt = ConfValue.getStringByValue(p, v);

                    p = devpath + "/remote-name";
                    v = mm.getElem(th, p);
                    String remoteName = ConfValue.getStringByValue(p, v);

                    p = devpath + "/remote-password";
                    v = mm.getElem(th, p);
                    String remotePassword = ConfValue.getStringByValue(p, v);

                    try {
                        session.expect(proxyPrompt);

                        if (remoteConnection.equals("ssh")) {
                            logInfo(worker, "Connecting to device using SSH");

                            session.println("ssh -p "
                                            +remotePort
                                            +" "
                                            +remoteName
                                            +"@"
                                            +remoteAddress);
                        } else {
                            logInfo(worker, "Connecting to device using TELNET");

                            session.println("telnet "
                                            +remoteAddress
                                            +" "
                                            +remotePort);
                            session.expect("[Uu]sername:");
                            session.println(remoteName);
                        }

                        logInfo(worker, "PROXY connected");

                        // Send password (mask from log)
                        session.expect("[Pp]assword:");
                        if (trace)
                            session.setTracer(null);
                        traceInfo(worker, "Sending password (NOT LOGGED)");
                        session.println(remotePassword);
                        if (trace)
                            session.setTracer(worker);
                    }
                    catch (Exception e) {
                        LOGGER.error("connect from proxy failed ",  e);
                        mm.finishTrans(th);
                        worker.connectError(NedWorker.CONNECT_CONNECTION_REFUSED,
                                            e.getMessage());
                        return;
                    }
                }
            }

            mm.finishTrans(th);

            // Send newline for terminal support.
            traceInfo(worker, "Sending extra newline");
            session.print("\n");

            // Wait for device prompt
            res = session.expect(new String[] { config_prompt, prompt}, worker);
            if (res.getHit() == 0) {
                traceInfo(worker, "exiting config mode");
                exitConfig2(worker, "logged directly into config mode");
            }

            // Set terminal settings
            session.print("terminal length 0\n");
            session.expect("terminal length 0", worker);
            session.expect(prompt, worker);
            session.print("terminal width 0\n");
            session.expect("terminal width 0", worker);
            session.expect(prompt, worker);

            // Issue show version to check device/os type
            session.print("show version brief\n");
            session.expect("show version brief", worker);
            String version = session.expect(prompt, worker);
            if (version.indexOf("Invalid input detected") >= 0) {
                session.print("show version\n");
                session.expect("show version", worker);
                version = session.expect(prompt, worker);
            }

            /* Scan version string */
            traceVerbose(worker, "Inspecting version string");
            if (version.indexOf("Cisco IOS XR Software") >= 0) {
                /*
                 * Found IOS-XR device
                 */
                NedCapability capas[] = new NedCapability[1];
                NedCapability statscapas[] = new NedCapability[1];

                if (version.indexOf("NETSIM") >= 0) {

                    traceInfo(worker, "Found name=ios-xr model=NETSIM version=NETSIM");

                    // Show CONFD&NED version used by NETSIM in ned trace
                    session.print("show confd-state version\n");
                    session.expect("show confd-state version");
                    session.expect(prompt, worker);
                    session.print("show confd-state loaded-data-models "+
                                  "data-model tailf-ned-cisco-ios-xr\n");
                    session.expect("show confd-state loaded-data-models "+
                                   "data-model tailf-ned-cisco-ios-xr");
                    session.expect(prompt, worker);
                    iosversion = "cisco-iosxr-" + version_string;
                    iosmodel = "NETSIM";
                    iosdevice = "netsim";
                    useCommitListForTransId = false;
                } else {
                    int n;
                    version = version.replaceAll("\\r", "");

                    // Get version
                    iosversion = findLine(version, "Cisco IOS XR Software, Version ");
                    if (iosversion != null) {
                        iosversion = iosversion.substring(31);
                        if ((n = iosversion.indexOf("[")) > 0)
                            iosversion = iosversion.substring(0,n);
                    } else {
                        iosmodel = "UNKNOWN";
                    }

                    // Get model
                    iosmodel = findLine(version, "\ncisco ");
                    if (iosmodel != null) {
                        iosmodel = iosmodel.trim();
                        iosmodel = iosmodel.replaceAll("cisco (.*) Series .*", "$1");
                        iosmodel = iosmodel.replaceAll("cisco (.*) \\(.*", "$1");
                    } else {
                        iosmodel = "UNKNOWN";
                    }

                    logInfo(worker, "DEVICE: model="+iosmodel+" version="+iosversion);
                }

                capas[0] = new NedCapability(
                    "",
                    "http://tail-f.com/ned/cisco-ios-xr",
                    "cisco-ios-xr",
                    "",
                    date_string,
                    "");

                statscapas[0] = new NedCapability(
                    "",
                    "http://tail-f.com/ned/cisco-ios-xr-stats",
                    "cisco-ios-xr-stats",
                    "",
                    date_string,
                    "");

                setConnectionData(capas,
                                  statscapas,
                                  false, // want reverse-diff
                                  TransactionIdMode.UNIQUE_STRING);

                /*
                 * On NSO 4.0 and later, do register device model and
                 * os version.
                 */
                if (Conf.LIBVSN >= 0x6000000) {
                    ConfXMLParam[] platformData =
                        new ConfXMLParam[] {
                            new ConfXMLParamStart("ncs", "platform"),
                            new ConfXMLParamValue("ncs", "name",
                                                  new ConfBuf("ios-xr")),
                            new ConfXMLParamValue("ncs", "version",
                                                  new ConfBuf(iosversion)),
                            new ConfXMLParamValue("ncs", "model",
                                                  new ConfBuf(iosmodel)),
                            new ConfXMLParamStop("ncs", "platform")
                    };

                    Method method = this.getClass().
                        getMethod("setPlatformData",
                                  new Class[]{ConfXMLParam[].class});
                    method.invoke(this, new Object[]{platformData});
                }

                // Create transform classes and NedSecrets
                try {
                    metaData = new MetaDataModify(worker, device_id, iosmodel, trace, showVerbose, autoVrfForwardingRestore);
                    nedData = new NedDataModify(session, worker, trace, device_id, showVerbose);
                    secrets = new NedSecrets(worker, device_id, trace, showVerbose);
                }
                catch (Exception e) {
                    worker.error(NedCmd.CONNECT_CLI, e.getMessage());
                    return;
                }

            } else {
                worker.error(NedCmd.CONNECT_CLI,
                             NedCmd.cmdToString(NedCmd.CONNECT_CLI),
                             "unknown device");
            }
        }
        catch (SSHSessionException e) {
            worker.error(NedCmd.CONNECT_CLI, e.getMessage());
        }
        catch (IOException e) {
            worker.error(NedCmd.CONNECT_CLI, e.getMessage());
        }
        catch (Exception e) {
            worker.error(NedCmd.CONNECT_CLI, e.getMessage());
        }
    }


    /**
     * setupTelnet2
     */
    public void setupTelnet2(NedWorker worker) throws Exception {
        TelnetSession tsession;
        NedExpectResult res;

        trace(worker, "TELNET connecting to host: "+ ip.getHostAddress()+":"+port, "out");
        if (trace)
            tsession = new TelnetSession(worker, ruser, readTimeout, worker, this);
        else
            tsession = new TelnetSession(worker, ruser, readTimeout, null, this);

        if (port != 23) {
            trace(worker, "TELNET port != 23, sending initial newline", "out");
            tsession.println("\r");
        }

        trace(worker, "TELNET looking for login prompt", "out");
        session = tsession;
        try {
            res = session.expect(new String[] {"[Ll]ogin:", "[Nn]ame:",
                                               "[Pp]assword:", "\\A\\S.*>", prompt});
        } catch (Throwable e) {
            throw new NedException(NedWorker.CONNECT_TIMEOUT, "No login prompt");
        }
        if (res.getHit() < 2) {
            // Login | Name -> send username
            trace(worker, "TELNET sending username", "out");
            session.println(ruser);
            trace(worker, "TELNET looking for password prompt", "out");
            try {
                res = session.expect(new String[]
                    { "\\A\\S.*>", prompt, "[Pp]assword:"});
            } catch (Throwable e) {
                throw new NedException(NedWorker.CONNECT_TIMEOUT, "No password prompt");
            }
        }
        if (res.getHit() == 2) {
            trace(worker, "TELNET sending password", "out");
            if (trace)
                session.setTracer(null);
            session.println(pass);
            if (trace)
                session.setTracer(worker);
        }
    }


    /**
     * reconnect
     */
    public void reconnect(NedWorker worker) {
        // all capas and transmode already set in constructor
        // nothing needs to be done
    }


    /**
     * Which Yang modules are covered by the class
     */
    public String [] modules() {
        return new String[] { "tailf-ned-cisco-ios-xr" };
    }


    /**
     * Which identity is implemented by the class
     */
    public String identity() {
        return "cisco-ios-xr-id:cisco-ios-xr";
    }


    /**
     * isCliError
     */
    private boolean isCliError(NedWorker worker, String reply, String line) {
        int n;

        reply = reply.trim();
        if (reply.isEmpty())
            return false;

        traceVerbose(worker, "Checking device reply='"+reply+")");

        // Ignore dynamic warnings [case sensitive]
        for (n = 0; n < dynamicWarning.size(); n++) {
            if (findString(dynamicWarning.get(n), reply) >= 0) {
                traceInfo(worker, "ignoring dynamic warning: "+reply);
                //warningsBuf += "> "+line+"\n"+reply+"\n" ;
                return false;
            }
        }

        // The following is device errors
        if (reply.toLowerCase().indexOf("error") >= 0 ||
            reply.toLowerCase().indexOf("aborted") >= 0 ||
            reply.toLowerCase().indexOf("exceeded") >= 0 ||
            reply.toLowerCase().indexOf("invalid") >= 0 ||
            reply.toLowerCase().indexOf("incomplete") >= 0 ||
            reply.toLowerCase().indexOf("duplicate name") >= 0 ||
            reply.toLowerCase().indexOf("may not be configured") >= 0 ||
            reply.toLowerCase().indexOf("should be in range") >= 0 ||
            reply.toLowerCase().indexOf("is used by") >= 0 ||
            reply.toLowerCase().indexOf("being used") >= 0 ||
            reply.toLowerCase().indexOf("cannot be deleted") >= 0 ||
            reply.toLowerCase().indexOf("bad mask") >= 0 ||
            reply.toLowerCase().indexOf("failed") >= 0) {
            traceInfo(worker, "Device reported ERROR: '"+reply+"'");
            return true;
        }

        // Not an error
        return false;
    }


    /**
     * print_line_exec
     */
    private String print_line_exec(NedWorker worker, String line)
        throws NedException, IOException, SSHSessionException, ApplyException {

        // Send command and wait for echo
        session.print(line + "\n");
        session.expect(new String[] { Pattern.quote(line) }, worker);

        // Return command output
        traceVerbose2(worker, "Waiting for prompt");
        return session.expect(prompt, worker);
    }


    /**
     * noprint_line_wait
     */
    private boolean noprint_line_wait(NedWorker worker, int cmd, String line, int retrying)
        throws NedException, IOException, SSHSessionException, ApplyException {
        NedExpectResult res;
        boolean isAtTop;

        line = line.trim();

        // First, expect the echo of the line we sent
        if (waitForEcho) {
            traceVerbose2(worker, "noprint_line_wait() - waiting for echo: '"+line+"'");
            res = session.expect(new String[] {
                    Pattern.quote(line),
                    Pattern.quote("Invalid number(-61) sent as ASCII"),
                }, worker);
            if (res.getHit() == 1)
                throw new ExtendedApplyException(line, "Invalid number(-61) sent as ASCII", false, true);
            traceVerbose2(worker, "noprint_line_wait() - got echo: '"+line+"'");
        } else {
            traceVerbose2(worker, "noprint_line_wait() - ignored echo: '"+line+"'");
        }

        // Second, wait for the prompt
        traceVerbose2(worker, "noprint_line_wait() - waiting for prompt");
        res = session.expect(noprint_line_wait_pattern, worker);
        //res = session.expect(noprint_line_wait_pattern, true, readTimeout, worker);
        traceVerbose2(worker, "noprint_line_wait() - prompt matched("+res.getHit()+"): '"+res.getText() + "'");

        // Third, check if we exited config mode
        switch (res.getHit()) {
        case 0: // (admin-config) | (config)
        case 1: // (cfg)
            isAtTop = true;
            break;
        case 2: // config*
        case 3: // cfg*
            isAtTop = false;
            break;
        case 4: // "Uncommitted changes found, commit them"
            session.print("no\n"); // Send a 'no'
            throw new ExtendedApplyException(line, "exited from config mode", false, false);
        default: // exec prompt
            throw new ExtendedApplyException(line, "exited from config mode with no config", false, false);
        }

        // Fourth, look for errors shown on screen while each command typed
        String reply = res.getText();
        traceVerbose2(worker, "REPLY='"+reply+"'");
        if (isCliError(worker, reply, line)) {
            throw new ExtendedApplyException(line, reply, isAtTop, true);
        }

        return isAtTop;
    }


    /**
     * print_line_wait_oper
     */
    private void print_line_wait_oper(NedWorker worker, int cmd, String line)
        throws NedException, IOException, SSHSessionException, ApplyException {
        NedExpectResult res;
        String reply;
        Pattern[] operPrompt = new Pattern[] {
            Pattern.compile("if your config is large. Confirm\\?[ ]?\\[y/n\\]\\[confirm\\]"),
            Pattern.compile(prompt),
            Pattern.compile(config_prompt)
        };

        // Send line + newline, wait for echo and prompt
        session.print(line+"\n");
        session.expect(new String[] { Pattern.quote(line) }, worker);
        res = session.expect(operPrompt, worker);

        // Check for a blocking confirmation prompt
        if (res.getHit() == 0) {
            session.print("y");
            // Note: not echoing 'y'
            res = session.expect(operPrompt, worker);
        }

        // Parse reply for error
        reply = res.getText();
        String lines[] = reply.split("\n|\r");
        for (int i = 0 ; i < lines.length ; i++) {
            if (lines[i].toLowerCase().indexOf("error") >= 0 ||
                lines[i].toLowerCase().indexOf("failed") >= 0) {
                if (line.trim().indexOf("commit") == 0) {
                    // For commits, try to show a more detailed error
                    String showcmd = "show configuration failed";
                    session.println(showcmd);
                    session.expect(showcmd, worker);

                    res = session.expect(new String[] { config_prompt, prompt}, worker);
                    String msg = res.getText();
                    if (msg.indexOf("No such configuration") >= 0) {
                        // This means there is no last failed error saved
                        throw new ExtendedApplyException(line, reply, true, true);
                    } else {
                        throw new ExtendedApplyException(line, msg, true, true);
                    }
                } else {
                    throw new ExtendedApplyException(line, lines[i], true, false);
                }
            }
        }
    }


    /**
     * moveToTopConfig
     */
    private void moveToTopConfig(NedWorker worker)
        throws IOException, SSHSessionException, ApplyException {
        NedExpectResult res;

        traceVerbose(worker, "moveToTopConfig()");

        // Send ENTER to begin by checking our mode
        traceVerbose(worker, "Sending newline");
        session.print("\n");

        for (int i = 0; i < 30; i++) {
            res = session.expect(move_to_top_pattern);
            switch (res.getHit()) {
            case 0: // (admin-config) | (config)
                return;
            case 1: // Invalid input detected at
                session.print("abort\n");
                res = session.expect(new String[] { Pattern.quote("abort") }, worker);
                break;
            case 2: // config sub-mode
                session.print("exit\n");
                res = session.expect(new String[] { Pattern.quote("exit") }, worker);
                break;
            }
        }
        throw new ExtendedApplyException("moveToTopConfig2()", "failed to exit to top", false, true);

    }


    /**
     * enterConfig
     */
    private boolean enterConfig(NedWorker worker, int cmd)
        throws NedException, IOException, SSHSessionException, ApplyException {
        NedExpectResult res;
        String line;

        if (useExclusiveConfig)
            line = "config exclusive";
        else
            line = "config terminal";
        session.print(line+"\n");

        // 0 = (admin-config) | (config)
        // 1 = exec mode
        res = session.expect(enter_config_pattern, worker);
        if (res.getHit() > 0) {
            throw new ExtendedApplyException(line, res.getText(), true, false);
        }

        inConfig = true;
        return true;
    }


    /**
     * exitConfig2
     */
    private void exitConfig2(NedWorker worker, String reason)
        throws IOException, SSHSessionException {
        NedExpectResult res;

        traceVerbose(worker, "exitConfig("+reason+")");

        // Check if session is in config mode or not
        session.print("\n");
        res = session.expect( new Pattern[] {
                Pattern.compile(config_prompt), // or admin exec mode
                Pattern.compile(prompt)
            });

        // In exec mode already
        if (res.getHit() != 0)
            return;

        // Exit config mode
        session.print("exit\n");
        while (true) {
            res = session.expect(exit_config_pattern);
            //traceVerbose(worker, "exitConfig2() : got prompt, getHit()="+res.getHit());
            switch (res.getHit()) {
            case 5:
                // exec prompt
                inConfig = false;
                return;
            case 6:
                // "You are exiting after a 'commit confirm'
                session.print("yes\n");
                session.expect(prompt);
                inConfig = false;
                return;
            case 7:
                // Uncommitted changes found, commit them before exiting
                session.print("no\n");
                session.expect(prompt);
                inConfig = false;
                return;
            case 8:
                // % Invalid input detected at
                session.print("abort\n");
                break;
            default:
                // 0-3: different config prompts
                // 4 admin exec prompt
                session.print("exit\n");
                break;
            }
        }
    }


    /**
     * exitPrompting
     */
    private void exitPrompting(NedWorker worker) throws IOException, SSHSessionException {
        NedExpectResult res;

        Pattern[] cmdPrompt = new Pattern[] {
            // Prompt patterns:
            Pattern.compile(prompt),
            Pattern.compile("\\A\\S*#"),
            // Question patterns:
            Pattern.compile(":\\s*$"),
            Pattern.compile("\\]\\s*$")
        };

        while (true) {
            traceVerbose(worker, "Sending CTRL-C");
            session.print("\u0003");
            traceVerbose(worker, "Waiting for non-question");
            res = session.expect(cmdPrompt, true, readTimeout, worker);
            if (res.getHit() <= 1) {
                return;
            }
        }
    }


    /**
     * getMatch
     */
    private String getMatch(String text, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find())
            return null;
        return matcher.group(1);
    }


    /**
     * getMatches
     */
    private String[] getMatches(NedWorker worker, String text, String regexp) {
        String[] matches;
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        if (!matcher.find())
            return null;
        matches = new String[matcher.groupCount()];
        for (int i = 0; i < matcher.groupCount(); i++) {
            matches[i] = matcher.group(i+1);
            traceVerbose(worker, "MATCH-"+i+"="+matches[i]);
        }
        return matches;
    }

    /**
     * indexOf
     */
    private static int indexOf(Pattern pattern, String s, int start) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find(start) ? matcher.start() : -1;
    }

    /**
     * getString
     */
    private static String getString(String buf, int offset) {
        int nl;
        nl = buf.indexOf("\n", offset);
        if (nl < 0)
            return buf;
        return buf.substring(offset, nl).trim();
    }

    /**
     * findString
     */
    private static int findString(String search, String text) {
        return indexOf(Pattern.compile(search), text, 0);
    }

    /**
     * findLine
     */
    private static String findLine(String buf, String search) {
        int i = buf.indexOf(search);
        if (i >= 0) {
            int nl = buf.indexOf("\n", i+1);
            if (nl >= 0)
                return buf.substring(i,nl);
            else
                return buf.substring(i);
        }
        return null;
    }


    /**
     * modifyLine
     */
    private String modifyLine(NedWorker worker, String line)
        throws NedException {
        int i, sp;
        String match;
        String[] group;

        if (line == null)
            return null;
        String trimmed = line.trim();

        // Ignore setting/deleting cached-show 'config'
        if (line.matches("^\\s*(no )?cached-show .*")) {
            line = "!" + line;
        }

        //
        // NETSIM
        //
        if (iosdevice.equals("netsim") == true) {

            // description patch for netsim, quote text and escape "
            if ((i = line.indexOf("description ")) >= 0) {
                String desc = line.substring(i+12).trim(); // Strip initial white spaces, added by NCS
                if (desc.charAt(0) != '"') {
                    desc = desc.replaceAll("\\\"", "\\\\\\\""); // Convert " to \"
                    line = line.substring(0,i+12) + "\"" + desc + "\""; // Quote string, add ""
                }
            }

            // no alias Java 'tailf:cli-no-value-on-delete' due to NCS bug
            else if (line.startsWith("no alias ") &&
                     (group =  getMatches(worker, line, "(no alias(?: exec| config)? \\S+ ).*")) != null) {
                line = group[0];
            }

            // alias patch
            else if (line.startsWith("alias ") &&
                     (group =  getMatches(worker, line, "(alias(?: exec| config)? \\S+ )(.*)")) != null) {
                String alias = group[1].replaceAll("\\\"", "\\\\\\\""); // Convert " to \"
                line = group[0] + "\"" + alias + "\""; // Quote string, add ""
            }

            return line;
        }

        //
        // REALHW
        //

        // Banner config, disable chunkMode
        // banner <type> <delim> "<quoted string>" <delim>
        if (line.matches("^\\s*banner .*$")) {
            int j = line.indexOf("banner ");
            sp = line.indexOf(" ",j+7);
            if (sp < 0)
                return line;

            String delimCh = line.substring(sp+1,sp+2);
            int delim2 = line.lastIndexOf(" " + delimCh);
            if (delim2 <= sp)
                return line;

            String banner = stringDequote(worker, line.substring(sp+3,delim2));
            banner = banner.replaceAll("\\r", "");  // device adds \r itself
            line = line.substring(0,sp+2) + banner + line.substring(delim2+1);
        }

        // interface * / ipv4 address x.y.z.w /prefix
        else if (line.matches("^\\s*(no )?ipv4 address \\S+ /(\\d+).*$")) {
            line = line.replaceFirst(" (\\S+) /(\\d+)", " $1/$2");
        }

        // class-map * / match vlan [inner] *
        else if (trimmed.startsWith("match vlan ")) {
            line = line.replace(",", " ");
        }

        return line;
    }


    /**
     * modifyData
     */
    private String[] modifyData(NedWorker worker, String data, String function)
        throws NedException {
        int i, n;
        int th;

        //
        // Scan meta-data and modify data
        //
        logVerbose(worker, function + " TRANSFORMING - meta-data");
        data = metaData.modifyData(data);


        //
        // NETSIM
        //
        if (iosdevice.equals("netsim") == true) {
            return data.split("\n");
        }

        //
        // BELOW FIXES IS FOR REAL DEVICES ONLY
        //

        //
        // RT19802 - dependency fix upon delete of class in policy-map [CSCtk60033 XR bug]
        //
        if (autoCSCtk60033Patch && data.indexOf("\npolicy-map ") >= 0 && data.indexOf("\n no class ") >= 0) {
            logVerbose(worker, function + " TRANSFORMING - injecting workaround for CSCtk60033 [policy-map / class delete]");
            String lines[] = data.split("\n");
            String prepend = "\n";
            for (n = 0; n < lines.length; n++) {
                if (lines[n].trim().isEmpty())
                    continue;
                if (lines[n].startsWith("policy-map ")
                    || lines[n].startsWith(" no class ")
                    || lines[n].startsWith(" end-policy-map")) {
                    prepend += lines[n] + "\n";
                }
            }
            data = prepend + "commit\n" + data;
        }

        //
        // modifySets - modify sets
        // vrfForwardingRestore - restore addresses after vrf modified
        //
        logVerbose(worker, function + " TRANSFORMING - restoring sets");
        data = nedData.modifySets(data);
        if (autoVrfForwardingRestore) {
            logVerbose(worker, function + " TRANSFORMING - restoring interface addresses");
            data = nedData.vrfForwardingRestore(data);
        }

        //
        // Split command into multiples of maximum 8 values per command
        //
        logVerbose(worker, function + " TRANSFORMING - splitting long lines");
        String[] maximumEight = {
            "((?:\\s+no)?\\s+match dscp(?: ipv4| ipv6)??) ([ \\d]{15,})"
        };
        for (n = 0; n < maximumEight.length; n++) {
            Pattern pattern = Pattern.compile(maximumEight[n]);
            Matcher matcher = pattern.matcher(data);
            int offset = 0;
            while (matcher.find()) {
                String insert = "";
                String values[] = matcher.group(2).split(" +");
                if (values.length < 9) {
                    continue;
                }
                for (i = 0; i < values.length; i += 8) {
                    insert += matcher.group(1);
                    for (int j = i; (j < i + 8) && (j < values.length); j++)
                        insert += " " + values[j];
                }
                data = data.substring(0, matcher.start(1) + offset)
                    + insert
                    + data.substring(matcher.end(2) + offset);
                offset = offset + insert.length() - (matcher.end(2) - matcher.start(1));
            }
        }

        // route-policy <NAME>
        // "if (xxx) then \r\n statement(s) \r\n endif\r\n"
        // end-policy
        // -> dequote single quoted string to make multiple lines
        logVerbose(worker, function + " TRANSFORMING - dequoting route policies");
        for (i = data.indexOf("\nroute-policy ");
             i >= 0;
             i = data.indexOf("\nroute-policy ", i + 12)) {
            int nl = data.indexOf("\n", i+1);
            if (nl < 0) {
                traceVerbose(worker, "missing route-policy newline");
                continue;
            }
            int start = data.indexOf("\"", nl+1);
            if (start < 0) {
                traceVerbose(worker, "missing route-policy start quote");
                continue;
            }
            int end = data.indexOf("\"", start+1);
            if (end < 0) {
                traceVerbose(worker, "missing route-policy end quote");
                continue;
            }
            String value = data.substring(start, end+1);
            value = stringDequote(worker, value);
            data = data.substring(0,start-1) + value + "\n" + data.substring(end+1);
        }

        // Split into lines
        return data.trim().split("\n");
    }


    //
    // applyConfig2
    //
    public void applyConfig2(NedWorker worker, int cmd, String data)
        throws NedException, IOException, SSHSessionException, ApplyException {
        // apply one line at a time
        String lines[];
        String line, meta = "";
        String chunk;
        int i = -1, n = -1, num;
        boolean isAtTop = true;
        long time;
        long lastTime = System.currentTimeMillis();

        // Modify data and split into lines
        lines = modifyData(worker, data, "APPLY-CONFIG");

        // Enter config mode
        enterConfig(worker, cmd);

        // Send config to device and check reply
        logInfo(worker, "APPLY-CONFIG MODIFIED DATA");
        traceVerbose(worker, "applyConfig2() lines=" + Integer.toString(lines.length));
        try {
            // Send chunk of chunkSize
            for (i = 0; i < lines.length; i += chunkSize) {

                // Copy in up to chunkSize config commands in chunk
                for (num = 0, chunk = "", n = i; n < lines.length && n < (i + chunkSize); n++) {
                    if (lines[n].trim().startsWith("! meta-data :: "))
                        continue;
                    line = modifyLine(worker, lines[n]);
                    lines[n] = line;
                    if (line != null && !line.isEmpty()) {
                        num++;
                        chunk = chunk + line + "\n";
                    }
                }

                // Send chunk of X lines to device
                traceVerbose(worker, "Sending chunk of " + num + " lines:");
                session.print(chunk);

                // Check device reply of one line at the time
                for (n = i; n < lines.length && n < (i + chunkSize); n++) {
                    line = lines[n];
                    if (line == null || line.isEmpty())
                        continue;
                    if (lines[n].trim().startsWith("! meta-data :: "))
                        continue;

                    // Wait for echo for all commands except:
                    waitForEcho = true;
                    if (line.startsWith("banner " )) {
                        traceVerbose(worker, "Sending banner -> disabling waitForEcho");
                        waitForEcho = false;  // works because banner lines(s) sent as one
                    }

                    // Set a large timeout if needed
                    time = System.currentTimeMillis();
                    if ((time - lastTime) > (0.8 * writeTimeout)) {
                        lastTime = time;
                        worker.setTimeout(writeTimeout);
                    }

                    // Check device echo and possible input error (note: not commit)
                    isAtTop = noprint_line_wait(worker, cmd, line, 0);
                }
            }

            // All commands accepted by device, prepare caching of secrets
            secrets.prepare(worker, lines);
        }
        catch (ApplyException e) {
            try {
                // Check if server side is open
                if (session.serverSideClosed()) {
                    traceInfo(worker, "Detected: Server side closed");
                    inConfig = false;
                    throw e;
                }

                // Flush I/O buffer by sending cookie and waiting for echo
                Pattern[] errorPrompt = new Pattern[] {
                    Pattern.compile("ned-trick-to-sync-io-not-an-error"),
                    Pattern.compile("\\A\\S*\\(config.*\\)#"),
                    Pattern.compile("\\A\\S*\\(cfg.*\\)#"),
                    Pattern.compile("\\A[^\\# ]+#[ ]?")
                };

                // Send cookie text
                traceVerbose(worker, "Sending 'ned-trick-to-sync-io-not-an-error'");
                session.print("ned-trick-to-sync-io-not-an-error\n");

                // Parse all device replies until seen cookie or in exec mode
                for (;;) {
                    // Check if server side is open
                    if (session.serverSideClosed()) {
                        traceInfo(worker, "Detected: Server side closed");
                        inConfig = false;
                        throw e;
                    }
                    traceVerbose(worker, "Waiting for 'ned-trick-to-sync-io-not-an-error echo'");
                    NedExpectResult res = session.expect(errorPrompt, worker);
                    if (res.getHit() == 0) {
                        traceInfo(worker, "Got ned-trick-to-sync-io-not-an-error");
                        traceVerbose(worker, "Waiting for prompt");
                        res = session.expect(exit_config_pattern);
                        traceVerbose(worker, "Got prompt, getHit()="+res.getHit());
                        break;
                    } else if (res.getHit() <= 2) {
                        traceVerbose(worker, "Got config prompt: '"+res.getText()+"'");
                    }
                    else {
                        traceInfo(worker, "Got exec prompt");
                        inConfig = false;
                        throw e;
                    }
                }
            } catch (Exception e2) {
                throw e;
            }
            // Exit config mode
            if (e.inConfigMode)
                exitConfig2(worker, "invalid configuration");
            throw e;
        }

        // Make sure we have exited from all submodes
        if (!isAtTop) {
            moveToTopConfig(worker);
        }

        /*
             prepare (send data to device)
                 /   \
                v     v
             abort | commit(send confirmed commit (ios would do noop))
                      /   \
                     v     v
                 revert | persist (send confirming commit)
        */
        if (this.useDirectCommit == false) {
            time = System.currentTimeMillis();
            if ((time - lastTime) > (0.8 * readTimeout)) {
                lastTime = time;
                worker.setTimeout(readTimeout);
            }
            print_line_wait_oper(worker, cmd, "commit confirmed");
        }
    }


    /**
     * applyConfig
     */
    public void applyConfig(NedWorker worker, int cmd, String data)
        throws NedException, IOException, SSHSessionException, ApplyException {

        logInfo(worker, "APPLY-CONFIG BEGIN");

        //
        // Apply optional admin config
        //
        data = "\n" + data;

        int start = data.indexOf("\nadmin\n");
        int end = data.indexOf("\n exit-admin-config");
        if (start >= 0 && end > start && !iosdevice.equals("netsim")) {
            String adminData = data.substring(start + 7, end + 1);
            data = data.substring(end + 19);

            // Enter exec admin mode in order to enter admin config mode
            traceInfo(worker, "Entering admin exec mode");
            String res = print_line_exec(worker, "admin");
            if (res.indexOf("Invalid input detected") >= 0)
                throw new ExtendedApplyException("admin", "Failed to enter exec admin mode", false, false);
            if (res.indexOf("This command is not authorized") >= 0)
                throw new ExtendedApplyException("admin", "admin config is not authorized with this user", false, false);

            // Apply the admin config (force confirmed commit method for admin data)
            boolean cachedUseDirectCommit = this.useDirectCommit;
            this.useDirectCommit = true;
            try {
                applyConfig2(worker, cmd, adminData);
                print_line_wait_oper(worker, NedCmd.COMMIT, "commit");
            }
            finally  {
                this.useDirectCommit = cachedUseDirectCommit;
                exitConfig2(worker, "admin");
            }
        }

        // Apply standard config
        if (!data.trim().isEmpty()) {
            applyConfig2(worker, cmd, data);
        }

        logInfo(worker, "DONE APPLY-CONFIG");
    }


    /**
     * prepareDry
     */
    public void prepareDry(NedWorker worker, String data)
        throws Exception {
        String lines[];
        String line;
        StringBuilder newdata = new StringBuilder();
        int i;

        // ShowRaw used in debugging, to see cli commands before modification
        if (showRaw) {
            worker.prepareDryResponse(data);
            showRaw = false;
            return;
        }

        // Modify data
        lines = modifyData(worker, data, "PREPARE-DRY");

        // Concatenate lines into a single string
        for (i = 0; i < lines.length; i++) {
            line = modifyLine(worker, lines[i]);
            if (line == null)
                continue;
            if (!showVerbose && line.trim().startsWith("! meta-data :: "))
                continue;
            newdata.append(line+"\n");
        }

        worker.prepareDryResponse(newdata.toString());
    }


    /**
     * abort
     */
    public void abort(NedWorker worker, String data)
        throws Exception {

        if (trace)
            session.setTracer(worker);
        traceInfo(worker, "abort()");

        if (inConfig) {
            print_line_wait_oper(worker, NedCmd.ABORT_CLI, "abort");
            inConfig = false;
        }
        worker.abortResponse();
    }


    /**
     * revert
     */
    public void revert(NedWorker worker, String data)
        throws Exception {

        if (trace)
            session.setTracer(worker);
        traceInfo(worker, "revert()");

        if (this.useDirectCommit  && !this.directCommitFailed) {
            try {
                // Some other particiapant of transaction failed, we must rollback
                print_line_wait_oper(worker, NedCmd.REVERT_CLI, "rollback configuration last 1");
            }
            catch (ApplyException e) {
                // Not much we can do, all bets are off!
                LOGGER.error("revert failed ",  e);
                throw e;
            }
        } else {
            print_line_wait_oper(worker, NedCmd.REVERT_CLI, "abort");
        }
        inConfig = false;
        worker.revertResponse();
    }


    /**
     * ExtendedApplyException
     */
    private class ExtendedApplyException extends ApplyException {
        public ExtendedApplyException(String line, String msg,
                                      boolean isAtTop,
                                      boolean inConfigMode) {
            super("command: "+line+": "+msg, isAtTop, inConfigMode);
         }
    }


    /**
     * commit
     */
    public void commit(NedWorker worker, int timeout)
        throws Exception {

        if (trace)
            session.setTracer(worker);
        traceInfo(worker, "commit()");

        // Temporary - see applyConfig above.
        /*
        if (timeout < 0) {
            print_line_wait_oper(worker, NedCmd.COMMIT, "commit");
            hasConfirming = false;
        } else if (timeout < 30) {
            print_line_wait_oper(worker, NedCmd.COMMIT, "commit confirmed 30"),
            hasConfirming = true;
        } else {
            print_line_wait(worker, NedCmd.COMMIT, "commit confirmed "+
                            timeout);
            hasConfirming = true;
        }
        */

        if (this.useDirectCommit) {
            this.directCommitFailed = false;
            print_line_wait_oper(worker, NedCmd.COMMIT, "commit");
            exitConfig2(worker, "direct commit");
        }

        worker.commitResponse();
    }


    /**
     * persist
     */
    public void persist(NedWorker worker) throws Exception {

        if (trace)
            session.setTracer(worker);
        traceInfo(worker, "persist()");

        if (inConfig && !this.useDirectCommit) {
            print_line_wait_oper(worker, NedCmd.COMMIT, "commit");
            exitConfig2(worker, "persist");
        }

        worker.persistResponse();
    }


    /**
     * close
     */
    public void close(NedWorker worker)
        throws NedException, IOException {
        logDebug(worker, "close(worker)");
        try {
            ResourceManager.unregisterResources(this);
        } catch (Exception ignore) {
        }
        super.close(worker);
    }

    public void close() {
        LOGGER.debug(device_id + " close");
        try {
            ResourceManager.unregisterResources(this);
        } catch (Exception ignore) {
        }
        super.close();
    }


    /**
     * trace
     */
    public void trace(NedWorker worker, String msg, String direction) {
        if (trace) {
            worker.trace("-- "+msg+" --\n", direction, device_id);
        }
    }

    /**
     * traceInfo
     */
    private void traceInfo(NedWorker worker, String info) {
        if (trace)
            worker.trace("-- " + info + "\n", "out", device_id);
    }

    /**
     * traceVerbose
     */
    private void traceVerbose(NedWorker worker, String info) {
        if (showVerbose && trace) {
            worker.trace("-- " + info + "\n", "out", device_id);
        }
    }

    /**
     * traceVerbose2
     */
    private void traceVerbose2(NedWorker worker, String info) {
        //traceVerbose(worker, info);
    }


    /**
     * logError
     */
    private void logError(NedWorker worker, String text, Exception e) {
        LOGGER.error(device_id + " " + text, e);
        if (trace)
            worker.trace("-- " + text + ": " + e.getMessage() + "\n", "out", device_id);
    }

    /**
     * logInfo
     */
    private void logInfo(NedWorker worker, String info) {
        LOGGER.info(device_id + " " + info);
        if (trace && worker != null)
            worker.trace("-- " + info + "\n", "out", device_id);
    }

    /**
     * logDebug
     */
    private void logDebug(NedWorker worker, String info) {
        LOGGER.debug(device_id + " " + info);
        if (trace && worker != null)
            worker.trace("-- " + info + "\n", "out", device_id);
    }

    /**
     * logVerbose
     */
    private void logVerbose(NedWorker worker, String info) {
        if (showVerbose) {
            logInfo(worker, info);
        }
    }


    /**
     * stringQuote
     */
    private String stringQuote(String aText) {
        StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator =
            new StringCharacterIterator(aText);
        char character =  iterator.current();
        result.append("\"");
        while (character != CharacterIterator.DONE ){
            if (character == '"')
                result.append("\\\"");
            else if (character == '\\')
                result.append("\\\\");
            else if (character == '\b')
                result.append("\\b");
            else if (character == '\n')
                result.append("\\n");
            else if (character == '\r')
                result.append("\\r");
            else if (character == (char) 11) // \v
                result.append("\\v");
            else if (character == '\f')
                result.append("'\f");
            else if (character == '\t')
                result.append("\\t");
            else if (character == (char) 27) // \e
                result.append("\\e");
            else
                // The char is not a special one, add it to the result as is
                result.append(character);
            character = iterator.next();
        }
        result.append("\"");
        return result.toString();
    }


    /**
     * stringDequote
     */
    private String stringDequote(NedWorker worker, String aText) {
        if (aText.indexOf("\"") != 0) {
            traceVerbose(worker, "stringDequote(ignored) : " + aText);
            return aText;
        }

        traceVerbose(worker, "stringDequote(parse) : " + aText);

        aText = aText.substring(1,aText.length()-1);

        StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator =
            new StringCharacterIterator(aText);
        char c1 = iterator.current();

        while (c1 != CharacterIterator.DONE ) {
            if (c1 == '\\') {
                char c2 = iterator.next();
                if (c2 == CharacterIterator.DONE)
                    result.append(c1);
                else if (c2 == 'b')
                    result.append('\b');
                else if (c2 == 'n')
                    result.append('\n');
                else if (c2 == 'r')
                    result.append('\r');
                else if (c2 == 'v')
                    result.append((char) 11); // \v
                else if (c2 == 'f')
                    result.append('\f');
                else if (c2 == 't')
                    result.append('\t');
                else if (c2 == 'e')
                    result.append((char) 27); // \e
                else
                    result.append(c2);
            }
            else {
                // The char is not a special one, add it to the result as is
                result.append(c1);
            }
            c1 = iterator.next();
        }
        traceVerbose(worker, "stringDequote(parsed) : " + result.toString());
        return result.toString();
    }


    /**
     * isTopExit
     */
    private boolean isTopExit(String line) {
        if (line.startsWith("exit"))
            return true;
        if (line.startsWith("!") && line.trim().equals("!"))
            return true;
        return false;
    }


    /**
     * linesToString
     */
    private String linesToString(String lines[]) {
        StringBuilder string = new StringBuilder();
        for (int n = 0; n < lines.length; n++) {
            if (lines[n].isEmpty())
                continue;
            string.append(lines[n]+"\n");
        }
        return string.toString();
    }


    /**
     * stripLineAll
     */
    private static String stripLineAll(String buf, String search) {
        buf = "\n" + buf;
        int i = buf.indexOf(search);
        while (i >= 0) {
            int nl = buf.indexOf("\n", i+1);
            if (nl > 0)
                buf = buf.substring(0,i) + buf.substring(nl);
            i = buf.indexOf(search);
        }
        return buf;
    }


    /**
     * trimConfig
     */
    private String trimConfig(NedWorker worker, String res) {
        int i, nl;

        // Strip everything before:
        i = res.indexOf("Building configuration...");
        if (i >= 0) {
            nl = res.indexOf("\n", i);
            if (nl > 0)
                res = res.substring(nl+1);
        }
        i = res.indexOf("!! Last configuration change");
        if (i >= 0) {
            nl = res.indexOf("\n", i);
            if (nl > 0)
                res = res.substring(nl+1);
        }
        i = res.indexOf("No entries found.");
        if (i >= 0) {
            nl = res.indexOf("\n", i);
            if (nl > 0)
                res = res.substring(nl+1);
        }

        // Strip everything after 'end'
        i = res.lastIndexOf("\nend");
        if (i >= 0) {
            res = res.substring(0,i);
        }

        // Trim comments
        res = stripLineAll(res, "\n!! ");

        return res + "\n";
    }


    /**
     * getAdminConfig
     */
    private String getAdminConfig(NedWorker worker)
        throws Exception {
        String cmd = "admin show running-config";
        session.print(cmd + "\n");
        session.expect(new String[] { Pattern.quote(cmd) }, worker);
        String res = session.expect(prompt, worker);
        if (res.indexOf("Building configuration") < 0)
            return "";
        // Trim beginning and end
        res = trimConfig(worker, res);
        // Wrap in admin mode context
        res = "admin\n" + res + "exit-admin-config\n\n";
        traceVerbose(worker, "SHOW_ADMIN:\n'"+res+"'");
        return res;
    }


    /**
     * getConfig
     */
    private String getConfig(NedWorker worker, boolean convert)
        throws Exception {
        int n, i, nl;

        //
        // Get config from device
        //
        if (showFixed) {
            traceInfo(worker, "Showing fixed-config");
            session.print("show fixed-config\n");
            session.expect("show fixed-config", worker);
        } else {
            if (inConfig)
                exitConfig2(worker, "getConfig()");
            session.print("show running-config\n");
            session.expect("show running-config", worker);
        }
        String res = session.expect(prompt, worker);
        worker.setTimeout(readTimeout);
        res = trimConfig(worker, res);  // Trim beginning and end

        //
        // Add admin config first
        //
        if (iosdevice.equals("netsim") == false) {
            logVerbose(worker, "SHOW READING - admin show running-config");
            res = getAdminConfig(worker) + res;
        }


        //
        // TRANSFORM CONFIG
        //
        logInfo(worker, "SHOW TRANSFORMING CONFIG");
        if (includeCachedShowVersion) {
            // Add cached-show info to config
            res = res + "cached-show version version " + iosversion + "\n";
            res = res + "cached-show version model " + iosmodel + "\n";
        }

        //
        // Update secrets - replace encrypted secrets with cleartext if not changed
        //
        if (iosdevice.equals("netsim") == false) {
            logVerbose(worker, "SHOW TRANSFORMING - updating secrets");
            res = secrets.update(worker, res, convert);
        }

        //
        // STRING QUOTE THE FOLLOWING ENTRIES
        //
        logVerbose(worker, "SHOW TRANSFORMING - quoting strings ");
        String[] quoteStrings = {
            "alias (?:exec \\S+|config \\S+|\\S+) (.*)"
        };
        for (n = 0; n < quoteStrings.length; n++) {
            Pattern pattern = Pattern.compile(quoteStrings[n]);
            Matcher matcher = pattern.matcher(res);
            int offset = 0;
            while (matcher.find()) {
                String quoted = stringQuote(matcher.group(1));
                //traceVerbose(worker, "QUOTED STRING("+matcher.group(0)+") = "+quoted);
                res = res.substring(0, matcher.start(1) + offset)
                    + quoted
                    + res.substring(matcher.start(1) + matcher.group(1).length() + offset);
                offset = offset + quoted.length() - matcher.group(1).length();
            }
        }

        //
        // Quote ' description ' strings (required for both hw & netsim)
        //
        logVerbose(worker, "SHOW TRANSFORMING - quoting descriptions");
        String lines[] = res.split("\n");
        res = null; // to provoke crash if used below
        String toptag = "";
        for (n = 0; n < lines.length; n++) {
            if (lines[n].isEmpty())
                continue;
            if (isTopExit(lines[n])) {
                toptag = "";
            } else if (Character.isLetter(lines[n].charAt(0))) {
                toptag = lines[n].trim();
            }
            i = lines[n].indexOf(" description ");
            if (i < 0)
                continue;
            if (toptag.startsWith("l2vpn"))
                continue;
            if (toptag.startsWith("router static"))
                continue;
            if (i > 1 && lines[n].charAt(i-1) == '#')
                continue; // Ignore '# description' entries in e.g. route-policy or sets

            // Quote description string
            String desc = stringQuote(lines[n].substring(i+13).trim());
            //traceVerbose(worker, "transformed: DESC="+desc);
            lines[n] = lines[n].substring(0,i+13) + desc;
        }


        //
        // NETSIM - leave early
        //
        if (iosdevice.equals("netsim") == true && showFixed == false) {
            res = linesToString(lines);
            return res;
        }


        //
        // REAL DEVICES BELOW:
        //

        //
        // MAIN LINE-BY-LINE LOOP
        //
        logVerbose(worker, "SHOW TRANSFORMING - line-by-line patches");
        for (n = 0, toptag = ""; n < lines.length; n++) {
            String transformed = null;
            String trimmed = lines[n].trim();
            if (trimmed.isEmpty())
                continue;

            // Update toptag
            if (isTopExit(lines[n])) {
                toptag = "";
            } else if (Character.isLetter(lines[n].charAt(0))) {
                toptag = trimmed;
            }

            //
            /// class-map
            //
            if (toptag.startsWith("class-map ")) {
                // class-map * / match vlan *
                if (trimmed.startsWith("match vlan "))
                    transformed = lines[n].replaceAll("([0-9])( )([0-9])", "$1,$3");
            }

            //
            // Transform lines[n] -> XXX
            //
            if (transformed != null && !transformed.equals(lines[n])) {
                if (transformed.isEmpty())
                    traceVerbose(worker, "transformed: stripped '"+trimmed+"'");
                else
                    traceVerbose(worker, "transformed: '"+trimmed+"' -> '"+transformed.trim()+"'");
                lines[n] = transformed;
            }

        } // for (line-by-line)

        // Convert back lines to string
        res = linesToString(lines);


        //
        // Enter version in 'config' leaf.
        //
        res = res.replaceFirst("\n!! IOS XR Configuration ", "\nversion ");
        res = res.replaceFirst("version version =", "version"); // xr 6.x fix

        //
        // Trim everything between boot-start-marker and boot-end-marker
        //
        logVerbose(worker, "SHOW TRANSFORMING - basic trimming");
        i = res.indexOf("boot-start-marker");
        if (i >= 0) {
            int x = res.indexOf("boot-end-marker");
            if (x > i) {
                nl = res.indexOf("\n", x);
                if (nl > 0)
                    res = res.substring(0,i)+res.substring(nl+1);
            }
        }

        //
        // Remove archive
        //
        i = res.indexOf("\narchive");
        if (i >= 0) {
            int x = res.indexOf("\n!", i);
            if (x >= 0) {
                res = res.substring(0,i)+res.substring(x);
            } else {
                res = res.substring(0,i);
            }
        }

        //
        // Compact interface encapsulation (dot1q & dot1ad) vlan commas ' , ' -> ','
        //
        logVerbose(worker, "SHOW TRANSFORMING - compacting vlan commas");
        for (i = res.indexOf("\n encapsulation dot1ad ");
             i >= 0;
             i = res.indexOf("\n encapsulation dot1ad ", i + 21)) {
            nl = res.indexOf("\n", i+1);
            if (nl < 0)
                break;
            String vlans = res.substring(i, nl);
            if (vlans.indexOf(" , ") < 0)
                continue;
            vlans = vlans.replace(" , ", ",");
            res = res.substring(0, i) + vlans + res.substring(nl);
        }
        for (i = res.indexOf("\n encapsulation dot1q ");
             i >= 0;
             i = res.indexOf("\n encapsulation dot1q ", i + 21)) {
            nl = res.indexOf("\n", i+1);
            if (nl < 0)
                break;
            String vlans = res.substring(i, nl);
            if (vlans.indexOf(" , ") < 0)
                continue;
            vlans = vlans.replace(" , ", ",");
            res = res.substring(0, i) + vlans + res.substring(nl);
        }

        //
        // look for banner(s) and quote banner-text, e.g.:
        // \nbanner motd $ MyBannerTextHereWithPossibleNewLines $
        logVerbose(worker, "SHOW TRANSFORMING - banners");
        for (i = res.indexOf("\nbanner ");
             i >= 0;
             i = res.indexOf("\nbanner ", i+8)) {
            // banner <type> <delimCh><MESSAGE><delimCh2>
            //?? banner <type> <MESSAGE>

            // Get index of first delimeter
            int space = res.indexOf(" ", i+8);
            if (space < 0)
                continue;
            int delim = space + 1;
            String delimCh = res.substring(delim, delim + 1);

            // Get index of second delimiter
            int delim2 = res.indexOf(delimCh, delim + 1);
            if (delim2 < 0)
                continue;

            // Get New Line of second delimiter
            nl = res.indexOf("\n", delim2);
            if (nl < 0)
                continue;

            String banner = res.substring(delim + 1, delim2);
            //if (banner.indexOf("\n") < 0)
            //banner = banner.substring(0,banner.length()-1);
            banner = stringQuote(banner);
            res = res.substring(0,delim) + delimCh + " "
                + banner + " " + delimCh + res.substring(nl);
        }

        //
        // ADD QUOTES AROUND THE FOLLOWING LINES (if contains whitespace)
        //
        String[] addQuoteStrings = {
            "\\s+route-policy (.*) (?:in|out)",
        };
        logVerbose(worker, "SHOW TRANSFORMING - adding quotes");
        for (n = 0; n < addQuoteStrings.length; n++) {
            Pattern pattern = Pattern.compile(addQuoteStrings[n]);
            Matcher matcher = pattern.matcher(res);
            int offset = 0;
            while (matcher.find()) {
                if (matcher.group(1).indexOf(" ") < 0)
                    continue;
                String string = "\"" + matcher.group(1) + "\"";
                traceVerbose(worker, "ADDED QUOTES("+matcher.group(0)+") = "+string);
                res = res.substring(0, matcher.start(1) + offset)
                    + string
                    + res.substring(matcher.start(1) + matcher.group(1).length() + offset);
                offset = offset + string.length() - matcher.group(1).length();
            }
        }

        //
        // route-policy, quote lines (i.e. make into a stringle string)
        //
        logVerbose(worker, "SHOW TRANSFORMING - route-policy");
        i = res.indexOf("\nroute-policy ");
        while (i >= 0) {
            int start = res.indexOf("\n", i+1);
            if (start > 0) {
                int end = res.indexOf("\nend-policy", start);
                if (end > 0) {
                    String buf = res.substring(start+1, end+1);
                    res = res.substring(0,start+1) + stringQuote(buf)
                        + "\n" + res.substring(end+1);
                }
            }
            i = res.indexOf("\nroute-policy ", i + 12);
        }

        //
        // Trim commas(s) from set contents
        //
        String[] sets = {
            "extcommunity-set rt",
            "extcommunity-set soo",
            "rd-set",
            "prefix-set",
            "as-path-set",
            "community-set"
        };
        logVerbose(worker, "SHOW TRANSFORMING - trimming set commas");
        for (n = 0; n < sets.length; n++) {
            Pattern pattern = Pattern.compile("\n"+sets[n]+" (\\S+)\\s*");
            Matcher matcher = pattern.matcher(res);
            String insert;
            int offset = 0;
            while (matcher.find()) {
                int end = indexOf(Pattern.compile("\\s*end-set\\s*"), res, matcher.end(0) + offset);
                if (end < 0)
                    continue;
                traceVerbose(worker, "Trimming ',' from set: "+matcher.group(0).trim());
                String buf0 = res.substring(matcher.end(0) + offset, end);
                String buf1 = buf0.replace(",", "");
                res = res.substring(0, matcher.end(0) + offset)
                    + buf1
                    + res.substring(end);
                offset = offset + buf1.length() - buf0.length();
            }
        }

        // STRICT MODE
        if (showRunningStrictMode) {
            logVerbose(worker, "SHOW TRANSFORMING - strict mode");
            res = res.replaceAll("\n!(\\s*)", "\nxyzroot 0$1");
            res = res.replaceAll("(\\s+)!(\\s*)", "$1exit$2");
        } else {
            // Mode-sensitive interface fix.
            logVerbose(worker, "SHOW TRANSFORMING - inserting top roots");
            res = res.replaceAll("\ninterface ", "\nxyzroot 0\ninterface ");

            // Dirty 'mpls traffic-eng / auto-tunnel backup' fix
            // Note: XR can't read its own running-config.
            res = res.replaceFirst("\n auto-tunnel backup",
                                   "\nmpls traffic-eng\n auto-tunnel backup");
        }

        // Dirty interface * / ipv4 address fix for a.b.c.d/prefix
        logVerbose(worker, "SHOW TRANSFORMING - ipv4 prefix fix");
        res = res.replaceAll("\n ipv4 address ([0-9.]+)/(\\d+)",
                             "\n ipv4 address $1 /$2");

        if (showFixed) {
            traceVerbose(worker, "SHOW_AFTER_FIXED=\n"+res);
            showFixed = false;
        } else {
            traceVerbose(worker, "SHOW_AFTER=\n"+res);
        }

        // Respond with updated show buffer
        return res;
    }


    /**
     * getTransId
     */
    public void getTransId(NedWorker worker)
        throws Exception {
        String res = null;

        if (trace)
            session.setTracer(worker);

        logInfo(worker, "GET-TRANS-ID");

        // If new cleartext secret set, scan running-config and cache encrypted secret(s)
        if (secrets.getNewEntries()) {
            res = getConfig(worker, false);
            secrets.clrNewEntries();
        }

        // Use commit list ID for string data
        if (useCommitListForTransId) {
            session.print("show configuration commit list 1\n");
            session.expect("show configuration commit list 1", worker);
            res = session.expect(prompt, worker);
            int i = res.indexOf("SNo.");
            if (i >= 0)
                res = res.substring(i);
        }

        // Use running-config for string data
        else if (res == null) {
            res = getConfig(worker, false);
        }
        worker.setTimeout(readTimeout);

        // Calculate checksum of running-config
        byte[] bytes = res.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytes);
        BigInteger md5Number = new BigInteger(1, thedigest);
        String md5String = md5Number.toString(16);

        logInfo(worker, "DONE GET-TRANS-ID ("+md5String+")");

        worker.getTransIdResponse(md5String);
    }


    /**
     * show
     */
    public void show(NedWorker worker, String toptag)
        throws Exception {
        session.setTracer(worker);

        if (toptag.equals("interface")) {
            logInfo(worker, "SHOW READING CONFIG");
            String res = getConfig(worker, true);
            logInfo(worker, "DONE SHOW");
            worker.showCliResponse(res);
        } else {
            // Only respond to one toptag
            worker.showCliResponse("");
        }
    }


    /**
     * isConnection
     */
    public boolean isConnection(String device_id,
                                InetAddress ip,
                                int port,
                                String proto,  // ssh or telnet
                                String ruser,
                                String pass,
                                String secpass,
                                String keydir,
                                boolean trace,
                                int connectTimeout, // msec
                                int readTimeout,
                                int writeTimeout) {
        return ((this.device_id.equals(device_id)) &&
                (this.ip.equals(ip)) &&
                (this.port == port) &&
                (this.proto.equals(proto)) &&
                (this.ruser.equals(ruser)) &&
                (this.pass.equals(pass)) &&
                (this.secpass.equals(secpass)) &&
                (this.trace == trace) &&
                (this.connectTimeout == connectTimeout) &&
                (this.readTimeout == readTimeout) &&
                (this.writeTimeout == writeTimeout));
    }


    /**
     * commandWash
     */
    private String commandWash(String cmd) {
        byte[] bytes = cmd.getBytes();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cmd.length(); ++i) {
            if (bytes[i] == 9)
                continue;
            if (bytes[i] == -61)
                continue;
            result.append(cmd.charAt(i));
        }
        return result.toString();
    }


    /**
     * command
     */
    public void command(NedWorker worker, String cmdName, ConfXMLParam[] p)
        throws Exception {
        String cmd  = cmdName;
        String reply = "";
        Pattern[] cmdPrompt;
        NedExpectResult res;
        boolean rebooting = false;
        String promptv[] = null;
        int i, promptc = 0;

        if (trace)
            session.setTracer(worker);

        /* Add arguments */
        for (i = 0; i < p.length; ++i) {
            ConfObject val = p[i].getValue();
            if (val != null)
                cmd = cmd + " " + val.toString();
        }

        // Debugging
        traceVerbose(worker, "command(" + cmd + ")");
        //byte[] b = cmd.getBytes();
        //for (i = 0; i < cmd.length(); i++)
        //traceVerbose(worker, "cmd["+i+"] = "+ cmd.charAt(i) + " " + b[i]);

        // Strip special characters blocking command
        cmd = commandWash(cmd);

        // Extract answer(s) to prompting questions
        Pattern pattern = Pattern.compile("(.*)\\|\\s*prompts\\s+(.*)");
        Matcher matcher = pattern.matcher(cmd);
        if (matcher.find()) {
            cmd = matcher.group(1).trim();
            traceVerbose(worker, "command = '"+cmd+"'");
            promptv = matcher.group(2).trim().split(" +");
            for (i = 0; i < promptv.length; i++)
                traceVerbose(worker, "promptv["+i+"] = '"+promptv[i]+"'");
        }

        // Deprecated crypto command
        if (cmd.startsWith("crypto "))
            cmd = cmd.replace("crypto ", "crypto key ");
        // Any command
        else if (cmd.startsWith("any ")) {
            cmd = cmd.substring(4);
        }

        // patch for service node bug, quoting command
        if (cmd.charAt(cmd.length() - 1) == '"') {
            traceInfo(worker, "PATCH: removing quotes inserted by bug in NCS");
            cmd = cmd.substring(0, cmd.length() -1 );
            cmd = cmd.replaceFirst("\"", "");
        }

        // show fixed-config [internal command]
        if (cmd.equals("show fixed-config")) {
            reply = "\nNext sync-from will show fixed config.\n";
            traceInfo(worker, reply);
            showFixed = true;
        }

        // show outformat raw [internal command]
        else if (cmd.equals("show outformat raw")) {
            reply = "\nNext dry-run will show raw (unmodified) format.\n";
            traceInfo(worker, reply);
            showRaw = true;
        }

        // DEVICE command
        else {
            cmdPrompt = new Pattern[] {
                // Prompt patterns:
                Pattern.compile(prompt),
                Pattern.compile("\\A\\S*#"),
                // Ignore patterns:
                Pattern.compile("\\[OK\\]"),
                Pattern.compile("\\[Done\\]"),
                Pattern.compile("timeout is \\d+ seconds:"),  // ping
                // Question patterns:
                Pattern.compile(":\\s*$"),
                Pattern.compile("\\][\\?]?\\s*$")
            };

            // On real device only
            if (iosdevice.equals("netsim") == true) {
                worker.error(NedCmd.CMD, "'"+cmd+"' not supported on NETSIM, "+
                             "use a real device");
                return;
            }

            // Send command to device and wait for echo
            traceVerbose(worker, "Sending command: " + cmd);
            session.print(cmd+"\n");
            session.expect(new String[] { Pattern.quote(cmd) }, worker);

            // Wait for prompt, answer prompting questions with | prompts info
            while (true) {
                traceVerbose(worker, "Waiting for command prompt");
                res = session.expect(cmdPrompt, true, readTimeout, worker);
                String output = res.getText();
                String answer = null;
                reply = reply + output;
                if (res.getHit() <= 1) {
                    traceVerbose(worker, "Got prompt '"+output+"'");
                    if (promptv != null && promptc < promptv.length) {
                        reply += "\n(unused prompts:";
                        for (i = promptc; i < promptv.length; i++)
                            reply += " "+promptv[i];
                        reply += ")";
                    }
                    break;
                } else if (res.getHit() <= 4
                           || cmd.startsWith("show ")) {
                    traceVerbose(worker, "Ignoring output '"+output+"'");
                    continue;
                }

                traceVerbose(worker, "Got question '"+output+"'");

                // Get answer from command line, i.e. '| prompts <val>'
                if (promptv != null && promptc < promptv.length) {
                    answer = promptv[promptc++];
                }

                // Look for answer in auto-prompts ned-settings
                else {
                    for (int n = autoPrompts.size()-1; n >= 0; n--) {
                        String entry[] = autoPrompts.get(n);
                        if (findString(entry[1], output) >= 0) {
                            traceInfo(worker, "Matched auto-prompt["+entry[0]+"]");
                            answer = entry[2];
                            reply += "(auto-prompt "+answer+") -> ";
                            break;
                        }
                    }
                }

                // Send answer to device. Check if rebooting
                if (answer != null) {
                    traceInfo(worker, "Sending: "+answer);
                    if (answer.equals("ENTER"))
                        session.print("\n");
                    else if (answer.length() == 1)
                        session.print(answer);
                    else
                        session.print(answer+"\n");
                    if (cmd.startsWith("reload")
                        && output.indexOf("Proceed with reload") >= 0
                        && answer.charAt(0) != 'n') {
                        rebooting = true;
                        break;
                    }
                    continue;
                }

                // Missing answer to a question prompt:
                reply = "\nMissing answer to a device question:\n+++" + reply;
                reply +="\n+++\nSet auto-prompts ned-setting or add '| prompts <answer>', e.g.:\n";
                reply += "devices device <devname> live-status exec any \"reload | prompts yes\"";
                reply += "\nNote: Single letter is sent without LF. Use 'ENTER' for LF only.";
                exitPrompting(worker);
                worker.error(NedCmd.CMD, reply);
                return;
            }
        }

        // Report reply
        worker.commandResponse(new ConfXMLParam[] {
                new ConfXMLParamValue("cisco-ios-xr-stats", "result",
                                      new ConfBuf(reply))});

        // Rebooting
        if (rebooting) {
            logInfo(worker, "Rebooting device...");
            logInfo(worker, "Sleeping 30 seconds to avoid premature reconnect");
            worker.setTimeout(10*60*1000);
            try {
                Thread.sleep(30*1000);
                logInfo(worker, "Woke up from 30 seconds sleep");
            } catch (InterruptedException e) {
                logInfo(worker, "reboot sleep interrupted");
            }
        }
    }


    /**
     * showStats
     */
    public void showStats(NedWorker worker, int th, ConfPath path)
        throws Exception {

        traceVerbose(worker, "showStats() "+path);

        worker.showStatsResponse(new NedTTL[] {
                new NedTTL(path, 10)
            });
    }


    /**
     * showStatsList
     */
    public void showStatsList(NedWorker worker, int th, ConfPath path)
        throws Exception {

        traceVerbose(worker, "showStatsList() "+path);

        worker.showStatsListResponse(10, null);
    }


    /**
     * newConnection
     */
    public NedCliBase newConnection(String device_id,
                                InetAddress ip,
                                int port,
                                String proto,  // ssh or telnet
                                String ruser,
                                String pass,
                                String secpass,
                                String publicKeyDir,
                                boolean trace,
                                int connectTimeout, // msec
                                int readTimeout,    // msec
                                int writeTimeout,   // msecs
                                NedMux mux,
                                NedWorker worker) {
        return new IosxrNedCli(device_id,
                               ip, port, proto, ruser, pass, secpass, trace,
                               connectTimeout, readTimeout, writeTimeout,
                               mux, worker);
    }


    /**
     * toString
     */
    public String toString() {
        if (ip == null)
            return device_id+"-<ip>:"+Integer.toString(port)+"-"+proto;
        return device_id+"-"+ip.toString()+":"+
            Integer.toString(port)+"-"+proto;
    }
}
