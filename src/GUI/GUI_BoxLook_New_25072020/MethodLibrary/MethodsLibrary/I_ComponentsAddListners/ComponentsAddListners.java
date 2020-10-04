package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.I_ComponentsAddListners;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComponentsAddListners extends ComponentsAddListners_abstract {

    /*** ---------------------------------------------------------------------------------------------------------* */
    /*---------------------------------------------------------------

     **                    MethodsLibrary()                              **

     ---------------------------------------------------------------*/
    public ComponentsAddListners(){}

    public ComponentsAddListners(GUIComponents aGUIComponents, Variables aVariables){
        initialize(aGUIComponents, aVariables);
    }

    private void initialize(GUIComponents aGUIComponents, Variables aVariables){
        initializeReferenceParameters(aGUIComponents, aVariables);
        initializeAddListeners();
    }

    private void initializeAddListeners(){
        addChangeListener();
        addActionListner();
        addListSelectionListener();
    }

    /*** ---------------------------------------------------------------------------------------------------------* */

    /*---------------------------------------------------------------

     **                    addChangeListener()                              **

     ---------------------------------------------------------------*/
    public void addChangeListener(){
        /***  -------------------------------- **/
//        this.aGUIComponents.spinner.addChangeListener(new ChangeListener()
//        {
//            public void stateChanged(ChangeEvent e)
//            {
//                float scale = ((Double) aGUIComponents.spinner.getValue()).floatValue();
//                setScale(scale);
//            }
//        });

        /***  -------------------------------- **/
        this.aGUIComponents.zoomSliderCenterChart.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider)event.getSource();
                int value = source.getValue();
                timeSeriesZoomCenterChart(value);
            }
        });

        /***  -------------------------------- **/
        this.aGUIComponents.zoomSliderBottomChart.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider)event.getSource();
                int value = source.getValue();
                timeSeriesZoomBottomChart(value);
            }
        });


        //        /***  -------------------------------- **/

//        this.aGUIComponents.btnZoomIn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                timeSeriesMoveLeft();
//                timeSeriesZoomIn();
//            }
//        });
//
//        /***  -------------------------------- **/
//
//        this.aGUIComponents.btnZoomOut.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                timeSeriesMoveLeft();
//                timeSeriesZoomOut();
//            }
//        });
//
//        /***  -------------------------------- **/
//
//        this.aGUIComponents.btnMoveLeft.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                timeSeriesMoveLeft();
//                timeSeriesMoveRight();
//            }
//        });
//
//        /***  -------------------------------- **/
//
//        this.aGUIComponents.btnMoveRight.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                timeSeriesMoveRight();
//                timeSeriesMoveLeft();
//            }
//        });
    }

    /*---------------------------------------------------------------

     **                    addActionListner()                              **

     ---------------------------------------------------------------*/
    public void addActionListner(){

        /***  -------------------------------- **/

        this.aGUIComponents.btnSetTsIdRange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectTSRange(Integer.parseInt ( aGUIComponents.timeSeriesRangeMinTextField.getText()),Integer.parseInt( aGUIComponents.timeSeriesRangeMaxTextField.getText()));
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.btnSetShptIdRange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectShapeletRange(Integer.parseInt(aGUIComponents.shapeletsRangeMinTextField.getText()),Integer.parseInt(aGUIComponents.shapeletsRangeMaxTextField.getText()));
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.btnSortedByLenShptIdASC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortedASC();
            }
        });

        /***  -------------------------------- **/
        this.aGUIComponents.btnSortedByLenShptIdDESC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortedDESC();
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.btnClearAllTraces.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                /*** Clear stack model shapelet index storage **/
                clearShapletContainer();
                clearAllTSTraces_AllCharts();
                btnClearAll_addBack();
                resetGlobalTimeseriesMinMax();
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.btnLoadDataset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                loadDataSet();
                System.out.println("loadDataSet() invoked");
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.btnLoadShapelet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadShapelet();
                /*** Default sort ***/
//                sortedByLength(); /*** Shiwen: I think the shapelets have sorted by hierarchy in EfficientLTS (but not by length) **/
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.btnSelectTop_K_Shapelets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectTop_K_Shapelets();
                /*** Default sort ***/
//                sortedByLength();
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.btnRunBspcover.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runBspcover();
            }
        });

        this.aGUIComponents.radiobtnStackModelOn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stackModelChange(e);
            }
        });

        this.aGUIComponents.radiobtnStackModelOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stackModelChange(e);
            }
        });

//        this.aGUIComponents.radiobtnSwitchDot.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                dotLineSwitch(e);
//            }
//        });
//
//        this.aGUIComponents.radiobtnSwitchLine.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                dotLineSwitch(e);
//            }
//        });
    }

    /*---------------------------------------------------------------

     **                    addListSelectionListener()                              **

     ---------------------------------------------------------------*/
    public void addListSelectionListener(){

        /***  -------------------------------- **/

        this.aGUIComponents.TS_labelList_Horizontal.addListSelectionListener(new ListSelectionListener() {
//            public void contentsChanged(ListDataEvent e){}
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {//This line prevents double events
                    changeTS_label();
                }
            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.TS_List.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent arg1) {
                if (!arg1.getValueIsAdjusting()) {//This line prevents double events
                    System.out.println("Invoke changeSelectedTS()");
                    changeSelectedTS();
                }
                /*** - list will fire the final event - selection operation is finished
                 - getValueIsAdjusting=false now
you can do whatever you want with final selection
                 To sum up - those additional events are fired to let you completely control list behavior on selection changes (on selection change sequence to be exact). You might want to ignore the selection changes when getValueIsAdjusting=true since there always will be a final event with getValueIsAdjusting=false which will inform you that selection changes are finished.
                 Also
when you change selection with key buttons list wouldn't know if you are going to change it after first key press or not
so getValueIsAdjusting will be always false for such changes.***/

            }
        });

        /***  -------------------------------- **/

        this.aGUIComponents.shapeletJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    changeSelectedShapelet();
                }
            }
        });

        /***  -------------------------------- **/
        this.aGUIComponents.shapeletLabelJList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    changeShapeletLabel();
                }
            }
        });
    }

}
