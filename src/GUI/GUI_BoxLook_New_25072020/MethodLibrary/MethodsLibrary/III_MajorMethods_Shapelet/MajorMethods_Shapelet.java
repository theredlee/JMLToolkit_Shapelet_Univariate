package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts.SetInfo_Charts;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;
import Looks.ShapeletLook;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import info.monitorenter.gui.chart.traces.painters.TracePainterLine;
import info.monitorenter.util.Range;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        this.aLocalLineShapeletTrace_topRight = new Trace2DLtd(null);
    }
     /*---------------------------------------------------------------
     **                     loadShapelet()                             **
     ---------------------------------------------------------------*/
    public void loadShapelet(){
        String subroot_I = "/datasets/Grace_dataset/v_2/shapelet";
        String subroot_II = "/datasets/ItalyPowerDemand_dataset/v_1/shapelet";
        String shapletGenerationPath = this.aVariables.root + subroot_I;
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
        if (shapeletChooser.showOpenDialog(this.aGUIComponents.frmTimeSeries) == JFileChooser.APPROVE_OPTION) {
            this.aVariables.shapeletDirectory = shapeletChooser.getSelectedFile();
            System.out.println(" Selected shapelet directory:" + this.aVariables.shapeletDirectory.getAbsolutePath() );
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
            BufferedReader brNum = Files.newBufferedReader(Paths.get(this.aVariables.shapeletDirectory.getAbsolutePath()));
            String line;
            int lineNum=0;
            while ((line = brNum.readLine()) != null) {
                lineNum++;
            }
            this.aVariables.SPLet_double = new ArrayList [lineNum];
            try (BufferedReader br = Files.newBufferedReader(Paths.get(this.aVariables.shapeletDirectory.getAbsolutePath()))) {
                // read line by line
                int i=0;
                while ((line = br.readLine()) != null) {
                    shapeletString = line.split(",");
                    this.aVariables.SPLet_double[i] = new ArrayList<Double>();
                    for(int k=0; k<shapeletString.length; k++){
                        this.aVariables.SPLet_double[i].add(Double.parseDouble(shapeletString[k]));
                    }
                    i++;
                }
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }
            /*** Transfer - shapelet **/
            shapeletHorizontaSegmentTransfer(this.aVariables.SPLet_double);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
        // set the no of points text field from any of the trajectories
        this.aGUIComponents.numOfShapeletsTextField.setVisible(true);
        this.aGUIComponents.numOfShapeletsTextField.setText("Num of shapelets: " + this.aVariables.SPLet_double.length );
        this.aGUIComponents.shapeletJList.setSelectedIndex(0);
        this.aGUIComponents.shapeletsRangeMinTextField.setText(String.valueOf(0));
        this.aGUIComponents.shapeletsRangeMaxTextField.setText(String.valueOf(this.aVariables.SPLet_double.length - 1));
        changeloadShapeletYesOrNoToTrue(); /*** Overwrite loadShapeletYesOrNo from 'false' to 'true' **/
        // Create an independent shapelet chart before invoke the following methods and mark traces
        createChartsAndTraces();
        /*** set marks **/
//        createShapletMark_centerChart();
        createShapletMarkTopRightChart();
        /** */
        // Invoking after createChart_TopRightChart()!
        initializeShapletContainer();
        classfyShapeletLabels();

        // Distances computation
        infoClassificaationTest("\nDistance sorting is on process ... ");
        try {
            this.aVariables.SPLet_toAllTS_distances = this.aDistanceClassification.classificationTest_v3();
            int sum = 0;
            for(ArrayList<ArrayList<double[]>> a: this.aVariables.SPLet_toAllTS_distances){
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
        distanceHistogram();
    }
    /*---------------------------------------------------------------
    **                  classfyShapeletLabels()                     **
    ---------------------------------------------------------------*/
    public void classfyShapeletLabels(){
        int labelPosIndex = 0;
        ArrayList<Integer> shapeletLabelArrayList = new ArrayList<> ();
        ArrayList<Integer> shapeletLabelCountArrayList = new ArrayList<> ();
        for(int i = 0; i < this.aVariables.SPLet_double.length; i++){
            int label = this.aVariables.SPLet_double[i].get(labelPosIndex).intValue();
            if(!shapeletLabelArrayList.contains(label)){ // If shapeletLabelArrayList doesn't contain this label
                shapeletLabelArrayList.add(label);
                shapeletLabelCountArrayList.add(1); // Add count 1 for a new index in shapeletLabelCountArrayList
            }else{ // If the shapeletLabelArrayList contains this label, count the quantity of it
                int index = shapeletLabelArrayList.indexOf(label); // Find the parallel index
                shapeletLabelCountArrayList.set(index, shapeletLabelCountArrayList.get(index) + 1);
            }
        }
//        System.out.println(shapeletLabelArrayList);
        this.aVariables.SPLet_labelArrayList = shapeletLabelArrayList;
        this.aVariables.SPLet_labelCountArrayList = shapeletLabelCountArrayList;
    }
    /*---------------------------------------------------------------
    **                   setShapeletComboBox()                     **
    ---------------------------------------------------------------*/
    public void setShapeletLabelJList(){
        int size = this.aVariables.SPLet_labelArrayList.size();
        DefaultListModel<String> model = new DefaultListModel<> ();
        for(int i=0; i<size; i++){
            model.addElement((String.valueOf(this.aVariables.SPLet_labelArrayList.get(i))));
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
        int size = this.aVariables.SPLet_labelCountArrayList.get(this.aVariables.SPLet_labelArrayList.indexOf(currentSelectedShapeletLabel));
        /***** ***/
        // Array list of array list
        ArrayList<Double> [] shapeletsWithCurrentLabelArray = new ArrayList [size];
        int myIndex = 0;
        for(int i = 0; i<this.aVariables.SPLet_double.length; i++){
            int label = this.aVariables.SPLet_double[i].get(labelPosIndex).intValue();
            if(label == currentSelectedShapeletLabel){
                shapeletsWithCurrentLabelArray[myIndex] = this.aVariables.SPLet_double[i];
                myIndex++;
            }
        }
        // Assign it to the global value
        this.aVariables.SPLet_withCurrentLabel = shapeletsWithCurrentLabelArray;
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
        int currentSelectedShapeletCountByLabel = this.aVariables.SPLet_labelCountArrayList.get(this.aVariables.SPLet_labelArrayList.indexOf(currentSelectedShapeletLabel));
        // populate the list with the time series trajectory indexes
        DefaultListModel shapeletJListModel = new DefaultListModel();
        for (int k = 0; k < currentSelectedShapeletCountByLabel; k++) {
            shapeletJListModel.addElement(String.valueOf(k));
        }
        this.aGUIComponents.shapeletsRangeMinTextField.setText(String.valueOf(0));
        this.aGUIComponents.shapeletsRangeMaxTextField.setText(String.valueOf(this.aVariables.SPLet_withCurrentLabel.length - 1));
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
                this.aVariables.lastSPLetIndex = selectedshapeletIndex;
                this.aVariables.currentSPLet_ = this.aVariables.SPLet_withCurrentLabel[selectedshapeletIndex];
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
        this.aVariables.SPLet_container = new HashSet<Integer>();
//        this.aVariables.shapletContainer = new int[this.aVariables.shapeletLabelArrayList.size()] [Collections. max(this.aVariables.shapeletLabelCountArrayList)];
    }
    public void clearShapletContainer(){
        if(!this.aVariables.load_SPLet_YesOrNo || this.aVariables.SPLet_container.isEmpty()){
            return;
        }
        this.aVariables.SPLet_container.clear();
    }
    public void addIntoShapletContainer(int s){
        this.aVariables.SPLet_container.add(s);
    }
    public void stackModelChange(ActionEvent e){
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
        if(this.aVariables.SPLet_double == null){
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
        this.aVariables.load_SPLet_YesOrNo = true;
    }
    /*---------------------------------------------------------------
    **                     createChart_TopRightChart()                           **
    ---------------------------------------------------------------*/
    public void createChartsAndTraces(){
        System.out.println("Invoke createChart_TopRightChart().");
        this.aVariables.topRightChart = new Chart2D();
        this.aGUIComponents.topRightPanel.add(this.aVariables.topRightChart);
        this.aVariables.topRightChart.setSize( this.aGUIComponents.topRightPanel.getSize() );
        this.aVariables.topRightChart.getAxisX().setAxisTitle(new IAxis.AxisTitle("Time"));
        this.aVariables.topRightChart.getAxisX().setRange( new Range(-10, this.aVariables.dataset_withCurrentLabel.numFeatures+10) ); /*** setRange( new Range(-1, dataset_withCurrentLabel.numFeatures+1) ); ***/
        this.aVariables.topRightChart.getAxisX().setRangePolicy( new RangePolicyFixedViewport(new Range(-10, this.aVariables.dataset_withCurrentLabel.numFeatures+10)));
        this.aVariables.topRightChart.getAxisY().setAxisTitle(new IAxis.AxisTitle("Value"));

//        this.aVariables.shapeletTrace_topRightChart = new Trace2DLtd(null);
        this.aVariables.SPLet_trace_topRightChart = new Trace2DLtd(null);
        this.aVariables.SPLet_trace_topRightChart.setTracePainter(new TracePainterDisc(2));
        this.aVariables.SPLet_trace_topRightChart.setStroke(new BasicStroke(2));
        this.aVariables.SPLet_trace_topRightChart.setColor(Color.DARK_GRAY);
        this.aVariables.topRightChart.addTrace(this.aVariables.SPLet_trace_topRightChart);
//
        /*** Add the initial shapelet to centerChart_I as well ***/
//        this.aVariables.shapeletTrace_centerChart = new Trace2DLtd(null);
        this.aVariables.SPLet_trace_centerChart = new Trace2DLtd(null);
        this.aVariables.SPLet_trace_centerChart.setTracePainter(new TracePainterDisc(3));
        this.aVariables.SPLet_trace_centerChart.setStroke(new BasicStroke(3));
        this.aVariables.SPLet_trace_centerChart.setColor(new Color(255, 77, 132));
        this.aVariables.centerChart.addTrace(this.aVariables.SPLet_trace_centerChart);

        /*** **/
        this.aVariables.centerChart.addTrace(this.aLocalLineShapeletTrace_center);
        this.aVariables.topRightChart.addTrace(this.aLocalLineShapeletTrace_topRight);
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_centerChart()                **
    ---------------------------------------------------------------*/
    public void createShapletMarkCenterChart(){
        /***** ***/
        this.aVariables.SPLet_mark_centerChart = new Trace2DLtd("Shapelet Trace ————————— ");
        this.aVariables.SPLet_mark_centerChart.setColor(Color.DARK_GRAY);
        this.aVariables.centerChart.addTrace(this.aVariables.SPLet_mark_centerChart);
        /*** **/
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void createShapletMarkTopRightChart(){
        /***** ***/
        this.aVariables.SPLet_mark_topRightChart = new Trace2DLtd("Shapelet Trace ————————— ");
        this.aVariables.SPLet_mark_topRightChart.setColor(Color.GREEN);
        this.aVariables.topRightChart.addTrace(this.aVariables.SPLet_mark_topRightChart);
        /*** **/
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void addShapeletTraceonCenterChart(){
        this.aVariables.centerChart.addTrace(this.aVariables.SPLet_trace_centerChart);
        this.aVariables.centerChart.addTrace(this.aLocalLineShapeletTrace_center);
    };
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void addShapeletTraceTopRightChart(){
        this.aVariables.topRightChart.addTrace(this.aVariables.SPLet_trace_topRightChart);
        this.aVariables.topRightChart.addTrace(this.aLocalLineShapeletTrace_topRight);
    };

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
            this.aGUIComponents.labelShapeletTextField.setText("Shapelet Length: " + (this.aVariables.currentSPLet_.size()-1));
            //            drawShapeletTrace_TopRightChart();
//            drawShapeletTrace_CenterChart();
            /*** Horizontal dot plot **/ /*** Horizontal line plot **/
//            shapelet_dotANDLine_plot("normal");
            /*** Stack model **/
            if(this.aVariables.stackModelOn){
                addIntoShapletContainer(this.aGUIComponents.shapeletJList.getSelectedIndex());
                shapeletDotANDLinePlot("stack");
            }else{
                clearShapletContainer(); // Clear stack model shapelet index storage
                shapeletDotANDLinePlot("normal");
            }
            /*
            drawShapeletChartStackModel();
             */
            setInfomationOnChart();
            findTopTenTS(this.aGUIComponents.shapeletLabelJList.getSelectedIndex() ,this.aGUIComponents.shapeletJList.getSelectedIndex());
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /*** --------------------------------------------- **/
    public void distanceHistogram(){
//        ArrayList<ArrayList<double[]>> distanceArr = this.aVariables.SPLet_toAllTS_distances.get(0);
        ArrayList<double[]> distanceArr = this.aVariables.SPLet_toAllTS_distances.get(0).get(2);
        ArrayList<double[]> distanceArr_2 = this.aVariables.SPLet_toAllTS_distances.get(0).get(3);
        double[] vals = new double[distanceArr.size()];
        double[] vals_2 = new double[distanceArr_2.size()];
        //
//        int count = 0;
//        for(int i=0; i<distanceArr.size(); i++){
//            for(int k=0; k<distanceArr.get(i).size(); k++){
//                vals[count] = distanceArr.get(i).get(k)[0];
//                count++;
//            }
//        }

        for(int i=0; i<vals.length; i++){
            vals[i] = distanceArr.get(i)[0];
        }

        for(int i=0; i<vals_2.length; i++){
            vals_2[i] = distanceArr_2.get(i)[0];
        }

        var dataset = new HistogramDataset();
        dataset.setType(HistogramType.RELATIVE_FREQUENCY);
        dataset.addSeries("key", vals, 10);
        dataset.addSeries("key_2", vals_2, 10);

        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = true;
        boolean toolTips = true;
        boolean urls = false;
        JFreeChart histogram = ChartFactory.createHistogram("Distance Histogram",
                "x values", "y values", dataset, orientation, show, toolTips, urls);

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
        this.aGUIComponents.layeredPane_distanceHist.add(aChartPanel, Integer.valueOf(0));
    }

    /*** --------------------------------------------- **/
    public void findTopTenTS(int selectedIndex, int selectedLabel){
        int defaultTopK = 10;
        int localIndex = selectedIndex;
        int localLabel = selectedLabel;

        ArrayList<double[]> allDistanceforOneSPLet = this.aVariables.SPLet_toAllTS_distances.get(localIndex).get(localLabel);

        for(int i=0; i<defaultTopK; i++){
            double[] arr = allDistanceforOneSPLet.get(i);
            double TS_lbl = arr[1]; // [1] is label. [2] is TS number
            int TS_Index = (int)arr[2];
//            System.out.println("TS_lbl: " + TS_lbl + ", TS_Index: " + TS_Index);

            DataSet datasetwithCurrentLabel = this.aVariables.dataSet.FilterByLabel(TS_lbl);
            DataInstance aTSDataInstance = datasetwithCurrentLabel.instances.get(TS_Index);
            ArrayList<Double> aryList = this.aMajorMethods_Timeseries.horizontalLineLookTSCenterChart(aTSDataInstance);
//            System.out.println("aryList: " + aryList);
            this.aMajorMethods_Timeseries.setTSMultiChartsandTraces(i, arr, aryList); //
        }
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

    /*---------------------------------------------------------------
     **          drawShapeletTrace_CenterChart()                    **
     ---------------------------------------------------------------*/
    public void drawShapeletTraceCenterChart(ArrayList<Double> aArylist){
        this.aVariables.SPLet_trace_centerChart.removeAllPoints();
        if(this.aVariables.currentSPLet_ != null){
            System.out.println("Shapelet length: " + (this.aVariables.currentSPLet_.size()-1));
            int labelIndex = 0;
            if(this.aVariables.currentSPLet_.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.SPLet_trace_centerChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.SPLet_trace_centerChart.setColor(new Color(51, 153, 255));
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
                this.aVariables.SPLet_trace_centerChart.addPoint((startPosition+i), newVal);
            }
        }
    }

    public double getShortestDistance(){ /*** Every plot after loading shapelet should calculate the distance between shapelet and TS **/
        int startPosition = 0;
        double distanceBetweenST = 0;
        double distanceMin = Double.MAX_VALUE;;
        for(int i = 0; i<(this.aVariables.TSDataInstance.features.size()-(this.aVariables.currentSPLet_.size()-1)); i++ ){ // Discard first label
            // index in indexthis.aVariables.currentShapelet
            distanceBetweenST = 0;
            for(int j = 1; j<this.aVariables.currentSPLet_.size(); j++){ // j=1 -> discard first label
                // index in indexthis.aVariables.currentShapelet
                distanceBetweenST += Math.pow(this.aVariables.TSDataInstance.features.get(j+i).value - this.aVariables.currentSPLet_.get(j), 2.0);
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

//        return distanceMin/((this.aVariables.currentSPLet_.size()-1)*1.0);
        return distanceMin*1.0;
    }

    public void getShortestDistance(ArrayList<Double> currentShapelet){ /*** Every plot after loading shapelet should calculate the distance between shapelet and TS **/
        int startPosition = 0;
        double distanceBetweenST = 0;
        double distanceMin = Double.MAX_VALUE;;
        for(int i=0; i<(this.aVariables.TSDataInstance.features.size()-(currentShapelet.size()-1)); i++ ){
            distanceBetweenST = 0;
            for(int j=0; j< currentShapelet.size(); j++){
                distanceBetweenST += Math.pow(this.aVariables.TSDataInstance.features.get(j+i).value - currentShapelet.get(j), 2.0);
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
    public void drawShapeletTraceTopRightChart(ArrayList<Double> aArylist){
        this.aVariables.SPLet_trace_topRightChart.removeAllPoints();
        if(this.aVariables.currentSPLet_ != null){
            int labelIndex = 0;
            if(this.aVariables.currentSPLet_.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.SPLet_trace_topRightChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.SPLet_trace_topRightChart.setColor(new Color(51, 153, 255));
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
                this.aVariables.SPLet_trace_topRightChart.addPoint((startPosition+i), newVal);
            }
        }
    }

    /*---------------------------------------------------------------
    **                     selectShapeletRange()                     **
    ---------------------------------------------------------------*/
    public void selectShapeletRange(int min, int max){
        if (max >= 0) {
            DefaultListModel newshapeletJListModel = new DefaultListModel();
            for(int i = 0; i < this.aVariables.SPLet_double.length; i++)
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
            if(this.aVariables.currentSPLet_.get(labelIndex).intValue()==0){ //label 0
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
                    this.aLocalLineShapeletTrace_center.addPoint(this.aVariables.globalStartPosition+xP, yP);
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
            if(this.aVariables.currentSPLet_.get(labelIndex).intValue()==0){ //label 0
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
                    aTrace.addPoint(this.aVariables.globalStartPosition+xP, yP);
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
            if(shapletIndex!=this.aVariables.lastSPLetIndex){
                return;
            }
            /*** **/
            if(!this.aLocalLineShapeletTrace_topRight.isEmpty()){
                this.aLocalLineShapeletTrace_topRight.removeAllPoints();
            }

            int labelIndex = 0;
            if(this.aVariables.currentSPLet_.get(labelIndex).intValue()==0){ //label 0
                this.aLocalLineShapeletTrace_topRight.setColor(new Color(51, 153, 255));
            }else{
                this.aLocalLineShapeletTrace_topRight.setColor(new Color(255, 51, 153));
            }

            this.aLocalLineShapeletTrace_topRight.setStroke(new BasicStroke(2));
            this.aLocalLineShapeletTrace_topRight.setTracePainter(new TracePainterLine());
            /*** **/

            /*** **/
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
                    this.aLocalLineShapeletTrace_topRight.addPoint(this.aVariables.globalStartPosition+xP, yP);
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

    public void shapeletDotANDLinePlot(String model){
        selectShapletPlotModel_centerChart();
        ArrayList<Double> aryList;

        if(this.aVariables.stackModelOn){
            /*** **/
            /*** Plot additional stack trace **/
            if(model.equalsIgnoreCase("stack")){
                for(int index: this.aVariables.SPLet_container){
                    System.out.println("shapelet_dotANDLine_plot() -> index: " + index);
                    if(index==this.aGUIComponents.shapeletJList.getSelectedIndex()){ //Avoid plot duplicate
                        continue;
                    }
                    ArrayList<Double> tempShapelet = this.aVariables.SPLet_withCurrentLabel[index];
                    getShortestDistance(tempShapelet);
                    aryList = horizontalDotLookShapelet(tempShapelet);
                    drawShapeletLineTransfer(aryList, model, index); // both center chart and top right chart
                }
            }
        }

        aryList = horizontalDotLookShapelet(this.aVariables.currentSPLet_);
        drawShapeletTraceCenterChart(aryList); // center chart
        drawShapeletTraceTopRightChart(aryList); // top right chart
        drawShapeletLineTransfer(aryList, model, this.aVariables.lastSPLetIndex); // both center chart and top right chart, aDistance = -1 means not stack model
        /*** This function clear all traces (temp shapelet traces) on center chart and add all necessary traces and info back **/

    }

    /*** ---------------------------------------------------------------------------------------------------------* */
    public void removeLocalShapeletTraceTwoCharts(){
        this.aLocalLineShapeletTrace_topRight.removeAllPoints();
        this.aLocalLineShapeletTrace_center.removeAllPoints();
    }
    /*** ---------------------------------------------------------------------------------------------------------* */

    public boolean getLoadShapeletYesOrNo(){
        return this.aVariables.load_SPLet_YesOrNo;
    }
}
