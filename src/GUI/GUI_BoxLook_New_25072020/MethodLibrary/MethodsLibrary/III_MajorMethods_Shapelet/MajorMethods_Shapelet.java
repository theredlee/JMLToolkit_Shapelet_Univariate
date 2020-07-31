package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet;

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
        if (shapeletChooser.showOpenDialog(this.aGUIComponents.frmTimeSeries) == JFileChooser.APPROVE_OPTION)
        {
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
            this.aVariables.shapeletDouble = new ArrayList [lineNum];
            try (BufferedReader br = Files.newBufferedReader(Paths.get(this.aVariables.shapeletDirectory.getAbsolutePath()))) {
                // read line by line
                int i=0;
                while ((line = br.readLine()) != null) {
                    shapeletString = line.split(",");
                    this.aVariables.shapeletDouble[i] = new ArrayList<Double>();
                    for(int k=0; k<shapeletString.length; k++){
                        this.aVariables.shapeletDouble[i].add(Double.parseDouble(shapeletString[k]));
                    }
                    i++;
                }
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }
            /*** Transfer - shapelet **/
            shapeletHorizontaSegmentTransfer(this.aVariables.shapeletDouble);
        }
        catch(Exception exc)
        {
            exc.printStackTrace();
        }
        // set the no of points text field from any of the trajectories
        this.aGUIComponents.numOfShapeletsTextField.setVisible(true);
        this.aGUIComponents.numOfShapeletsTextField.setText("Num of shapelets: " + this.aVariables.shapeletDouble.length );
        this.aGUIComponents.shapeletJList.setSelectedIndex(0);
        this.aGUIComponents.shapeletsRangeMinTextField.setText(String.valueOf(0));
        this.aGUIComponents.shapeletsRangeMaxTextField.setText(String.valueOf(this.aVariables.shapeletDouble.length - 1));
        changeloadShapeletYesOrNoToTrue(); /*** Overwrite loadShapeletYesOrNo from 'false' to 'true' **/
        // Create an independent shapelet chart before invoke the following methods and mark traces
        createChartsAndTraces();
        /*** set marks **/
//        createShapletMark_centerChart();
        createShapletMark_topRightChart();
        /** */
        // Invoking after createChart_TopRightChart()!
        initializeShapletContainer();
        classfyShapeletLabels();
        setShapeletLabelJList();

        /*** Classification test **/
//        while(true){
//            if(!(this.aVariables.dataSet==null || this.aVariables.shapeletDouble==null
//                    || this.aVariables.TS_labelArryList==null || this.aVariables.shapeletLabelArrayList==null)){
//                break;
//            }
//        };
//        infoClassificaationTest("\nClassification test is on process ... ");
//        try {
//            this.aDistanceClassification.classificationTest();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }
    /*---------------------------------------------------------------
    **                  classfyShapeletLabels()                     **
    ---------------------------------------------------------------*/
    public void classfyShapeletLabels(){
        int labelPosIndex = 0;
        ArrayList<Integer> shapeletLabelArrayList = new ArrayList<> ();
        ArrayList<Integer> shapeletLabelCountArrayList = new ArrayList<> ();
        for(int i = 0; i < this.aVariables.shapeletDouble.length; i++){
            int label = this.aVariables.shapeletDouble[i].get(labelPosIndex).intValue();
            if(!shapeletLabelArrayList.contains(label)){ // If shapeletLabelArrayList doesn't contain this label
                shapeletLabelArrayList.add(label);
                shapeletLabelCountArrayList.add(1); // Add count 1 for a new index in shapeletLabelCountArrayList
            }else{ // If the shapeletLabelArrayList contains this label, count the quantity of it
                int index = shapeletLabelArrayList.indexOf(label); // Find the parallel index
                shapeletLabelCountArrayList.set(index, shapeletLabelCountArrayList.get(index) + 1);
            }
        }
//        System.out.println(shapeletLabelArrayList);
        this.aVariables.shapeletLabelArrayList = shapeletLabelArrayList;
        this.aVariables.shapeletLabelCountArrayList = shapeletLabelCountArrayList;
    }
    /*---------------------------------------------------------------
    **                   setShapeletComboBox()                     **
    ---------------------------------------------------------------*/
    public void setShapeletLabelJList(){
        int size = this.aVariables.shapeletLabelArrayList.size();
        DefaultListModel<String> model = new DefaultListModel<> ();
        for(int i=0; i<size; i++){
            model.addElement((String.valueOf(this.aVariables.shapeletLabelArrayList.get(i))));
        }
        this.aGUIComponents.shapeletLabelJList.setModel(model);
        this.aGUIComponents.shapeletLabelJList.setSelectedIndex(0);
    }
    /*---------------------------------------------------------------
    **                  latest_aClassShapelet()                     **
    ---------------------------------------------------------------*/
    public void latest_aShapeletClass(){
        int currentSelectedShapeletLabel = Integer.valueOf((String)this.aGUIComponents.shapeletLabelJList.getSelectedValue());
        System.out.println("From latest_aShapeletClass() -> currentSelectedShapeletLabel: " + currentSelectedShapeletLabel);
        int labelPosIndex = 0;
        int size = this.aVariables.shapeletLabelCountArrayList.get(this.aVariables.shapeletLabelArrayList.indexOf(currentSelectedShapeletLabel));
        /***** ***/
        // Array list of array list
        ArrayList<Double> [] shapeletsWithCurrentLabelArray = new ArrayList [size];
        int myIndex = 0;
        for(int i=0; i<this.aVariables.shapeletDouble.length; i++){
            int label = this.aVariables.shapeletDouble[i].get(labelPosIndex).intValue();
            if(label == currentSelectedShapeletLabel){
                shapeletsWithCurrentLabelArray[myIndex] = this.aVariables.shapeletDouble[i];
                myIndex++;
            }
        }
        // Assign it to the global value
        this.aVariables.shapeletsWithCurrentLabel = shapeletsWithCurrentLabelArray;
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
        int currentSelectedShapeletCountByLabel = this.aVariables.shapeletLabelCountArrayList.get(this.aVariables.shapeletLabelArrayList.indexOf(currentSelectedShapeletLabel));
        // populate the list with the time series trajectory indexes
        DefaultListModel shapeletJListModel = new DefaultListModel();
        for (int k = 0; k < currentSelectedShapeletCountByLabel; k++) {
            shapeletJListModel.addElement(String.valueOf(k));
        }
        this.aGUIComponents.shapeletsRangeMinTextField.setText(String.valueOf(0));
        this.aGUIComponents.shapeletsRangeMaxTextField.setText(String.valueOf(this.aVariables.shapeletsWithCurrentLabel.length - 1));
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
    public int latest_aShapelet(){
        try {
            if(!this.aGUIComponents.shapeletJList.isSelectionEmpty()){
                int selectedshapeletIndex = Integer.parseInt(this.aGUIComponents.shapeletJList.getSelectedValue().toString());
                this.aVariables.lastShapeletIndex = selectedshapeletIndex;
                this.aVariables.currentShapelet = this.aVariables.shapeletsWithCurrentLabel[selectedshapeletIndex];
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
        this.aVariables.shapletContainer = new HashSet<Integer>();
//        this.aVariables.shapletContainer = new int[this.aVariables.shapeletLabelArrayList.size()] [Collections. max(this.aVariables.shapeletLabelCountArrayList)];
    }
    public void clearShapletContainer(){
        if(!this.aVariables.loadShapeletYesOrNo || this.aVariables.shapletContainer.isEmpty()){
            return;
        }
        this.aVariables.shapletContainer.clear();
    }
    public void addIntoShapletContainer(int s){
        this.aVariables.shapletContainer.add(s);
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
    public void selectTop_K_Shapelets(){
        if(this.aVariables.shapeletDouble == null){
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
        this.aVariables.loadShapeletYesOrNo = true;
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
        this.aVariables.shapeletTrace_topRightChart = new Trace2DLtd(null);
        this.aVariables.shapeletTrace_topRightChart.setTracePainter(new TracePainterDisc(2));
        this.aVariables.shapeletTrace_topRightChart.setStroke(new BasicStroke(2));
        this.aVariables.shapeletTrace_topRightChart.setColor(Color.DARK_GRAY);
        this.aVariables.topRightChart.addTrace(this.aVariables.shapeletTrace_topRightChart);
//
        /*** Add the initial shapelet to centerChart_I as well ***/
//        this.aVariables.shapeletTrace_centerChart = new Trace2DLtd(null);
        this.aVariables.shapeletTrace_centerChart = new Trace2DLtd(null);
        this.aVariables.shapeletTrace_centerChart.setTracePainter(new TracePainterDisc(3));
        this.aVariables.shapeletTrace_centerChart.setStroke(new BasicStroke(3));
        this.aVariables.shapeletTrace_centerChart.setColor(new Color(255, 77, 132));
        this.aVariables.centerChart.addTrace(this.aVariables.shapeletTrace_centerChart);

        /*** **/
        this.aVariables.centerChart.addTrace(this.aLocalLineShapeletTrace_center);
        this.aVariables.topRightChart.addTrace(this.aLocalLineShapeletTrace_topRight);
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_centerChart()                **
    ---------------------------------------------------------------*/
    public void createShapletMark_centerChart(){
        /***** ***/
        this.aVariables.shapeletMark_centerChart = new Trace2DLtd("Shapelet Trace ————————— ");
        this.aVariables.shapeletMark_centerChart.setColor(Color.DARK_GRAY);
        this.aVariables.centerChart.addTrace(this.aVariables.shapeletMark_centerChart);
        /*** **/
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void createShapletMark_topRightChart(){
        /***** ***/
        this.aVariables.shapeletMark_topRightChart = new Trace2DLtd("Shapelet Trace ————————— ");
        this.aVariables.shapeletMark_topRightChart.setColor(Color.GREEN);
        this.aVariables.topRightChart.addTrace(this.aVariables.shapeletMark_topRightChart);
        /*** **/
    }
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void addShapeletTraceonCenterChart(){
        this.aVariables.centerChart.addTrace(this.aVariables.shapeletTrace_centerChart);
        this.aVariables.centerChart.addTrace(this.aLocalLineShapeletTrace_center);
    };
    /*---------------------------------------------------------------
    **                 createShapletMark_topRightChart()                **
    ---------------------------------------------------------------*/
    public void addShapeletTrace_TopRightChart(){
        this.aVariables.topRightChart.addTrace(this.aVariables.shapeletTrace_topRightChart);
        this.aVariables.topRightChart.addTrace(this.aLocalLineShapeletTrace_topRight);
    };

    public void change_shapeletLabel(){
        /*** resetBottomTSTraceCount to 0 **/
        resetBottomTSTraceCount(0);
        /*** Normal model **/
//        selectShapletPlotModel_centerChart("normal");
        /*** Stack model **/
        clearShapletContainer(); // Clear stack model shapelet index storage
        selectShapletPlotModel_centerChart();
        /*** -> **/
        latest_aShapeletClass();
        setShapeletJList();
    }
    /*---------------------------------------------------------------
    **                     changeSelectedShapelet()                   **
    ---------------------------------------------------------------*/
    public void change_selectedShapelet(){
        try
        {
            /*** -> **/
            if(latest_aShapelet()==-1){
                return;
            }
            this.aGUIComponents.labelShapeletTextField.setVisible(true);
            this.aGUIComponents.labelShapeletTextField.setText("Shapelet Length: " + this.aVariables.currentShapelet.size());
            //            drawShapeletTrace_TopRightChart();
//            drawShapeletTrace_CenterChart();
            /*** Horizontal dot plot **/ /*** Horizontal line plot **/
//            shapelet_dotANDLine_plot("normal");
            /*** Stack model **/
            if(this.aVariables.stackModelOn){
                addIntoShapletContainer(this.aGUIComponents.shapeletJList.getSelectedIndex());
                shapelet_dotANDLine_plot("stack");
            }else{
                clearShapletContainer(); // Clear stack model shapelet index storage
                shapelet_dotANDLine_plot("normal");
            }
            /*
            drawShapeletChartStackModel();
             */
            setInfomationOnChart();
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    /*** --------------------------------------------- **/
    public void shapeletHorizontaSegmentTransfer(ArrayList<Double>[] aDataArray){
        this.aVariables.aShapeletLook = new ShapeletLook(aDataArray);
        this.aVariables.aShapeletLook.PAALike_initial();
    }

    public ArrayList<Double> horizontalDotLook_shapelet(ArrayList<Double> aShapeletData){
        ArrayList<Double> aShapelet_Arylist = this.aVariables.aShapeletLook.revalue(aShapeletData);
        /*** The return array list has discarded the label **/
        return aShapelet_Arylist;
    }

    /*---------------------------------------------------------------
     **          drawShapeletTrace_CenterChart()                    **
     ---------------------------------------------------------------*/
    public void drawShapeletTrace_centerChart(ArrayList<Double> aArylist){
        this.aVariables.shapeletTrace_centerChart.removeAllPoints();
        if(this.aVariables.currentShapelet != null){
            System.out.println("Shapelet length: " + this.aVariables.currentShapelet.size());
            int labelIndex = 0;
            if(this.aVariables.currentShapelet.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.shapeletTrace_centerChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.shapeletTrace_centerChart.setColor(new Color(51, 153, 255));
            }
            double distanceBetweenST = getShortestDistance();
            int startPosition = this.aVariables.globalStartPosition;
            this.aGUIComponents.distanceSTTextField.setVisible(true);
            this.aGUIComponents.distanceSTTextField.setText("distance TS: " + Math.round(distanceBetweenST * 100.0)/100.0);
            //System.out.println("startPosition "+startPosition);
            double newVal, oldVal=-1;
            for(int i = 0; i<aArylist.size(); i++){ //The transferred shapelet array list has discarded the label in the first index
                newVal =  aArylist.get(i);
                if(!(i==0) && newVal==oldVal){
                    continue;
                }else{
                    oldVal=newVal;
                }
                this.aVariables.shapeletTrace_centerChart.addPoint((startPosition+i), newVal);
            }
        }
    }

    public double getShortestDistance(){ /*** Every plot after loading shapelet should calculate the distance between shapelet and TS **/
        int startPosition = 0;
        double distanceBetweenST = 0;
        double distanceMin = Double.MAX_VALUE;;
        for(int i=0; i<(this.aVariables.TSDataInstance.features.size()-(this.aVariables.currentShapelet.size()-1)); i++ ){ // Discard first label
            // index in indexthis.aVariables.currentShapelet
            distanceBetweenST = 0;
            for(int j=1; j<this.aVariables.currentShapelet.size(); j++){ // j=1 -> discard first label
                // index in indexthis.aVariables.currentShapelet
                distanceBetweenST += Math.pow(this.aVariables.TSDataInstance.features.get(j+i).value - this.aVariables.currentShapelet.get(j), 2.0);
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

        return distanceMin/((this.aVariables.currentShapelet.size()-1)*1.0);
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
    public void drawShapeletTrace_topRightChart(ArrayList<Double> aArylist){
        this.aVariables.shapeletTrace_topRightChart.removeAllPoints();
        if(this.aVariables.currentShapelet != null){
            int labelIndex = 0;
            if(this.aVariables.currentShapelet.get(labelIndex).intValue()==0){ //label 0
                this.aVariables.shapeletTrace_topRightChart.setColor(new Color(255, 51, 153));
            }else{
                this.aVariables.shapeletTrace_topRightChart.setColor(new Color(51, 153, 255));
            }
            /*** No need to calculate distance again, the center chart has got it. **/
            int startPosition = this.aVariables.globalStartPosition;
            this.aGUIComponents.distanceSTTextField_II.setVisible(true);
            this.aGUIComponents.distanceSTTextField_II.setText("Empty");
            //System.out.println("startPosition "+startPosition);
            double newVal, oldVal=-1;
            for(int i=0; i<aArylist.size(); i++){ //The transferred shapelet array list has discarded the label in the first index
                newVal =  aArylist.get(i);
                if(!(i==0) && newVal==oldVal){
                    continue;
                }else{
                    oldVal=newVal;
                }
                this.aVariables.shapeletTrace_topRightChart.addPoint((startPosition+i), newVal);
            }
        }
    }

    /*---------------------------------------------------------------
    **                     selectShapeletRange()                     **
    ---------------------------------------------------------------*/
    public void selectShapeletRange(int min, int max){
        if (max >= 0) {
            DefaultListModel newshapeletJListModel = new DefaultListModel();
            for(int i = 0; i < this.aVariables.shapeletDouble.length; i++)
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

    public void draw_shapeletLineTransfer(ArrayList<Double> anArylist, String model, int shapletIndex){
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
        removeLocalShapeletTrace_twoCharts();
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
            addLocalTrace_centerChart(divider, indexP, anArylist);
        }else if(model.equalsIgnoreCase("stack")){
            addLocalTrace_centerChart_stack(divider, indexP, anArylist);
        }else{
            Logger logger
                    = Logger.getLogger(
                    MajorMethods_Shapelet.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, "Select either 'normal' or 'stack' shapelet model on trace drawing. ");
        }
        /*** Each time plots the current shapelet **/
        addLocalTrace_topRightChart(divider, indexP, anArylist, shapletIndex);
    }

    public void addLocalTrace_centerChart(int divider, int startP, ArrayList<Double> singleAry){
        try{
            if(!aLocalLineShapeletTrace_center.isEmpty()){
                aLocalLineShapeletTrace_center.removeAllPoints();
            }

            int labelIndex = 0;
            if(this.aVariables.currentShapelet.get(labelIndex).intValue()==0){ //label 0
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

    public void addLocalTrace_centerChart_stack(int divider, int startP, ArrayList<Double> singleAry){
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
            if(this.aVariables.currentShapelet.get(labelIndex).intValue()==0){ //label 0
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

    public void addLocalTrace_topRightChart(int divider, int startP, ArrayList<Double> singleAry, int shapletIndex){
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
            if(this.aVariables.currentShapelet.get(labelIndex).intValue()==0){ //label 0
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

    public void shapelet_dotANDLine_plot(String model){
        selectShapletPlotModel_centerChart();
        ArrayList<Double> aryList;

        if(this.aVariables.stackModelOn){
            /*** **/
            /*** Plot additional stack trace **/
            if(model.equalsIgnoreCase("stack")){
                for(int index: this.aVariables.shapletContainer){
                    System.out.println("shapelet_dotANDLine_plot() -> index: " + index);
                    if(index==this.aGUIComponents.shapeletJList.getSelectedIndex()){ //Avoid plot duplicate
                        continue;
                    }
                    ArrayList<Double> tempShapelet = this.aVariables.shapeletsWithCurrentLabel[index];
                    getShortestDistance(tempShapelet);
                    aryList = horizontalDotLook_shapelet(tempShapelet);
                    draw_shapeletLineTransfer(aryList, model, index); // both center chart and top right chart
                }
            }
        }

        aryList = horizontalDotLook_shapelet(this.aVariables.currentShapelet);
        drawShapeletTrace_centerChart(aryList); // center chart
        drawShapeletTrace_topRightChart(aryList); // top right chart
        draw_shapeletLineTransfer(aryList, model, this.aVariables.lastShapeletIndex); // both center chart and top right chart, aDistance = -1 means not stack model
        /*** This function clear all traces (temp shapelet traces) on center chart and add all necessary traces and info back **/

    }

    /*** ---------------------------------------------------------------------------------------------------------* */
    public void removeLocalShapeletTrace_twoCharts(){
        this.aLocalLineShapeletTrace_topRight.removeAllPoints();
        this.aLocalLineShapeletTrace_center.removeAllPoints();
    }
    /*** ---------------------------------------------------------------------------------------------------------* */

    public boolean getLoadShapeletYesOrNo(){
        return this.aVariables.loadShapeletYesOrNo;
    }
}
