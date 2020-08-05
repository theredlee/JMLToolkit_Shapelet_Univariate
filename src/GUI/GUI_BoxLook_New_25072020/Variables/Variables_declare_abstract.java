package GUI.GUI_BoxLook_New_25072020.Variables;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import Looks.ShapeletLook;
import Looks.TSLook;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

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
    // SP: shapelet, TS: time series
    public ArrayList<Double>[] SPLet_double, SPLet_withCurrentLabel, SPLet_weight;
    public ArrayList<ArrayList<ArrayList<double[]>>> SPLet_toAllTS_distances;
    public ArrayList<Double> currentSPLet_;
    public ArrayList<Integer> SPLet_labelArrayList, SPLet_labelCountArrayList;
    public ArrayList<Integer> TS_labelArryList;
    /*** Charts **/
    public Chart2D centerChart, topRightChart, bottomChart;
    public Chart2D[] multiCharts;
    /*** TS --- **/
    public ITrace2D TSMark_centerChart, TSMark_bottomChart;
    public ITrace2D TSTrace;
    /*** interpolated TS --- **/
    public ITrace2D interpolatedTSMark_centerChart;
    public ITrace2D interpolatedTimeSeriesTrace;
    /*** Shapelet --- **/
    public ITrace2D SPLet_mark_centerChart, SPLet_mark_topRightChart;
    public ITrace2D SPLet_trace_centerChart, SPLet_trace_topRightChart;

    public boolean initializeTS, load_SPLet_YesOrNo;
    public boolean initialize_TS_list, setting_TS_listModal;
    public boolean firstTSDrawing; /*** A sign for the first TS when a new shapelet is occurring **/
    public boolean firstTSDrawing_linePlot;
    public boolean stackModelOn;
    // public shapelets
    public double centerChartXL, centerChartXR, centerChartYU, centerChartYD;
    public double oldScale, NewScale;
    public static double centerChartWidth;
    public Set<Integer> SPLet_container; // Label: [i][], shapelet: [][k]
    public int globalStartPosition;
    public int globalBestMatchSP, globalBestMatchEP;
    public int lastTSIndex; // Default value = 0
    public int lastSPLetIndex; // Default value = 0
    public int bottomTSTraceCount;
    public String root, shapeletSubroot;
}
