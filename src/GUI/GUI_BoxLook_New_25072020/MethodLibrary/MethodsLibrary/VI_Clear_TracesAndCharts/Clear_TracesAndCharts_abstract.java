package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VI_Clear_TracesAndCharts;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet.MajorMethods_Shapelet;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.II_MajorMethods_TImeseries.MajorMethods_Timeseries;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts.SetInfo_Charts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.I_ComponentsAddListners.ComponentsAddListners;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VII_SortData.SortData;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.V_SetScaleAndPosition_AllCharts.SetScaleAndPosition_AllCharts;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

public abstract class Clear_TracesAndCharts_abstract extends Clear_TracesAndCharts_declare_abstract {
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
        this.aMajorMethods_Timeseries = aMajorMethods_Timeseries;
        this.aMajorMethods_Shapelet = aMajorMethods_Shapelet;
//        this.aSetInfo_Charts = aSetInfo_Charts;
        this.aSetScaleAndPosition_AllCharts = aSetScaleAndPosition_AllCharts;
//        this.aClear_TracesAndCharts = aClear_TracesAndCharts;
//        this.aSortData = aSortData;
    }

    protected void localAddTraceBack_topRightChart(){
        if(getLoadShapeletYesOrNo()){
            aMajorMethods_Shapelet.addShapeletTraceTopRightChart();
        }
    };

    protected void localAddTraceBack__pointsKept_centerChart() {
        /*** 1st. set marks TS - centerChart **/
        aMajorMethods_Timeseries.createTSMarkCenterChart();

        /*** 2nd. set marks interpolated TS - centerChart **/
        aMajorMethods_Timeseries.createinterpolatedTSMarkCenterChart();

        /*** 3rd. set marks shapelet - centerChart **/
        aMajorMethods_Shapelet.createShapletMarkCenterChart();

        /*** ------------------------------------------------- **/

        /*** Add back TS trace on center chart **/
        aMajorMethods_Timeseries.addGlobalTSTraceOnCenterChart();

        aMajorMethods_Timeseries.addLocalTSTraceonCenterChart();

        /*** Add TS trace back on center chart **/
        if(getLoadShapeletYesOrNo()){
            aMajorMethods_Shapelet.addShapeletTraceonCenterChart();
        }
        /*** ------------------------------------------------- **/
    }

    protected void localAddTraceBack__pointsRemoved_centerChart(){
        /*** 1st. set marks TS - centerChart **/
        aMajorMethods_Timeseries.createTSMarkCenterChart();

        /*** 2nd. set marks interpolated TS - centerChart **/
        aMajorMethods_Timeseries.createinterpolatedTSMarkCenterChart();

        /*** 3rd. set marks shapelet - centerChart **/
        aMajorMethods_Shapelet.createShapletMarkCenterChart();

        /*** ------------------------------------------------- **/
        /*** Trace point remove **/
        aVariables.TSTrace.removeAllPoints();
        aMajorMethods_Timeseries.removeLocalTSTracePointsCenterChart();
        /*** ------------------------------------------------- **/

        /*** Add back TS trace on center chart **/
        aMajorMethods_Timeseries.addGlobalTSTraceOnCenterChart();

        aMajorMethods_Timeseries.addLocalTSTraceonCenterChart();

        /*** Add TS trace back on center chart **/
        if(getLoadShapeletYesOrNo()){
            aMajorMethods_Shapelet.addShapeletTraceonCenterChart();
        }
        /*** ------------------------------------------------- **/
    }

    protected void localAddTraceBack_bottomChart(){
        /*** Add marks TS back - bottomChart **/
        aMajorMethods_Timeseries.createTSMarkBottomChart();
        /*** Since all traces on bottom chart are local temp traces, they cannot retrieve back **/
    }

    protected void setScale(){
        aSetScaleAndPosition_AllCharts.setScaleAndPosition();
    }

    protected boolean getLoadShapeletYesOrNo(){
        return aMajorMethods_Shapelet.getLoadShapeletYesOrNo();
    }

}
