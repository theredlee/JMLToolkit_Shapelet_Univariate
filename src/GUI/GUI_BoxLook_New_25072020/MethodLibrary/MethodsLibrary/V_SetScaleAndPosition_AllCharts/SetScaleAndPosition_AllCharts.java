package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.V_SetScaleAndPosition_AllCharts;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;
import info.monitorenter.util.Range;

public class SetScaleAndPosition_AllCharts extends SetScaleAndPosition_AllCharts_abstract {

    /*** ---------------------------------------------------------------------------------------------------------* */
    public SetScaleAndPosition_AllCharts(){}

    public SetScaleAndPosition_AllCharts(GUIComponents aGUIComponents, Variables aVariables){
        initialize(aGUIComponents, aVariables);
    }

    public void initialize(GUIComponents aGUIComponents, Variables aVariables){
        initializeReferenceParameters(aGUIComponents, aVariables);
    }

    /*---------------------------------------------------------------

     **                       setScale()                              **

     ---------------------------------------------------------------*/
    public void setScale(double scale){
        double myScale = scale - this.aVariables.oldScale;
        double xL = this.aVariables.centerChartXL;
        double xR = this.aVariables.centerChartXR;
        double myWidth = this.aVariables.centerChartWidth;

        this.aVariables.centerChart.getAxisX().setRange(new Range(xL += myWidth * myScale/2, xR -= myWidth * myScale/2));

        this.aVariables.centerChartXL = xL;
        this.aVariables.centerChartXR = xR;

        this.aVariables.oldScale = scale;

//        System.out.println("myWidth: " + myWidth);
//        System.out.println("centerChartXL: " + this.aVariables.centerChartXL);
//        System.out.println("centerChartXR: " + this.aVariables.centerChartXR);
    }

    /*---------------------------------------------------------------

     **                    timeSeriesZoomIn()                       **

     ---------------------------------------------------------------*/
    public void timeSeriesZoomIn(){

        double max = 2.5;
        double currentVal = ((Double)this.aGUIComponents.spinner.getValue()).floatValue();
        System.out.println("currentVal of zoom rate from timeSeriesZoomIn(): " + currentVal);

        if(currentVal < max - 0.1) { //Only if currentVal < max, it can work [2.5 = 2.499999, it should minus 0.1]
            this.aGUIComponents.spinner.setValue(Double.valueOf(currentVal+0.1));
//            setScale(currentVal);
        }

    }

    /*---------------------------------------------------------------

     **                    timeSeriesZoomOut()                       **

     ---------------------------------------------------------------*/
    public void timeSeriesZoomOut(){

        double min = 0.5;
        double currentVal = ((Double)this.aGUIComponents.spinner.getValue()).floatValue();
        System.out.println("currentVal of zoom rate from timeSeriesZoomOut(): " + currentVal);

        if(currentVal > min) {  //Only if currentVal > min, it can work
            this.aGUIComponents.spinner.setValue(Double.valueOf(currentVal-0.1));
//            setScale(currentVal);
        }

    }

    /*---------------------------------------------------------------

     **                    timeSeriesMoveLeft()                       **

     ---------------------------------------------------------------*/
    public void timeSeriesMoveLeft(){
        double xL = this.aVariables.centerChartXL;
        double xR = this.aVariables.centerChartXR;

        if(xR<(this.aVariables.dataset_withCurrentLabel.numFeatures+1)*1.1) {
            this.aVariables.centerChart.getAxisX().setRange(new Range(xL += 1, xR += 1));
        }

        this.aVariables.centerChartXL = xL;
        this.aVariables.centerChartXR = xR;
    }

    /*---------------------------------------------------------------

     **                    timeSeriesMoveRight()                      **

     ---------------------------------------------------------------*/
    public void timeSeriesMoveRight(){
        double xL = this.aVariables.centerChartXL;
        double xR = this.aVariables.centerChartXR;

        if(xL>-10) {
            this.aVariables.centerChart.getAxisX().setRange(new Range(xL -= 1, xR -= 1));
        }

        this.aVariables.centerChartXL = xL;
        this.aVariables.centerChartXR = xR;
    }

    /*---------------------------------------------------------------

     **                    setScaleAndPosition()                       **

     ---------------------------------------------------------------*/
    public void setScaleAndPosition(){
        this.aVariables.oldScale = 1.0;
        this.aVariables.centerChartXL = -5;
        this.aVariables.centerChartXR = this.aVariables.dataset_withCurrentLabel.numFeatures+5;
    }

    public void spinnerSetScale(){
        this.aGUIComponents.spinner.setValue(this.aVariables.oldScale);
    }

    public void centerChartSetRange(){
        this.aVariables.centerChart.getAxisX().setRange(new Range(this.aVariables.centerChartXL, this.aVariables.centerChartXR));
    }

    public void bottomChartSetRange(){
        this.aVariables.bottomChart.getAxisX().setRange(new Range(this.aVariables.centerChartXL, this.aVariables.centerChartXR));
    }

    /*** ---------------------------------------------------------------------------------------------------------* */
}
