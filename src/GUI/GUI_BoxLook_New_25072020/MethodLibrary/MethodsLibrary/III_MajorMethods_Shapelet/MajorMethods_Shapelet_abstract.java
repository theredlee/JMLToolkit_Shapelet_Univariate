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

public abstract class MajorMethods_Shapelet_abstract extends MajorMethods_Shapelet_declare_abstract {
    /*** All classes create an GUIComponents reference **/
    protected void initializeReferenceParameters(GUIComponents aGUIComponents, Variables aVariables){
        /*** Java only passes by value **/
        /*** even though Java passes parameters to methods by value,
         * if the variable points to an object reference, the real object will also be changed.**/
        this.aGUIComponents = aGUIComponents;
        this.aVariables = aVariables;
    }

    public void initializePartners(DistanceClassification aDistanceClassification, ComponentsAddListners aComponentsAddListners, MajorMethods_Timeseries aMajorMethods_Timeseries,
                                   MajorMethods_Shapelet aMajorMethods_Shapelet, SetInfo_Charts aSetInfo_Charts,
                                   SetScaleAndPosition_AllCharts aSetScaleAndPosition_AllCharts,
                                   Clear_TracesAndCharts aClear_TracesAndCharts,
                                   SortData aSortData){
        /*** No self **/
        /*** No reference no need to assign as well **/
        this.aDistanceClassification = aDistanceClassification;
//        this.aComponentsAddListners = aComponentsAddListners;
        this.aMajorMethods_Timeseries = aMajorMethods_Timeseries;
//        this.aMajorMethods_Shapelet = aMajorMethods_Shapelet;
        this.aSetInfo_Charts = aSetInfo_Charts;
        this.aSetScaleAndPosition_AllCharts = aSetScaleAndPosition_AllCharts;
        this.aClear_TracesAndCharts = aClear_TracesAndCharts;
//        this.aSortData = aSortData;
    }

    protected void setScale(){
        aSetScaleAndPosition_AllCharts.setSlideZoomDefaultValue();
        aSetScaleAndPosition_AllCharts.setScaleAndPosition();
//        aSetScaleAndPosition_AllCharts.spinnerSetScale();
        aSetScaleAndPosition_AllCharts.centerChartSetRange();
    }

    protected void setInfomationOnChart(){
        this.aSetInfo_Charts.resetInfoTextField_CenterChart();
    };

    protected void resetBottomTSTraceCount(int val){
        this.aMajorMethods_Timeseries.resetBottomTSTraceCount(val);
    }

    protected void selectShapletPlotModel_centerChart(){
        this.aClear_TracesAndCharts.removeAllTraces_centerChart();
        this.aClear_TracesAndCharts.addTraceBack_pointsKept_centerChart();
    };

    protected void infoClassificaationTest(String str){
        this.aSetInfo_Charts.infoClassificaationTest(str);
    };
}
