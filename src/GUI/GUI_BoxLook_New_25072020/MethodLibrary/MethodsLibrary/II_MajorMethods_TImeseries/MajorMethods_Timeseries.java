package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.II_MajorMethods_TImeseries;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import DataStructures.FeaturePoint;
import DataStructures.Matrix;
import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.HSLColor.HSLColor;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.DistanceClassification.DistanceClassification;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;
import Looks.TSLook;
import TimeSeries.Distorsion;
import TimeSeries.TransformationFieldsGenerator;
import Utilities.GlobalValues;
import Utilities.Logging;
import com.jcraft.jsch.*;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import info.monitorenter.gui.chart.traces.painters.TracePainterLine;
import info.monitorenter.util.Range;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MajorMethods_Timeseries extends MajorMethods_Timeseries_abstract {
    /*---------------------------------------------------------------

    /*** ---------------------------------------------------------------------------------------------------------* */

    /*** ---------------------------------------------------------------------------------------------------------* */
    public MajorMethods_Timeseries(){}

    public MajorMethods_Timeseries(GUIComponents aGUIComponents, Variables aVariables){
        initialize(aGUIComponents, aVariables);
    }

    public void initialize(GUIComponents aGUIComponents, Variables aVariables){
        initializeReferenceParameters(aGUIComponents, aVariables);
    }

    /*** ---------------------------------------------------------------------------------------------------------* */

    /*---------------------------------------------------------------

     **                    loadDataSet()                              **

     ---------------------------------------------------------------*/
    public void loadDataSet(){
        String subroot_I = "/datasets/Grace_dataset/v_2/Grace_MEAN";
        String subroot_II = "/datasets/ItalyPowerDemand_dataset/v_1/ItalyPowerDemand";
        String subroot_III = "/datasets/Grace_dataset/v_3/Grace_MI";

        String TSGenerationPath = this.aVariables.root + subroot_II;
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new File(TSGenerationPath));
        chooser.setDialogTitle("Pick a time series dataset Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);

        //
        if (chooser.showOpenDialog(this.aGUIComponents.frmTimeSeries) == JFileChooser.APPROVE_OPTION) {

            this.aVariables.dataSetDirectory = chooser.getSelectedFile();
            System.out.println(" Selected dataset directory: " + this.aVariables.dataSetDirectory.getAbsolutePath() );
        } else {
            System.out.println("No Selection ");
            return;
        }

        try {
            /***** Modified part - for horizontal-line representation ***/
//            DataSet aDataSet = new DataSet();
//            aDataSet.LoadDataSetFolder(dataSetDirectory);
            /*** **/

            this.aVariables.dataSet = new DataSet();
            this.aVariables.dataset_withCurrentLabel = new DataSet(); /*** changeLabelSelection() initialize it **/

            /***** Modified part - for horizontal-line representation ***/
//            dataSet = new TSRepresentation(aDataSet);
            /*** **/

            this.aVariables.dataSet.LoadDataSetFolder(this.aVariables.dataSetDirectory);

//            System.out.println("loadDataSet() -> dataSet.numFeatures+10: " + (dataSet.numFeatures+10));

            this.aVariables.dataSet.NormalizeDatasetInstances();

            /*** Two choices: **/
            /*** 1. Present the line-lot-like oldest effect look. ------------> **/

            // createTSChartsAndTraces() before assign dataset !
            createTSChartsAndTraces();
            /*** set marks - centerChart **/
            createTSMarkCenterChart();
            createinterpolatedTSMarkCenterChart();

            /*** set marks - bottomChart **/
            createTSMarkBottomChart();

            /*** 2. Horizontal-line representation ------------> **/

            horizontalLineTransfer(this.aVariables.dataSet);

            /*** -> **/
            classfyTSLabels();

            /*** -> **/
            setTSLabelJList();

        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }

        // populate the list with the time series trajectory indexes

        this.aGUIComponents.noPointsTextField.setVisible(true);
    }

    /*---------------------------------------------------------------

     **                 classfyShapeletLabels()                     **

     ---------------------------------------------------------------*/
    public void classfyTSLabels(){
        try{
            /*** Load Label ***/
            ArrayList<Integer> TS_labelArryList = new ArrayList<> ();
            ArrayList<Integer> shapelet_LabelCountArrayList = new ArrayList<> ();

            for(Double d : this.aVariables.dataSet.GetnominalLabels()){
                TS_labelArryList.add(d.intValue());
            }

            for(Integer label: TS_labelArryList){
                shapelet_LabelCountArrayList.add(this.aVariables.dataSet.FilterByLabel(label).instances.size());
            }

            System.out.println("TS_labelArryList: " + TS_labelArryList);
            System.out.println("shapelet_LabelCountArrayList: " + shapelet_LabelCountArrayList);

            this.aVariables.TS_labelArryList = TS_labelArryList;
            this.aVariables.SPLet_labelCountArrayList = shapelet_LabelCountArrayList;
        }catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /*---------------------------------------------------------------

     **                    set_TSLabel_JList()                     **

     ---------------------------------------------------------------*/
    public void setTSLabelJList(){
        try{
            int size = this.aVariables.TS_labelArryList.size();

            DefaultListModel<String> model = new DefaultListModel<> ();

            for(int i=0; i<size; i++){
                model.addElement((String.valueOf(this.aVariables.TS_labelArryList.get(i))));
            }

            this.aGUIComponents.TS_labelList_Horizontal.setModel(model);

            if(this.aGUIComponents.TS_labelList_Horizontal.getModel().getSize() > 0)
            {
                this.aGUIComponents.TS_labelList_Horizontal.setSelectedIndex(0);
            }

        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /*---------------------------------------------------------------

     **                         set_TS_JList()                     **

     ---------------------------------------------------------------*/
    public void setTSJList(){
        try{
            int startPoint = 0, endPoint = 10000; /*** The Maximum always sets to the size of timeseries data set **/
            if(this.aVariables.initializeTS){ /*** If first time to load the TS and label, the timeSeriesRangeMaxTextField equals to lengh of timeseries list **/
                this.aGUIComponents.timeSeriesRangeMinTextField.setText("0");
            }else{ /*** Else ... **/
                startPoint = Integer.parseInt(this.aGUIComponents.timeSeriesRangeMinTextField.getText());
                this.aVariables.lastTSIndex = this.aGUIComponents.TS_List.getSelectedIndex();
            }
            System.out.println("lastTimeseriesListIndex: " + this.aVariables.lastTSIndex);

            int timeSeriesListLen = this.aVariables.dataset_withCurrentLabel.instances.size(); /*** After reassign the values, get the size **/
            endPoint = timeSeriesListLen;
            this.aGUIComponents.timeSeriesRangeMinTextField.setText(String.valueOf(startPoint));
            this.aGUIComponents.timeSeriesRangeMaxTextField.setText(String.valueOf(endPoint-1));

            // set the no of points text field from any of the trajectories
            this.aGUIComponents.noPointsTextField.setText("Num of time series: " + this.aVariables.dataset_withCurrentLabel.instances.size());

            DefaultListModel newListModel = new DefaultListModel(); /*** newListModel: New time series list with current label **/
            if(!this.aVariables.initialize_TS_list){
                this.aVariables.setting_TS_listModal = true;
            }

            if(this.aVariables.initialize_TS_list){
                this.aVariables.initialize_TS_list = false;
            }

//            for(int i = 0; i < dataset_withCurrentLabel.instances.size(); i++){}
            for(int i = startPoint; i < endPoint; i++)
            {
                newListModel.addElement(String.valueOf(i)); /*** addElement(String.valueOf(i)) ***/
            }

            this.aGUIComponents.TS_List.setModel(newListModel);

            /*** This setting will invoke the value change of timeseries list twice **/
            if(this.aGUIComponents.TS_List.getModel().getSize() > 0)
            {
                this.aGUIComponents.TS_List.setSelectedIndex(this.aVariables.lastTSIndex); /** default index: 0 **/
            }
        }catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /*---------------------------------------------------------------

     **             set_currentTSJlist_content()                     **

     ---------------------------------------------------------------*/
    public void setCurrentTSJlistContent(){
        try{
            /*** **/
            int selectedTSIndex = 0; /** default value: 0 **/

            if(this.aGUIComponents.TS_List.getModel().getSize() > 0) {
                try{
                    selectedTSIndex = Integer.parseInt( this.aGUIComponents.TS_List.getSelectedValue().toString() ); /** default index: 0 **/

                }catch( NullPointerException exc )
                {
                    System.out.println(exc);
                }
            }

            this.aVariables.TSDataInstance = this.aVariables.dataset_withCurrentLabel.instances.get(selectedTSIndex);

//            System.out.println("From changeSelectedTS() -> TSDataInstance.features.size(): " + TSDataInstance.features.size());

            this.aVariables.TSDataInstance_bottomChart = this.aVariables.TSDataInstance;

            this.aGUIComponents.labelTextField.setVisible(true);
            this.aGUIComponents.labelTextField.setText("Label class: " + ((int) this.aVariables.TSDataInstance.target));

            if(this.aVariables.load_SPLet_YesOrNo){
//                drawShapeletTrace_CenterChart();
//                shapelet_dotANDLine_plot();
                /*** stackModel **/
            }
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    //
    public void sshReadFile() {

        String user = "shiwenli";
        String password = "LIshiwen123@";
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
            String localFile = this.aVariables.dataSetDirectory.getPath();
            System.out.println("localFile: " + localFile);

            String remoteDir = "/home/comp/shiwenli/BSPCOVER/datasetInput/"; // /home/comp/shiwenli/datasetInput/

//            sftpChannel.put(localFile, remoteDir);
            recursiveFolderUpload(localFile, remoteDir);

            String parametPath = this.aVariables.root + "/datasets/ItalyPowerDemand_dataset/v_1/parameter/parameter.txt";

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
                String localDir = this.aVariables.root + "/datasets/ItalyPowerDemand_dataset/v_1/shapelet/shapelet&weight";
                recursiveFolderDownload(remoteDirShapelet, localDir);
            }catch (Exception e){
                e.printStackTrace();
            }

            //
        } catch (JSchException | SftpException | FileNotFoundException e) {

            System.out.println("End of this manager! Bye...11");
            if (session != null) {
                session.disconnect();
            }

            if (sftpChannel != null){
                sftpChannel.disconnect();
            }

            e.printStackTrace();
        } catch (IOException e) {
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
    private void writeParameter() throws IOException {
        String path = this.aVariables.root + "/datasets/ItalyPowerDemand_dataset/v_1/parameter/parameter.txt";

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

    //
    public void runBspcoverRemote() {
        try {
            writeParameter();
            sshReadFile();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*---------------------------------------------------------------

     **                    runBspcover()                              **

     ---------------------------------------------------------------*/
    public void runBspcover(){
//            this.aVariables.shapeletSubroot = "/datasets/Grace_dataset/v_3/shapelet/shapelet&weight";
        this.aVariables.shapeletSubroot = "/datasets/ItalyPowerDemand_dataset/v_1/shapelet/shapelet&weight";
            /*---------------------------------------------------------------**
             ******   The shapelets output path is in EfficientLTS.java!   ******
             ---------------------------------------------------------------*/

        // show the running info
        this.aGUIComponents.bspcoverInfoTextArea.setText("BSPCOVER Console: ");
        this.aGUIComponents.bspcoverInfoTextArea.paintImmediately(this.aGUIComponents.bspcoverInfoTextArea.getBounds());

        //Default parameter
        // 88+% 1000, 5, 0.5, 0.5, 2 (Grace_MI)
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
        //test path
        //String filedir = "experiment/Italy/";
        String filedir = this.aVariables.dataSetDirectory.getParentFile().getPath() + "/";

        try{
            EfficientLTSrun(filedir, this.aVariables.root, this.aVariables.shapeletSubroot, maxEpochs, alphabetSize, stepRatio, paaRatio, pcover);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void EfficientLTSrun(String filedir_, String root, String subroot, int maxEpochs_, int alphabetSize_, double stepRatio_, double paaRatio_, int pcover_) throws Exception {
        String filedir = filedir_;
        File file = new File(filedir);
        File[] files = file.listFiles();
        String ds;
        String sp = File.separator;

        //Default parameter
        int maxEpochs = maxEpochs_;
        int alphabetSize = alphabetSize_;
        double stepRatio = stepRatio_;
        double paaRatio = paaRatio_;
        int pcover = pcover_;

        for (int j = 0; j < files.length; j++) {
            ds = files[j].getName();

            // values of hyperparameters
            double eta = 0.1, lambdaW = 0.01, alpha = -25, L = 0.2, K = -1;

            double falsePositiveProbability = 0.0001;
            int expectedNumberOfElements = 60000;
            String trainSetPath = filedir + ds + sp + ds + "_TRAIN", testSetPath = filedir + ds + sp + ds + "_TEST";
            //String trainSetPath = filedir + ds, testSetPath = filedir + ds;

            Logging.println("dataset: " + files[j].getName() + " stepRatio: " + stepRatio + " alphabetSize: "
                    + alphabetSize, Logging.LogLevel.DEBUGGING_LOG);
            this.aGUIComponents.bspcoverInfoTextArea.setText("dataset: " + files[j].getName() + " stepRatio: " + stepRatio + " alphabetSize: "
                    + alphabetSize);

            long startTime = System.currentTimeMillis();

            // load dataset
            DataSet trainSet = new DataSet();
            trainSet.LoadDataSetFile(new File(trainSetPath));
            DataSet testSet = new DataSet();
            testSet.LoadDataSetFile(new File(testSetPath));

            // normalize the data instance
            trainSet.NormalizeDatasetInstances();
            testSet.NormalizeDatasetInstances();

//                    // output normalized dataset
//                    System.out.println("trainSet:");
//                    System.out.println(trainSet.GetNumInstances());
//                    System.out.println(trainSet.numFeatures);
//                    System.out.println(trainSet.instances.get(0).features.size());
//                    System.exit(0);
//                    for(int i=0;i<trainSet.GetNumInstances(); i++){
//                        for(int l=0; j< trainSet.numFeatures; l++){
//                            System.out.println(trainSet.GetNumInstances());
//                        }
//                    }

            // predictor variables T
            Matrix T = new Matrix();
            T.LoadDatasetFeatures(trainSet, false);
            T.LoadDatasetFeatures(testSet, true);
            // outcome variable O
            Matrix O = new Matrix();
            O.LoadDatasetLabels(trainSet, false);
            O.LoadDatasetLabels(testSet, true);

            /*** * */
            TimeSeries.EfficientLTS eLTS = new TimeSeries.EfficientLTS(root, subroot, this.aGUIComponents.bspcoverInfoTextArea);
            /*** * */

            // initialize the sizes of data structures
            eLTS.ITrain = trainSet.GetNumInstances();
            eLTS.ITest = testSet.GetNumInstances();
            eLTS.Q = T.getDimColumns();
            //Logging.println(eLTS.ITrain+" "+eLTS.ITest+" "+eLTS.Q);
            // set the time series and labels
            eLTS.T = T;
            eLTS.Y = O;

            eLTS.alphabetSize = alphabetSize;
            eLTS.stepRatio = stepRatio;
            eLTS.paaRatio = paaRatio;
            eLTS.pcover = pcover;

            eLTS.falsePositiveProbability = falsePositiveProbability;
            eLTS.expectedNumberOfElements = expectedNumberOfElements;

            // set the learn rate and the number of iterations
            eLTS.maxIter = maxEpochs;

            eLTS.slidingWindow_miniunit = (int) (L * T.getDimColumns());
            //eLTS.R = R;
            // set the regularization parameter
            eLTS.lambdaW = lambdaW;
            eLTS.eta = eta;
            eLTS.alpha = alpha;
            trainSet.ReadNominalTargets();
            eLTS.nominalLabels = new ArrayList<Double>(trainSet.nominalLabels);

            long before_learn = System.currentTimeMillis();

            eLTS.Learn(trainSet);
            long endTime = System.currentTimeMillis();

            String str = String.valueOf(eLTS.ReduceGetMCRTestSet()) + " " + String.valueOf(eLTS.GetMCRTrainSet()) + " "
                    + String.valueOf(eLTS.AccuracyLossTrainSet()) + " "
                    + "learn time=" + (endTime - before_learn) + " "
                    + "before_learn time=" + (before_learn - startTime);
            Logging.println(str
                    , Logging.LogLevel.DEBUGGING_LOG
            );
            this.aGUIComponents.bspcoverInfoTextArea.append("\n" + str);
        }
    }

    /*** -----------------------------------------------------> * */
    public void horizontalLineTransfer(DataSet aDataSet){
        this.aVariables.aTSLook = new TSLook(aDataSet);
        this.aVariables.aTSLook.PAALike_initial();
    }

    public ArrayList<Double> horizontalLineLookTSBothCharts(DataInstance aTSDataInstance){
        ArrayList<Double> aTS_Arylist = this.aVariables.aTSLook.revalue(this.aVariables.aTSLook.arrayListTransfer(aTSDataInstance));
        return aTS_Arylist;
    }
    /*** -----------------------------------------------------> * */

    /*** ----- **/
    public void dotLineSwitch(ActionEvent e){
        AbstractButton aButton = (AbstractButton)e.getSource();
        if(aButton.getText().equalsIgnoreCase("DOT")){
            this.aVariables.switchDot = true;
            this.aGUIComponents.btnClearAllTraces.doClick();
            //
            this.aMajorMethods_Shapelet.changeSelectedShapelet();
            changeSelectedTS();
        }else if(aButton.getText().equalsIgnoreCase("LINE")){
            this.aVariables.switchDot = false;
            this.aGUIComponents.btnClearAllTraces.doClick();
            //
            this.aMajorMethods_Shapelet.changeSelectedShapelet();
            changeSelectedTS();
        }
    }

    /*---------------------------------------------------------------

     **                    createTSChartsAndTraces()                              **

     ---------------------------------------------------------------*/
    public void createTSChartsAndTraces(){

        // Test
        //
        JPanel multiJpanelsPanel = new JPanel();
//        multiJpanelsPanel.setLayout(new MigLayout("wrap 1", "[]0[]", "[]5[]"));
        multiJpanelsPanel.setLayout(new MigLayout());
        multiJpanelsPanel.setBackground(Color.DARK_GRAY);
        int numOfCharts = 10;
        this.aVariables.multiCharts = new Chart2D[numOfCharts];
        this.aGUIComponents.lblMultiChartTSClass = new JLabel[numOfCharts];
        this.aGUIComponents.lblMultiChartTSNum = new JLabel[numOfCharts];
        this.aGUIComponents.lblMultiChartSPLetNum = new JLabel[numOfCharts];
        this.aGUIComponents.lblMultiChartSPLetClass = new JLabel[numOfCharts];
        this.aGUIComponents.lblTopk = new JLabel[numOfCharts];
        Font font = new Font("SansSerif", Font.PLAIN, 8);
        for(int i=0; i<numOfCharts; i++){
            JPanel aPanel = new JPanel();
            aPanel.setLayout(new MigLayout()
            );
            aPanel.setBackground(Color.WHITE);
            // Labels first
            /***  -------------------------------- **/
            JLabel lblMultiChartTSClass = new JLabel();
            lblMultiChartTSClass.setText("Timeseries Label:");
            lblMultiChartTSClass.setBorder(null);
//            lblMultiChartTSClass.setFont(font);
            lblMultiChartTSClass.setFont(new Font("SansSerif", Font.BOLD, 10));
            lblMultiChartTSClass.setForeground(Color.RED);

            /***  -------------------------------- **/
            JLabel lblMultiChartTSNum = new JLabel();
            lblMultiChartTSNum.setText("Timeseries No.:");
            lblMultiChartTSNum.setBorder(null);
//            lblMultiChartTSClass.setFont(font);
            lblMultiChartTSNum.setFont(new Font("SansSerif", Font.BOLD, 10));
            lblMultiChartTSNum.setForeground(Color.BLUE);

            /***  -------------------------------- **/
            JLabel lblMultiChartSPLetClass = new JLabel();
            lblMultiChartSPLetClass.setText("Shapelet Label.:");
            lblMultiChartSPLetClass.setBorder(null);
            lblMultiChartSPLetClass.setFont(font);
            lblMultiChartSPLetClass.setForeground(Color.BLACK);

            /***  -------------------------------- **/
            JLabel lblMultiChartSPLetNum = new JLabel();
            lblMultiChartSPLetNum.setText("Shapelet No.:");
            lblMultiChartSPLetNum.setBorder(null);
            lblMultiChartSPLetNum.setFont(font);
            lblMultiChartSPLetNum.setForeground(Color.ORANGE);

            /***  -------------------------------- **/
            JLabel lblTopK = new JLabel();
            lblTopK.setText("Top ");
            lblTopK.setBorder(null);
            lblTopK.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblTopK.setForeground(Color.MAGENTA);

            aPanel.add(lblMultiChartSPLetClass);
            aPanel.add(lblMultiChartSPLetNum, "gapright 70");
            aPanel.add(lblTopK, "wrap");
            aPanel.add(lblMultiChartTSClass);
            aPanel.add(lblMultiChartTSNum,"wrap");

            this.aGUIComponents.lblMultiChartTSClass[i] = lblMultiChartTSClass;
            this.aGUIComponents.lblMultiChartTSNum[i] = lblMultiChartTSNum;
            this.aGUIComponents.lblMultiChartSPLetClass[i] = lblMultiChartSPLetClass;
            this.aGUIComponents.lblMultiChartSPLetNum[i] = lblMultiChartSPLetNum;
            this.aGUIComponents.lblTopk[i] = lblTopK;

            // Charts second
            Chart2D aChart = new Chart2D();
            aChart.setPreferredSize(new Dimension((905-400)/2, (250)/2));
            aChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
            aChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));

//            multiJpanelsPanel.add(aChart,  "span");
            aPanel.add(aChart,  "span");
            this.aVariables.multiCharts[i] = aChart;

            multiJpanelsPanel.add(aPanel,  "wrap");
        }

        JScrollPane multiChartsScrollPane = new JScrollPane(multiJpanelsPanel);
        multiChartsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        multiChartsScrollPane.setBounds(0, 0, (905-400)/2+55, 580);
        this.aGUIComponents.layeredPane_multiCharts.add(multiChartsScrollPane);
        //

        this.aVariables.centerChartXL = -5;
        this.aVariables.centerChartXR = this.aVariables.dataset_withCurrentLabel.numFeatures+5;
        System.out.println("centerChartXR: " + this.aVariables.centerChartXR);

        this.aVariables.oldScale = 1.0;

        this.aVariables.centerChartWidth = (this.aVariables.centerChartXR - this.aVariables.centerChartXL)/2;
        System.out.println("centerChartWidth: " + this.aVariables.centerChartWidth);

        this.aVariables.centerChart = new Chart2D();
        this.aVariables.bottomChart = new Chart2D();

        /***** Create trace ***/
        this.aVariables.TSTrace = new Trace2DLtd(null);
        this.aVariables.TSTrace.setTracePainter(new TracePainterDisc(4));

        this.aVariables.TSTrace.setColor(Color.BLUE);
        this.aVariables.TSTrace.setStroke(new BasicStroke(4));

        this.aVariables.centerChart.addTrace(this.aVariables.TSTrace);

        /*** **/

        /*** initialize the localTrace **/
        this.aLocalLineTrace = new Trace2DLtd(null);;
        this.aVariables.centerChart.addTrace(this.aLocalLineTrace);

        // Test
        /*** **/
//        this.aVariables.multiCharts[0].addTrace(this.aVariables.TSTrace);
//        this.aVariables.multiCharts[0].addTrace(this.aLocalLineTrace);
//        this.aVariables.multiCharts[0].getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-10, this.aVariables.dataset_withCurrentLabel.numFeatures+10)));
//        System.out.println();
        /*** **/

        /*
        interpolatedTimeSeriesTrace = new Trace2DLtd(null);
        interpolatedTimeSeriesTrace.setColor(Color.PINK);
        interpolatedTimeSeriesTrace.setStroke(new BasicStroke(4));
        centerChart.addTrace(interpolatedTimeSeriesTrace);
        */

        this.aGUIComponents.centerChartPanel.add(this.aVariables.centerChart);
        this.aGUIComponents.bottomChartPanel.add(this.aVariables.bottomChart);

        this.aVariables.centerChart.setSize( this.aGUIComponents.centerChartPanel.getSize() );
        this.aVariables.bottomChart.setSize( this.aGUIComponents.bottomChartPanel.getSize() );

        this.aVariables.centerChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
//        centerChart.getAxisX().setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); /*** setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); ***/
        this.aVariables.centerChart.getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-5, this.aVariables.dataset_withCurrentLabel.numFeatures+5)));

        this.aVariables.bottomChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
//        bottomChart.getAxisX().setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); /*** setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); ***/
        this.aVariables.bottomChart.getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-5, this.aVariables.dataset_withCurrentLabel.numFeatures+5)));

        this.aVariables.centerChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));

        this.aVariables.bottomChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));

        /*---------------------------------------------------------------
         ---------------------------------------------------------------*/
        /*---------------------------------------------------------------
         ******************************************************************/

    }

    /*---------------------------------------------------------------

     **                  createTSMark_centerChart()                **

     ---------------------------------------------------------------*/
    public void createTSMarkCenterChart(){

        /***** ***/

        this.aVariables.TSMark_centerChart = new Trace2DLtd("Original Time Series Trace ————————— ");
        this.aVariables.TSMark_centerChart.setColor(Color.BLUE);

        this.aVariables.centerChart.addTrace(this.aVariables.TSMark_centerChart);
        /*** **/
    }

    /*---------------------------------------------------------------

     **                 createTSMark_bottomChart()                **

     ---------------------------------------------------------------*/
    public void createTSMarkBottomChart(){

        /***** ***/

        this.aVariables.TSMark_bottomChart = new Trace2DLtd("Time Series Traces ————————— ");
        this.aVariables.TSMark_bottomChart.setColor(Color.ORANGE);

        this.aVariables.bottomChart.addTrace(this.aVariables.TSMark_bottomChart);
        /*** **/
    }

    /*---------------------------------------------------------------

     **                         addTSTrace_centerChart()                       **

     ---------------------------------------------------------------*/
    public void addGlobalTSTraceOnCenterChart(){
        this.aVariables.centerChart.addTrace(this.aVariables.TSTrace);
    }

    /*---------------------------------------------------------------

     **          createinterpolatedTSMark_centerChart()            **

     ---------------------------------------------------------------*/
    public void createinterpolatedTSMarkCenterChart(){

        /***** ***/

        this.aVariables.interpolatedTSMark_centerChart = new Trace2DLtd("Interpolated Trace ————————— ");
        this.aVariables.interpolatedTSMark_centerChart.setColor(Color.RED);

        this.aVariables.centerChart.addTrace(this.aVariables.interpolatedTSMark_centerChart);
        /*** **/
    }

    /*---------------------------------------------------------------

     **                 setInitializeTSToFalse()                     **

     ---------------------------------------------------------------*/
    private void setInitializeTSToFalse(){
        this.aVariables.initializeTS = false; /*** Overwrite to 'false' **/
    }

    /*---------------------------------------------------------------

     **                    changeLabelSelection()                     **

     ---------------------------------------------------------------*/
    public void changeTSLabel(){

        if( this.aGUIComponents.shuffleDataSetCheckBox.isSelected() )
        {
            String tfFolder ="/Users/leone/Documents/BSPCOVER/DATESET_SOURCE/transformation_fields";
            TransformationFieldsGenerator.getInstance().transformationScale = 0.5;
            this.aVariables.dataset_withCurrentLabel = Distorsion.getInstance().DistortTransformationField(this.aVariables.dataset_withCurrentLabel, tfFolder);
        }

        try
        {
            /*** get current label **/
            float selectedLabelIndex = Float.parseFloat( this.aGUIComponents.TS_labelList_Horizontal.getSelectedValue().toString() );

            System.out.println("selectedLabelIndex: " + selectedLabelIndex);

            this.aVariables.dataset_withCurrentLabel = this.aVariables.dataSet.FilterByLabel(selectedLabelIndex);

            System.out.println("dataset_withCurrentLabel.instances.size(): " + this.aVariables.dataset_withCurrentLabel.instances.size());
            /*** **/

            /*** resetBottomTSTraceCount to 0 **/
            resetBottomTSTraceCount(0);

            setTSJList();
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
//
//        noPointsTextField.setVisible(true);
//        noPointsTextField.setText("No time series: " + dataset_withCurrentLabel.numFeatures );

        if(this.aVariables.initializeTS){
            setInitializeTSToFalse(); /*** Overwrite the initializeTS from 'true' to 'false' at the second time **/
        }

    }

    /*---------------------------------------------------------------

     **                    changeSelectedTS()                **

     ---------------------------------------------------------------*/
    public void changeSelectedTS(){

        /*** Stop an additonal call from the list by value change **/
        if(this.aVariables.setting_TS_listModal){
            this.aVariables.setting_TS_listModal = false;
            return;
        }
        try {
            setCurrentTSJlistContent();
            setInfomationOnChart();

            /*** 1. Draw shapelet first, -> It will clear all the traces on center chart and add TS trace back
             * (Because of the temp shapelet traces) **/
            if(this.aVariables.load_SPLet_YesOrNo){
                /*** Normal model **/
                /*** Why we call this? Because every time the distance between shapelet and TS is different! **/
                if(this.aVariables.switchDot){
                    if(this.aVariables.stackModelOn){
                        shapeletDotDirectlyConnectLinePlot("stack");
                    }else{
                        shapeletDotDirectlyConnectLinePlot("normal");
                    }
                }else{
                    if(this.aVariables.stackModelOn){
                        shapeletDotHorizontallyTransferredLinePlot("stack");
                    }else {
                        shapeletDotHorizontallyTransferredLinePlot("normal");
                    }
                }
            }
            /*** 2. Then, redraw TS trace **/
            /*** Horizontal dot plot **/ /*** Horizontal line plot **/
            if(this.aVariables.switchDot){
                TSDotDirectlyConnectLinePlot();
            }else{
                TSDotHorizontallyTransferredLinePlot();
            }
        }
        catch( Exception exc ) {
            exc.printStackTrace();
        }
    }

    /*---------------------------------------------------------------
     **                         colorHSLAdjust()                    **
     ---------------------------------------------------------------*/
    public Color colorHSLAdjust(String aColor, int count){
        HSLColor aHSLColor;
        float H, S, L;
        switch(aColor.toUpperCase()) {
            case "RED":
                // code block
                /*** Red: H=0, L=50, only change S from 100 -> 41 (color descending), since S =0 is black **/
                H=360;
                L=50;
                if((S=count+41)>99){
                    S=100;
                }

                aHSLColor = new HSLColor(H, S, L);
                break;
            case "DARK":
                /*** Dark Green: H=152, L=50, only change L from 100 -> 41 (color descending), since S =0 is black  **/
                // code block
                H=152;
                L=50;
                if((S=count+41)>99){
                    S=100;
                }

                aHSLColor = new HSLColor(H, S, L);
                break;
            default:
                // default "PINK" -> warning
                aHSLColor = new HSLColor(334, 100, 50);
        }

        return aHSLColor.getRGB();
    }

    /*** --------------------------------------------- **/
    public void setTSMultiChartsandTraces(int chartIndex, double[] arr, ArrayList<Double> aTSAraylist){
        try{
//            setScale();
            this.aVariables.multiCharts[chartIndex].removeAllTraces();

            if( aTSAraylist != null ){
                // Dot trace --------------------------

                Trace2DLtd dotTrace = new Trace2DLtd("Distance: " + String.format("%.5g%n", arr[0]));
//                Trace2DLtd dotTrace = new Trace2DLtd(null);
                dotTrace.setTracePainter(new TracePainterDisc(2));
                dotTrace.setColor(Color.BLUE);
                dotTrace.setStroke(new BasicStroke(2));
                //
                this.aVariables.multiCharts[chartIndex].addTrace(dotTrace);
                //
                int numPoints = aTSAraylist.size();
                for(int i = 0; i < numPoints; i++) {
                    dotTrace.addPoint(i, aTSAraylist.get(i));
                }

                // Line trace --------------------------
                if(this.aVariables.switchDot){
                    TSDotDirectlyConnectLineTopTenMultiCharts(aTSAraylist, chartIndex);
                }else{
                    TSDotTransferAndLineDraw(aTSAraylist, "top ten chart", chartIndex); // The second parameter: "top ten chart" (case insensitive)
                }
            }

        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(MajorMethods_Timeseries.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void TSDotDirectlyConnectLineTopTenMultiCharts(ArrayList<Double> singleAry, int chartIndex){
        Trace2DLtd lineTrace = new Trace2DLtd(null);
        lineTrace.setColor(Color.ORANGE);
        lineTrace.setStroke(new BasicStroke(2));
        lineTrace.setTracePainter(new TracePainterLine());
        //
        Chart2D aChart = this.aVariables.multiCharts[chartIndex];
        aChart.addTrace(lineTrace);
        //
        int numPoints = singleAry.size();
        for(int i = 0; i < numPoints; i++) {
            lineTrace.addPoint(i, singleAry.get(i));
        }
    }

    /*** --------------------------------------------- **/
    public void TSDotDirectlyConnectLinePlot(){
        drawTSTraceCenterChart(); // centerChart
        drawTSTraceBottomChart();
        TSLineDraw("center chart");
        TSLineDraw("bottom chart");
    }
    /*---------------------------------------------------------------

     **                    drawTSTrace_CenterChart()                    **

     ---------------------------------------------------------------*/
    // redraw the chart of the time series
    public void drawTSTraceCenterChart() {
        /*** Why I decide to comment these two "RemoveAlLPoints"? ***/
        try{
            setScale();
            this.aVariables.TSTrace.removeAllPoints();
            //
            if( this.aVariables.TSDataInstance != null ){
                int numPoints = this.aVariables.TSDataInstance.features.size();
                System.out.println("-> TSDataInstance.features.size():" + numPoints);
                for(int i = 0; i < numPoints; i++) {
                    FeaturePoint p = this.aVariables.TSDataInstance.features.get(i);
                    if( p.status == FeaturePoint.PointStatus.PRESENT && p.value != GlobalValues.MISSING_VALUE )
                        this.aVariables.TSTrace.addPoint(i, p.value);
                }
            }

        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(MajorMethods_Timeseries.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }
    /*---------------------------------------------------------------

     **                   drawTSTrace_BottomChart()                  **

     ---------------------------------------------------------------*/
    public void drawTSTraceBottomChart() { /****** Bug  ***/
        try{
            setScale_bottomChartSetRange();

            int startPoint = 0; /*** Best match piece's start point **/

            Trace2DLtd aTSTrace = new Trace2DLtd(null);
            aTSTrace.setTracePainter(new TracePainterDisc(1));

            if(this.aVariables.TSDataInstance_bottomChart.target <= 0.0){
//                aTSTrace.setColor(Color.DARK_GRAY);
                aTSTrace.setColor(colorHSLAdjust("DARK", this.aVariables.bottomTSTraceCount));
            }else{
//                aTSTrace.setColor(Color.RED);
                aTSTrace.setColor(colorHSLAdjust("RED", this.aVariables.bottomTSTraceCount));
                /*** When to reset the color scale: after the shapelet changed **/
            }

            aTSTrace.setStroke(new BasicStroke(3));
            this.aVariables.bottomChart.addTrace(aTSTrace);

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/

            if (this.aVariables.TSDataInstance_bottomChart != null) {
                int numPoints_III = this.aVariables.TSDataInstance_bottomChart.features.size();
                /**** Here needs a block to set the values of i & numPoints.
                 * In order to shift the trace, it can add additional zero points by two sides -> double: 0.0 ***/
//                System.out.println("drawTSTrace_horizontally_BottomChart() -> this.aVariables.firstTSDrawing: " + this.aVariables.firstTSDrawing);
                if(this.aVariables.firstTSDrawing && this.aVariables.load_SPLet_YesOrNo){
                    /***** Update to the latest shapelet ***/
//                    System.out.println("Ever invoked");
                    this.aVariables.globalBestMatchSP = this.aVariables.globalStartPosition; /*** shapelet's start point **/
                    /*** -> **/

                    /*** **/
                    this.aVariables.globalBestMatchEP = this.aVariables.currentSPLet.size(); /*** shapelet's length **/
//                    System.out.println("globalBestMatchEP: " + this.aVariables.globalBestMatchEP);
                    this.aVariables.firstTSDrawing = false;
                }else{
                    startPoint = this.aVariables.globalBestMatchSP - this.aVariables.globalStartPosition;
                }
                /*** **/

                double newVal, oldVal=-1;
                for (int i = 0; i < numPoints_III; i++){ /*** Change the values of i & numPoints **/
                    newVal =  this.aVariables.TSDataInstance_bottomChart.features.get(i).value;
                    if(!(i==0) && newVal==oldVal){
                        continue;
                    }else{
                        oldVal=newVal;
                    }
                    aTSTrace.addPoint(i+startPoint, newVal);
                }
            }
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MajorMethods_Timeseries.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void TSLineDraw(String chartChoiceStr){
        try{
            if(chartChoiceStr.equalsIgnoreCase("center chart")){
                removeLocalTSTracePointsCenterChart();
//                System.out.println("I am here!");
                //
                this.aLocalLineTrace.setColor(Color.ORANGE);
                this.aLocalLineTrace.setStroke(new BasicStroke(3));
                this.aLocalLineTrace.setTracePainter(new TracePainterLine());

                /***** We need to change codes here  ***/
                /*** A bug here is the same timeseries may be presented by two totally different shapes **/
                if( this.aVariables.TSDataInstance != null ){
                    int numPoints = this.aVariables.TSDataInstance.features.size();
                    System.out.println("-> TSDataInstance.features.size():" + numPoints);
                    for(int i = 0; i < numPoints; i++) {
                        FeaturePoint p = this.aVariables.TSDataInstance.features.get(i);
                        if( p.status == FeaturePoint.PointStatus.PRESENT && p.value != GlobalValues.MISSING_VALUE )
                            this.aLocalLineTrace.addPoint(i, p.value);
                    }
                }
                /*****  ***/
            }else if(chartChoiceStr.equals("bottom chart")){
                setScale_bottomChartSetRange();

                int startPoint = 0; /*** Best match piece's start point **/

                Trace2DLtd aTSTrace = new Trace2DLtd(null);
                aTSTrace.setTracePainter(new TracePainterLine());

                if(this.aVariables.TSDataInstance_bottomChart.target <= 0.0){
//                aTSTrace.setColor(Color.DARK_GRAY);
                    aTSTrace.setColor(colorHSLAdjust("DARK", this.aVariables.bottomTSTraceCount));
                }else{
//                aTSTrace.setColor(Color.RED);
                    aTSTrace.setColor(colorHSLAdjust("RED", this.aVariables.bottomTSTraceCount));
                    /*** When to reset the color scale: after the shapelet changed **/
                }

                aTSTrace.setStroke(new BasicStroke(2));
                this.aVariables.bottomChart.addTrace(aTSTrace);

                /***** We need to change codes here  ***/
                /*** A bug here is the same timeseries may be presented by two totally different shapes **/

                if (this.aVariables.TSDataInstance_bottomChart != null) {
                    int numPoints_III = this.aVariables.TSDataInstance_bottomChart.features.size();
                    /**** Here needs a block to set the values of i & numPoints.
                     * In order to shift the trace, it can add additional zero points by two sides -> double: 0.0 ***/
//                System.out.println("drawTSTrace_horizontally_BottomChart() -> this.aVariables.firstTSDrawing: " + this.aVariables.firstTSDrawing);
                    if(this.aVariables.firstTSDrawing && this.aVariables.load_SPLet_YesOrNo){
                        /***** Update to the latest shapelet ***/
//                    System.out.println("Ever invoked");
                        this.aVariables.globalBestMatchSP = this.aVariables.globalStartPosition; /*** shapelet's start point **/
                        /*** -> **/

                        /*** **/
                        this.aVariables.globalBestMatchEP = this.aVariables.currentSPLet.size(); /*** shapelet's length **/
//                    System.out.println("globalBestMatchEP: " + this.aVariables.globalBestMatchEP);
                        this.aVariables.firstTSDrawing = false;
                    }else{
                        startPoint = this.aVariables.globalBestMatchSP - this.aVariables.globalStartPosition;
                    }

                    /*** **/

                    double newVal, oldVal=-1;
                    for (int i = 0; i < numPoints_III; i++){ /*** Change the values of i & numPoints **/
                        newVal =  this.aVariables.TSDataInstance_bottomChart.features.get(i).value;
                        if(!(i==0) && newVal==oldVal){
                            continue;
                        }else{
                            oldVal=newVal;
                        }
                        aTSTrace.addPoint(i+startPoint, newVal);
                    }
                }
                /*****  ***/
            }else{
                Logger logger
                        = Logger.getLogger(
                        DistanceClassification.class.getName());
                // log messages using log(Level level, String msg)
                logger.log(Level.WARNING, "Your chart selection is wrong. Please type it correctly before you invoke it.");
                throw new IllegalArgumentException();
            }
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(MajorMethods_Timeseries.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    /*** --------------------------------------------- **/
    /*---------------------------------------------------------------

     **         drawTSTrace_horizontally_CenterChart()              **

     ---------------------------------------------------------------*/
    public void drawTSTraceHorizontallyCenterChart(ArrayList<Double> aTSAraylist){
        /*** Draw horizontal lines **/
        /*** Why I decide to comment these two "RemoveAlLPoints"? ***/
        try{
            setScale();
            this.aVariables.TSTrace.removeAllPoints();
//            System.out.println("drawTSTrace_horizontally_CenterChart(ArrayList<Double> aTSAraylist) -> dataset_withCurrentLabel.numFeatures+10: " + (dataset_withCurrentLabel.numFeatures+10));

            /*
            interpolatedTimeSeriesTrace.removeAllPoints();
            */

            /*** To ensure multiple timeseries can leave on one canvas ***/
            /*** And at the same time, I create an independent clearAllTSTraces_AllCharts() method at the end to points on canvas ***/

            /*** !!! However, here's a thing, I do not satisfy the non-duplicated timeseries on canvas yet!!! ***/
            /*** -> Done now :) **/

        /*
        if( manipulatedTimeSeries != null)
        {
            for(int i = 0; i < manipulatedTimeSeries.features.size(); i++)
            {
                FeaturePoint p = manipulatedTimeSeries.features.get(i);
                if( p.status == PointStatus.PRESENT )
                    interpolatedTimeSeriesTrace.addPoint(i, p.value);
            }
        }
         */

            if( aTSAraylist != null ){
                int numPoints = aTSAraylist.size();
//                System.out.println("-> aTSAraylist.size():" + numPoints);
                double newVal, oldVal=-1;
                for(int i = 0; i < numPoints; i++)
                {
                    newVal =  aTSAraylist.get(i);
                    if(!(i==0) && newVal==oldVal){
                        continue;
                    }else{
                        oldVal=newVal;
                    }
                    this.aVariables.TSTrace.addPoint(i, newVal);
                }
            }

        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(MajorMethods_Timeseries.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    /*---------------------------------------------------------------

     **         drawTSTrace_horizontally_BottomChart()              **

     ---------------------------------------------------------------*/
    public void drawTSTraceHorizontallyBottomChart(ArrayList<Double> aTSAraylist){
        /*** Draw horizontal lines **/
        try{
            setScale_bottomChartSetRange();

            int startPoint = 0; /*** Best match piece's start point **/

            Trace2DLtd aTSTrace = new Trace2DLtd(null);
            aTSTrace.setTracePainter(new TracePainterDisc(1));

            if(this.aVariables.TSDataInstance_bottomChart.target <= 0.0){
//                aTSTrace.setColor(Color.DARK_GRAY);
                aTSTrace.setColor(colorHSLAdjust("DARK", this.aVariables.bottomTSTraceCount));
            }else{
//                aTSTrace.setColor(Color.RED);
                aTSTrace.setColor(colorHSLAdjust("RED", this.aVariables.bottomTSTraceCount));
                /*** When to reset the color scale: after the shapelet changed **/
            }

            aTSTrace.setStroke(new BasicStroke(3));
            this.aVariables.bottomChart.addTrace(aTSTrace);

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/

            if (aTSAraylist != null) {
                int numPoints_III = aTSAraylist.size();
                /**** Here needs a block to set the values of i & numPoints.
                 * In order to shift the trace, it can add additional zero points by two sides -> double: 0.0 ***/
//                System.out.println("drawTSTrace_horizontally_BottomChart() -> this.aVariables.firstTSDrawing: " + this.aVariables.firstTSDrawing);
                if(this.aVariables.firstTSDrawing && this.aVariables.load_SPLet_YesOrNo){
                    /***** Update to the latest shapelet ***/
//                    System.out.println("Ever invoked");
                    this.aVariables.globalBestMatchSP = this.aVariables.globalStartPosition; /*** shapelet's start point **/
                    /*** -> **/

                    /*** **/
                    this.aVariables.globalBestMatchEP = this.aVariables.currentSPLet.size(); /*** shapelet's length **/
//                    System.out.println("globalBestMatchEP: " + this.aVariables.globalBestMatchEP);
                    this.aVariables.firstTSDrawing = false;
                }else{
                    startPoint = this.aVariables.globalBestMatchSP - this.aVariables.globalStartPosition;
                }

                /*** **/

                double newVal, oldVal=-1;
                for (int i = 0; i < numPoints_III; i++){ /*** Change the values of i & numPoints **/
                    newVal =  aTSAraylist.get(i);
                    if(!(i==0) && newVal==oldVal){
                        continue;
                    }else{
                        oldVal=newVal;
                    }
                    aTSTrace.addPoint(i+startPoint, newVal);
                }
            }

        }catch (NullPointerException e){
            // Create a Logger
//            Logger logger
//                    = Logger.getLogger(
//                    MajorMethods_Timeseries.class.getName());
//
//            // log messages using log(Level level, String msg)
//            logger.log(Level.WARNING, e.toString());
            e.printStackTrace();
        }
    }

    public void increaseBottomTSTraceCount(int val){
        this.aVariables.bottomTSTraceCount = this.aVariables.bottomTSTraceCount + val;
    }

    public void resetBottomTSTraceCount(int val){
        this.aVariables.bottomTSTraceCount = val;
    }

    /*---------------------------------------------------------------

     **                    selectTSRange()                     **

     ---------------------------------------------------------------*/
    public void selectTSRange(int min, int max){
        if (max >= 0) {
            DefaultListModel newnewListModel = new DefaultListModel(); /*** newnewListModel: New time series list for scoping action **/
            for(int i = 0; i < this.aVariables.dataset_withCurrentLabel.instances.size(); i++)
            {
                if(i>=min && i<=max){
                    newnewListModel.addElement(String.valueOf(i)); /*** addElement(String.valueOf(i)) ***/
                }

            }
            this.aGUIComponents.TS_List.setModel(newnewListModel);
        }
        JOptionPane.showMessageDialog(this.aGUIComponents.frmTimeSeries,
                "Scoping successfully!");
    }

    /*** ---------------------------------------------------------------------------------------------------------* */

    /*** ---------------------------------------------------------------------------------------------------------* */

    public void TSDotTransferAndLineDraw(ArrayList<Double> anArylist, String chartChoiceStr){
        /*** divider must bo odd number **/
        int divider = 5;
        /*** **/

        /*** This value depends on the divder **/
        int startStep;

//        DataInstance mydata = TSDataInstance;

        ArrayList<Double> dataArylist = anArylist;

//        int size = mydata.features.size();

        int size = dataArylist.size();

        int myIndex;

//        FeaturePoint aData;

        double aData;

        /*** |_|_| **/
        /*** I_|_I_I_|_I_I_|_I **/
        /*** Draw the scale and measurement in paper **/
        int expandedSize = size * divider;
        Double[] dataArray = new Double[expandedSize];

        for(int i = 0; i < size; i++){

            /*** Draw the scale and measurement in paper **/
            startStep = (divider-1)/2;
            myIndex = divider*i + startStep;

//            aData = mydata.features.get(i);

            aData = dataArylist.get(i);

            for(int j=0; j<(divider-1)/2; j++){
                dataArray[myIndex-(1+j)] = aData;
            }

            dataArray[myIndex] = aData;

            for(int k=0; k<(divider-1)/2; k++){
                dataArray[myIndex+(1+k)] = aData;
            }

            dataArray[myIndex+1] = aData;
        }

        if(chartChoiceStr.equalsIgnoreCase("center chart")){
            /*** center chart **/
            horizontalLinePlotWithVerticalLineCenterChartController(divider, dataArray);
        }else if(chartChoiceStr.equals("bottom chart")){
            /*** bottom chart **/
            horizontalLinePlotWithVerticalLineBottomChartController(divider, dataArray);
        }else{
            Logger logger
                    = Logger.getLogger(
                    DistanceClassification.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, "Your chart selection is wrong. Please type it correctly before you invoke it.");
            throw new IllegalArgumentException();
        }

    }

    public void TSDotTransferAndLineDraw(ArrayList<Double> anArylist, String chartChoiceStr, int chartIndex){
        /*** divider must bo odd number **/
        int divider = 5;
        /*** **/

        /*** This value depends on the divder **/
        int startStep;

        ArrayList<Double> dataArylist = anArylist;

        int size = dataArylist.size();

        int myIndex;

        double aData;

        /*** |_|_| **/
        /*** I_|_I_I_|_I_I_|_I **/
        /*** Draw the scale and measurement in paper **/
        int expandedSize = size * divider;
        Double[] dataArray = new Double[expandedSize];

        for(int i = 0; i < size; i++){

            /*** Draw the scale and measurement in paper **/
            startStep = (divider-1)/2;
            myIndex = divider*i + startStep;

//            aData = mydata.features.get(i);

            aData = dataArylist.get(i);

            for(int j=0; j<(divider-1)/2; j++){
                dataArray[myIndex-(1+j)] = aData;
            }

            dataArray[myIndex] = aData;

            for(int k=0; k<(divider-1)/2; k++){
                dataArray[myIndex+(1+k)] = aData;
            }

            dataArray[myIndex+1] = aData;
        }

        if(chartChoiceStr.equalsIgnoreCase("top ten chart")){
            /*** center chart **/
            horizontalLinePlotWithVerticalLineTopTenChartController(divider, dataArray, chartIndex);
        }else{
            Logger logger
                    = Logger.getLogger(
                    DistanceClassification.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, "Your chart selection is wrong. Please type it correctly before you invoke it.");
            throw new IllegalArgumentException();
        }
    }

    // _Without_ vertical connecting line
    public void horizontalLinePlot_withoutVerticalLine_centerChart_controller(int divider, Double[] myDataArray){

        /*** Clear the line plots at first **/
//        clearHorizontalTrace_centerChart();

        double head = 0;
        double val;

        int aSize = myDataArray.length;

        /*** We need to control the position of each point **/
        ArrayList<Double> anArylist = new ArrayList<>();
        /*** **/

        /*** An index to record the start position of a set of points, initial value: 0 **/
        int indexP = 0;

        System.out.println("myDataArray.length: " + aSize);

        for(int i = 0; i < aSize; i++){
            val = myDataArray[i];

            if(head == val){
                anArylist.add(val);
                /*** -> i<(aSize-1): for the last loop **/
                if(i==(aSize-1)){
                    System.out.println("---------------------> Loop [i]: " + i);

                    /*** If not head differs the precedent value, plot the precedent values in a line at first**/
                    addLocalTraceCenterChart(divider, indexP, anArylist);
                    System.out.println("-------- > anArylist.size(): " + anArylist.size());
                    anArylist.clear();
                    System.out.println("anArylist.size() after clear(): " + anArylist.size());
                }
            }
            else{

                System.out.println("---------------------> Loop [i]: " + i);

                /*** If not head differs the precedent value, plot the precedent values in a line at first**/
                addLocalTraceCenterChart(divider, indexP, anArylist);
                System.out.println("-------- > anArylist.size(): " + anArylist.size());
                anArylist.clear();
                System.out.println("anArylist.size() after clear(): " + anArylist.size());

                /***** ***/
                indexP = i;
                anArylist.add(val);
                head = val;
                /*** **/
            }

        }

    }
    //

    // With vertical connecting line - top ten charts
    public void horizontalLinePlotWithVerticalLineTopTenChartController(int divider, Double[] myDataArray, int chartIndex){

        double head = 0;
        double val;

        int aSize = myDataArray.length;

        /*** We need to control the position of each point **/
        ArrayList<Double> anArylist = new ArrayList<>();
        /*** **/

        /*** An index to record the start position of a set of points, initial value: 0 **/
        int indexP = 0;

        /*** Version II test **/
        for(int i = 0; i < aSize; i++){
            val = myDataArray[i];
            anArylist.add(val);
        }

        addLocalTraceTopTenMultiCharts(divider, indexP, anArylist, chartIndex);
    }

    // With vertical connecting line - center chart
    public void horizontalLinePlotWithVerticalLineCenterChartController(int divider, Double[] myDataArray){

        /*** Use aLocalLineTrace instead of temp trace **/
        /*** Clear it at first **/
        /*** Remember to keep it on the center chart **/
        removeLocalTSTracePointsCenterChart();

        double head = 0;
        double val;

        int aSize = myDataArray.length;

        /*** We need to control the position of each point **/
        ArrayList<Double> anArylist = new ArrayList<>();
        /*** **/

        /*** An index to record the start position of a set of points, initial value: 0 **/
        int indexP = 0;

        /*** Version II test **/
        for(int i = 0; i < aSize; i++){
            val = myDataArray[i];
            anArylist.add(val);
        }

        addLocalTraceCenterChart(divider, indexP, anArylist);
    }

    // With vertical connecting line - bottom chart
    public void horizontalLinePlotWithVerticalLineBottomChartController(int divider, Double[] myDataArray){

        /*** No need to clear the line plots **/

        double head = 0;
        double val;

        int aSize = myDataArray.length;

        /*** We need to control the position of each point **/
        ArrayList<Double> anArylist = new ArrayList<>();
        /*** **/

        /*** An index to record the start position of a set of points, initial value: 0 **/
        int indexP = 0;

        /*** Version II test **/
        for(int i = 0; i < aSize; i++){
            val = myDataArray[i];
            anArylist.add(val);
        }
        addLocalTraceBottomChart(divider, indexP, anArylist);
    }

    public void addLocalTraceTopTenMultiCharts(int divider, int startP, ArrayList<Double> singleAry, int chartIndex){
        try{
            Trace2DLtd lineTrace = new Trace2DLtd(null);
            lineTrace.setColor(Color.ORANGE);
            lineTrace.setStroke(new BasicStroke(2));
            lineTrace.setTracePainter(new TracePainterLine());
            //
            Chart2D aChart = this.aVariables.multiCharts[chartIndex];
            aChart.addTrace(lineTrace);

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            if (singleAry != null) {

                int numPoint = singleAry.size();
//                System.out.println("-> singleAry.size():" + numPoint);
                double yP;
                double xP;
                int backwardIndex = (divider-1)/2;
                for(int i = 0; i < numPoint; i++)
                {
                    yP = singleAry.get(i);

                    /*** X position scale **/
                    /*** (i+startP)/3 -> Wrong (Integer discard), (i+startP)/3.0 -> Correct (Float transfer) **/
                    xP = (i+startP)/((double)divider)  - 1/((double)divider)*backwardIndex;

                    lineTrace.addPoint(xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(MajorMethods_Timeseries.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void addLocalTraceCenterChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            this.aLocalLineTrace.setColor(Color.ORANGE);
            this.aLocalLineTrace.setStroke(new BasicStroke(3));
            this.aLocalLineTrace.setTracePainter(new TracePainterLine());

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            if (singleAry != null) {

                int numPoint = singleAry.size();
//                System.out.println("-> singleAry.size():" + numPoint);
                double yP;
                double xP;
                int backwardIndex = (divider-1)/2;
                for(int i = 0; i < numPoint; i++)
                {
                    yP = singleAry.get(i);

                    /*** X position scale **/
                    /*** (i+startP)/3 -> Wrong (Integer discard), (i+startP)/3.0 -> Correct (Float transfer) **/
                    xP = (i+startP)/((double)divider)  - 1/((double)divider)*backwardIndex;

                    this.aLocalLineTrace.addPoint(xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(MajorMethods_Timeseries.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void addLocalTraceBottomChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            int startPoint = 0; /*** Best match segment's start point **/

            /*** Use temp trace instead of class global trace **/
            Trace2DLtd aTSTrace = new Trace2DLtd(null);

            if(this.aVariables.TSDataInstance_bottomChart.target <= 0.0){
//                aTSTrace.setColor(Color.DARK_GRAY);
                aTSTrace.setColor(colorHSLAdjust("DARK", this.aVariables.bottomTSTraceCount));
            }else{
//                aTSTrace.setColor(Color.RED);
                aTSTrace.setColor(colorHSLAdjust("RED", this.aVariables.bottomTSTraceCount));
            }

            aTSTrace.setStroke(new BasicStroke(1));
            aTSTrace.setTracePainter(new TracePainterLine());
            this.aVariables.bottomChart.addTrace(aTSTrace);

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            /*** --> setIndex() in changeLabelSelection() cause the issue !!! **/
            /*** Solution: set a if statement at the caller **/
            if (singleAry != null) {
                int numPoint = singleAry.size();
//                System.out.println("-> singleAry.size():" + numPoint);

                /**** Here needs a block to set the values of i & numPoints.
                 * In order to shift the trace, it can add additional zero points by two sides -> double: 0.0 ***/
                if(this.aVariables.firstTSDrawing_linePlot && this.aVariables.load_SPLet_YesOrNo){
                    /*** -> **/

//                    System.out.println("Ever invoked");
//                    this.aVariables.globalBestMatchSP = this.aVariables.globalStartPosition; /*** shapelet's start point **/
//
//                    this.aVariables.globalBestMatchEP = this.aVariables.currentShapelet.size(); /*** shapelet's length **/
//                    System.out.println("globalBestMatchEP: " + this.aVariables.globalBestMatchEP);
                    this.aVariables.firstTSDrawing_linePlot = false;
                }else{
//                    System.out.println("Or only invoked");
                    startPoint = this.aVariables.globalBestMatchSP - this.aVariables.globalStartPosition;
                }
                /*** **/

                double yP;
                double xP;
                int backwardIndex = (divider-1)/2;
                for(int i = 0; i < numPoint; i++) {
                    yP = singleAry.get(i);

                    /*** X position scale **/
                    /*** (i+startP)/3 -> Wrong (Integer discard), (i+startP)/3.0 -> Correct (Float transfer) **/
                    xP = (i+startP)/((double)divider)  - 1/((double)divider)*backwardIndex;

                    aTSTrace.addPoint(startPoint+xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(MajorMethods_Timeseries.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void removeLocalTSTracePointsCenterChart(){
        if(!this.aLocalLineTrace.isEmpty()){
            this.aLocalLineTrace.removeAllPoints();
        }
    }

    public void addLocalTSTraceonCenterChart(){ // Clear class call it. TS class is no need to call it.
        this.aVariables.centerChart.addTrace(this.aLocalLineTrace);
    }

    public void TSDotHorizontallyTransferredLinePlot(){
        increaseBottomTSTraceCount(1);
        ArrayList<Double> aryList = horizontalLineLookTSBothCharts(this.aVariables.TSDataInstance);
        drawTSTraceHorizontallyCenterChart(aryList); // centerChart
        drawTSTraceHorizontallyBottomChart(aryList);
        TSDotTransferAndLineDraw(aryList, "center chart");
        TSDotTransferAndLineDraw(aryList, "bottom chart");
    }

    /*** ---------------------------------------------------------------------------------------------------------* */

}
