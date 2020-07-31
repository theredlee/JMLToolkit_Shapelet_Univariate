package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.DistanceClassification.DistanceClassification;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.II_MajorMethods_TImeseries.MajorMethods_Timeseries;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts.SetInfo_Charts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.I_ComponentsAddListners.ComponentsAddListners;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VII_SortData.SortData;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VI_Clear_TracesAndCharts.Clear_TracesAndCharts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.V_SetScaleAndPosition_AllCharts.SetScaleAndPosition_AllCharts;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

public abstract class MajorMethods_Shapelet_declare_abstract {
    protected GUIComponents aGUIComponents;
    protected Variables aVariables;

    /*** No self **/
    protected DistanceClassification aDistanceClassification;
    protected ComponentsAddListners aComponentsAddListners;
    protected MajorMethods_Timeseries aMajorMethods_Timeseries;
//    protected MajorMethods_Shapelet aMajorMethods_Shapelet;
    protected SetInfo_Charts aSetInfo_Charts;
    protected SetScaleAndPosition_AllCharts aSetScaleAndPosition_AllCharts;
    protected Clear_TracesAndCharts aClear_TracesAndCharts;
    protected SortData aSortData;

    /*** Unique variable in shapelet class **/
    protected Trace2DLtd aLocalLineShapeletTrace_topRight;
    protected Trace2DLtd aLocalLineShapeletTrace_center;
}
