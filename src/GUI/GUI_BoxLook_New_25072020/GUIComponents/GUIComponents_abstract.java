package GUI.GUI_BoxLook_New_25072020.GUIComponents;

public abstract class GUIComponents_abstract extends GUIComponents_declare_abstract {
    public void initializeGUIComponents(GUIComponents aGUIComponents){
        /*** initializeMeasurements --**/
        this.deskTopX = aGUIComponents.deskTopX;
        this.deskTopY = aGUIComponents.deskTopY;
        this.frameX = aGUIComponents.frameX;
        this.frameY = aGUIComponents.frameY;
        this.frameHeight = aGUIComponents.frameHeight;
        this.frameWidth = aGUIComponents.frameWidth;
        this.frameCentroidX = aGUIComponents.frameCentroidX;
        this.frameCentroidY = aGUIComponents.frameCentroidY;
        this.labelWidth = aGUIComponents.labelWidth;
        this.labelHeight = aGUIComponents.labelHeight;
        this.buttonWidth = aGUIComponents.buttonWidth;
        this.buttonHeight = aGUIComponents.buttonHeight;
        this.comboBoxWidth = aGUIComponents.comboBoxWidth;
        this.comboBoxHeight = aGUIComponents.comboBoxHeight;
        /*** initializeJFrame --**/
        this.frmTimeSeries = aGUIComponents.frmTimeSeries;
        /*** initializeLayers--**/
        this.frmTimeSeriesLayerFirst = aGUIComponents.frmTimeSeriesLayerFirst;
        this.frmTimeSeriesLayerSecond = aGUIComponents.frmTimeSeriesLayerSecond;
        /*** initializeFont --**/
        this.myFont = aGUIComponents.myFont;
        this.font = aGUIComponents.font;
        /*** initializeJLabels --**/
        this.lblZoomScale = aGUIComponents.lblZoomScale;
        this.lblHorizontallyMove = aGUIComponents.lblHorizontallyMove;
        this.lblShapeletsNumSelector = aGUIComponents.lblShapeletsNumSelector;
        this.lblMissingRatio = aGUIComponents.lblMissingRatio;
        this.lblGapSizeRatio = aGUIComponents.lblGapSizeRatio;
        this.lblImputation = aGUIComponents.lblImputation;
        this.eigenSeriesInterpolation = aGUIComponents.eigenSeriesInterpolation;
        this.lblLabel_TS_List = aGUIComponents.lblLabel_TS_List;
        this.lblLabel_shapelet_List = aGUIComponents.lblLabel_shapelet_List;
        this.lblTimeseries = aGUIComponents.lblTimeseries;
        this.lblShapelet = aGUIComponents.lblShapelet;
        this.lblStackmodel = aGUIComponents.lblStackmodel;
        this.lblDotLineSwitch = aGUIComponents.lblDotLineSwitch;
        this.lblLogo = aGUIComponents.lblLogo;
        this.lblTopTenCharts = aGUIComponents.lblTopTenCharts;
        this.lblDOMInfo = aGUIComponents.lblDOMInfo;
        /*** initializeJSpinner --**/
        this.spinner = aGUIComponents.spinner;
        /*** initializeJButton --**/
        this.btnZoomIn = aGUIComponents.btnZoomIn;
        this.btnZoomOut = aGUIComponents.btnZoomOut;
        this.btnMoveLeft = aGUIComponents.btnMoveLeft;
        this.btnMoveRight = aGUIComponents.btnMoveRight;
        this.btnSetTsIdRange = aGUIComponents.btnSetTsIdRange;
        this.btnSetShptIdRange = aGUIComponents.btnSetShptIdRange;
        this.btnSortedByLenShptIdASC = aGUIComponents.btnSortedByLenShptIdASC;
        this.btnSortedByLenShptIdDESC = aGUIComponents.btnSortedByLenShptIdDESC;
        this.btnClearAllTraces = aGUIComponents.btnClearAllTraces;
        this.btnLoadDataset = aGUIComponents.btnLoadDataset;
        this.btnLoadShapelet = aGUIComponents.btnLoadShapelet;
        this.btnSelectTop_K_Shapelets = aGUIComponents.btnSelectTop_K_Shapelets;
        this.btnRunBspcover = aGUIComponents.btnRunBspcover;
        this.btnInvokeDT = aGUIComponents.btnInvokeDT;
        /*** initializeJRadioButton --**/
        this.radiobtnStackModelOn = aGUIComponents.radiobtnStackModelOn;
        this.radiobtnStackModelOff = aGUIComponents.radiobtnStackModelOff;
        this.radiobtnSwitchDot = aGUIComponents.radiobtnSwitchDot;
        this.radiobtnSwitchLine = aGUIComponents.radiobtnSwitchLine;
        /*** initializeJTextField --**/
        this.iterationTextField = aGUIComponents.iterationTextField;
        this.alphabetSizeTextField = aGUIComponents.alphabetSizeTextField;
        this.timeSeriesRangeMinTextField = aGUIComponents.timeSeriesRangeMinTextField;
        this.timeSeriesRangeMaxTextField = aGUIComponents.timeSeriesRangeMaxTextField;
        this.shapeletsRangeMinTextField = aGUIComponents.shapeletsRangeMinTextField;
        this.shapeletsRangeMaxTextField = aGUIComponents.shapeletsRangeMaxTextField;
        this.centerChartTSLabelTextField = aGUIComponents.centerChartTSLabelTextField;
        this.centerChartTSNumTextField = aGUIComponents.centerChartTSNumTextField;
        this.centerChartSPLetNumTextField = aGUIComponents.centerChartSPLetNumTextField;
        this.chartII_Shapelet_TextField = aGUIComponents.chartII_Shapelet_TextField;
        this.chartII_ShapeletLabel_TextField = aGUIComponents.chartII_ShapeletLabel_TextField;
        this.top_K_shapeletsTextField = aGUIComponents.top_K_shapeletsTextField;
        this.labelShapeletTextField = aGUIComponents.labelShapeletTextField;
        this.datasetTextField = aGUIComponents.datasetTextField;
        this.noPointsTextField = aGUIComponents.noPointsTextField;
        this.numOfShapeletsTextField = aGUIComponents.numOfShapeletsTextField;
        this.distanceSTTextField = aGUIComponents.distanceSTTextField;
        this.distanceSTTextField_II = aGUIComponents.distanceSTTextField_II;
        this.pcoverTextField = aGUIComponents.pcoverTextField;
        this.labelTextField = aGUIComponents.labelTextField;
        this.lblMultiChartTSClass = aGUIComponents.lblMultiChartTSClass;
        this.lblMultiChartTSNum = aGUIComponents.lblMultiChartTSNum;
        this.lblMultiChartSPLetNum = aGUIComponents.lblMultiChartSPLetNum;
        this.lblMultiChartSPLetClass = aGUIComponents.lblMultiChartSPLetClass;
        this.lblTopk = aGUIComponents.lblTopk;
        /*** initializeJTextArea --**/
        this.bspcoverInfoTextArea = aGUIComponents.bspcoverInfoTextArea;
        this.textAreaInOnChart_I = aGUIComponents.textAreaInOnChart_I;
        /*** initializeJCheckBox --**/
        this.enableSupervisionCheckBox = aGUIComponents.enableSupervisionCheckBox;
        this.imputeCheckBox = aGUIComponents.imputeCheckBox;
        this.shuffleDataSetCheckBox = aGUIComponents.shuffleDataSetCheckBox;
        this.enableDTWingCheckBox = aGUIComponents.enableDTWingCheckBox;
        this.imputationTechnique = aGUIComponents.imputationTechnique;
        this.classListItem = aGUIComponents.classListItem;
        /*** initializeJList --**/
        this.labelList = aGUIComponents.labelList;
        this.TS_labelList_Horizontal = aGUIComponents.TS_labelList_Horizontal;
        this.TS_List = aGUIComponents.TS_List;
        this.shapeletJList = aGUIComponents.shapeletJList;
        this.shapeletLabelJList = aGUIComponents.shapeletLabelJList;
        /*** initializeJScrollPane --**/
        this.TS_labelScrollPane = aGUIComponents.TS_labelScrollPane;
        this.labelScrollPane = aGUIComponents.labelScrollPane;
        this.timeSeriesScrollPane = aGUIComponents.timeSeriesScrollPane;
        this.shapeLetScrollPane = aGUIComponents.shapeLetScrollPane;
        this.labelListScrollPane = aGUIComponents.labelListScrollPane;
        this.bspcoverInfoScrollPane = aGUIComponents.bspcoverInfoScrollPane;
        /*** initializeJPanel --**/
        this.centerChartPanel = aGUIComponents.centerChartPanel;
        this.topRightPanel = aGUIComponents.topRightPanel;
        this.bottomChartPanel = aGUIComponents.bottomChartPanel;
        this.distanceHistPanel = aGUIComponents.distanceHistPanel;
        this.weightHistPanel = aGUIComponents.weightHistPanel;
        this.DOMInfoPanel = aGUIComponents.DOMInfoPanel;
        /*** initializeLayeredPane --**/
        this.layeredPane_CenterChart = aGUIComponents.layeredPane_CenterChart;
        this.layeredPane_TopRightChart = aGUIComponents.layeredPane_TopRightChart;
        this.layeredPane_BottomChart = aGUIComponents.layeredPane_BottomChart;
        this.layeredPane_distanceHist = aGUIComponents.layeredPane_distanceHist;
        this.layeredPane_weightHist = aGUIComponents.layeredPane_weightHist;
        this.layeredPane_logo = aGUIComponents.layeredPane_logo;
        this.layeredPane_multiCharts = aGUIComponents.layeredPane_multiCharts;
        this.layeredPane_toptenChartLabel = aGUIComponents.layeredPane_toptenChartLabel;
    }
}
