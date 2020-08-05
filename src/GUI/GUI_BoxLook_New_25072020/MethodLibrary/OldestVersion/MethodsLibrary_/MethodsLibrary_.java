package GUI.GUI_BoxLook_New_25072020.MethodLibrary.OldestVersion.MethodsLibrary_;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import DataStructures.FeaturePoint;
import DataStructures.Matrix;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.OldestVersion.GUIComponents_.GUIComponents_;
import Looks.ShapeletLook;
import Looks.TSLook;
import TimeSeries.Distorsion;
import TimeSeries.EfficientLTS;
import TimeSeries.TransformationFieldsGenerator;
import Utilities.GlobalValues;
import Utilities.Logging;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import info.monitorenter.gui.chart.traces.painters.TracePainterLine;
import info.monitorenter.util.Range;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MethodsLibrary_ extends MethodsLibrary_abstract {

    /*** ---------------------------------------------------------------------------------------------------------* */
    /*---------------------------------------------------------------

     **                    MethodsLibrary()                              **

     ---------------------------------------------------------------*/
    public MethodsLibrary_(){
        initialize();
    }

    /*** ---------------------------------------------------------------------------------------------------------* */
    /*---------------------------------------------------------------

     **                    initialize()                              **

     ---------------------------------------------------------------*/
    public void initialize(){
        myGUIComponents = new GUIComponents_();
        myGUIComponents.frmTimeSeries.setVisible(true);
        activateCompoents();
        addChangeListener();
        addActionListner();
        addListSelectionListener();
    }

    public void activateCompoents(){
        timeSeriesRangeMinTextField = myGUIComponents.timeSeriesRangeMinTextField;
        timeSeriesRangeMaxTextField = myGUIComponents.timeSeriesRangeMaxTextField;
        noPointsTextField = myGUIComponents.noPointsTextField;
        shuffleDataSetCheckBox = myGUIComponents.shuffleDataSetCheckBox;
        timeSeriesList = myGUIComponents.timeSeriesList;
        TS_labelList_Horizontal = myGUIComponents.TS_labelList_Horizontal;
        spinner = myGUIComponents.spinner;

        btnZoomIn = myGUIComponents.btnZoomIn;
        btnZoomOut = myGUIComponents.btnZoomOut;
        btnMoveLeft = myGUIComponents.btnMoveLeft;
        btnMoveRight = myGUIComponents.btnMoveRight;
        btnSetTsIdRange = myGUIComponents.btnSetTsIdRange;
        timeSeriesRangeMinTextField = myGUIComponents.timeSeriesRangeMinTextField;
        timeSeriesRangeMaxTextField = myGUIComponents.timeSeriesRangeMaxTextField;
        btnSetShptIdRange = myGUIComponents.btnSetShptIdRange;
        shapeletsRangeMinTextField = myGUIComponents.shapeletsRangeMinTextField;
        shapeletsRangeMaxTextField = myGUIComponents.shapeletsRangeMaxTextField;
        btnSortedByLenShptIdASC = myGUIComponents.btnSortedByLenShptIdASC;
        btnSortedByLenShptIdDESC = myGUIComponents.btnSortedByLenShptIdDESC;
        btnClearAllTraces = myGUIComponents.btnClearAllTraces;
        btnLoadDataset = myGUIComponents.btnLoadDataset;
        btnLoadShapelet = myGUIComponents.btnLoadShapelet;
        btnSelectTop_K_Shapelets = myGUIComponents.btnSelectTop_K_Shapelets;
        btnRunBspcover = myGUIComponents.btnRunBspcover;
        shapeletJList = myGUIComponents.shapeletJList;
        shapeletLabelJList = myGUIComponents.shapeletLabelJList;

        frmTimeSeries = myGUIComponents.frmTimeSeries;
        noPointsTextField = myGUIComponents.noPointsTextField;
        TS_labelList_Horizontal = myGUIComponents.TS_labelList_Horizontal;
        timeSeriesList = myGUIComponents.timeSeriesList;
        bspcoverInfoTextArea = myGUIComponents.bspcoverInfoTextArea;
        iterationTextField = myGUIComponents.iterationTextField;
        alphabetSizeTextField = myGUIComponents.alphabetSizeTextField;
        pcoverTextField = myGUIComponents.pcoverTextField;
        centerChartPanel = myGUIComponents.centerChartPanel;
        bottomChartPanel = myGUIComponents.bottomChartPanel;
        labelTextField = myGUIComponents.labelTextField;
        imputeCheckBox = myGUIComponents.imputeCheckBox;
        shapeletJList = myGUIComponents.shapeletJList;
        chartI_TS_TextField = myGUIComponents.chartI_TS_TextField;
        chartI_Interpolated_TextField = myGUIComponents.chartI_Interpolated_TextField;
        chartI_Shapelet_TextField = myGUIComponents.chartI_Shapelet_TextField;
        chartII_Shapelet_TextField = myGUIComponents.chartII_Shapelet_TextField;

        numOfShapeletsTextField = myGUIComponents.numOfShapeletsTextField;
        shapeletsRangeMinTextField = myGUIComponents.shapeletsRangeMinTextField;
        shapeletsRangeMaxTextField = myGUIComponents.shapeletsRangeMaxTextField;
        shapeletLabelJList = myGUIComponents.shapeletLabelJList;
        frmTimeSeriesLayerFirst = myGUIComponents.frmTimeSeriesLayerFirst;
        top_K_shapeletsTextField = myGUIComponents.top_K_shapeletsTextField;
        topRightPanel = myGUIComponents.topRightPanel;
        labelShapeletTextField = myGUIComponents.labelShapeletTextField;
        distanceSTTextField = myGUIComponents.distanceSTTextField;
        distanceSTTextField_II = myGUIComponents.distanceSTTextField_II;
        frmTimeSeries = myGUIComponents.frmTimeSeries;
        shapeletJList = myGUIComponents.shapeletJList;
        spinner = myGUIComponents.spinner;

    }

    /*---------------------------------------------------------------

     **                    addChangeListener()                              **

     ---------------------------------------------------------------*/
    public void addChangeListener(){
        /***  -------------------------------- **/

        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                float scale = ((Double)spinner.getValue()).floatValue();
                setScale(scale);
            }
        });
    }

    /*---------------------------------------------------------------

     **                    addActionListner()                              **

     ---------------------------------------------------------------*/
    public void addActionListner(){
        /***  -------------------------------- **/

        btnZoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                timeSeriesMoveLeft();
                timeSeriesZoomIn();
            }
        });

        /***  -------------------------------- **/

        btnZoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                timeSeriesMoveLeft();
                timeSeriesZoomOut();
            }
        });

        /***  -------------------------------- **/

        btnMoveLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                timeSeriesMoveLeft();
                timeSeriesMoveRight();
            }
        });

        /***  -------------------------------- **/

        btnMoveRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                timeSeriesMoveRight();
                timeSeriesMoveLeft();
            }
        });

        /***  -------------------------------- **/

        btnSetTsIdRange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectTSRange(Integer.parseInt(timeSeriesRangeMinTextField.getText()),Integer.parseInt(timeSeriesRangeMaxTextField.getText()));
            }
        });

        /***  -------------------------------- **/

        btnSetShptIdRange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectShapeletRange(Integer.parseInt(shapeletsRangeMinTextField.getText()),Integer.parseInt(shapeletsRangeMaxTextField.getText()));
            }
        });

        /***  -------------------------------- **/

        btnSortedByLenShptIdASC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortedASC();
            }
        });

        /***  -------------------------------- **/
        btnSortedByLenShptIdDESC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortedDESC();
            }
        });

        /***  -------------------------------- **/

        btnClearAllTraces.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                clearAllTSTraces_AllCharts();
            }
        });

        /***  -------------------------------- **/

        btnLoadDataset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                loadDataSet();
                System.out.println("loadDataSet() invoked");
            }
        });

        /***  -------------------------------- **/

        btnLoadShapelet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadShapelet();
                /*** Default sort ***/
//                sortedByLength(); /*** Shiwen: I think the shapelets have sorted by hierarchy in EfficientLTS (but not by length) **/
            }
        });

        /***  -------------------------------- **/

        btnSelectTop_K_Shapelets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectTop_K_Shapelets();
                /*** Default sort ***/
//                sortedByLength();
            }
        });

        /***  -------------------------------- **/

        btnRunBspcover.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    runBspcover();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    /*---------------------------------------------------------------

     **                    addListSelectionListener()                              **

     ---------------------------------------------------------------*/
    public void addListSelectionListener(){

        /***  -------------------------------- **/

        TS_labelList_Horizontal.addListSelectionListener(new ListSelectionListener() {
            public void contentsChanged(ListDataEvent e){

            }
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {//This line prevents double events
                    changeLabelSelection();
                }
            }
        });

        /***  -------------------------------- **/

        timeSeriesList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg1) {
                if (!arg1.getValueIsAdjusting()) {//This line prevents double events
                    System.out.println("Invoke changeSelectedTS()");
                    changeSelectedTS();
                }
                /*** - list will fire the final event - selection operation is finished
                 - getValueIsAdjusting=false now, you can do whatever you want with final selection
                 To sum up - those additional events are fired to let you completely control list behavior on selection changes (on selection change sequence to be exact). You might want to ignore the selection changes when getValueIsAdjusting=true since there always will be a final event with getValueIsAdjusting=false which will inform you that selection changes are finished.
                 Also, when you change selection with key buttons list wouldn't know if you are going to change it after first key press or not, so getValueIsAdjusting will be always false for such changes.***/

            }
        });

        /***  -------------------------------- **/

        shapeletJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    changeSelectedShapelet();
                }
            }
        });

        /***  -------------------------------- **/
        shapeletLabelJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    /*** -> **/
                    latest_aClassShapelet();
                    setShapeletJList();
                    /*** Update to the latest shaplet (globally) **/
                    latest_aShapelet();
                }
            }
        });
    }

    /*** ---------------------------------------------------------------------------------------------------------* */
    /*---------------------------------------------------------------

     **                    loadDataSet()                              **

     ---------------------------------------------------------------*/
    public void loadDataSet(){
        String subroot_I = "/datasets/Grace_dataset/v_2/Grace_MEAN";
        String subroot_II = "/datasets/ItalyPowerDemand_dataset/v_1/ItalyPowerDemand";

        String TSGenerationPath = root + subroot_I;
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory(new File(TSGenerationPath));
        chooser.setDialogTitle("Pick a time series dataset Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);

        //
        if (chooser.showOpenDialog(frmTimeSeries) == JFileChooser.APPROVE_OPTION) {

            dataSetDirectory = chooser.getSelectedFile();
            System.out.println(" Selected dataset directory: " + dataSetDirectory.getAbsolutePath() );
        }
        else {
            System.out.println("No Selection ");
            return;
        }

        try {
            /***** Modified part - for horizontal-line representation ***/
//            DataSet aDataSet = new DataSet();
//            aDataSet.LoadDataSetFolder(dataSetDirectory);
            /*** **/

            dataSet = new DataSet();
            dataset_withCurrentLabel = new DataSet(); /*** changeLabelSelection() initialize it **/

            /***** Modified part - for horizontal-line representation ***/
//            dataSet = new TSRepresentation(aDataSet);
            /*** **/

            dataSet.LoadDataSetFolder(dataSetDirectory);

//            System.out.println("loadDataSet() -> dataSet.numFeatures+10: " + (dataSet.numFeatures+10));

            dataSet.NormalizeDatasetInstances();


            /*** Two choices: **/
            /*** 1. Present the line-lot-like oldest effect look. ------------> **/

            // createTSChartsAndTraces() before assign dataset !
            createTSChartsAndTraces();
            /*** set marks - centerChart **/
            createTSMark_centerChart();
            createinterpolatedTSMark_centerChart();

            /*** set marks - bottomChart **/
            createTSMark_bottomChart();

            /*** 2. Horizontal-line representation ------------> **/

            horizontalLineTransfer(dataSet);

            /*** -> **/
            classfy_TS_Labels();

            /*** -> **/
            set_TSLabel_JList();

            /*** -> **/
//            set_TS_JList(); // Do not invoke this function, it will cause duplicate invoking
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }

        // populate the list with the time series trajectory indexes

        noPointsTextField.setVisible(true);

    }

    /*---------------------------------------------------------------

     **                 classfyShapeletLabels()                     **

     ---------------------------------------------------------------*/
    public void classfy_TS_Labels(){
        try{
            /*** Load Label ***/
            ArrayList<Integer> TS_labelArryList = new ArrayList<> ();
            ArrayList<Integer> TS_shapeletLabelCountArrayList = new ArrayList<> ();

            for(Double d : dataSet.GetnominalLabels()){
                TS_labelArryList.add(d.intValue());
            }

            for(Integer label: TS_labelArryList){
                TS_shapeletLabelCountArrayList.add(dataSet.FilterByLabel(label).instances.size());
            }

            System.out.println("TS_labelArryList: " + TS_labelArryList);
            System.out.println("TS_shapeletLabelCountArrayList: " + TS_shapeletLabelCountArrayList);

            this.TS_labelArryList = TS_labelArryList;
            this.TS_shapeletLabelCountArrayList = TS_shapeletLabelCountArrayList;
        }catch(Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /*---------------------------------------------------------------

     **                    set_TSLabel_JList()                     **

     ---------------------------------------------------------------*/
    public void set_TSLabel_JList(){

        try{

            int size = TS_labelArryList.size();

            DefaultListModel<String> model = new DefaultListModel<> ();

            for(int i=0; i<size; i++){
                model.addElement((String.valueOf(TS_labelArryList.get(i))));
            }

            TS_labelList_Horizontal.setModel(model);

            if(TS_labelList_Horizontal.getModel().getSize() > 0)
            {
                TS_labelList_Horizontal.setSelectedIndex(0);
            }

        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }

    }

    /*---------------------------------------------------------------

     **                  setShapeletComboBox()                     **

     ---------------------------------------------------------------*/
    public void set_TS_JList(){

        try{


            int current_label_index = TS_labelList_Horizontal.getSelectedIndex();

            System.out.println("Total: " + dataSet.instances.size() + " time series.");
            for(int i=0; i<TS_labelArryList.size(); i++){
                System.out.println("Label [" + TS_labelArryList.get(i).toString() + "] list: " + dataSet.FilterByLabel(TS_labelArryList.get(i)).instances.size() + " time series.");
            }

            /***** ***/
            dataset_withCurrentLabel = dataSet.FilterByLabel(TS_labelArryList.get(current_label_index)); /*** dataset_withCurrentLabel stores the time series with current label selection, e.g. default: 0.0 **/

            DefaultListModel listModel = new DefaultListModel();
            for(int i = 0; i < dataset_withCurrentLabel.instances.size(); i++)
            {
                listModel.addElement(String.valueOf(i)); /*** addElement(String.valueOf(i)) ***/
            }
            timeSeriesList.setModel(listModel); /*** Assign time series N0. to timeSeriesList **/
            System.out.println("Current number of time series: " + dataset_withCurrentLabel.instances.size() + " with label " + current_label_index + ".");

            if(timeSeriesList.getModel().getSize() > 0)
            {
                timeSeriesList.setSelectedIndex(0);
            }
            /*** **/
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }

    }

    /*---------------------------------------------------------------

     **                    runBspcover()                              **

     ---------------------------------------------------------------*/
    public void runBspcover() throws IOException {
//        String subroot_I = "/datasets/Grace_dataset/v_2/Grace_MEAN";
//        String subroot_II = "/datasets/ItalyPowerDemand_dataset/v_1/ItalyPowerDemand";
        String myroot = "/";
        String mysubroot = "/";
        String shapeletGenerationPath = root + mysubroot;

        /*---------------------------------------------------------------**
         ******   The shapelets output path is in EfficientLTS.java!   ******
         ---------------------------------------------------------------*/


        // show the running info
        bspcoverInfoTextArea.setText("BSPCOVER Console: ");
        bspcoverInfoTextArea.paintImmediately(bspcoverInfoTextArea.getBounds());

        //test path
        //String filedir = "experiment/Italy/";
        String filedir = dataSetDirectory.getParentFile().getPath();
        File file = new File(filedir);
        File[] files = file.listFiles();
        String ds;
        String sp = File.separator;

        //Default parameter
        int maxEpochs = 1000;
        int alphabetSize = 4;
        double stepRatio = 0.5; /*** (int) (stepRatio * Tp.getDimColumns()); **/
        double paaRatio = 0.2;
        int pcover = 1; /*** amount - shapelets **/

        maxEpochs =  Integer.parseInt(iterationTextField.getText());
        alphabetSize = Integer.parseInt(alphabetSizeTextField.getText());
        pcover = Integer.parseInt(pcoverTextField.getText());

        for (int j = 0; j < files.length; j++) {
            ds = files[j].getName();

            // values of hyperparameters
            double eta = 0.1, lambdaW = 0.01, alpha = -25, L = 0.2, K = -1;
            double falsePositiveProbability = 0.0001;
            int expectedNumberOfElements = 60000;
            String trainSetPath = filedir  + sp + ds + sp + ds + "_TRAIN", testSetPath = filedir + sp + ds + sp +  ds + "_TEST";
            //String trainSetPath = filedir + ds, testSetPath = filedir + ds;

            Logging.println("dataset: " + files[j].getName() + " stepRatio: " + stepRatio + " alphabetSize: "
                    + alphabetSize, Logging.LogLevel.DEBUGGING_LOG);
            bspcoverInfoTextArea.append("Dataset: " + files[j].getName());
            //bspcoverInfoTextArea.append("\n"+"Parameters file: experiment/parameter.txt");
            bspcoverInfoTextArea.append("\n"+"Iteration: "+ maxEpochs + ", pCover: "+ pcover + ", alphabetSize: " + alphabetSize);
            bspcoverInfoTextArea.append("\n"+"BSPCOVER Running ...");
            bspcoverInfoTextArea.paintImmediately(bspcoverInfoTextArea.getBounds());

            long startTime = System.currentTimeMillis();

            // load dataset
            DataSet trainSet = new DataSet();
            trainSet.LoadDataSetFile(new File(trainSetPath));
            DataSet testSet = new DataSet();
            testSet.LoadDataSetFile(new File(testSetPath));

            // normalize the data instance
            trainSet.NormalizeDatasetInstances();
            testSet.NormalizeDatasetInstances();

            // predictor variables T
            Matrix T = new Matrix();
            T.LoadDatasetFeatures(trainSet, false);
            T.LoadDatasetFeatures(testSet, true);
            // outcome variable O
            Matrix O = new Matrix();
            O.LoadDatasetLabels(trainSet, false);
            O.LoadDatasetLabels(testSet, true);

            EfficientLTS eLTS = new EfficientLTS(myroot, mysubroot);
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

            Logging.println(
                    String.valueOf(eLTS.ReduceGetMCRTestSet()) + " " + String.valueOf(eLTS.GetMCRTrainSet()) + " "
                            + String.valueOf(eLTS.AccuracyLossTrainSet()) + " "
                            + "learn time=" + (endTime - before_learn) + " "
                            + "before_learn time=" + (before_learn - startTime), Logging.LogLevel.DEBUGGING_LOG
            );
            bspcoverInfoTextArea.append("\n" + "BSPCOVER Done !");
            bspcoverInfoTextArea.append("\n" + "Test Error rate: "+ String.valueOf(eLTS.ReduceGetMCRTestSet())
                    + ", Train Error rate: " + String.valueOf(eLTS.GetMCRTrainSet())
                    + ", Test Loss: " + String.valueOf(eLTS.AccuracyLossTrainSet()));
            bspcoverInfoTextArea.append(("\n" + "learn time: " + (endTime - before_learn) + "ms" + ", initial time: " + (before_learn - startTime) + "ms"));
            bspcoverInfoTextArea.paintImmediately(bspcoverInfoTextArea.getBounds());
            bspcoverInfoTextArea.append("\n" + "Shapelete Generated path: ");
            bspcoverInfoTextArea.append("\n"+ shapeletGenerationPath);
            bspcoverInfoTextArea.append("\n"+ "Generate shapelets successfully !");
            bspcoverInfoTextArea.paintImmediately(bspcoverInfoTextArea.getBounds());

        }

    }

    /*** -----------------------------------------------------> * */
    public void horizontalLineTransfer(DataSet aDataSet){
        aTSLook = new TSLook(aDataSet);
        aTSLook.PAALike_initial();
    }

    public ArrayList<Double> horizontalLineLook_TS_centerChart(DataInstance aTSDataInstance){
        ArrayList<Double> aTS_Arylist = aTSLook.revalue(aTSLook.arrayListTransfer(aTSDataInstance));
        return aTS_Arylist;
    }

    public void createHorizontalLineLook_TS_bottomChart(){

    }
    /*** -----------------------------------------------------> * */

    /*---------------------------------------------------------------

     **                    createTSChartsAndTraces()                              **

     ---------------------------------------------------------------*/
    public void createTSChartsAndTraces(){


        centerChartXL = -10;
        centerChartXR = dataset_withCurrentLabel.numFeatures+10;
        System.out.println("centerChartXR: " + centerChartXR);

        oldScale = 1.0;

        centerChartWidth = (centerChartXR - centerChartXL)/2;
        System.out.println("centerChartWidth: " + centerChartWidth);

        centerChart = new Chart2D();
        bottomChart = new Chart2D();

        /***** Create trace ***/
        TSTrace = new Trace2DLtd(null);
        TSTrace.setTracePainter(new TracePainterDisc(4));
        TSTrace.setColor(Color.BLUE);
        TSTrace.setStroke(new BasicStroke(4));

        centerChart.addTrace(TSTrace);
        /*** **/

        /*
        interpolatedTimeSeriesTrace = new Trace2DLtd(null);
        interpolatedTimeSeriesTrace.setColor(Color.PINK);
        interpolatedTimeSeriesTrace.setStroke(new BasicStroke(4));
        centerChart.addTrace(interpolatedTimeSeriesTrace);
        */

        centerChartPanel.add(centerChart);
        bottomChartPanel.add(bottomChart);

        centerChart.setSize( centerChartPanel.getSize() );
        bottomChart.setSize( bottomChartPanel.getSize() );

        centerChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
//        centerChart.getAxisX().setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); /*** setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); ***/
        centerChart.getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-10, dataset_withCurrentLabel.numFeatures+10)));

        bottomChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
//        bottomChart.getAxisX().setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); /*** setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); ***/
        bottomChart.getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-10, dataset_withCurrentLabel.numFeatures+10)));

        centerChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));

        bottomChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));

        /*---------------------------------------------------------------
         ---------------------------------------------------------------*/

        /*---------------------------------------------------------------
         ******************************************************************/

    }

    /*---------------------------------------------------------------

     **                  createTSMark_centerChart()                **

     ---------------------------------------------------------------*/
    public void createTSMark_centerChart(){

        /***** ***/

        TSMark_centerChart = new Trace2DLtd("Original Time Series Trace ————————— ");
        TSMark_centerChart.setColor(Color.BLUE);

        centerChart.addTrace(TSMark_centerChart);
        /*** **/
    }

    /*---------------------------------------------------------------

     **                 createTSMark_bottomChart()                **

     ---------------------------------------------------------------*/
    public void createTSMark_bottomChart(){

        /***** ***/

        TSMark_bottomChart = new Trace2DLtd("Time Series Traces ————————— ");
        TSMark_bottomChart.setColor(Color.ORANGE);

        bottomChart.addTrace(TSMark_bottomChart);
        /*** **/
    }

    /*---------------------------------------------------------------

     **                         addTSTrace_centerChart()                       **

     ---------------------------------------------------------------*/
    public void addTSTrace_centerChart(){
        centerChart.addTrace(TSTrace);
    }

    /*---------------------------------------------------------------

     **          createinterpolatedTSMark_centerChart()            **

     ---------------------------------------------------------------*/
    public void createinterpolatedTSMark_centerChart(){

        /***** ***/

        interpolatedTSMark_centerChart = new Trace2DLtd("Interpolated Trace ————————— ");
        interpolatedTSMark_centerChart.setColor(Color.RED);

        centerChart.addTrace(interpolatedTSMark_centerChart);
        /*** **/
    }

    /*---------------------------------------------------------------

     **                 setInitializeTSToFalse()                     **

     ---------------------------------------------------------------*/
    private void setInitializeTSToFalse(){
        initializeTS = false; /*** Overwrite to 'false' **/
    }

    /*---------------------------------------------------------------

     **                    changeLabelSelection()                     **

     ---------------------------------------------------------------*/
    public void changeLabelSelection(){

        int startPoint = 0, endPoint = 10000; /*** The Maximum always sets to the size of timeseries data set **/
        if(initializeTS){ /*** If first time to load the TS and label, the timeSeriesRangeMaxTextField equals to lengh of timeseries list **/
            timeSeriesRangeMinTextField.setText("0");
        }else{ /*** Else ... **/
            startPoint = Integer.parseInt(timeSeriesRangeMinTextField.getText());
            lastTimeseriesListIndex = timeSeriesList.getSelectedIndex();
        }

        System.out.println("lastTimeseriesListIndex: " + lastTimeseriesListIndex);

        if( shuffleDataSetCheckBox.isSelected() )
        {
            String tfFolder ="/Users/leone/Documents/BSPCOVER/DATESET_SOURCE/transformation_fields";
            TransformationFieldsGenerator.getInstance().transformationScale = 0.5;
            dataset_withCurrentLabel = Distorsion.getInstance().DistortTransformationField(dataset_withCurrentLabel, tfFolder);
        }

        try
        {

            float selectedLabelIndex = Float.parseFloat( TS_labelList_Horizontal.getSelectedValue().toString() );

            System.out.println("selectedLabelIndex: " + selectedLabelIndex);

            dataset_withCurrentLabel = dataSet.FilterByLabel(selectedLabelIndex);

            System.out.println("dataset_withCurrentLabel.instances.size(): " + dataset_withCurrentLabel.instances.size());

            int timeSeriesListLen = dataset_withCurrentLabel.instances.size(); /*** After reassign the values, get the size **/
            endPoint = timeSeriesListLen;
            timeSeriesRangeMinTextField.setText(String.valueOf(startPoint));
            timeSeriesRangeMaxTextField.setText(String.valueOf(endPoint-1));

            // set the no of points text field from any of the trajectories
            noPointsTextField.setText("Num of time series: " + dataset_withCurrentLabel.instances.size());

            DefaultListModel newListModel = new DefaultListModel(); /*** newListModel: New time series list with current label **/
            setting_TS_ListModal = true;

//            for(int i = 0; i < dataset_withCurrentLabel.instances.size(); i++){}
            for(int i = startPoint; i < endPoint; i++)
            {
                newListModel.addElement(String.valueOf(i)); /*** addElement(String.valueOf(i)) ***/
            }

            timeSeriesList.setModel(newListModel);

            System.out.println("Test I.");
            /*** This setting will invoke the value change of timeseries list twice **/
            if(timeSeriesList.getModel().getSize() > 0)
            {
                timeSeriesList.setSelectedIndex(lastTimeseriesListIndex); /** default index: 0 **/
            }
            System.out.println("Test II.");

        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
//
//        noPointsTextField.setVisible(true);
//        noPointsTextField.setText("No time series: " + dataset_withCurrentLabel.numFeatures );

        if(initializeTS){
            setInitializeTSToFalse(); /*** Overwrite the initializeTS from 'true' to 'false' at the second time **/
        }

    }

    /*---------------------------------------------------------------

     **                    changeSelectedTS()                **

     ---------------------------------------------------------------*/
    public void changeSelectedTS(){


        System.out.println("setting_TS_ListModal: " + setting_TS_ListModal);

        /*** Stop an additonal call from the list by value change **/
        if(setting_TS_ListModal){
            setting_TS_ListModal = false;
            return;
        }

        System.out.println("Invoke changeSelectedTS() inside!");

        try
        {
            int selectedTSIndex = 0; /** default value: 0 **/

            if(timeSeriesList.getModel().getSize() > 0) {
                try{
                    selectedTSIndex = Integer.parseInt( timeSeriesList.getSelectedValue().toString() ); /** default index: 0 **/

                }catch( NullPointerException exc )
                {
                    System.out.println(exc);
                }
            }

            TSDataInstance = dataset_withCurrentLabel.instances.get(selectedTSIndex);

//            System.out.println("From changeSelectedTS() -> TSDataInstance.features.size(): " + TSDataInstance.features.size());

            TSDataInstance_bottomChart = TSDataInstance;

            labelTextField.setVisible(true);
            labelTextField.setText("Label class: " + ((int) TSDataInstance.target));

            /*
            if( imputeCheckBox.isSelected() )
            {
                GenerateManipulatedTimeSeries();
            }
             */

            System.out.println("Invoke drawTSTrace_CenterChart() + drawTSTrace_BottomChart() + resetInfoTextField_CenterChart()");
            /*** Original curve plot **/
            /*
            drawTSTrace_CenterChart(); // centerChart
            drawTSTrace_BottomChart(); // bottomChart
             */

            resetInfoTextField_CenterChart();

            /*** Horizontal dot plot **/ /*** Horizontal line plot **/
            TS_dotANDLine_plot();

            if(loadShapeletYesOrNo){
//                drawShapeletTrace_CenterChart();
//                ArrayList<Double> aryList_shapelet = horizontalLineLook_shapelet_centerChart(currentShapelet);
//                drawShapeletTrace_horizontally_centerChart(aryList_shapelet);
                shapelet_dotANDLine_plot_centetChart();
            }
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }
    }

    /*---------------------------------------------------------------

     **                    drawTSTrace_CenterChart()                    **

     ---------------------------------------------------------------*/
    // redraw the chart of the time series
    public void drawTSTrace_CenterChart(){
        /*** Why I decide to comment these two "RemoveAlLPoints"? ***/
        try{
            setScaleAndPosition();
            spinnerSetScale();
            centerChartSetRange();

            TSTrace.removeAllPoints();

            /*
            interpolatedTimeSeriesTrace.removeAllPoints();
            */

            /*** To ensure multiple timeseries can leave on one canvas ***/
            /*** And at the same time, I create an independent clearAllTSTraces_AllCharts() method at the end to points on canvas ***/

            /*** !!! However, here's a thing, I do not satisfy the non-duplicated timeseries on canvas yet!!! ***/

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

            if( TSDataInstance != null ){
                //System.out.println("***ReDrawTimeSeriesChart -> TSDataInstance***");
                int numPoints = TSDataInstance.features.size();
                System.out.println("-> TSDataInstance.features.size():" + numPoints);
                for(int i = 0; i < numPoints; i++)
                {
                    FeaturePoint p = TSDataInstance.features.get(i);
                    if( p.status == FeaturePoint.PointStatus.PRESENT && p.value != GlobalValues.MISSING_VALUE )
                        TSTrace.addPoint(i, p.value);
                }
            }

        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }

    }

    /*---------------------------------------------------------------

     **         drawTSTrace_horizontally_CenterChart()              **

     ---------------------------------------------------------------*/
    public void drawTSTrace_horizontally_CenterChart(ArrayList<Double> aTSAraylist){
        /*** Draw horizontal lines **/

        /*** Why I decide to comment these two "RemoveAlLPoints"? ***/
        try{
            setScaleAndPosition();
            spinnerSetScale();
            centerChartSetRange();

            TSTrace.removeAllPoints();

//            System.out.println("drawTSTrace_horizontally_CenterChart(ArrayList<Double> aTSAraylist) -> dataset_withCurrentLabel.numFeatures+10: " + (dataset_withCurrentLabel.numFeatures+10));

            /*
            interpolatedTimeSeriesTrace.removeAllPoints();
            */

            /*** To ensure multiple timeseries can leave on one canvas ***/
            /*** And at the same time, I create an independent clearAllTSTraces_AllCharts() method at the end to points on canvas ***/

            /*** !!! However, here's a thing, I do not satisfy the non-duplicated timeseries on canvas yet!!! ***/

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
                System.out.println("-> aTSAraylist.size():" + numPoints);
                for(int i = 0; i < numPoints; i++)
                {
                        TSTrace.addPoint(i, aTSAraylist.get(i));
                }

            }

        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }

    }

    /*---------------------------------------------------------------

     **                   drawTSTrace_BottomChart()                  **

     ---------------------------------------------------------------*/
    public void drawTSTrace_BottomChart() { /****** Bug  ***/
        try{
            bottomChartSetRange();

            int startPoint = 0; /*** Best match piece's start point **/

            Trace2DLtd aTSTrace = new Trace2DLtd(null);
            aTSTrace.setTracePainter(new TracePainterDisc(3));

            if(TSDataInstance_bottomChart.target <= 0.0){
                aTSTrace.setColor(Color.DARK_GRAY);
            }else{
                aTSTrace.setColor(Color.RED);
            }

            aTSTrace.setStroke(new BasicStroke(3));
            bottomChart.addTrace(aTSTrace);

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            if (TSDataInstance_bottomChart != null) {
                int numPoints_III = TSDataInstance_bottomChart.features.size();
                System.out.println("-> TSDataInstance_bottomChart.features.size():" + numPoints_III);

                /**** Here needs a block to set the values of i & numPoints.
                 * In order to shift the trace, it can add additional zero points by two sides -> double: 0.0 ***/
                if(firstTSDrawing){
                    System.out.println("Ever invoked");
                    globalBestMatchSP = globalStartPosition; /*** shapelet's start point **/

                    /*** -> **/
                    latest_aShapelet();
                    globalBestMatchEP = currentShapelet.size(); /*** shapelet's length **/
                    System.out.println("globalBestMatchEP: " + globalBestMatchEP);
                    firstTSDrawing = false;
                }else{
                    System.out.println("Or only invoked");
                    startPoint = globalBestMatchSP - globalStartPosition;
                }
                System.out.print("startPoint: " + startPoint);
                System.out.print(" ,globalStartPosition: " + globalStartPosition);
                System.out.print(" ,globalBestMatchSP: " + globalBestMatchSP);
                System.out.println(" ,globalBestMatchEP: " + globalBestMatchEP);
                /*** **/

                for (int i = 0; i < numPoints_III; i++){ /*** Change the values of i & numPoints **/
                    FeaturePoint p_III = TSDataInstance_bottomChart.features.get(i); /*** public List<FeaturePoint> features **/
                    if (p_III.status == FeaturePoint.PointStatus.PRESENT && p_III.value != GlobalValues.MISSING_VALUE) /*** p_III.status == PointStatus.PRESENT && p_III.value != GlobalValues.MISSING_VALUE***/
                        aTSTrace.addPoint(i+startPoint, p_III.value); /*** double p_III.value **/
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    /*---------------------------------------------------------------

     **         drawTSTrace_horizontally_BottomChart()              **

     ---------------------------------------------------------------*/
    public void drawTSTrace_horizontally_BottomChart(ArrayList<Double> aTSAraylist){
        /*** Draw horizontal lines **/
        try{
            bottomChartSetRange();

            int startPoint = 0; /*** Best match piece's start point **/

            Trace2DLtd aTSTrace = new Trace2DLtd(null);
            aTSTrace.setTracePainter(new TracePainterDisc(3));

            if(TSDataInstance_bottomChart.target <= 0.0){
                aTSTrace.setColor(Color.DARK_GRAY);
            }else{
                aTSTrace.setColor(Color.RED);
            }

            aTSTrace.setStroke(new BasicStroke(3));
            bottomChart.addTrace(aTSTrace);

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/


            if (aTSAraylist != null) {
                int numPoints_III = aTSAraylist.size();

                /**** Here needs a block to set the values of i & numPoints.
                 * In order to shift the trace, it can add additional zero points by two sides -> double: 0.0 ***/
                if(firstTSDrawing){
                    globalBestMatchSP = globalStartPosition; /*** shapelet's start point **/
                    /*** -> **/
                    /***** Update to the latest shapelet ***/
                    latest_aShapelet();
                    /*** **/
                    globalBestMatchEP = currentShapelet.size(); /*** shapelet's length **/
                    System.out.println("globalBestMatchEP: " + globalBestMatchEP);
                    firstTSDrawing = false;
                }else{
                    startPoint = globalBestMatchSP - globalStartPosition;
                }

                System.out.print("startPoint: " + startPoint);
                System.out.print(" ,globalStartPosition: " + globalStartPosition);
                System.out.print(" ,globalBestMatchSP: " + globalBestMatchSP);
                System.out.println(" ,globalBestMatchEP: " + globalBestMatchEP);
                /*** **/

                for (int i = 0; i < numPoints_III; i++){ /*** Change the values of i & numPoints **/
                    aTSTrace.addPoint(i+startPoint, aTSAraylist.get(i));
                }
            }

        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    /*---------------------------------------------------------------

     **        resetInfoTextField_CenterChart()           **

     ---------------------------------------------------------------*/
    public void resetInfoTextField_CenterChart(){


        int selectedTSIndex = 0; /** default value: 0 **/

        try{
            selectedTSIndex = Integer.parseInt( timeSeriesList.getSelectedValue().toString() ); /** **/
        }
        catch( NullPointerException exc )
        {
            System.out.println(exc);
        }

        try{
            chartI_TS_TextField.setText("Class Label No.: " + ((int) dataset_withCurrentLabel.instances.get(selectedTSIndex).target));
            chartI_Interpolated_TextField.setText("Time Series No.: " + selectedTSIndex);

            if(loadShapeletYesOrNo){
                chartI_Shapelet_TextField.setText("Shapelet No.: " + shapeletJList.getSelectedValue().toString());
                chartII_Shapelet_TextField.setText("Shapelet No.: " + shapeletJList.getSelectedValue().toString());
            }
        }
        catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    /*---------------------------------------------------------------

     **                    selectTSRange()                     **

     ---------------------------------------------------------------*/
    public void selectTSRange(int min, int max){


        if (max >= 0) {
            DefaultListModel newnewListModel = new DefaultListModel(); /*** newnewListModel: New time series list for scoping action **/
            for(int i = 0; i < dataset_withCurrentLabel.instances.size(); i++)
            {
                if(i>=min && i<=max){
                    newnewListModel.addElement(String.valueOf(i)); /*** addElement(String.valueOf(i)) ***/
                }

            }
            timeSeriesList.setModel(newnewListModel);
        }
        JOptionPane.showMessageDialog(frmTimeSeries,
                "Scoping successfully!");
    }

    public void switchTS_centerChartToTrue(){
//        isSwitchingTS_centerChart = true;
    }

//    public void switchTS_centerChartToFalse(){
//        isSwitchingTS_centerChart = false;
//    }

    /*** ---------------------------------------------------------------------------------------------------------* */

    public void TS_dotToLineTransfer(ArrayList<Double> anArylist){
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

        /*** center chart **/
        horizontalLinePlot_withVerticalLine_centerChart_controller(divider, dataArray);

        /*** bottom chart **/

        horizontalLinePlot_withVerticalLine_bottomChart_controller(divider, dataArray);
    }

    /*** With vertical connecting line - center chart**/
    public void horizontalLinePlot_withVerticalLine_centerChart_controller(int divider, Double[] myDataArray){

        /*** Clear the line plots at first **/
        clearHorizontalTrace_centerChart();

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
        singleLinePlot_centerChart(divider, indexP, anArylist);

    }

    /*** With vertical connecting line - bottom chart **/
    public void horizontalLinePlot_withVerticalLine_bottomChart_controller(int divider, Double[] myDataArray){

//        System.out.println("horizontalLinePlot_withVerticalLine_bottomChart_controller is invoked. ");
        /*** No need to clear the line plots **/
//        clearHorizontalTrace_bottomChart();

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
        singleLinePlot_bottomChart(divider, indexP, anArylist);

    }

    /*** _Without_ vertical connecting line **/
    public void horizontalLinePlot_withoutVerticalLine_centerChart_controller(int divider, Double[] myDataArray){

        /*** Clear the line plots at first **/
        clearHorizontalTrace_centerChart();

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
                    singleLinePlot_centerChart(divider, indexP, anArylist);
                    System.out.println("-------- > anArylist.size(): " + anArylist.size());
                    anArylist.clear();
                    System.out.println("anArylist.size() after clear(): " + anArylist.size());
                }
            }
            else{

                System.out.println("---------------------> Loop [i]: " + i);

                /*** If not head differs the precedent value, plot the precedent values in a line at first**/
                singleLinePlot_centerChart(divider, indexP, anArylist);
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
    /*** **/

    public void singleLinePlot_centerChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            Trace2DLtd aTSTrace = new Trace2DLtd(null);

            aTSTrace.setColor(Color.ORANGE);

            aTSTrace.setStroke(new BasicStroke(3));
            aTSTrace.setTracePainter(new TracePainterLine());
            centerChart.addTrace(aTSTrace);

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

//                    System.out.println("----------<> xP: " + xP);
//                    System.out.println("-------<> yP: " + yP);

                    aTSTrace.addPoint(xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void singleLinePlot_bottomChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            int startPoint = 0; /*** Best match segment's start point **/

            Trace2DLtd aTSTrace = new Trace2DLtd(null);

            if(TSDataInstance_bottomChart.target <= 0.0){
                aTSTrace.setColor(Color.DARK_GRAY);
            }else{
                aTSTrace.setColor(Color.RED);
            }

            aTSTrace.setStroke(new BasicStroke(3));
            aTSTrace.setTracePainter(new TracePainterLine());
            bottomChart.addTrace(aTSTrace);

            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            /*** --> setIndex() in changeLabelSelection() cause the issue !!! **/
            /*** Solution: set a if statement at the caller **/
            if (singleAry != null) {

                int numPoint = singleAry.size();
                System.out.println("-> singleAry.size():" + numPoint);

                /**** Here needs a block to set the values of i & numPoints.
                 * In order to shift the trace, it can add additional zero points by two sides -> double: 0.0 ***/
                if(firstTSDrawing_linePlot){
//                    System.out.println("Ever invoked");
                    globalBestMatchSP = globalStartPosition; /*** shapelet's start point **/

                    /*** -> **/
                    latest_aShapelet();
                    globalBestMatchEP = currentShapelet.size(); /*** shapelet's length **/
                    System.out.println("globalBestMatchEP: " + globalBestMatchEP);
                    firstTSDrawing_linePlot = false;
                }else{
//                    System.out.println("Or only invoked");
                    startPoint = globalBestMatchSP - globalStartPosition;
                }
                /*** **/

                double yP;
                double xP;
                int backwardIndex = (divider-1)/2;
                for(int i = 0; i < numPoint; i++)
                {
                    yP = singleAry.get(i);

                    /*** X position scale **/
                    /*** (i+startP)/3 -> Wrong (Integer discard), (i+startP)/3.0 -> Correct (Float transfer) **/
                    xP = (i+startP)/((double)divider)  - 1/((double)divider)*backwardIndex;

//                    System.out.println("----------<> xP: " + xP);
//                    System.out.println("-------<> yP: " + yP);

                    aTSTrace.addPoint(startPoint+xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void clearHorizontalTrace_centerChart(){
        clearTSTrace_centerChart();

        addTraceBack_centerChart();
    }

    /*** No need to invoke this function **/
    public void clearHorizontalTrace_bottomChart(){}

    public void TS_dotANDLine_plot(){
        ArrayList<Double> aryList = horizontalLineLook_TS_centerChart(TSDataInstance);
        drawTSTrace_horizontally_CenterChart(aryList); // centerChart
        drawTSTrace_horizontally_BottomChart(aryList);
        TS_dotToLineTransfer(aryList);
    }

    /*** ---------------------------------------------------------------------------------------------------------* */






























































    /*** ---------------------------------------------------------------------------------------------------------* */
    /*---------------------------------------------------------------

     **                    loadShapelet()                             **

     ---------------------------------------------------------------*/
    public void loadShapelet(){
        String subroot_I = "/datasets/Grace_dataset/v_2/shapelet";
        String subroot_II = "/datasets/ItalyPowerDemand_dataset/v_1/shapelet";

        String shapletGenerationPath = root + subroot_I;

        JFileChooser shapeletChooser = new JFileChooser();
//        shapeletChooser.setCurrentDirectory(new java.io.File("/Users/leone/Documents/BSPCOVER/GitHub/tsc/JMLToolkit/experimentI"));
        shapeletChooser.setCurrentDirectory(new File(shapletGenerationPath));
        shapeletChooser.setDialogTitle("Pick a Shapelet File");
        shapeletChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //
        // disable the "All files" option.
        //
        shapeletChooser.setAcceptAllFileFilterUsed(false);

        //
        if (shapeletChooser.showOpenDialog(frmTimeSeries) == JFileChooser.APPROVE_OPTION)
        {

            shapeletDirectory = shapeletChooser.getSelectedFile();
            System.out.println(" Selected shapelet directory:" + shapeletDirectory.getAbsolutePath() );
        }
        else
        {
            System.out.println("No Selection ");
            return;
        }

        // then load the data by the cm manager
        try{
            StringBuilder sb = new StringBuilder();
            String [] shapeletString;

            // get the line number, namely, the number of shapelet
            BufferedReader brNum = Files.newBufferedReader(Paths.get(shapeletDirectory.getAbsolutePath()));
            String line;
            int lineNum=0;
            while ((line = brNum.readLine()) != null) {
                lineNum++;
            }

            shapeletDouble = new ArrayList [lineNum];
            try (BufferedReader br = Files.newBufferedReader(Paths.get(shapeletDirectory.getAbsolutePath()))) {

                // read line by line
                int i=0;
                while ((line = br.readLine()) != null) {
                    shapeletString = line.split(",");
                    shapeletDouble[i] = new ArrayList<Double>();
                    for(int k=0; k<shapeletString.length; k++){
                        shapeletDouble[i].add(Double.parseDouble(shapeletString[k]));
                    }
                    i++;
                }

            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }

            /*** Transfer - shapelet **/
            shapelet_horizontalLineTransfer(shapeletDouble);

        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }

        // set the no of points text field from any of the trajectories
        numOfShapeletsTextField.setVisible(true);
        numOfShapeletsTextField.setText("Num of shapelets: " + shapeletDouble.length );

        shapeletJList.setSelectedIndex(0);

        shapeletsRangeMinTextField.setText(String.valueOf(0));
        shapeletsRangeMaxTextField.setText(String.valueOf(shapeletDouble.length - 1));

        changeloadShapeletYesOrNoToTrue(); /*** Overwrite loadShapeletYesOrNo from 'false' to 'true' **/

        // Create an independent shapelet chart before invoke the following methods and mark traces
        createChart_TopRightChart();

        /*** set marks **/
        createShapletMark_centerChart();
        createShapletMark_topRightChart();
        /** */

        // Invoking after createChart_TopRightChart()!
        classfyShapeletLabels();
        setShapeletLabelJList();

    }

    /*---------------------------------------------------------------

     **                 classfyShapeletLabels()                     **

     ---------------------------------------------------------------*/
    public void classfyShapeletLabels(){
        int labelPosIndex = 0;
        ArrayList<Integer> shapeletLabelArrayList = new ArrayList<> ();
        ArrayList<Integer> shapeletLabelCountArrayList = new ArrayList<> ();

        for(int i = 0; i < shapeletDouble.length; i++){
            int label = shapeletDouble[i].get(labelPosIndex).intValue();
            if(!shapeletLabelArrayList.contains(label)){ // If shapeletLabelArrayList doesn't contain this label
                shapeletLabelArrayList.add(label);
                shapeletLabelCountArrayList.add(1); // Add count 1 for a new index in shapeletLabelCountArrayList
            }else{ // If the shapeletLabelArrayList contains this label, count the quantity of it
                int index = shapeletLabelArrayList.indexOf(label); // Find the parallel index
                shapeletLabelCountArrayList.set(index, shapeletLabelCountArrayList.get(index) + 1);
            }
        }
//        System.out.println(shapeletLabelArrayList);
        this.shapeletLabelArrayList = shapeletLabelArrayList;
        this.shapeletLabelCountArrayList = shapeletLabelCountArrayList;
    }

    /*---------------------------------------------------------------

     **                  setShapeletComboBox()                     **

     ---------------------------------------------------------------*/
    public void setShapeletLabelJList(){


        int size = shapeletLabelArrayList.size();

        DefaultListModel<String> model = new DefaultListModel<> ();

        for(int i=0; i<size; i++){
            model.addElement((String.valueOf(shapeletLabelArrayList.get(i))));
        }

        shapeletLabelJList.setModel(model);

        shapeletLabelJList.setSelectedIndex(0);
    }

    /*---------------------------------------------------------------

     **        setShapeletJList()                     **

     ---------------------------------------------------------------*/
    public void setShapeletJList(){ // Causing crash



        int currentSelectedShapeletLabel = Integer.valueOf((String)shapeletLabelJList.getSelectedValue());

        // Set a Jlist with a model
        // Get the count of shapelet by corresponding label
//            System.out.println(shapeletLabelCountArrayList);
        int currentSelectedShapeletCountByLabel = shapeletLabelCountArrayList.get(shapeletLabelArrayList.indexOf(currentSelectedShapeletLabel));

        // populate the list with the time series trajectory indexes
        DefaultListModel shapeletJListModel = new DefaultListModel();

        for (int k = 0; k < currentSelectedShapeletCountByLabel; k++) {
            shapeletJListModel.addElement(String.valueOf(k));
        }

        shapeletsRangeMinTextField.setText(String.valueOf(0));
        shapeletsRangeMaxTextField.setText(String.valueOf(shapeletsWithCurrentLabel.length - 1));

        shapeletJList.setModel(shapeletJListModel);

        // set the no of points text field from any of the trajectories
        numOfShapeletsTextField.setVisible(true);

        if(shapeletsWithCurrentLabel.length>0){
            shapeletJList.setSelectedIndex(0);
        }

    }

    /*---------------------------------------------------------------

     **                 latest_aClassShapelet()                     **

     ---------------------------------------------------------------*/
    public void latest_aClassShapelet(){


        int currentSelectedShapeletLabel = Integer.valueOf((String)shapeletLabelJList.getSelectedValue());

        int labelPosIndex = 0;

        int size = shapeletLabelCountArrayList.get(shapeletLabelArrayList.indexOf(currentSelectedShapeletLabel));

        /***** ***/
        // Array list of array list
        ArrayList<Double> [] shapeletsWithCurrentLabelArray = new ArrayList [size];

        int myIndex = 0;

        for(int i=0; i<shapeletDouble.length; i++){
            int label = shapeletDouble[i].get(labelPosIndex).intValue();
            if(label == currentSelectedShapeletLabel){
                shapeletsWithCurrentLabelArray[myIndex] = shapeletDouble[i];
                myIndex++;
            }
        }

        // Assign it to the global value

        shapeletsWithCurrentLabel = shapeletsWithCurrentLabelArray;

        /*** **/
    }

    /*---------------------------------------------------------------

     **                 latest_aShapelet()                     **

     ---------------------------------------------------------------*/
    public void latest_aShapelet(){

        int selectedshapeletIndex = Integer.parseInt(shapeletJList.getSelectedValue().toString());

        currentShapelet = shapeletsWithCurrentLabel[selectedshapeletIndex];
    }

    /*---------------------------------------------------------------

     **                    selectTop_K_Shapelets()                    **

     ---------------------------------------------------------------*/
    public void selectTop_K_Shapelets(){
        if(shapeletDouble == null){
            JOptionPane.showMessageDialog(frmTimeSeriesLayerFirst,
                    "Load shapelets first!");
            return;
        }else{
            int top_K_shapelets = Integer.parseInt(top_K_shapeletsTextField.getText());

            // populate the list with the time series trajectory indexes
            DefaultListModel shapeletJListModel = new DefaultListModel();

            for (int k = 0; k < top_K_shapelets; k++) {
                shapeletJListModel.addElement(String.valueOf(k));
            }

            shapeletsRangeMinTextField.setText(String.valueOf(0));
            shapeletsRangeMaxTextField.setText(String.valueOf(top_K_shapelets- 1));

            shapeletJList.setModel(shapeletJListModel);

            numOfShapeletsTextField.setText(String.valueOf(top_K_shapelets));

        }
    }

    /*---------------------------------------------------------------

     **             changeloadShapeletYesOrNoToTrue()                **

     ---------------------------------------------------------------*/
    private void changeloadShapeletYesOrNoToTrue(){
        loadShapeletYesOrNo = true;
    }

    /*---------------------------------------------------------------

     **                    createChart_TopRightChart()                           **

     ---------------------------------------------------------------*/
    public void createChart_TopRightChart(){
        System.out.println("Invoke createChart_TopRightChart().");

        topRightChart = new Chart2D();

        shapeletTrace_topRightChart = new Trace2DLtd(null);
        shapeletTrace_topRightChart.setTracePainter(new TracePainterDisc(2));

        shapeletTrace_topRightChart.setStroke(new BasicStroke(2));
        shapeletTrace_topRightChart.setColor(Color.GREEN);

        topRightChart.addTrace(shapeletTrace_topRightChart);

        topRightPanel.add(topRightChart);

        topRightChart.setSize( topRightPanel.getSize() );

        topRightChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
        topRightChart.getAxisX().setRange( new Range(-10, dataset_withCurrentLabel.numFeatures+10) ); /*** setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); ***/
        topRightChart.getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-10, dataset_withCurrentLabel.numFeatures+10)));

        topRightChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));
//        topRightChart.getAxisY().setRange( new Range(-11, 45) );
//        topRightChart.getAxisY().setRangePolicy( new RangePolicyFixedViewport(new Range(-11, 45)));
        /*** Add the initial shapelet to centerChart_I as well ***/
        shapeletTrace_centerChart = new Trace2DLtd(null);
        shapeletTrace_centerChart.setTracePainter(new TracePainterDisc(3));

        shapeletTrace_centerChart.setStroke(new BasicStroke(3));
        shapeletTrace_centerChart.setColor(Color.GREEN);
        /*** **/
        centerChart.addTrace(shapeletTrace_centerChart);
    }

    /*---------------------------------------------------------------

     **                createShapletMark_centerChart()                **

     ---------------------------------------------------------------*/
    public void createShapletMark_centerChart(){

        /***** ***/
        shapeletMark_centerChart = new Trace2DLtd("Shapelet Trace ————————— ");

        shapeletMark_centerChart.setColor(Color.GREEN);

        centerChart.addTrace(shapeletMark_centerChart);
        /*** **/
    }

    /*---------------------------------------------------------------

     **                createShapletMark_topRightChart()                **

     ---------------------------------------------------------------*/
    public void createShapletMark_topRightChart(){

        /***** ***/
        shapeletMark_topRightChart = new Trace2DLtd("Shapelet Trace ————————— ");

        shapeletMark_topRightChart.setColor(Color.GREEN);

        topRightChart.addTrace(shapeletMark_topRightChart);
        /*** **/


    }

    /*---------------------------------------------------------------

     **                createShapletMark_topRightChart()                **

     ---------------------------------------------------------------*/
    public void addShapeletTrace_centerChart(){
        centerChart.addTrace(shapeletTrace_centerChart);
    };

    /*---------------------------------------------------------------

     **                createShapletMark_topRightChart()                **

     ---------------------------------------------------------------*/
    public void addShapeletTrace_TopRightChart(){
        topRightChart.addTrace(shapeletTrace_topRightChart);
    };

    /*---------------------------------------------------------------

 **                    changeSelectedShapelet()                   **

 ---------------------------------------------------------------*/
    public void changeSelectedShapelet(){
        try
        {
            System.out.println("Invoke changeSelectedShapelet().");
            System.out.println("firstTSDrawing: " + firstTSDrawing);
            if(clearTS_bottomChart){
                System.out.println("Invoke bottomChart.removeAllTraces() in changeSelectedShapelet().");
                clearTSTrace_BottomChart();
            }

            firstTSDrawing = true;
            clearTS_bottomChart = true;

            /*** -> **/
            latest_aShapelet();
            labelShapeletTextField.setVisible(true);
            labelShapeletTextField.setText("Shapelet Length: " + currentShapelet.size());

//            drawShapeletTrace_TopRightChart();
//            drawShapeletTrace_CenterChart();
            shapelet_dotANDLine_plot_centetChart();
            shapelet_dotANDLine_plot_topRightChart();
            /*
             */
            /*
            drawShapeletChartStackModel();
             */

            resetInfoTextField_CenterChart();
//            chartII_ShapeletLabel_TextField.setText("Shapelet Label: " + currentShapelet.get(0));
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /*** --------------------------------------------- **/
    public void shapelet_horizontalLineTransfer(ArrayList<Double>[] aDataArray){
        aShapeletLook = new ShapeletLook(aDataArray);
        aShapeletLook.PAALike_initial();
    }

    public ArrayList<Double> horizontalLineLook_shapelet_centerChart(ArrayList<Double> aShapeletData){
        ArrayList<Double> aShapelet_Arylist = aShapeletLook.revalue(aShapeletLook.arrayListTransfer(aShapeletData));
        return aShapelet_Arylist;
    }

    /*** --------------------------------------------- **/

    /*---------------------------------------------------------------

 **                    drawShapeletChartStackModel()                      **

 ---------------------------------------------------------------*/
    //    public void drawShapeletChartStackModel(){

    //
    //        int size = shapeletJList.getModel().getSize();
    //        int colorR, colorG, colorB;
    //        float basicStroke = (float) 4.0;
    //
    //        double ratio = ((float) shapeletJList.getSelectedIndex())/size;
    //
    //        Random rand = new Random();
    //        int colorVal = rand.nextInt(256); // 0 - 255
    //        colorR = 255;
    //        colorG = (int)( ratio * 255);
    //        colorB = colorG;
    //
    //        float my_stroke = (float)(basicStroke * (1 - ratio));
    //
    //        ITrace2D newshapeletTrace_centerChart = new Trace2DLtd("");
    //        newshapeletTrace_centerChart.setTracePainter(new TracePainterDisc(Math.round(my_stroke)));
    ////        newshapeletTrace_centerChart.setTracePainter(new TracePainterDisc(3));
    //
    //        newshapeletTrace_centerChart.setStroke(new BasicStroke(my_stroke));
    ////        newshapeletTrace_centerChart.setStroke(new BasicStroke(3));
    //
    //        newshapeletTrace_centerChart.setColor(new Color(colorR, colorG, colorG));
    ////        newshapeletTrace_centerChart.setColor(Color.GREEN);
    //
    //        centerChart.addTrace(newshapeletTrace_centerChart);
    //
    //        /*** **/
    //
    //        int startPosition = 0;
    //        double distanceBetweenST = 0;
    //        double distanceTmp = Double.MAX_VALUE;;
    //        if(currentShapelet != null){
    //            System.out.println("shapelet length: "+currentShapelet.size());
    //            for(int i=0; i<(TSDataInstance.features.size()-currentShapelet.size()+1); i++ ){
    //                distanceBetweenST = 0;
    //                for(int j=0; j< currentShapelet.size(); j++){
    //                    distanceBetweenST += Math.pow(TSDataInstance.features.get(j+i).value - currentShapelet.get(j), 2.0);
    //                }
    //                distanceBetweenST = Math.sqrt(distanceBetweenST);
    ////                System.out.println("distanceBetweenST: " + distanceBetweenST);
    //                if(distanceBetweenST < distanceTmp){
    //                    distanceTmp = distanceBetweenST;
    //                    startPosition = i; /*** Interesting **/
    //                }
    //            }
    //            distanceSTTextField.setVisible(true);
    //            distanceSTTextField.setText("distance TS: " + Math.round(distanceBetweenST * 100.0)/100.0);
    //            //System.out.println("startPosition "+startPosition);
    //
    //            for(int i = 1; i< currentShapelet.size(); i++){ /*** i=0 -> class label  **/
    //                newshapeletTrace_centerChart.addPoint((startPosition+i), currentShapelet.get(i));
    //            }
    //            globalStartPosition = startPosition;
    //        }
    //    }

    /*---------------------------------------------------------------

 **                    drawShapeletTrace_CenterChart()                      **

 ---------------------------------------------------------------*/
    public void drawShapeletTrace_CenterChart(){
        shapeletTrace_centerChart.removeAllPoints();

        int startPosition = 0;
        double distanceBetweenST = 0;
        double distanceTmp = Double.MAX_VALUE;;
        if(currentShapelet != null){
            System.out.println("Shapelet length: " + currentShapelet.size());
            System.out.println("TSDataInstance.features.size() isnull()? " + TSDataInstance.features.size());
            for(int i=0; i<(TSDataInstance.features.size()-currentShapelet.size()+1); i++ ){
                distanceBetweenST = 0;
                for(int j=0; j< currentShapelet.size(); j++){
                    distanceBetweenST += Math.pow(TSDataInstance.features.get(j+i).value - currentShapelet.get(j), 2.0);
                }
                distanceBetweenST = Math.sqrt(distanceBetweenST);
                //System.out.println("distanceBetweenST "+distanceBetweenST);
                if(distanceBetweenST < distanceTmp){
                    distanceTmp = distanceBetweenST;
                    startPosition = i; /*** Interesting **/
                }
            }
            distanceSTTextField.setVisible(true);
            distanceSTTextField.setText("distance TS: " + Math.round(distanceBetweenST * 100.0)/100.0);
            //System.out.println("startPosition "+startPosition);

            for(int i = 1; i< currentShapelet.size(); i++){ /*** i=0 -> class label  **/
                System.out.println("i: " + i);
                System.out.println("startPosition+i: " + (startPosition+i));
                System.out.println("currentShapelet.get(i): " + currentShapelet.get(i));
                shapeletTrace_centerChart.addPoint((startPosition+i), currentShapelet.get(i));
            }
            globalStartPosition = startPosition;
        }
    }

    /*---------------------------------------------------------------

     **        drawShapeletTrace_horizontally_centerChart()        **

     ---------------------------------------------------------------*/
    public void drawShapeletTrace_horizontally_centerChart(ArrayList<Double> aShapeletraylist){
        shapeletTrace_centerChart.removeAllPoints();

        ArrayList<Double> myShapeletlist = aShapeletraylist;

        int startPosition = 0;
        double distanceBetweenST = 0;
        double distanceTmp = Double.MAX_VALUE;;

        if(currentShapelet != null){

            /*** --------------------------------------------------- **/
            /*** Test situation 1: This Euclidean distance part will not change **/
            System.out.println("Shapelet length: " + currentShapelet.size());
            System.out.println("TSDataInstance.features.size() isnull()? " + TSDataInstance.features.size());
            for(int i=0; i<(TSDataInstance.features.size()-currentShapelet.size()+1); i++ ){
                distanceBetweenST = 0;
                for(int j=0; j< currentShapelet.size(); j++){
                    distanceBetweenST += Math.pow(TSDataInstance.features.get(j+i).value - currentShapelet.get(j), 2.0);
                }
                distanceBetweenST = Math.sqrt(distanceBetweenST);
                //System.out.println("distanceBetweenST "+distanceBetweenST);
                if(distanceBetweenST < distanceTmp){
                    distanceTmp = distanceBetweenST;
                    startPosition = i; /*** Interesting **/
                }
            }
            distanceSTTextField.setVisible(true);
            distanceSTTextField.setText("distance TS: " + Math.round(distanceBetweenST * 100.0)/100.0);
            //System.out.println("startPosition "+startPosition);

            /*** --------------------------------------------------- **/


            /*** Test situation 1: Only change the look **/
            for(int i = 1; i< myShapeletlist.size(); i++){ /*** i=0 -> class label  **/
                shapeletTrace_centerChart.addPoint((startPosition+i), myShapeletlist.get(i));
            }
            globalStartPosition = startPosition;
        }
    }

    /*---------------------------------------------------------------

 **     drawShapeletTrace_TopRightChart() for topRightPanel only           **

 ---------------------------------------------------------------*/
    public void drawShapeletTrace_TopRightChart(){

        shapeletTrace_topRightChart.removeAllPoints();

        int startPosition = 0;
        double distanceBetweenST = 0;
        double distanceTmp = Double.MAX_VALUE;;

        if(currentShapelet != null){
            //System.out.println("shapelet length: "+currentShapelet.size());
            for(int i=0; i<(TSDataInstance.features.size()-currentShapelet.size()+1); i++ ){
                distanceBetweenST = 0;
                for(int j=0; j< currentShapelet.size(); j++){
                    distanceBetweenST += Math.pow(TSDataInstance.features.get(j+i).value - currentShapelet.get(j), 2.0);
                }
                distanceBetweenST = Math.sqrt(distanceBetweenST);
                //System.out.println("distanceBetweenST "+distanceBetweenST);
                if(distanceBetweenST < distanceTmp){
                    distanceTmp = distanceBetweenST;
                    startPosition = i;
                }
            }
            distanceSTTextField_II.setVisible(true);
            distanceSTTextField_II.setText("distance_II TS: " + Math.round(distanceBetweenST * 100.0)/100.0);
            //System.out.println("startPosition "+startPosition);

            for(int i =1; i< currentShapelet.size(); i++){
                shapeletTrace_topRightChart.addPoint((startPosition+i), currentShapelet.get(i));
            }
        }
    }

    public void drawShapeletTrace_horizontally_topRightChart(ArrayList<Double> aShapeletraylist){
        shapeletTrace_topRightChart.removeAllPoints();

        ArrayList<Double> myShapeletlist = aShapeletraylist;

        int startPosition = 0;
        double distanceBetweenST = 0;
        double distanceTmp = Double.MAX_VALUE;;

        if(currentShapelet != null){

            /*** --------------------------------------------------- **/
            /*** Test situation 1: This Euclidean distance part will not change **/
            System.out.println("Shapelet length: " + currentShapelet.size());
            System.out.println("TSDataInstance.features.size() isnull()? " + TSDataInstance.features.size());
            for(int i=0; i<(TSDataInstance.features.size()-currentShapelet.size()+1); i++ ){
                distanceBetweenST = 0;
                for(int j=0; j< currentShapelet.size(); j++){
                    distanceBetweenST += Math.pow(TSDataInstance.features.get(j+i).value - currentShapelet.get(j), 2.0);
                }
                distanceBetweenST = Math.sqrt(distanceBetweenST);
                //System.out.println("distanceBetweenST "+distanceBetweenST);
                if(distanceBetweenST < distanceTmp){
                    distanceTmp = distanceBetweenST;
                    startPosition = i; /*** Interesting **/
                }
            }
            distanceSTTextField.setVisible(true);
            distanceSTTextField.setText("distance TS: " + Math.round(distanceBetweenST * 100.0)/100.0);
            //System.out.println("startPosition "+startPosition);

            /*** --------------------------------------------------- **/


            /*** Test situation 1: Only change the look **/
            for(int i = 1; i< myShapeletlist.size(); i++){ /*** i=0 -> class label  **/
                shapeletTrace_topRightChart.addPoint((startPosition+i), myShapeletlist.get(i));
            }
            globalStartPosition = startPosition;
        }
    }

    /*---------------------------------------------------------------

     **                    selectShapeletRange()                     **

     ---------------------------------------------------------------*/
    public void selectShapeletRange(int min, int max){

        if (max >= 0) {

            DefaultListModel newshapeletJListModel = new DefaultListModel();
            for(int i = 0; i < shapeletDouble.length; i++)
            {
                if(i>=min && i<max){
                    newshapeletJListModel.addElement(String.valueOf(i)); /*** addElement(String.valueOf(i)) ***/
                }

            }
            shapeletJList.setModel(newshapeletJListModel);
        }
        JOptionPane.showMessageDialog(frmTimeSeries,
                "Ranging Shapelets successfully!");
    }

    /*** ---------------------------------------------------------------------------------------------------------* */

    public void shapelet_dotToLineTransfer(ArrayList<Double> anArylist){

//        System.out.println("shapelet_dotToLineTransfer() is invoked. ");

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

        /*** center chart **/
        shapelet_horizontalLinePlot_withVerticalLine_centerChart_controller(divider, dataArray);

        /*** top right chart **/
        shapelet_horizontalLinePlot_withVerticalLine_topRightChart_controller(divider, dataArray);
    }

    /*** With vertical connecting line - center chart**/
    public void shapelet_horizontalLinePlot_withVerticalLine_centerChart_controller(int divider, Double[] myDataArray){

        /*** Clear the line plots at first **/
//        shapelet_clearHorizontalTrace_centerChart();

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
        shapelet_singleLinePlot_centerChart(divider, indexP, anArylist);

    }

    /*** With vertical connecting line - top right chart**/
    public void shapelet_horizontalLinePlot_withVerticalLine_topRightChart_controller(int divider, Double[] myDataArray){

        /*** clear old trace at first **/
        shapelet_clearHorizontalTrace_topRightChart();

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
        shapelet_singleLinePlot_topRightChart(divider, indexP, anArylist);
    }

    /*** _Without_ vertical connecting line **/
    public void shapelet_horizontalLinePlot_withoutVerticalLine_centerChart_controller(int divider, Double[] myDataArray){

        /*** Clear the line plots at first **/
        shapelet_clearHorizontalTrace_centerChart();

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
                    singleLinePlot_centerChart(divider, indexP, anArylist);
                    System.out.println("-------- > anArylist.size(): " + anArylist.size());
                    anArylist.clear();
                    System.out.println("anArylist.size() after clear(): " + anArylist.size());
                }
            }
            else{

                System.out.println("---------------------> Loop [i]: " + i);

                /*** If not head differs the precedent value, plot the precedent values in a line at first**/
                singleLinePlot_centerChart(divider, indexP, anArylist);
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
    /*** **/

    public void shapelet_singleLinePlot_centerChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            Trace2DLtd aTSTrace = new Trace2DLtd(null);

            aTSTrace.setColor(Color.GREEN);

            aTSTrace.setStroke(new BasicStroke(2));
            aTSTrace.setTracePainter(new TracePainterLine());
            centerChart.addTrace(aTSTrace);

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

                    aTSTrace.addPoint(xP+globalStartPosition, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void shapelet_singleLinePlot_topRightChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            Trace2DLtd aTSTrace = new Trace2DLtd(null);

            aTSTrace.setColor(Color.GREEN);

            aTSTrace.setStroke(new BasicStroke(1));
            aTSTrace.setTracePainter(new TracePainterLine());
            topRightChart.addTrace(aTSTrace);

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

//                    System.out.println("----------<> xP: " + xP);
//                    System.out.println("-------<> yP: " + yP);

                    aTSTrace.addPoint(xP+globalStartPosition, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MethodsLibrary_.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void shapelet_clearHorizontalTrace_centerChart(){
        clearShapeletTrace_centerChart();

        addTraceBack_centerChart();
    }

    public void shapelet_dotANDLine_plot_centetChart(){
        ArrayList<Double> aryList = horizontalLineLook_shapelet_centerChart(currentShapelet);
        drawShapeletTrace_horizontally_centerChart(aryList); // centerChart
        // centerChart
        shapelet_dotToLineTransfer(aryList);
    }

    public void shapelet_dotANDLine_plot_topRightChart(){
        ArrayList<Double> aryList = horizontalLineLook_shapelet_centerChart(currentShapelet);
        drawShapeletTrace_horizontally_topRightChart(aryList); // centerChart
        // topRrightChart -> it has been called in the shapelet_dotANDLine_plot_centetChart()
//        shapelet_dotToLineTransfer(aryList);
    }

    /*** ---------------------------------------------------------------------------------------------------------* */




































































    /*---------------------------------------------------------------

     **                       setScale()                              **

     ---------------------------------------------------------------*/
    public void setScale(double scale){
        double myScale = scale - oldScale;
        double xL = centerChartXL;
        double xR = centerChartXR;
        double myWidth = centerChartWidth;

        centerChart.getAxisX().setRange(new Range(xL += myWidth * myScale/2, xR -= myWidth * myScale/2));

        centerChartXL = xL;
        centerChartXR = xR;

        oldScale = scale;

        System.out.println("myWidth: " + myWidth);
        System.out.println("centerChartXL: " + centerChartXL);
        System.out.println("centerChartXR: " + centerChartXR);
    }

    /*---------------------------------------------------------------

     **                    timeSeriesZoomIn()                       **

     ---------------------------------------------------------------*/
    public void timeSeriesZoomIn(){

        double max = 2.5;
        double currentVal = ((Double)spinner.getValue()).floatValue();
        System.out.println("currentVal of zoom rate from timeSeriesZoomIn(): " + currentVal);

        if(currentVal < max - 0.1) { //Only if currentVal < max, it can work [2.5 = 2.499999, it should minus 0.1]
            spinner.setValue(Double.valueOf(currentVal+0.1));
//            setScale(currentVal);
        }

    }

    /*---------------------------------------------------------------

     **                    timeSeriesZoomOut()                       **

     ---------------------------------------------------------------*/
    public void timeSeriesZoomOut(){

        double min = 0.5;
        double currentVal = ((Double)spinner.getValue()).floatValue();
        System.out.println("currentVal of zoom rate from timeSeriesZoomOut(): " + currentVal);

        if(currentVal > min) {  //Only if currentVal > min, it can work
            spinner.setValue(Double.valueOf(currentVal-0.1));
//            setScale(currentVal);
        }

    }

    /*---------------------------------------------------------------

     **                    timeSeriesMoveLeft()                       **

     ---------------------------------------------------------------*/
    public void timeSeriesMoveLeft(){
        double xL = centerChartXL;
        double xR = centerChartXR;

        if(xR<(dataset_withCurrentLabel.numFeatures+1)*1.1) {
            centerChart.getAxisX().setRange(new Range(xL += 1, xR += 1));
        }

        centerChartXL = xL;
        centerChartXR = xR;
    }

    /*---------------------------------------------------------------

     **                    timeSeriesMoveRight()                      **

     ---------------------------------------------------------------*/
    public void timeSeriesMoveRight(){
        double xL = centerChartXL;
        double xR = centerChartXR;

        if(xL>-10) {
            centerChart.getAxisX().setRange(new Range(xL -= 1, xR -= 1));
        }

        centerChartXL = xL;
        centerChartXR = xR;
    }

    /*---------------------------------------------------------------

     **                    sortedByLength()                       **

     ---------------------------------------------------------------*/
    public void sortedByLength(){
        /*** Default sorted ASC***/
        ArrayList<Double>[] shapeletDoubleTemp = shapeletDouble;

        int[] myArray = new int[shapeletDouble.length];

        for(int i=0; i < shapeletDouble.length; i++){
//            System.out.println("shapelet [" + i + "] length: "+ shapeletDouble[i].size());
            myArray[i] = shapeletDouble[i].size(); /*** Each shapelet length (size) to myArray ***/
        }

        /*** Binary Insertion Sort ***/
        for (int i = 1; i < myArray.length; i++)
        {
            int x = myArray[i];
            ArrayList<Double> x_shapelet = shapeletDouble[i];

            // Find location to insert using binary search
            int j = Math.abs(Arrays.binarySearch(myArray, 0, i, x) + 1);

            //Shifting array to one location right
            System.arraycopy(myArray, j, myArray, j+1, i-j);

            //Placing element at its correct location
            myArray[j] = x;

            /*** Use j as a bridge to sort ***/
            System.arraycopy(shapeletDoubleTemp, j, shapeletDoubleTemp, j+1, i-j);
            shapeletDoubleTemp[j] = x_shapelet;
        }
        /*** ***/

//        System.out.println("shapelet length: "+currentShapelet.size());

        shapeletDouble = shapeletDoubleTemp;

        for(int i=0; i<shapeletDouble.length; i++) {
            System.out.println("shapeletDouble[" + i + "] length: " + shapeletDouble[i].size());
        }
    }

    /*---------------------------------------------------------------

     **                    sortedASC()                       **

     ---------------------------------------------------------------*/
    public void sortedASC(){

        DefaultListModel shapeletJListModelASC = new DefaultListModel();
        for(int i = 0; i < shapeletDouble.length; i++)
        {
            shapeletJListModelASC.addElement(String.valueOf(i));
        }
        shapeletJList.setModel(shapeletJListModelASC);
        JOptionPane.showMessageDialog(frmTimeSeries,
                "Shapelets sorted by ascend successfully!");
    }

    /*---------------------------------------------------------------

     **                    sortedDESC()                       **

     ---------------------------------------------------------------*/
    public void sortedDESC(){

        DefaultListModel shapeletJListModelDESC = new DefaultListModel();
        for(int i = shapeletDouble.length - 1; i >= 0; i--)
        {
            shapeletJListModelDESC.addElement(String.valueOf(i));
        }
        shapeletJList.setModel(shapeletJListModelDESC);
        JOptionPane.showMessageDialog(frmTimeSeries,
                "Shapelets sorted by descend successfully!");
    }

    /*---------------------------------------------------------------

     **                    setScaleAndPosition()                       **

     ---------------------------------------------------------------*/
    public void setScaleAndPosition(){
        oldScale = 1.0;
        centerChartXL = -10;
        centerChartXR = dataset_withCurrentLabel.numFeatures+10;
    }

    public void spinnerSetScale(){
        spinner.setValue(oldScale);
    }

    public void centerChartSetRange(){
        centerChart.getAxisX().setRange(new Range(centerChartXL, centerChartXR));
    }

    public void bottomChartSetRange(){
        bottomChart.getAxisX().setRange(new Range(centerChartXL, centerChartXR));
    }

    /*** ---------------------------------------------------------------------------------------------------------* */

    /*---------------------------------------------------------------

     **                  clearTSTrace_BottomChart()                  **

     ******************************************************************/
    public void clearTSTrace_BottomChart(){
        /*** set back to default scale **/
        setScaleAndPosition();

        bottomChart.removeAllTraces();

        TS_dotANDLine_plot();

//        bottomChart.addTrace(TSMark_bottomChart); /*** Add a new label trace under the cleaned bottomChart **/

        System.out.println("-> Cleared AllTimeSeriesInChart_II!");
//        drawTSTrace_BottomChart();
//        ArrayList<Double> aryList = horizontalLineLook_TS_centerChart(TSDataInstance);
//        drawTSTrace_horizontally_BottomChart(aryList); // bottomChart
//        TS_dotToLineTransfer(aryList);
    }

    /*---------------------------------------------------------------

     **                    clearTSTrace_centerChart()                 **

     ******************************************************************/
    public void clearTSTrace_centerChart(){
        /*** set back to default scale **/
        setScaleAndPosition();

        centerChart.removeAllTraces();
    }

    /*---------------------------------------------------------------

     **          clearShapeletTrace_centerChart()                 **

     ******************************************************************/
    public void clearShapeletTrace_centerChart(){
        centerChart.removeAllTraces();
    };

    /*---------------------------------------------------------------

     **      shapelet_clearHorizontalTrace_topRightChart()           **

     ******************************************************************/
    public void shapelet_clearHorizontalTrace_topRightChart(){
        topRightChart.removeAllTraces();
    }

    /*---------------------------------------------------------------

     **                    addTraceBack_centerChart()                 **

     ******************************************************************/
    public void addTraceBack_centerChart() {
        /*** 1st. set marks TS - centerChart **/
        createTSMark_centerChart();

        /*** 2nd. set marks interpolated TS - centerChart **/
        createinterpolatedTSMark_centerChart();

        /*** 3rd. set marks shapelet - centerChart **/
        createShapletMark_centerChart();

        /*** Add back trace on chart **/
        addTSTrace_centerChart();

        /*** Add TS trace back on center chart **/
        if(loadShapeletYesOrNo){
            addShapeletTrace_centerChart();
        }

    }

    /*---------------------------------------------------------------

     **                    clearAllTSTraces_AllCharts()                       **

     ******************************************************************/
    public void clearAllTSTraces_AllCharts(){
        /*** set back to default scale **/
        setScaleAndPosition();

        clearPointsOnTrace();

        centerChart.removeAllTraces();

        bottomChart.removeAllTraces();

        if(loadShapeletYesOrNo){
            topRightChart.removeAllTraces();
        }

        addAllTraceBack();

        System.out.println("-> Cleared AllTimeSeries!");
    }

    /*---------------------------------------------------------------

     **                      clearPointsOnTrace()                      **

     ******************************************************************/
    public void clearPointsOnTrace(){
        /*** set back to default scale **/
        setScaleAndPosition();

        TSTrace.removeAllPoints();
        /*
        interpolatedTSMark_centerChart.removeAllPoints();
        */
        if(loadShapeletYesOrNo){
            shapeletTrace_centerChart.removeAllPoints();
        }

        /*** No need to clear the points on shapeletTrace_topRightChart **/
        /*
        shapeletTrace_topRightChart.removeAllPoints();
         */
    }

    /*---------------------------------------------------------------

     **                      addAllTraceBack()                      **

     ******************************************************************/
    public void addAllTraceBack(){
        /*** 1st. set marks TS - centerChart **/
        createTSMark_centerChart();

        /*** 2nd. set marks interpolated TS - centerChart **/
        createinterpolatedTSMark_centerChart();

        /*** 3rd. set marks shapelet - centerChart **/
        createShapletMark_centerChart();

        /*** 4th. set marks TS - bottomChart **/
        createTSMark_bottomChart();


        /*** Add back trace on chart **/
        addTSTrace_centerChart();

        if(loadShapeletYesOrNo){
            /*** 5th. set marks shapelet - topRightCharts **/
            createShapletMark_topRightChart();
            /*** Add TS trace back on center chart **/
            addShapeletTrace_centerChart();
            /*** Add shapelet trace back on top right chart **/
            addShapeletTrace_TopRightChart();
        }

    }


}
