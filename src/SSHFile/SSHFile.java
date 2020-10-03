package SSHFile;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import com.jcraft.jsch.*;

import javax.swing.*;
import java.io.*;
import java.util.Vector;

public class SSHFile extends SSHFile_abstract {

    public SSHFile(File dataSetDirectory, String root, GUIComponents aGUIComponents, String sshUser, String sshPwd) {
        this.dataSetDirectory = dataSetDirectory;
        this.root = root;
        this.aGUIComponents = aGUIComponents;
        this.sshUser = sshUser;
        this.sshPwd = sshPwd;
    }

    //
    public void sshReadFile() {

//        String user = "username";
//        String password = "password";
        String user = this.sshUser;
        String password = this.sshPwd;
        String host = "csr30.comp.hkbu.edu.hk";
        int port = 22;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");

//            String localFile = "src/GUI/GUI_BoxLook_New_25072020/BookShop/Test2.txt";
            String localFile = this.dataSetDirectory.getPath();
            System.out.println("localFile: " + localFile);

            String remoteDir = "/home/comp/shiwenli/BSPCOVER/datasetInput/"; // /home/comp/shiwenli/datasetInput/

//            sftpChannel.put(localFile, remoteDir);
            recursiveFolderUpload(localFile, remoteDir);

            String parametPath = this.root + "/datasets/Parameter/parameter.txt";

            remoteDir = "/home/comp/shiwenli/BSPCOVER/parameter/";

            recursiveFolderUpload(parametPath, remoteDir);

            String command1 = "java -jar /home/comp/shiwenli/BSPCOVER/EfficientLTS/EfficientLTS.jar";
            Channel exeChannel = session.openChannel("exec");
            ((ChannelExec)exeChannel).setCommand(command1);
            exeChannel.setInputStream(null);
            ((ChannelExec)exeChannel).setErrStream(System.err);

            InputStream in=exeChannel.getInputStream();
            exeChannel.connect();
            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                    this.aGUIComponents.bspcoverInfoTextArea.append(new String(tmp, 0, i));
                }
                if(exeChannel.isClosed()){
                    System.out.println("exit-status: " + exeChannel.getExitStatus());
                    this.aGUIComponents.bspcoverInfoTextArea.append("\n" + "exit-status: " + exeChannel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            exeChannel.disconnect();

            //
            try {
                String remoteDirShapelet = "/home/comp/shiwenli/BSPCOVER/shapeletOutput";
                String localDir = this.root + "/datasets/Grace_dataset/v_4/shapelet/shapelet&weight";
//                String localDir = this.shapeletDirectory.getPath();
                recursiveFolderDownload(remoteDirShapelet, localDir);
            }catch (Exception e){
                String eTrace = e.toString();
                this.aGUIComponents.bspcoverInfoTextArea.setText(eTrace);
                e.printStackTrace();
            }

            //
        } catch (JSchException | SftpException | FileNotFoundException e) {
            String eTrace = e.toString();
            this.aGUIComponents.bspcoverInfoTextArea.setText(eTrace);
            System.out.println("End of this manager! Bye...");
            if (session != null) {
                session.disconnect();
            }

            if (sftpChannel != null){
                sftpChannel.disconnect();
            }

            e.printStackTrace();
        } catch (IOException e) {
            String eTrace = e.toString();
            this.aGUIComponents.bspcoverInfoTextArea.setText(eTrace);
            e.printStackTrace();
        }
        //
        System.out.println("End of this manager! Bye...");
        if (session != null) {
            session.disconnect();
        }

        if (sftpChannel != null){
            sftpChannel.disconnect();
        }
    }

    //
    private static void recursiveFolderUpload(String sourcePath, String destinationPath) throws SftpException, FileNotFoundException {

        File sourceFile = new File(sourcePath);
        if (sourceFile.isFile()) {

            // copy if it is a file
            sftpChannel.cd(destinationPath);
            if (!sourceFile.getName().startsWith("."))
                sftpChannel.put(new FileInputStream(sourceFile), sourceFile.getName(), ChannelSftp.OVERWRITE);

        } else {

            System.out.println("inside else " + sourceFile.getName());
            File[] files = sourceFile.listFiles();

            if (files != null && !sourceFile.getName().startsWith(".")) {

                sftpChannel.cd(destinationPath);
                SftpATTRS attrs = null;

                // check if the directory is already existing
                try {
                    attrs = sftpChannel.stat(destinationPath + "/" + sourceFile.getName());
                } catch (Exception e) {
                    System.out.println(destinationPath + "/" + sourceFile.getName() + " not found");
                }

                // else create a directory
                if (attrs != null) {
                    System.out.println("Directory exists IsDir=" + attrs.isDir());
                } else {
                    System.out.println("Creating dir " + sourceFile.getName());
                    sftpChannel.mkdir(sourceFile.getName());
                }

                for (File f: files) {
                    recursiveFolderUpload(f.getAbsolutePath(), destinationPath + "/" + sourceFile.getName());
                }

            }
        }

    }

    //
    private static void recursiveFolderDownload(String sourcePath, String destinationPath) throws SftpException, IOException {
        Vector<ChannelSftp.LsEntry> fileAndFolderList = sftpChannel.ls(sourcePath); // Let list of folder content

        //Iterate through list of folder content
        for (ChannelSftp.LsEntry item : fileAndFolderList) {

            if (!item.getAttrs().isDir()) { // Check if it is a file (not a directory).
//                if (!(new File(destinationPath + PATHSEPARATOR + item.getFilename())).exists()
//                        || (item.getAttrs().getMTime() > Long
//                        .valueOf(new File(destinationPath + PATHSEPARATOR + item.getFilename()).lastModified()
//                                / (long) 100)
//                        .intValue())) { // Download only if changed later.

                System.out.println("sourcePath + PATHSEPARATOR + item.getFilename(): " + sourcePath + PATHSEPARATOR + item.getFilename());
                System.out.println("destinationPath + PATHSEPARATOR + item.getFilename(): " + destinationPath + PATHSEPARATOR + item.getFilename());

                byte[] buffer = new byte[1024];
                BufferedInputStream bis = new BufferedInputStream(sftpChannel.get(sourcePath + PATHSEPARATOR + item.getFilename()));
                File newFile = new File(destinationPath + PATHSEPARATOR + item.getFilename());
                OutputStream os = new FileOutputStream(newFile);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                int readCount;
                while ((readCount = bis.read(buffer)) > 0) {
                    System.out.println("Writing: ...");
                    bos.write(buffer, 0, readCount);
                }
                bis.close();
                bos.close();

//                    }
            } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) {
                new File(destinationPath + PATHSEPARATOR + item.getFilename()).mkdirs(); // Empty folder copy.
                recursiveFolderDownload(sourcePath + PATHSEPARATOR + item.getFilename(),
                        destinationPath + PATHSEPARATOR + item.getFilename()); // Enter found folder on server to read its contents and create locally.
            }
        }
    }

    //
    public void writeParameter() throws IOException {
        String path = this.root + "/datasets/Parameter/parameter.txt";

        int maxEpochs = 1000;
        int alphabetSize = 5;
        double stepRatio = 0.5; /*** (int) (stepRatio * Tp.getDimColumns()); **/
        double paaRatio = 0.5;
        int pcover = 2; /*** amount - shapelets **/

        try {
            if(!this.aGUIComponents.iterationTextField.getText().equalsIgnoreCase("default")){
                Double num = Double.parseDouble(this.aGUIComponents.iterationTextField.getText());
                maxEpochs =  num.intValue();
            }
            if(!this.aGUIComponents.alphabetSizeTextField.getText().equalsIgnoreCase("DefaultValue")){
                Double num = Double.parseDouble(this.aGUIComponents.alphabetSizeTextField.getText());
                alphabetSize = num.intValue();
            }
            if(!this.aGUIComponents.pcoverTextField.getText().equalsIgnoreCase("DefaultValue")){
                Double num = Double.parseDouble(this.aGUIComponents.pcoverTextField.getText());
                pcover = num.intValue();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), "UTF-8"));
        try {
            out.write(maxEpochs + ",");
            out.write(alphabetSize + ",");
            out.write(stepRatio + ",");
            out.write(paaRatio + ",");
            out.write(pcover + "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
