package GUI.GUI_BoxLook_New_25072020.Variables;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import Looks.ShapeletLook;
import Looks.TSLook;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public abstract class Variables_declare_abstract {
    public File dataSetDirectory;
    public File shapeletDirectory;
    public DataSet dataSet, dataset_withCurrentLabel;
    public DataInstance TSDataInstance, TSDataInstance_bottomChart;
    public TSLook aTSLook;
    public ShapeletLook aShapeletLook;
    // public protected DataSet shapeletSet;
    public ArrayList<Double>[] shapeletDouble, shapeletsWithCurrentLabel;
    public ArrayList<Double> currentShapelet;
    public ArrayList<Integer> shapeletLabelArrayList, shapeletLabelCountArrayList;
    public ArrayList<Integer> TS_labelArryList, shapelet_LabelCountArrayList;
    /*** Charts **/
    public Chart2D centerChart, topRightChart, bottomChart;
    /*** TS --- **/
    public ITrace2D TSMark_centerChart, TSMark_bottomChart;
    public ITrace2D TSTrace;
    /*** interpolated TS --- **/
    public ITrace2D interpolatedTSMark_centerChart;
    public ITrace2D interpolatedTimeSeriesTrace;
    /*** Shapelet --- **/
    public ITrace2D shapeletMark_centerChart, shapeletMark_topRightChart;
    public ITrace2D shapeletTrace_centerChart, shapeletTrace_topRightChart;

    public boolean initializeTS, loadShapeletYesOrNo;
    public boolean initialize_TS_list, setting_TS_listModal;
    public boolean firstTSDrawing; /*** A sign for the first TS when a new shapelet is occurring **/
    public boolean firstTSDrawing_linePlot;
    public boolean stackModelOn;
    // public shapelets
    public double aDoubleAryShapelets[][][];
    public double centerChartXL, centerChartXR, centerChartYU, centerChartYD;
    public double oldScale, NewScale;
    public static double centerChartWidth;
    public Set<Integer> shapletContainer; // Label: [i][], shapelet: [][k]
    public int globalStartPosition;
    public int globalBestMatchSP, globalBestMatchEP;
    public int lastTSListIndex; // Default value = 0
    public int lastShapeletIndex; // Default value = 0
    public int bottomTSTraceCount;
    public String root;
}
