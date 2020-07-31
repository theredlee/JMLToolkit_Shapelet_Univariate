package GUI.GUI_BoxLook_New_25072020.MethodLibrary.OldestVersion.MethodsLibrary_;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.OldestVersion.GUIComponents_.GUIComponents_;
import Looks.ShapeletLook;
import Looks.TSLook;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public abstract class MethodsLibrary_abstract {
    JFrame frmTimeSeries;
    JPanel centerChartPanel;
    JPanel centercenterChartPanel;
    JPanel topRightPanel;
    JLayeredPane frmTimeSeriesLayerFirst;
    JList timeSeriesList;
    JList TS_labelList_Horizontal;
    JList shapeletJList;
    JList shapeletLabelJList;
    JButton btnZoomIn;
    JButton btnZoomOut;
    JButton btnMoveLeft;
    JButton btnMoveRight;
    JButton btnSetTsIdRange;
    JButton btnSetShptIdRange;
    JButton btnSortedByLenShptIdASC;
    JButton btnSortedByLenShptIdDESC;
    JButton btnClearAllTraces;
    JButton btnLoadDataset;
    JButton btnLoadShapelet;
    JButton btnSelectTop_K_Shapelets;
    JButton btnRunBspcover;
    JTextArea bspcoverInfoTextArea;
    JTextField noPointsTextField;
    JTextField timeSeriesRangeMinTextField;
    JTextField timeSeriesRangeMaxTextField;
    JTextField shapeletsRangeMinTextField;
    JTextField shapeletsRangeMaxTextField;
    JTextField iterationTextField;
    JTextField alphabetSizeTextField;
    JTextField pcoverTextField;
    JTextField labelTextField;
    JTextField chartI_TS_TextField;
    JTextField chartI_Interpolated_TextField;
    JTextField chartI_Shapelet_TextField;
    JTextField chartII_Shapelet_TextField;
    JTextField numOfShapeletsTextField;
    JTextField labelShapeletTextField;
    JTextField distanceSTTextField;
    JTextField distanceSTTextField_II;
    JTextField top_K_shapeletsTextField;
    JSpinner spinner;
    JCheckBox imputeCheckBox;
    JCheckBox shuffleDataSetCheckBox;

    File dataSetDirectory;
    File shapeletDirectory;
    DataSet dataSet, dataset_withCurrentLabel;
    DataInstance TSDataInstance, TSDataInstance_bottomChart;
    TSLook aTSLook;
    ShapeletLook aShapeletLook;
    //protected DataSet shapeletSet;
    ArrayList<Double>[] shapeletDouble, shapeletsWithCurrentLabel;
    ArrayList<Double> currentShapelet;
    ArrayList<Integer> shapeletLabelArrayList, shapeletLabelCountArrayList;
    ArrayList<Integer> TS_labelArryList, TS_shapeletLabelCountArrayList;

    /*** Charts **/
    Chart2D centerChart, topRightChart, bottomChart;
    /*** TS --- **/
    ITrace2D TSMark_centerChart, TSMark_bottomChart;
    ITrace2D TSTrace;
    /*** interpolated TS --- **/
    ITrace2D interpolatedTSMark_centerChart;
    ITrace2D interpolatedTimeSeriesTrace;
    /*** Shapelet --- **/
    ITrace2D shapeletMark_centerChart, shapeletMark_topRightChart;
    ITrace2D shapeletTrace_centerChart, shapeletTrace_topRightChart;

    boolean initializeTS = true, loadShapeletYesOrNo = false;
    boolean setting_TS_ListModal = false;
    boolean firstTSDrawing = false, clearTS_bottomChart = false; /*** A sign for the first TS when a new shapelet is occurring **/
    boolean firstTSDrawing_linePlot = false;
    // shapelets
    double Shapelets[][][];
    double centerChartXL, centerChartXR, centerChartYU, centerChartYD;
    double oldScale, NewScale;
    static double centerChartWidth;
    int globalStartPosition = 0;
    int globalBestMatchSP = 0, globalBestMatchEP = 0;
    int lastTimeseriesListIndex = 0; // Default value = 0

    String root = System.getProperty("user.dir");

    /*---------------------------------------------------------------
     ******************************************************************/

    /*
     * initialize a GUIComponents instance
     */
    GUIComponents_ myGUIComponents;
}
