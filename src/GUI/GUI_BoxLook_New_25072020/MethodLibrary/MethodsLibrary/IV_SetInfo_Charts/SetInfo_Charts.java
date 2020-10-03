package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.IV_SetInfo_Charts;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SetInfo_Charts extends SetInfo_Charts_abstract {

    /*** ---------------------------------------------------------------------------------------------------------* */
    public SetInfo_Charts(){}

    public SetInfo_Charts(GUIComponents aGUIComponents, Variables aVariables){
        initialize(aGUIComponents, aVariables);
    }

    public void initialize(GUIComponents aGUIComponents, Variables aVariables){
        initializeReferenceParameters(aGUIComponents, aVariables);
    }

    /*---------------------------------------------------------------

     **        resetInfoTextField_CenterChart()           **

     ---------------------------------------------------------------*/
    public void resetInfoTextField_CenterChart(){

        int selectedTSIndex = 0; /** default value: 0 **/

        try{
            if(!this.aGUIComponents.TS_List.isSelectionEmpty()){
                selectedTSIndex = Integer.parseInt( this.aGUIComponents.TS_List.getSelectedValue().toString() ); /** **/
            }
        }
        catch( NullPointerException exc )
        {
            System.out.println(exc);
        }

        try{
            this.aGUIComponents.centerChartTSLabelTextField.setText("Class Label No.: " + ((int) this.aVariables.dataset_withCurrentLabel.instances.get(selectedTSIndex).target));
            this.aGUIComponents.centerChartTSNumTextField.setText("Time Series No.: " + selectedTSIndex);

            if(this.aVariables.load_Shapelet_YesOrNo){
                if(!this.aGUIComponents.shapeletJList.isSelectionEmpty()){
                    this.aGUIComponents.centerChartShapeletNumTextField.setText("Shapelet No.: " + this.aGUIComponents.shapeletJList.getSelectedValue().toString());
                    this.aGUIComponents.chartII_Shapelet_TextField.setText("Shapelet No.: " + this.aGUIComponents.shapeletJList.getSelectedValue().toString());
                }
            }
        }
        catch (NullPointerException e){
            // Create a Logger
            Logger logger = Logger.getLogger(SetInfo_Charts.class.getName());

            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, e.toString());
        }
    }

    public void infoClassificaationTest(String str){
        this.aGUIComponents.bspcoverInfoTextArea.setText("Classification test: " + str);
    }
}
