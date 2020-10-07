package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import DataStructures.FeaturePoint;
import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.DistanceClassification.DistanceClassification;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.DistanceClassification.QuickSort;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.II_MajorMethods_TImeseries.MajorMethods_Timeseries;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts.SetInfo_Charts;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;
import Looks.ShapeletLook;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import info.monitorenter.gui.chart.traces.painters.TracePainterLine;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MajorMethods_Shapelet extends MajorMethods_Shapelet_abstract {
    /*** ---------------------------------------------------------------------------------------------------------* */
    /*---------------------------------------------------------------
     **                     MethodsLibrary()                              **
     ---------------------------------------------------------------*/
    public MajorMethods_Shapelet(){}
    public MajorMethods_Shapelet(GUIComponents aGUIComponents, Variables aVariables){
        initialize(aGUIComponents, aVariables);
    }
    public void initialize(GUIComponents aGUIComponents, Variables aVariables){
//        initializeShapeletTrace_horizontalLine_Arylist();
        initializeReferenceParameters(aGUIComponents, aVariables);
        initializeLocalTraces();
    }
    /*** ---------------------------------------------------------------------------------------------------------* */
    public void initializeLocalTraces(){
        this.aLocalLineShapeletTrace_center = new Trace2DLtd(null);
        this.aLocalLineShapeletTrace_topRight = new Trace2DLtd("Shapelet ————————— ");
    }
     /*---------------------------------------------------------------
     **                     loadShapelet()                             **
     ---------------------------------------------------------------*/
    public void loadShapelet(){
        //
        // THe shaplet data will be loaded into Arraylist<Double>[]. The first element represents the class label (From inputted file structure).
        // [0] -> Class label.
        // Please be very careful when handling the Arraylist<Double>[].
        //

        String subroot_I = "/datasets/Grace_dataset/v_2/shapelet";
        String subroot_II = "/datasets/ItalyPowerDemand_dataset/v_1/shapelet";
        String subroot_III = "/datasets/Grace_dataset/v_3/shapelet";
//        String subroot_IV = "/datasets/Grace_dataset/v_4/shapelet";
        String subroot_V = "/datasets/Grace_dataset/v_5/shapelet";
//        String subroot_VI = "/datasets/Grace_dataset/v_6/shapelet";
        String subroot_VII = "/datasets/Grace_dataset/v_7/shapelet";


        String shapletGenerationPath = this.aVariables.root + subroot_VII;
        JFileChooser shapeletChooser = new JFileChooser();
//        shapeletChooser.setCurrentDirectory(new java.io.File("/Users/leone/Documents/BSPCOVER/GitHub/tsc/JMLToolkit/experimentI"));
        shapeletChooser.setCurrentDirectory(new java.io.File(shapletGenerationPath));
        shapeletChooser.setDialogTitle("Pick a Shapelet File");
        shapeletChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //
        // disable the "All files" option.
        //
        shapeletChooser.setAcceptAllFileFilterUsed(false);
        //
        if (shapeletChooser.showOpenDialog(this.aGUIComponents.frmTimeSeries) == JFileChooser.APPROVE_OPTION){
            this.aVariables.shapeletDirectory = shapeletChooser.getSelectedFile();
            System.out.println("-> Selected shapelet directory:" + this.aVariables.shapeletDirectory.getAbsolutePath() );

            File dataSetFolder = this.aVariables.shapeletDirectory;
            ShapeletFileNameFilter filter = new ShapeletFileNameFilter();

            File[] shapeletFilesList = null;

            if (dataSetFolder != null) {
                shapeletFilesList = dataSetFolder.listFiles(filter);

                for( File shapeletFile : shapeletFilesList ) {
                    // then load the data by the cm manager
                    try{
                        StringBuilder sb = new StringBuilder();
                        String [] shapeletWeightString;

                        //
                        if(shapeletFile.getPath().toLowerCase().contains("shapelet-original")){
                            // get the line number, namely, the number of shapelet
                            BufferedReader brNum = Files.newBufferedReader(Paths.get(shapeletFile.getAbsolutePath()));
                            String line;
                            int lineNum=0;
                            while ((line = brNum.readLine()) != null) {
                                lineNum++;
                            }
                            this.aVariables.Shapelet_double_firstIndexIsLable = new ArrayList [lineNum];
                            try (BufferedReader br = Files.newBufferedReader(Paths.get(shapeletFile.getAbsolutePath()))) {
                                // read line by line
                                int i=0;
                                while ((line = br.readLine()) != null) {
                                    shapeletWeightString = line.split(",");
                                    this.aVariables.Shapelet_double_firstIndexIsLable[i] = new ArrayList<Double>();
                                    for(int k=0; k<shapeletWeightString.length; k++){
                                        this.aVariables.Shapelet_double_firstIndexIsLable[i].add(Double.parseDouble(shapeletWeightString[k]));
                                    }
                                    i++;
                                }
                            } catch (IOException e) {
                                System.err.format("IOException: %s%n", e);
                            }

                        }else if(shapeletFile.getPath().toLowerCase().contains("shapelet-weight")){
                            // get the line number, namely, the number of shapelet
                            BufferedReader brNum = Files.newBufferedReader(Paths.get(shapeletFile.getAbsolutePath()));
                            String line;
                            int lineNum=0;
                            while ((line = brNum.readLine()) != null) {
                                lineNum++;
                            }
                            this.aVariables.Shapelet_weight = new ArrayList [lineNum];
                            try (BufferedReader br = Files.newBufferedReader(Paths.get(shapeletFile.getAbsolutePath()))) {
                                // read line by line
                                int i=0;
                                while ((line = br.readLine()) != null) {
                                    shapeletWeightString = line.split(",");
                                    this.aVariables.Shapelet_weight[i] = new ArrayList<Double>();
                                    for(int k=0; k<shapeletWeightString.length; k++){
                                        this.aVariables.Shapelet_weight[i].add(Double.parseDouble(shapeletWeightString[k]));
                                    }
                                    i++;
                                }

                                // test
//                                for(ArrayList arrlist: this.aVariables.Shapelet_weight){
//                                    System.out.println("arrlist: " + arrlist);
//                                }
                            } catch (IOException e) {
                                System.err.format("IOException: %s%n", e);
                            }
                        }else{
                            Logger logger
                                    = Logger.getLogger(
                                    DistanceClassification.class.getName());
                            // log messages using log(Level level, String msg)
                            logger.log(Level.WARNING, "Shapelet data file or shapelet weight data file name format is incorrect. Please rename the file name.");
                            throw new IllegalArgumentException();
                        }
                    }
                    catch(Exception exc) {
                        exc.printStackTrace();
                    }
                }
                /*** Transfer - shapelet **/
                shapeletHorizontaSegmentTransfer(this.aVariables.Shapelet_double_firstIndexIsLable);
            }
            else {
                System.err.println("Cannot open data directory!");
                System.exit(-1);
            }
        }
        else {
            System.out.println("No Selection ");
            return;
        }

        // set the no of points text field from any of the trajectories
        this.aGUIComponents.numOfShapeletsTextField.setVisible(true);
        this.aGUIComponents.numOfShapeletsTextField.setText("Num of shapelets: " + this.aVariables.Shapelet_double_firstIndexIsLable.length );
        this.aGUIComponents.shapeletJList.setSelectedIndex(0);
        this.aGUIComponents.shapeletsRangeMinTextField.setText(String.valueOf(0));
        this.aGUIComponents.shapeletsRangeMaxTextField.setText(String.valueOf(this.aVariables.Shapelet_double_firstIndexIsLable.length - 1));
        changeloadShapeletYesOrNoToTrue(); /*** Overwrite loadShapeletYesOrNo from 'false' to 'true' **/
        // Create an independent shapelet chart before invoke the following methods and mark traces
        createChartsAndTraces();
        /*** set marks **/
//        createShapletMark_centerChart();
//        createShapletMarkTopRightChart();
        /** */
        // Invoking after createChart_TopRightChart()!
        initializeShapletContainer();
        classfyShapeletLabels();

        // Distances computation
        infoClassificaationTest("\nDistance sorting is on process ... ");
        try {
            this.aVariables.Shapelet_toAllTS_distances = this.aDistanceClassification.classificationTest_v3();
            int sum = 0;
            for(ArrayList<ArrayList<double[]>> a: this.aVariables.Shapelet_toAllTS_distances){
                for(ArrayList<double[]> b: a){
                    sum += b.size();
                }
            }
            System.out.println("sum length: " + sum);
            infoClassificaationTest("\nDistance sorting done. ");
        }catch (Exception e){
            e.printStackTrace();
        }

        // setShapeletLabelJList() must be behind the Distances computation
        setShapeletLabelJList();

        //
        weightHistogram();
    }
    /*---------------------------------------------------------------
    **                  classfyShapeletLabels()                     **
    ---------------------------------------------------------------*/
    public void classfyShapeletLabels(){
        int labelPosIndex = 0;
        ArrayList<Integer> shapeletLabelArrayList = new ArrayList<> ();
        ArrayList<Integer> shapeletLabelCountArrayList = new ArrayList<> ();
        for(int i = 0; i < this.aVariables.Shapelet_double_firstIndexIsLable.length; i++){
            int label = this.aVariables.Shapelet_double_firstIndexIsLable[i].get(labelPosIndex).intValue();
            if(!shapeletLabelArrayList.contains(label)){ // If shapeletLabelArrayList doesn't contain this label
                shapeletLabelArrayList.add(label);
                shapeletLabelCountArrayList.add(1); // Add count 1 for a new index in shapeletLabelCountArrayList
            }else{ // If the shapeletLabelArrayList contains this label, count the quantity of it
                int index = shapeletLabelArrayList.indexOf(label); // Find the parallel index
                shapeletLabelCountArrayList.set(index, shapeletLabelCountArrayList.get(index) + 1);
            }
        }
//        System.out.println(shapeletLabelArrayList);
        this.aVariables.Shapelet_labelArrayList = shapeletLabelArrayList;
        this.aVariables.Shapelet_labelCountArrayList = shapeletLabelCountArrayList;
    }
    /*---------------------------------------------------------------
    **                   setShapeletComboBox()                     **
    ---------------------------------------------------------------*/
    public void setShapeletLabelJList(){
        int size = this.aVariables.Shapelet_labelArrayList.size();
        DefaultListModel<String> model = new DefaultListModel<> ();
        for(int i=0; i<size; i++){
            model.addElement((String.valueOf(this.aVariables.Shapelet_labelArrayList.get(i))));
        }
        this.aGUIComponents.shapeletLabelJList.setModel(model);
        this.aGUIComponents.shapeletLabelJList.setSelectedIndex(0);
    }
    /*---------------------------------------------------------------
    **                  latest_aClassShapelet()                     **
    ---------------------------------------------------------------*/
    public void latestShapeletClass(){
        int currentSelectedShapeletLabel = Integer.valueOf((String)this.aGUIComponents.shapeletLabelJList.getSelectedValue());
        System.out.println("From latest_aShapeletClass() -> currentSelectedShapeletLabel: " + currentSelectedShapeletLabel);
        int labelPosIndex = 0;
        int size = this.aVariables.Shapelet_labelCountArrayList.get(this.aVariables.Shapelet_labelArrayList.indexOf(currentSelectedShapeletLabel));
        /***** ***/
        // Array list of array list
        ArrayList<Double> [] shapeletsWithCurrentLabelArray_firstIndexIsLable = new ArrayList [size];
        int myIndex = 0;
        for(int i = 0; i<this.aVariables.Shapelet_double_firstIndexIsLable.length; i++){
            int label = this.aVariables.Shapelet_double_firstIndexIsLable[i].get(labelPosIndex).intValue();
            if(label == currentSelectedShapeletLabel){
                shapeletsWithCurrentLabelArray_firstIndexIsLable[myIndex] = this.aVariables.Shapelet_double_firstIndexIsLable[i];
                myIndex++;
            }
        }
        // Assign it to the global value
        this.aVariables.Shapelet_withCurrentLabel_firstIndexIsLable = shapeletsWithCurrentLabelArray_firstIndexIsLable;
        /*** **/
    }
    /*---------------------------------------------------------------
    **         setShapeletJList()                     **
    ---------------------------------------------------------------*/
    public void setShapeletJList(){ // Causing crash
        int currentSelectedShapeletLabel = Integer.valueOf((String)this.aGUIComponents.shapeletLabelJList.getSelectedValue());
        // Set a Jlist with a model
        // Get the count of shapelet by corresponding label
//            System.out.println(shapeletLabelCountArrayList);
        int currentSelectedShapeletCountByLabel = this.aVariables.Shapelet_labelCountArrayList.get(this.aVariables.Shapelet_labelArrayList.indexOf(currentSelectedShapeletLabel));
        // populate the list with the time series trajectory indexes
        DefaultListModel shapeletJListModel = new DefaultListModel();
        for (int k = 0; k < currentSelectedShapeletCountByLabel; k++) {
            shapeletJListModel.addElement(String.valueOf(k));
        }
        this.aGUIComponents.shapeletsRangeMinTextField.setText(String.valueOf(0));
        this.aGUIComponents.shapeletsRangeMaxTextField.setText(String.valueOf(this.aVariables.Shapelet_withCurrentLabel_firstIndexIsLable.length - 1));
        this.aGUIComponents.shapeletJList.setModel(shapeletJListModel);
        // set the no of points text field from any of the trajectories
        this.aGUIComponents.numOfShapeletsTextField.setVisible(true);
        if(this.aGUIComponents.shapeletJList.getModel().getSize()>0){
            this.aGUIComponents.shapeletJList.setSelectedIndex(0);
        }
    }
    /*---------------------------------------------------------------
    **                  latest_aShapelet()                     **
    ---------------------------------------------------------------*/
    public int latestShapelet(){
        try {
            if(!this.aGUIComponents.shapeletJList.isSelectionEmpty()){
                int selectedshapeletIndex = Integer.parseInt(this.aGUIComponents.shapeletJList.getSelectedValue().toString());
                this.aVariables.lastShapeletIndex = selectedshapeletIndex;
                this.aVariables.currentShapelet_firstIndexIsLable = this.aVariables.Shapelet_withCurrentLabel_firstIndexIsLable[selectedshapeletIndex];
                return selectedshapeletIndex;
            }
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(SetInfo_Charts.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
        return -1;
    }
    /*---------------------------------------------------------------
    **                initializeShapletContainer()                  **
    ---------------------------------------------------------------*/
    public void initializeShapletContainer(){
        this.aVariables.Shapelet_container = new HashSet<Integer>();
//        this.aVariables.shapletContainer = new int[this.aVariables.shapeletLabelArrayList.size()] [Collections. max(this.aVariables.shapeletLabelCountArrayList)];
    }

    public void clearShapletContainer(){
        if(!this.aVariables.load_Shapelet_YesOrNo || this.aVariables.Shapelet_container.isEmpty()){
            return;
        }
        this.aVariables.Shapelet_container.clear();
    }

    public void addIntoShapletContainer(int s){
        this.aVariables.Shapelet_container.add(s);
    }

    public void shapeletCenterChartStackModelChange(ActionEvent e){
        AbstractButton aButton = (AbstractButton)e.getSource();
        if(aButton.getText().equalsIgnoreCase("ON")){
            this.aVariables.stackModelOn = true;
        }else if(aButton.getText().equalsIgnoreCase("OFF")){
            this.aVariables.stackModelOn = false;
        }
    }
    /*---------------------------------------------------------------
    **                     selectTop_K_Shapelets()                    **
    ---------------------------------------------------------------*/
    public void selectTopKShapelets(){
        if(this.aVariables.Shapelet_double_firstIndexIsLable == null){
            JOptionPane.showMessageDialog(this.aGUIComponents.frmTimeSeriesLayerFirst,
                    "Load shapelets first!");
            return;
        }else{
            int top_K_shapelets = Integer.parseInt(this.aGUIComponents.top_K_shapeletsTextField.getText());
            // populate the list with the time series trajectory indexes
            DefaultListModel shapeletJListModel = new DefaultListModel();
            for (int k = 0; k < top_K_shapelets; k++) {
                shapeletJListModel.addElement(String.valueOf(k));
            }
            this.aGUIComponents.shapeletsRangeMinTextField.setText(String.valueOf(0));
            this.aGUIComponents.shapeletsRangeMaxTextField.setText(String.valueOf(top_K_shapelets- 1));
            this.aGUIComponents.shapeletJList.setModel(shapeletJListModel);
            this.aGUIComponents.numOfShapeletsTextField.setText(String.valueOf(top_K_shapelets));
        }
    }
    /*---------------------------------------------------------------
    **              changeloadShapeletYesOrNoToTrue()                **
    ---------------------------------------------------------------*/
    private void changeloadShapeletYesOrNoToTrue(){
        this.aVariables.load_Shapelet_YesOrNo = true;
    }

    /*---------------------------------------------------------------
    **                     createChart_TopRightChart()                           **
    ---------------------------------------------------------------*/
    public void createChartsAndTraces(){
        int lblAtFirstIndexDiscard = 1;
        System.out.println("Invoke createChart_TopRightChart().");
        this.aVariables.topRightShapeletChart = new Chart2D();
        this.aGUIComponents.topRightShapeletPanel.add(this.aVariables.topRightShapeletChart);

        this.aVariables.topRightShapeletChart.setSize( this.aGUIComponents.topRightShapeletPanel.getSize() );
        this.aVariables.topRightShapeletChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
//        this.aVariables.topRightChart.getAxisX().setRange( new Range(-1, this.aVariables.dataset_withCurrentLabel.numFeatures+1) ); /*** setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); ***/
//        this.aVariables.topRightChart.getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-1, this.aVariables.dataset_withCurrentLabel.numFeatures+1)));
        this.aVariables.topRightShapeletChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));


        this.aVariables.topRightShapeletChart.addTrace(this.aLocalLineShapeletTrace_topRight);

//        this.aVariables.shapeletTrace_topRightChart = new Trace2DLtd(null);
        this.aVariables.shapeletLineTraceTopRightChart = new Trace2DLtd(null);
        this.aVariables.shapeletLineTraceTopRightChart.setStroke(new BasicStroke(2));
        this.aVariables.shapeletLineTraceTopRightChart.setTracePainter(new TracePainterDisc(2));

        this.aVariables.topRightShapeletChart.addTrace(this.aVariables.shapeletLineTraceTopRightChart);
//
        /*** **/
        this.aVariables.centerChart.addTrace(this.aLocalLineShapeletTrace_center);

        /*** Add the initial shapelet to centerChart_I as well ***/
//        this.aVariables.shapeletTrace_centerChart = new Trace2DLtd(null);
        this.aVariables.shapeletDotTraceCenterChart = new Trace2DLtd(null);
        this.aVariables.shapeletDotTraceCenterChart.setTracePainter(new TracePainterDisc(2));
        this.aVariables.shapeletDotTraceCenterChart.setStroke(new BasicStroke(2));
        this.aVariables.shapeletDotTraceCenterChart.setColor(new Color(255, 77, 132));
        this.aVariables.centerChart.addTrace(this.aVariables.shapeletDotTraceCenterChart);
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_centerChart()                **
    ---------------------------------------------------------------*/
    public void createShapletMarkCenterChart(){
        /***** ***/
        this.aVariables.shapeletMarkCenterChart = new Trace2DLtd("Shapelet Trace ————————— ");
        this.aVariables.shapeletMarkCenterChart.setColor(Color.DARK_GRAY);
        this.aVariables.centerChart.addTrace(this.aVariables.shapeletMarkCenterChart);
        /*** **/
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void createShapletMarkTopRightChart(){
        /***** ***/
        this.aVariables.shapeletMarkTopRightChart = new Trace2DLtd(null);
        this.aVariables.shapeletMarkTopRightChart.setColor(Color.GREEN);
        this.aVariables.topRightShapeletChart.addTrace(this.aVariables.shapeletMarkTopRightChart);
        /*** **/
    }

    public void changeShapeletLabel(){
        /*** resetBottomTSTraceCount to 0 **/
        resetBottomTSTraceCount(0);
        /*** Normal model **/
//        selectShapletPlotModel_centerChart("normal");
        /*** Stack model **/
        clearShapletContainer(); // Clear stack model shapelet index storage
        selectShapletPlotModel_centerChart();
        /*** -> **/
        latestShapeletClass();
        setShapeletJList();
    }
    /*---------------------------------------------------------------
    **                     changeSelectedShapelet()                   **
    ---------------------------------------------------------------*/
    public void changeSelectedShapelet(){
        try {
            /*** -> **/
            if(latestShapelet()==-1){
                return;
            }
            this.aGUIComponents.labelShapeletTextField.setVisible(true);
            this.aGUIComponents.labelShapeletTextField.setText("Shapelet Length: " + (this.aVariables.currentShapelet_firstIndexIsLable.size()-1));
            //            drawShapeletTrace_TopRightChart();
//            drawShapeletTrace_CenterChart();
            /*** Horizontal dot plot **/ /*** Horizontal line plot **/
//            shapelet_dotANDLine_plot("normal");

            findMinMaxShapeletDataset();

            if(this.aVariables.switchDot){
//                System.out.println("I'm here!");
                /*** Stack model **/
                if(this.aVariables.stackModelOn){
                    addIntoShapletContainer(this.aGUIComponents.shapeletJList.getSelectedIndex());
                    shapeletDotDirectlyConnectLinePlot("stack");
                }else{
                    clearShapletContainer(); // Clear stack model shapelet index storage
                    shapeletDotDirectlyConnectLinePlot("normal");
                }

                topRightChartTimeseriesAndShapeletDraw();
                drawShapeletTraceTopRightTimeseriesChart();

            }else{
//                System.out.println("I'm there!");
                /*** Stack model **/
                if(this.aVariables.stackModelOn){
                    addIntoShapletContainer(this.aGUIComponents.shapeletJList.getSelectedIndex());
                    shapeletDotHorizontallyTransferredLinePlot("stack");
                }else{
                    clearShapletContainer(); // Clear stack model shapelet index storage
                    shapeletDotHorizontallyTransferredLinePlot("normal");
                }
            }

            /*
            drawShapeletChartStackModel();
             */
            setInfomationOnChart();
            //
            findTopTenTS(this.aGUIComponents.shapeletLabelJList.getSelectedIndex(), this.aGUIComponents.shapeletJList.getSelectedIndex());
            //
            distanceHistogram(this.aGUIComponents.shapeletLabelJList.getSelectedIndex(), this.aGUIComponents.shapeletJList.getSelectedIndex());
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
    }

    /*** --------------------------------------------- **/
    public void weightHistogram(){
        double[][] vals = new double[this.aVariables.Shapelet_weight.length][]; // Here not to initialize the second level size
        ArrayList<Double>[] tempContainer = this.aVariables.Shapelet_weight;

//         test
        for(ArrayList arrlist: this.aVariables.Shapelet_weight){
            System.out.println("arrlist: " + arrlist);
        }

        // Arraylist to array

        int startIndex = 1; // the 0 index is label
        for(int i=0; i<tempContainer.length; i++){
            int size = tempContainer[i].size() - startIndex;
            vals[i] = new double[size]; // Here initialize the second level size
            //
            for(int j=0; j<size; j++){
                vals[i][j] = tempContainer[i].get(j+startIndex); // distanceArr.get(i)[0] is weight value
            }
        }

//        System.out.println("vals.length: " + vals.length);
//        System.out.println("vals[0].length: " + vals[0].length);
//        System.out.println("vals[1].length: " + vals[1].length);

        // Create a unsort array
        int totalCount = 0;
        for(int k=0; k<vals.length; k++){
            totalCount += vals[k].length;
        }
//        System.out.println("totalCount: " + totalCount);

        double[][] unsortedArr = new double[totalCount][3]; // [][0]: weight value, [][1]: label, [][2]: No.
        totalCount = 0;
        for(int j=0; j<vals.length; j++){
            for(int l=0; l<vals[j].length; l++){
//                System.out.println("j: " + j);
//                System.out.println("l: " + l);
//                System.out.println("totalCount: " + totalCount);
//                System.out.println("-----");
                unsortedArr[totalCount][0] = vals[j][l]; // [][0]: must ensure that the [][0] (the second dimension) is the unsorted value. It secure that the sort method
                // tracks the correct value
                unsortedArr[totalCount][1] = j; // Play a tricky representation, let value of j be the class label.
                // It is because each first dimension [*][] in the 2d array vals[][] represents one class of wight (The second dimension contains its weight values).
                // And now we are going to sort all weights together and reserve their labels. Therefore, swap all the wight values into the first dimension.
                unsortedArr[totalCount][2] = l; // !To do this, all the shapelet data loaded from file to Array<Double>[] (goloba; variable to store shapelet data)
                // should not change its order and values. [Consistency regulation]
                totalCount++;
            }
        }
        // Quick sort
        QuickSort aQuickSort = new QuickSort();
        aQuickSort.sort(unsortedArr, 0, unsortedArr.length- 1);
//        aQuickSort.printArray(unsortedArr);
        // Now the unsortedArr has been a sorted array
        var dataset = new DefaultCategoryDataset();
        //
        int topTen =10; // minimum
//        for(int i=0; i<unsortedArr.length; i++){
        for(int i=unsortedArr.length-1; i>unsortedArr.length-1-topTen; i--){
            String key = "Class label-" + (int)unsortedArr[i][1]; // In unsortedArr, the second dimension [][*] withholds the label.
            String columnKey = "S[" + (int)unsortedArr[i][1] + "][" + (int)unsortedArr[i][2] +  "]";
            double wight = unsortedArr[i][0];
            dataset.setValue(wight, key, columnKey);
        }

//        var dataset = new DefaultCategoryDataset();
//        dataset.setValue(46, "Gold medals", "USA");
//        dataset.setValue(38, "Gold medals", "China");
//        dataset.setValue(29, "Gold medals", "UK");
//        dataset.setValue(22, "Gold medals", "Russia");
//        dataset.setValue(13, "Gold medals", "South Korea");
//        dataset.setValue(11, "Gold medals", "Germany");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Shapelet - 10 Maximum Weights",
                "",
                "Weight value",
                dataset,
                PlotOrientation.HORIZONTAL,
                true, true, false);

        barChart.setBackgroundPaint(Color.white);

        ChartPanel aChartPanel = new ChartPanel(barChart);
        aChartPanel.setBounds(0, 0, this.aGUIComponents.layeredPane_weightHist.getBounds().width, this.aGUIComponents.layeredPane_weightHist.getBounds().height);

        if(this.aGUIComponents.layeredPane_weightHist != null ){
            this.aGUIComponents.layeredPane_weightHist.removeAll(); // Remove all layers at first
        }
        this.aGUIComponents.layeredPane_weightHist.add(aChartPanel, Integer.valueOf(0));

    }

    /*** --------------------------------------------- **/
    public void distanceHistogram(int ShapeletLabel, int ShapeletIndex){
        ArrayList<double[]> distanceArr = this.aVariables.Shapelet_toAllTS_distances.get(ShapeletLabel).get(ShapeletIndex);
        double[][] vals = new double[this.aVariables.Shapelet_labelArrayList.size()][]; // Here not to initialize the second level size
        ArrayList<Double>[] tempContainer = new ArrayList[this.aVariables.Shapelet_labelArrayList.size()];

        //
        for(int i=0; i<tempContainer.length; i++){
            tempContainer[i] = new ArrayList<>();
        }

        //
        for(int i=0; i<distanceArr.size(); i++){
            double lbl = distanceArr.get(i)[1];
            //
            if(lbl==0.0){
                tempContainer[(int)lbl].add(distanceArr.get(i)[0]);
            }else if(lbl==1.0){
                tempContainer[(int)lbl].add(distanceArr.get(i)[0]);
            }else{
                Logger logger
                        = Logger.getLogger(
                        DistanceClassification.class.getName());
                // log messages using log(Level level, String msg)
                logger.log(Level.WARNING, "Default labels limit is 2 (0 and 1). \nNumber of shapelet labels is out pf the default design range. Please maintain the codes to support your dataset needs.");
                throw new IllegalArgumentException();
            }
        }

        // Arraylist to array
        for(int i=0; i<tempContainer.length; i++){
            int size = tempContainer[i].size();
            vals[i] = new double[size]; // Here initialize the second level size
            //
            for(int j=0; j<size; j++){
                vals[i][j] = tempContainer[i].get(j); // distanceArr.get(i)[0] is distance value
            }
        }

//        System.out.println("vals[0].length: " + vals[0].length);
//        System.out.println("vals[1].length: " + vals[1].length);

        var dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        //
        for(int k=0; k<vals.length; k++){
            String key = "Class label: " + k; // Each first level [*][] stands for one class of distance. Therefore, play a tricky representation.
            dataset.addSeries(key, vals[k], 25);
        }

        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = true;
        boolean toolTips = true;
        boolean urls = false;
        JFreeChart histogram = ChartFactory.createHistogram("Distance - Shapelet To Time Series",
                "distance", "frequency", dataset, orientation, show, toolTips, urls);

        XYPlot plot = (XYPlot)histogram.getPlot();
        plot.setBackgroundPaint(Color.white);
        XYBarRenderer renderer = (XYBarRenderer)plot.getRenderer();
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setShadowVisible(false);
        renderer.setSeriesPaint(0, new Color(1, 0, 0, 0.8f));
        renderer.setSeriesPaint(1, new Color(0, 0, 1, 0.8f));
        renderer.setSeriesPaint(2, new Color(0, 1, 0, 0.8f));

        ChartPanel aChartPanel = new ChartPanel(histogram);
        aChartPanel.setBounds(0, 0, 905-540, 350);

        if(this.aGUIComponents.layeredPane_distanceHist != null ){
            this.aGUIComponents.layeredPane_distanceHist.removeAll(); // Remove all layers at first
        }
        this.aGUIComponents.layeredPane_distanceHist.add(aChartPanel, Integer.valueOf(0));
    }

    /*** --------------------------------------------- **/
    public void findTopTenTS(int selectedIndex, int selectedLabel){
        boolean switchDot = this.aVariables.switchDot;
        int defaultTopK = 10;
        int localIndex = selectedIndex;
        int localLabel = selectedLabel;

        ArrayList<double[]> allDistanceforOneShapelet = this.aVariables.Shapelet_toAllTS_distances.get(localIndex).get(localLabel);

        for(int i=0; i<defaultTopK; i++){
            double[] arr = allDistanceforOneShapelet.get(i);
            double TS_lbl = arr[1]; // [1] is label. [2] is TS number
            int TS_Index = (int)arr[2];
//            System.out.println("TS_lbl: " + TS_lbl + ", TS_Index: " + TS_Index);

            DataSet datasetwithCurrentLabel = this.aVariables.dataSet.FilterByLabel(TS_lbl);
            DataInstance aTSDataInstance = datasetwithCurrentLabel.instances.get(TS_Index);
            ArrayList<Double> aryList = new ArrayList<>();
            if( aTSDataInstance!= null ) {
                if( switchDot ) {
                    for(FeaturePoint p: aTSDataInstance.features) {
                        aryList.add(p.value);
//                        System.out.println("aryList: " + aryList);
                    }
                }else{
                    if( aTSDataInstance!= null ) {
                        aryList = this.aMajorMethods_Timeseries.horizontalLineLookTSBothCharts(aTSDataInstance);
                    }
                }
            }

            this.aGUIComponents.lblMultiChartShapeletClass[i].setText("Shapelet Label: " +  (int)selectedIndex);
            this.aGUIComponents.lblMultiChartTSNum[i].setText("Time Series No.: " + TS_Index);
            this.aGUIComponents.lblMultiChartTSClass[i].setText("Timeseries Label: " + (int)TS_lbl);
            this.aGUIComponents.lblMultiChartShapeletNum[i].setText("Shapelet No.: " +  localLabel);
            this.aGUIComponents.lblTopk[i].setText(""+ (i+1));

//            System.out.println("aryList: " + aryList);
            this.aMajorMethods_Timeseries.setTSMultiChartsandTraces(i, arr, aryList); //
        }
    }

    /*** --------------------------------------------- **/
    public void shapeletDotDirectlyConnectLinePlot(String model){
        selectShapletPlotModel_centerChart();

        if(this.aVariables.stackModelOn){
            /*** **/
            /*** Plot additional stack trace **/
            if(model.equalsIgnoreCase("stack")){
                for(int index: this.aVariables.Shapelet_container){
//                    System.out.println("shapeletDotHorizontallyTransferredLinePlot() -> index: " + index);
                    if(index==this.aGUIComponents.shapeletJList.getSelectedIndex()){ //Avoid plot duplicate
                        continue;
                    }
                    ArrayList<Double> tempShapelet_firstIndexIsLable = this.aVariables.Shapelet_withCurrentLabel_firstIndexIsLable[index];
                    getShortestDistance(tempShapelet_firstIndexIsLable);

                    shapeletLineDrawStack(tempShapelet_firstIndexIsLable); // both center chart and top right chart
                }
            }
        }

        drawShapeletTraceCenterChart(); // centerChart
        drawShapeletTraceTopRightShapeletChart();
        shapeletLineDraw("top right chart");
        shapeletLineDraw("center chart");
    }

    /*---------------------------------------------------------------

 **                    drawShapeletTrace_CenterChart()                      **

 ---------------------------------------------------------------*/
    public void drawShapeletTraceCenterChart(){
        setScale();
        this.aVariables.shapeletDotTraceCenterChart.removeAllPoints();

        if(this.aVariables.currentShapelet_firstIndexIsLable != null){
//            System.out.println("Shapelet length: " + (this.aVariables.currentShapelet.size()-1));
            int labelIndex = 0;
            if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.shapeletDotTraceCenterChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.shapeletDotTraceCenterChart.setColor(new Color(51, 153, 255));
            }
            double distanceBetweenST = getShortestDistance();
            int startPosition = this.aVariables.globalStartPosition;
            this.aGUIComponents.distanceSTTextField.setVisible(true);
            this.aGUIComponents.distanceSTTextField.setText("distance TS: " + String.format("%.5g%n", distanceBetweenST));
            //System.out.println("startPosition "+startPosition);
            int lblAtFirstIndexDiscard = 1;
            for(int i = 0; i<this.aVariables.currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard; i++){ //The original shapelet array list did not discarded the label in the first index,
                // therefore we need to a label index discard at first
                this.aVariables.shapeletDotTraceCenterChart.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i+lblAtFirstIndexDiscard));
//                this.aVariables.shapeletDotTraceCenterChart.addPoint(i, this.aVariables.currentShapelet_firstIndexIsLable.get(i+lblAtFirstIndexDiscard));
            }
        }
    }

    /*---------------------------------------------------------------

 **     drawShapeletTrace_TopRightChart() for topRightPanel only           **

 ---------------------------------------------------------------*/
    public void drawShapeletTraceTopRightShapeletChart(){
        this.aVariables.shapeletLineTraceTopRightChart.removeAllPoints();

        if(this.aVariables.currentShapelet_firstIndexIsLable != null){
            int labelIndex = 0;
            if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.shapeletLineTraceTopRightChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.shapeletLineTraceTopRightChart.setColor(new Color(51, 153, 255));
            }
            /*** No need to calculate distance again, the center chart has got it. **/
            int startPosition = this.aVariables.globalStartPosition;
            int lblAtFirstIndexDiscard = 1;
            for(int i = lblAtFirstIndexDiscard; i<this.aVariables.currentShapelet_firstIndexIsLable.size(); i++){ //The original shapelet array list did not discarded the label in the first index,
                // therefore we need to a label index discard at first
                this.aVariables.shapeletLineTraceTopRightChart.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i));
//                this.aVariables.shapeletLineTraceTopRightChart.addPoint(i, this.aVariables.currentShapelet_firstIndexIsLable.get(i));
            }
        }
    }

    /*---------------------------------------------------------------

     **               TSLineTraceTopRightChartDraw()                **

     ---------------------------------------------------------------*/
    public void drawShapeletTraceTopRightTimeseriesChart() { /****** Bug  ***/
        try{
            Trace2DLtd aShapeletDotTrace = new Trace2DLtd(null);
            Trace2DLtd aShapeletLineTrace = new Trace2DLtd("Shapelet ————————— ");

            aShapeletDotTrace.setTracePainter(new TracePainterDisc(2));
            aShapeletDotTrace.setStroke(new BasicStroke(3));

            aShapeletLineTrace.setStroke(new BasicStroke(2));
            aShapeletLineTrace.setTracePainter(new TracePainterLine());

            this.aVariables.topRightTimeseriesChart.addTrace(aShapeletLineTrace);
            this.aVariables.topRightTimeseriesChart.addTrace(aShapeletDotTrace);

            if(this.aVariables.currentShapelet_firstIndexIsLable != null){
                int labelIndex = 0;
                if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                    aShapeletDotTrace.setColor(new Color(255, 77, 132));
                    aShapeletLineTrace.setColor(new Color(51, 153, 255));
                }else{
                    aShapeletDotTrace.setColor(new Color(51, 153, 255));
                    aShapeletLineTrace.setColor(new Color(255, 51, 153));
                }
                double distanceBetweenST = getShortestDistance();
                int startPosition = this.aVariables.globalStartPosition;
                int lblAtFirstIndexDiscard = 1;
                for(int i = 0; i<this.aVariables.currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard; i++){ //The original shapelet array list did not discarded the label in the first index,
                    // therefore we need to a label index discard at first
                    aShapeletLineTrace.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i+lblAtFirstIndexDiscard));
//                    aShapeletDotTrace.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i+lblAtFirstIndexDiscard));
                }

                for(int i = 0; i<this.aVariables.currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard; i++){ //The original shapelet array list did not discarded the label in the first index,
                    // therefore we need to a label index discard at first
//                    aShapeletLineTrace.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i+lblAtFirstIndexDiscard));
                    aShapeletDotTrace.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i+lblAtFirstIndexDiscard));
                }
            }
        }catch (NullPointerException e){
//            // Create a Logger
//            Logger logger
//                    = Logger.getLogger(
//                    MajorMethods_Timeseries.class.getName());
//
//            // log messages using log(Level level, String msg)
//            logger.log(Level.WARNING, e.toString());
            e.printStackTrace();
        }
    }

    public void shapeletLineDraw(String chartChoiceStr){
        try {
            if (chartChoiceStr.equalsIgnoreCase("center chart")) {
                if(!aLocalLineShapeletTrace_center.isEmpty()){
                    aLocalLineShapeletTrace_center.removeAllPoints();
                }

                this.aLocalLineShapeletTrace_center.setStroke(new BasicStroke(2));

                int labelIndex = 0;
                if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                    this.aLocalLineShapeletTrace_center.setColor(new Color(51, 153, 255));
                }else{
                    this.aLocalLineShapeletTrace_center.setColor(new Color(255, 51, 153));
                }

                if(this.aVariables.currentShapelet_firstIndexIsLable != null){
                    /*** No need to calculate distance again, the center chart has got it. **/
                    int startPosition = this.aVariables.globalStartPosition;

                    int lblAtFirstIndexDiscard = 1;
                    for(int i = 0; i<this.aVariables.currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard; i++){ //The original shapelet array list did not discarded the label in the first index,
                        // therefore we need to a label index discard at first
                        this.aLocalLineShapeletTrace_center.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i+lblAtFirstIndexDiscard));
                    }
                }
                /*****  ***/
            }else if(chartChoiceStr.equals("top right chart")){
                if(!aLocalLineShapeletTrace_topRight.isEmpty()){
                    aLocalLineShapeletTrace_topRight.removeAllPoints();
                }
                /*** Each time plots the current shapelet **/
                this.aLocalLineShapeletTrace_topRight.setStroke(new BasicStroke(2));

                int labelIndex = 0;
                if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                    this.aLocalLineShapeletTrace_topRight.setColor(new Color(51, 153, 255));
                }else{
                    this.aLocalLineShapeletTrace_topRight.setColor(new Color(255, 51, 153));
                }

                if(this.aVariables.currentShapelet_firstIndexIsLable != null){
                    /*** No need to calculate distance again, the center chart has got it. **/
                    int startPosition = this.aVariables.globalStartPosition;
                    //System.out.println("startPosition "+startPosition);

                    int lblAtFirstIndexDiscard = 1;
                    for(int i = lblAtFirstIndexDiscard; i<this.aVariables.currentShapelet_firstIndexIsLable.size(); i++){ //The original shapelet array list did not discarded the label in the first index,
                        // therefore we need to a label index discard at first
                        this.aLocalLineShapeletTrace_topRight.addPoint((startPosition+i), this.aVariables.currentShapelet_firstIndexIsLable.get(i));
//                        this.aLocalLineShapeletTrace_topRight.addPoint(i, this.aVariables.currentShapelet_firstIndexIsLable.get(i));
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

    public void shapeletLineDrawStack(ArrayList<Double> anArylist){
        Trace2DLtd aTrace = new Trace2DLtd(null);
        int labelIndex = 0;
        if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
            aTrace.setColor(new Color(51, 153, 255));
        }else{
            aTrace.setColor(new Color(255, 51, 153));
        }

        aTrace.setStroke(new BasicStroke(2));
        aTrace.setTracePainter(new TracePainterLine());
        this.aVariables.centerChart.addTrace(aTrace);

        if(anArylist != null){
            /*** No need to calculate distance again, the center chart has got it. **/
            int startPosition = this.aVariables.globalStartPosition;

            int lblAtFirstIndexDiscard = 1;
            for(int i = lblAtFirstIndexDiscard; i<this.aVariables.currentShapelet_firstIndexIsLable.size(); i++){ //This passedby shapelet array list did not discarded the label in the first index,
                // therefore we need to a label index discard at first
                aTrace.addPoint((startPosition+i), anArylist.get(i));
            }
        }
        /*****  ***/
    }

    /*** --------------------------------------------- **/
    public void shapeletHorizontaSegmentTransfer(ArrayList<Double>[] aDataArray){
        this.aVariables.aShapeletLook = new ShapeletLook(aDataArray);
        this.aVariables.aShapeletLook.PAALike_initial();
    }

    public ArrayList<Double> horizontalDotLookShapelet(ArrayList<Double> aShapeletData){
        ArrayList<Double> aShapelet_Arylist = this.aVariables.aShapeletLook.revalue(aShapeletData);
        /*** The return array list has discarded the label **/
        return aShapelet_Arylist;
    }

    public double getShortestDistance(){ /*** Every plot after loading shapelet should calculate the distance between shapelet and TS **/
        int startPosition = 0;
        int lblAtFirstIndexDiscard = 1;
        double distanceBetweenST = 0;
        double distanceMin = Double.MAX_VALUE;;
        for(int i = 0; i<(this.aVariables.TSDataInstance.features.size()-(this.aVariables.currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard)); i++ ){ // Discard first label
            // index in indexthis.aVariables.currentShapelet
            distanceBetweenST = 0;
            for(int j = 0; j<this.aVariables.currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard; j++){ // j=1 -> discard first label
                // index in indexthis.aVariables.currentShapelet
                distanceBetweenST += Math.pow(this.aVariables.TSDataInstance.features.get(j+i).value - this.aVariables.currentShapelet_firstIndexIsLable.get(j+lblAtFirstIndexDiscard), 2.0);
            }
            distanceBetweenST = Math.sqrt(distanceBetweenST);
            //System.out.println("distanceBetweenST "+distanceBetweenST);
            if(distanceBetweenST < distanceMin){
                distanceMin = distanceBetweenST;
                startPosition = i; /*** Interesting **/
            }
        }
//        System.out.println("From drawShapeletTrace_centerChart() -> startPoint: " + startPosition);
        this.aVariables.globalStartPosition = startPosition;

//        return distanceMin/((this.aVariables.currentShapelet_.size()-1)*1.0);
        return distanceMin*1.0;
    }

    public void getShortestDistance(ArrayList<Double> currentShapelet_firstIndexIsLable){ /*** Every plot after loading shapelet should calculate the distance between shapelet and TS **/
        int startPosition = 0;
        int lblAtFirstIndexDiscard = 1;
        double distanceBetweenST = 0;
        double distanceMin = Double.MAX_VALUE;
        for(int i=0; i<(this.aVariables.TSDataInstance.features.size()-(currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard)); i++ ){
            distanceBetweenST = 0;
            for(int j=0; j< currentShapelet_firstIndexIsLable.size()-lblAtFirstIndexDiscard; j++){
                distanceBetweenST += Math.pow(this.aVariables.TSDataInstance.features.get(j+i).value - currentShapelet_firstIndexIsLable.get(j+lblAtFirstIndexDiscard), 2.0);
            }
            distanceBetweenST = Math.sqrt(distanceBetweenST);
            //System.out.println("distanceBetweenST "+distanceBetweenST);
            if(distanceBetweenST < distanceMin){
                distanceMin = distanceBetweenST;
                startPosition = i; /*** Interesting **/
            }
        }
//        System.out.println("From drawShapeletTrace_centerChart() -> startPoint: " + startPosition);
        this.aVariables.globalStartPosition = startPosition;
    }

    /*---------------------------------------------------------------
    **      drawShapeletTrace_TopRightChart() for topRightPanel only **
    ---------------------------------------------------------------*/
    public void drawShapeletTraceTopRightShapeletChart(ArrayList<Double> aArylist){
        this.aVariables.shapeletLineTraceTopRightChart.removeAllPoints();
        if(this.aVariables.currentShapelet_firstIndexIsLable != null){
            int labelIndex = 0;
            if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.shapeletLineTraceTopRightChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.shapeletLineTraceTopRightChart.setColor(new Color(51, 153, 255));
            }
            /*** No need to calculate distance again, the center chart has got it. **/
            int startPosition = this.aVariables.globalStartPosition;
            //System.out.println("startPosition "+startPosition);
            double newVal, oldVal=-1;
            for(int i=0; i<aArylist.size(); i++){ //The transferred shapelet array list has discarded the label in the first index
                newVal =  aArylist.get(i);
                if(!(i==0) && newVal==oldVal){
                    continue;
                }else{
                    oldVal=newVal;
                }
                this.aVariables.shapeletLineTraceTopRightChart.addPoint((startPosition+i), newVal);
//                this.aVariables.shapeletLineTraceTopRightChart.addPoint(i, newVal);
            }
        }
    }

    /*---------------------------------------------------------------
     **          drawShapeletTrace_CenterChart()                    **
     ---------------------------------------------------------------*/
    public void drawShapeletTraceCenterChart(ArrayList<Double> aArylist){
        this.aVariables.shapeletDotTraceCenterChart.removeAllPoints();
        if(this.aVariables.currentShapelet_firstIndexIsLable != null){
            System.out.println("Shapelet length: " + (this.aVariables.currentShapelet_firstIndexIsLable.size()-1));
            int labelIndex = 0;
            if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.shapeletDotTraceCenterChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.shapeletDotTraceCenterChart.setColor(new Color(51, 153, 255));
            }
            double distanceBetweenST = getShortestDistance();
            int startPosition = this.aVariables.globalStartPosition;
            this.aGUIComponents.distanceSTTextField.setVisible(true);
            this.aGUIComponents.distanceSTTextField.setText("distance TS: " + String.format("%.5g%n", distanceBetweenST));
            //System.out.println("startPosition "+startPosition);
            double newVal, oldVal=-1;
            for(int i = 0; i<aArylist.size(); i++){ //The transferred shapelet array list has discarded the label in the first index
                newVal =  aArylist.get(i);
                if(!(i==0) && newVal==oldVal){
                    continue;
                }else{
                    oldVal=newVal;
                }
                this.aVariables.shapeletDotTraceCenterChart.addPoint((startPosition+i), newVal);
//                this.aVariables.shapeletDotTraceCenterChart.addPoint((i), newVal);
            }
        }
    }

    /*---------------------------------------------------------------
    **                     selectShapeletRange()                     **
    ---------------------------------------------------------------*/
    public void selectShapeletRange(int min, int max){
        if (max >= 0) {
            DefaultListModel newshapeletJListModel = new DefaultListModel();
            for(int i = 0; i < this.aVariables.Shapelet_double_firstIndexIsLable.length; i++)
            {
                if(i>=min && i<max){
                    newshapeletJListModel.addElement(String.valueOf(i)); /*** addElement(String.valueOf(i)) ***/
                }
            }
            this.aGUIComponents.shapeletJList.setModel(newshapeletJListModel);
        }
        JOptionPane.showMessageDialog(this.aGUIComponents.frmTimeSeries,
                "Ranging Shapelets successfully!");
    }

    /*** ---------------------------------------------------------------------------------------------------------* */

    public void drawShapeletLineTransfer(ArrayList<Double> anArylist, String model, int shapletIndex){
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
        for(int i = 0; i < size; i++){ // Label: i=0 -> avoid label
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
        shapeletHasVerticalController(divider, dataArray, model, shapletIndex);
    }

    public void shapeletHasVerticalController(int divider, Double[] myDataArray, String model, int shapletIndex){
        /*** Change TS has called the all traces clear -> No need to call it again **/
        removeLocalShapeletTraceTwoCharts();
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

        if(model.equalsIgnoreCase("normal")){
            addLocalTraceCenterChart(divider, indexP, anArylist);
        }else if(model.equalsIgnoreCase("stack")){
            addLocalTraceCenterChartStack(divider, indexP, anArylist);
        }else{
            Logger logger
                    = Logger.getLogger(
                    MajorMethods_Shapelet.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, "Select either 'normal' or 'stack' shapelet model on trace drawing. ");
        }
        /*** Each time plots the current shapelet **/
        addLocalTraceTopRightChart(divider, indexP, anArylist, shapletIndex);
    }

    public void addLocalTraceCenterChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            if(!aLocalLineShapeletTrace_center.isEmpty()){
                aLocalLineShapeletTrace_center.removeAllPoints();
            }

            int labelIndex = 0;
            if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                this.aLocalLineShapeletTrace_center.setColor(new Color(51, 153, 255));
            }else{
                this.aLocalLineShapeletTrace_center.setColor(new Color(255, 51, 153));
            }

            this.aLocalLineShapeletTrace_center.setStroke(new BasicStroke(2));
            /*** **/

            /*** **/
            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            if (singleAry != null) {
                int startPosition = this.aVariables.globalStartPosition;
                int numPoint = singleAry.size();
//                System.out.println("-> singleAry.size():" + numPoint);
                double yP;
                double xP;
                int backwardIndex = (divider-1)/2;
                for(int i = 0; i < numPoint; i++) {
                    yP = singleAry.get(i);
                    /*** X position scale **/
                    /*** (i+startP)/3 -> Wrong (Integer discard), (i+startP)/3.0 -> Correct (Float transfer) **/
                    xP = (i+startP)/((double)divider)  - 1/((double)divider)*backwardIndex;
                    this.aLocalLineShapeletTrace_center.addPoint(startPosition+xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MajorMethods_Shapelet.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void addLocalTraceCenterChartStack(int divider, int startP, ArrayList<Double> singleAry){
        try{
//            int oldL = this.aVariables.shapletandlabel_currentSelect[0];
//            int oldS = this.aVariables.shapletandlabel_currentSelect[1];
//            int newL = this.aGUIComponents.shapeletLabelJList.getSelectedIndex();
//            int newS = this.aGUIComponents.shapeletJList.getSelectedIndex();
//            /*** Avoid drawing the same shapelet **/
//            if(oldL == newL && oldS==newS){
//                return;
//            }else{
//                System.out.println("oldL: " + oldL + "oldS: " + oldS);
//                this.aVariables.shapletandlabel_currentSelect[0] = newL;
//                this.aVariables.shapletandlabel_currentSelect[1] = newS;
//            }
            Trace2DLtd aTrace = new Trace2DLtd(null);
            int labelIndex = 0;
            if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                aTrace.setColor(new Color(51, 153, 255));
            }else{
                aTrace.setColor(new Color(255, 51, 153));
            }

            aTrace.setStroke(new BasicStroke(2));
            aTrace.setTracePainter(new TracePainterLine());
            this.aVariables.centerChart.addTrace(aTrace);
            /*** **/

            /*** **/
            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            if (singleAry != null) {
                int startPosition = this.aVariables.globalStartPosition;
                int numPoint = singleAry.size();
//                System.out.println("-> singleAry.size():" + numPoint);
                double yP;
                double xP;
                int backwardIndex = (divider-1)/2;
                for(int i = 0; i < numPoint; i++) {
                    yP = singleAry.get(i);
                    /*** X position scale **/
                    /*** (i+startP)/3 -> Wrong (Integer discard), (i+startP)/3.0 -> Correct (Float transfer) **/
                    xP = (i+startP)/((double)divider)  - 1/((double)divider)*backwardIndex;
                    aTrace.addPoint(startPosition+xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MajorMethods_Shapelet.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void addLocalTraceTopRightChart(int divider, int startP, ArrayList<Double> singleAry, int shapletIndex){
        try{
            /*** Each time plots the current shapelet **/
            if(shapletIndex!=this.aVariables.lastShapeletIndex){
                return;
            }
            /*** **/
            if(!this.aLocalLineShapeletTrace_topRight.isEmpty()){
                this.aLocalLineShapeletTrace_topRight.removeAllPoints();
            }

            int labelIndex = 0;
            if(this.aVariables.currentShapelet_firstIndexIsLable.get(labelIndex).intValue()==0){ //label 0
                this.aLocalLineShapeletTrace_topRight.setColor(new Color(51, 153, 255));
            }else{
                this.aLocalLineShapeletTrace_topRight.setColor(new Color(255, 51, 153));
            }

            this.aLocalLineShapeletTrace_topRight.setStroke(new BasicStroke(2));
            /*** **/

            /*** **/
            /***** We need to change codes here  ***/
            /*** A bug here is the same timeseries may be presented by two totally different shapes **/
            if (singleAry != null) {
                int startPosition = this.aVariables.globalStartPosition;
                int numPoint = singleAry.size();
                //                System.out.println("-> singleAry.size():" + numPoint);
                double yP;
                double xP;
                int backwardIndex = (divider-1)/2;
                for(int i = 0; i < numPoint; i++) {
                    yP = singleAry.get(i);
                    /*** X position scale **/
                    /*** (i+startP)/3 -> Wrong (Integer discard), (i+startP)/3.0 -> Correct (Float transfer) **/
                    xP = (i+startP)/((double)divider)  - 1/((double)divider)*backwardIndex;
                    this.aLocalLineShapeletTrace_topRight.addPoint(startPosition+xP, yP);
//                    this.aLocalLineShapeletTrace_topRight.addPoint(xP, yP);
                }
            }
            /*****  ***/
        }catch (NullPointerException e){
            // Create a Logger
            Logger logger
                    = Logger.getLogger(
                    MajorMethods_Shapelet.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void shapeletDotHorizontallyTransferredLinePlot(String model){
        selectShapletPlotModel_centerChart();
        ArrayList<Double> aryList;

        if(this.aVariables.stackModelOn){
            /*** **/
            /*** Plot additional stack trace **/
            if(model.equalsIgnoreCase("stack")){
                for(int index: this.aVariables.Shapelet_container){
//                    System.out.println("shapeletDotHorizontallyTransferredLinePlot() -> index: " + index);
                    if(index==this.aGUIComponents.shapeletJList.getSelectedIndex()){ //Avoid plot duplicate
                        continue;
                    }
                    ArrayList<Double> tempShapelet = this.aVariables.Shapelet_withCurrentLabel_firstIndexIsLable[index];
                    getShortestDistance(tempShapelet);
                    aryList = horizontalDotLookShapelet(tempShapelet);
                    drawShapeletLineTransfer(aryList, model, index); // both center chart and top right chart
                }
            }
        }

        aryList = horizontalDotLookShapelet(this.aVariables.currentShapelet_firstIndexIsLable);
        drawShapeletTraceCenterChart(aryList); // center chart
        drawShapeletTraceTopRightShapeletChart(aryList); // top right chart
        drawShapeletLineTransfer(aryList, model, this.aVariables.lastShapeletIndex); // both center chart and top right chart, aDistance = -1 means not stack model
        /*** This function clear all traces (temp shapelet traces) on center chart and add all necessary traces and info back **/

    }

    /*** ---------------------------------------------------------------------------------------------------------* */
    public void removeLocalShapeletTraceTwoCharts(){
        this.aLocalLineShapeletTrace_topRight.removeAllPoints();
        this.aLocalLineShapeletTrace_center.removeAllPoints();
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void addShapeletTraceonCenterChart(){
        this.aVariables.centerChart.addTrace(this.aVariables.shapeletDotTraceCenterChart);
        this.aVariables.centerChart.addTrace(this.aLocalLineShapeletTrace_center);
    };
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void addShapeletTraceTopRightChart(){
        this.aVariables.topRightShapeletChart.addTrace(this.aVariables.shapeletLineTraceTopRightChart);
        this.aVariables.topRightShapeletChart.addTrace(this.aLocalLineShapeletTrace_topRight);
    };

    public void removePointsCenterChart(){ // Since the this.aLocalLineShapeletTrace_topRight is a protected local trace,
        // it'd better to remove locally
        this.aVariables.shapeletDotTraceCenterChart.removeAllPoints();
        this.aLocalLineShapeletTrace_center.removeAllPoints();
    };

    public void removePointsTopRightChart(){ // Since the this.aLocalLineShapeletTrace_topRight is a protected local trace,
        // it'd better to remove locally
        this.aVariables.shapeletLineTraceTopRightChart.removeAllPoints();
        this.aLocalLineShapeletTrace_topRight.removeAllPoints();
    };
    /*** ---------------------------------------------------------------------------------------------------------* */

    public boolean getLoadShapeletYesOrNo(){
        return this.aVariables.load_Shapelet_YesOrNo;
    }

    public void findMinMaxShapeletDataset(){
        int skipFirstIndex = 1;

        ArrayList<Double> ashapeletDouble_firstIndexIsLable = this.aVariables.currentShapelet_firstIndexIsLable;
        double[] arr = new double[ashapeletDouble_firstIndexIsLable.size()-skipFirstIndex];


        for(int i=0; i<arr.length; i++){
            arr[i] = (ashapeletDouble_firstIndexIsLable.get(i+skipFirstIndex));
        }

        Arrays.sort(arr);

        double[] minMax = new double[2];
        minMax[0] = arr[0];
        minMax[1] = arr[ashapeletDouble_firstIndexIsLable.size()-(1+skipFirstIndex)];

        System.out.println("minMax[0]:" + minMax[0]);

        this.aVariables.minMaxShapeletDataset = minMax;
    }
}
