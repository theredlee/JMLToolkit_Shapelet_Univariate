package GUI.GUI_BoxLook_New_25072020.Controller.II_MethodsController;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.DistanceClassification.DistanceClassification;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.III_MajorMethods_Shapelet.MajorMethods_Shapelet;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.II_MajorMethods_TImeseries.MajorMethods_Timeseries;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts.SetInfo_Charts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.I_ComponentsAddListners.ComponentsAddListners;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VII_SortData.SortData;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VI_Clear_TracesAndCharts.Clear_TracesAndCharts;
import GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.V_SetScaleAndPosition_AllCharts.SetScaleAndPosition_AllCharts;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

public abstract class MethodsController_abstract extends MethodsController_declare_abstract {
    /*** All classes create an GUIComponents reference **/

    public void initializeOutsideObjects(GUIComponents aGUIComponents, Variables aVariables){
        this.aComponentsAddListners = new ComponentsAddListners(aGUIComponents, aVariables);
        this.aMajorMethods_Timeseries = new MajorMethods_Timeseries(aGUIComponents, aVariables);
        this.aMajorMethods_Shapelet = new MajorMethods_Shapelet(aGUIComponents, aVariables);
        this.aSetInfo_Charts = new SetInfo_Charts(aGUIComponents, aVariables);
        this.aSetScaleAndPosition_AllCharts = new SetScaleAndPosition_AllCharts(aGUIComponents, aVariables);
        this.aClear_TracesAndCharts = new Clear_TracesAndCharts(aGUIComponents, aVariables);
        this.aSortData = new SortData(aGUIComponents, aVariables);

        this.aDistanceClassification = new DistanceClassification(aGUIComponents, aVariables);
        this.aDistanceClassification.initializePartners(this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);

        this.aComponentsAddListners.initializePartners(this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);
        this.aMajorMethods_Timeseries.initializePartners(this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);
        this.aMajorMethods_Shapelet.initializePartners(this.aDistanceClassification, this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);
        this.aSetInfo_Charts.initializePartners(this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);
        this.aSetScaleAndPosition_AllCharts.initializePartners(this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);
        this.aClear_TracesAndCharts.initializePartners(this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);
        this.aSortData.initializePartners(this.aComponentsAddListners, this.aMajorMethods_Timeseries, this.aMajorMethods_Shapelet,
                this.aSetInfo_Charts, this.aSetScaleAndPosition_AllCharts, this.aClear_TracesAndCharts,
                this.aSortData);
    }

}
