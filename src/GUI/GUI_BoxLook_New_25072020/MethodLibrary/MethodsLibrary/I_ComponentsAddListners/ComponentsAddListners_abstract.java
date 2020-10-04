package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.I_ComponentsAddListners;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet.MajorMethods_Shapelet;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.II_MajorMethods_TImeseries.MajorMethods_Timeseries;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts.SetInfo_Charts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VII_SortData.SortData;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VI_Clear_TracesAndCharts.Clear_TracesAndCharts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.V_SetScaleAndPosition_AllCharts.SetScaleAndPosition_AllCharts;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

import java.awt.event.ActionEvent;

public abstract class ComponentsAddListners_abstract extends ComponentsAddListners_declare_abstract {
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
                                   SortData aSortData) {
        /*** No self **/
        /*** No reference no need to assign as well **/
//        this.aComponentsAddListners = aComponentsAddListners;
        this.aMajorMethods_Timeseries = aMajorMethods_Timeseries;
        this.aMajorMethods_Shapelet = aMajorMethods_Shapelet;
//        this.aSetInfo_Charts = aSetInfo_Charts;
        this.aSetScaleAndPosition_AllCharts = aSetScaleAndPosition_AllCharts;
        this.aClear_TracesAndCharts = aClear_TracesAndCharts;
        this.aSortData = aSortData;
    }

    /*** rewrite methods that need invoke outsides methods in other classes **/

    protected void setScale(double scale){
//        aSetScaleAndPosition_AllCharts.setScale(scale);
    }

    protected void timeSeriesZoomCenterChart(int value){
        aSetScaleAndPosition_AllCharts.timeSeriesSlideZoomCenterChart(value);
    }

    protected void timeSeriesZoomBottomChart(int value){
        aSetScaleAndPosition_AllCharts.timeSeriesSlideZoomBottomChart(value);
    }

    protected void timeSeriesZoomIn(){
        aSetScaleAndPosition_AllCharts.timeSeriesZoomIn();
    }

    protected void timeSeriesZoomOut(){
        aSetScaleAndPosition_AllCharts.timeSeriesZoomOut();
    }

    protected void timeSeriesMoveRight(){
        aSetScaleAndPosition_AllCharts.timeSeriesMoveRight();
    }

    protected void timeSeriesMoveLeft(){
        aSetScaleAndPosition_AllCharts.timeSeriesMoveLeft();
    }

    protected void selectTSRange(int aMin, int aMax){
        aMajorMethods_Timeseries.selectTSRange(aMin, aMax);
    }

    protected void loadDataSet(){
        aMajorMethods_Timeseries.loadDataSet();
    }

    protected void runBspcover() {
//        aMajorMethods_Timeseries.runBspcover();
        aMajorMethods_Timeseries.runBspcoverRemote();
    }

    protected void stackModelChange(ActionEvent e){
        aMajorMethods_Shapelet.ShapeletCenterChartStackModelChange(e);
    }

    protected void dotLineSwitch(ActionEvent e){
        aMajorMethods_Timeseries.dotLineSwitch(e);
    }
    protected void changeTS_label(){
        aMajorMethods_Timeseries.changeTSLabel();
    }

    protected void changeSelectedTS(){
        aMajorMethods_Timeseries.changeSelectedTS();
    }

    protected void selectShapeletRange(int aMin, int aMax){
        aMajorMethods_Shapelet.selectShapeletRange(aMin, aMax);
    }

    protected void loadShapelet(){
        aMajorMethods_Shapelet.loadShapelet();
    }

    protected void selectTop_K_Shapelets(){
        aMajorMethods_Shapelet.selectTopKShapelets();
    }

    protected void changeShapeletLabel(){
        aMajorMethods_Shapelet.changeShapeletLabel();
    }

    protected void changeSelectedShapelet(){
        aMajorMethods_Shapelet.changeSelectedShapelet();
    }

    protected void clearAllTSTraces_AllCharts(){
        aClear_TracesAndCharts.clearAllTSTraces_AllCharts();
    }

    protected void btnClearAll_addBack(){
        aClear_TracesAndCharts.btnClearAll_addBack();
    }

    protected void sortedASC(){
        aSortData.sortedASC();
    }

    protected void sortedDESC(){
        aSortData.sortedDESC();
    }

    protected void sortedByLength(){
        aSortData.sortedByLength();
    }

    protected void clearShapletContainer(){
        aMajorMethods_Shapelet.clearShapletContainer();

    }

    protected void resetGlobalTimeseriesMinMax(){
        this.aSetScaleAndPosition_AllCharts.resetGlobalTimeseriesMinMax();
    }

}
