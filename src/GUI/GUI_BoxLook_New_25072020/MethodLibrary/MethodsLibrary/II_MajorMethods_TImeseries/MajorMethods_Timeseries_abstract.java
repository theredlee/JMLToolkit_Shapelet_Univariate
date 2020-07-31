package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.II_MajorMethods_TImeseries;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet.MajorMethods_Shapelet;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts.SetInfo_Charts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.I_ComponentsAddListners.ComponentsAddListners;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VII_SortData.SortData;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VI_Clear_TracesAndCharts.Clear_TracesAndCharts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.V_SetScaleAndPosition_AllCharts.SetScaleAndPosition_AllCharts;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

public abstract class MajorMethods_Timeseries_abstract extends MajorMethods_Timeseries_declare_abstract {
    /*** All classes create an GUIComponents reference **/
    protected void initializeReferenceParameters(GUIComponents aGUIComponents, Variables aVariables){
        /*** Java only passes by value **/
        /*** even though Java passes parameters to methods by value,
         * if the variable points to an object reference, the real object will also be changed.**/
        this.aGUIComponents = aGUIComponents;
        this.aVariables = aVariables;
    }

    public void initializePartners(ComponentsAddListners aComponentsAddListners, MajorMethods_Timeseries aMajorMethods_Timeseries,
                                 MajorMethods_Shapelet aMajorMethods_Shapelet, SetInfo_Charts aSetInfo_Charts,
                                 SetScaleAndPosition_AllCharts aSetScaleAndPosition_AllCharts,
                                 Clear_TracesAndCharts aClear_TracesAndCharts,
                                 SortData aSortData){
        /*** No self **/
        /*** No reference no need to assign as well **/
//        this.aComponentsAddListners = aComponentsAddListners;
//        this.aMajorMethods_Timeseries = aMajorMethods_Timeseries;
        this.aMajorMethods_Shapelet = aMajorMethods_Shapelet;
        this.aSetInfo_Charts = aSetInfo_Charts;
        this.aSetScaleAndPosition_AllCharts = aSetScaleAndPosition_AllCharts;
        this.aClear_TracesAndCharts = aClear_TracesAndCharts;
//        this.aSortData = aSortData;
    }

    protected void setScale(){
        aSetScaleAndPosition_AllCharts.setScaleAndPosition();
        aSetScaleAndPosition_AllCharts.spinnerSetScale();
        aSetScaleAndPosition_AllCharts.centerChartSetRange();
    }

    protected void setScale_bottomChartSetRange(){
        aSetScaleAndPosition_AllCharts.bottomChartSetRange();
    }

    protected void setInfomationOnChart(){
        aSetInfo_Charts.resetInfoTextField_CenterChart();
    };

    protected void shapelet_dotANDLine_plot(){
//        aMajorMethods_Shapelet.shapelet_dotANDLine_plot("normal");
        aMajorMethods_Shapelet.shapelet_dotANDLine_plot("stack");
    }

    protected int updateTolatest_aShapelet(){
        return aMajorMethods_Shapelet.latest_aShapelet();
    }

}
