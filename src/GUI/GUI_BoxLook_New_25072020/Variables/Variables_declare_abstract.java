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
    public double[] minMaxTimeSeriesDataset;
    public double[] minMaxShapeletDataset;
    // public protected DataSet shapeletSet;
    // SP: shapelet, TS: time series
    public ArrayList<Double>[] Shapelet_double_firstIndexIsLable, Shapelet_withCurrentLabel_firstIndexIsLable, Shapelet_weight;
    public ArrayList<ArrayList<ArrayList<double[]>>> Shapelet_toAllTS_distances;
    public ArrayList<Double> currentShapelet_firstIndexIsLable;
    public ArrayList<Integer> Shapelet_labelArrayList, Shapelet_labelCountArrayList;
    public ArrayList<Integer> TS_labelArryList;
    /*** Charts **/
    public Chart2D topRightShapeletChart, topRightTimeseriesChart, centerChart, bottomChart;
    public Chart2D[] multiCharts;
    /*** TS --- **/
    public ITrace2D TSMark_centerChart, TSMark_bottomChart;
    public ITrace2D TSDotTrace;
    /*** interpolated TS --- **/
    public ITrace2D interpolatedTSMark_centerChart;
    public ITrace2D interpolatedTimeSeriesTrace;
    /*** Shapelet --- **/
    public ITrace2D shapeletMarkCenterChart, shapeletMarkTopRightChart;
    public ITrace2D shapeletDotTraceCenterChart, shapeletLineTraceTopRightChart;

    public boolean initializeTS, load_Shapelet_YesOrNo, load_Timeseries_YesOrNo;
    public boolean initialize_TS_list, setting_TS_listModal;
    public boolean firstTSDrawing; /*** A sign for the first TS when a new shapelet is occurring **/
    public boolean firstTSDrawing_linePlot;
    public boolean stackModelOn, switchDot;
    // public shapelets
    public double centerChartXL, centerChartXR, centerChartYU, centerChartYD;
    public double oldScale, NewScale;
    public static double centerChartWidth;
    public Set<Integer> Shapelet_container; // Label: [i][], shapelet: [][k]
    public int globalStartPosition;
    public int globalBestMatchSP, globalBestMatchEP;
    public int lastTSIndex; // Default value = 0
    public int lastShapeletIndex; // Default value = 0
    public int bottomTSTraceCount;
    public String root, shapeletSubroot;
}
