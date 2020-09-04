package SSHFile;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.io.File;

public abstract class SSHFile_abstract_declare {
    protected static File dataSetDirectory;
    protected static String root;
    protected static Session session = null;
    protected static ChannelSftp sftpChannel = null;
    protected static String PATHSEPARATOR = "/";
    protected static GUIComponents aGUIComponents;
    protected static String sshUser;
    protected static String sshPwd;
}
