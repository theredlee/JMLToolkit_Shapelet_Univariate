package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.VII_SortData;

import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SortData extends SortData_abstract {

    public SortData(){}

    public SortData(GUIComponents aGUIComponents, Variables aVariables){
        initialize(aGUIComponents, aVariables);
    }

    public void initialize(GUIComponents aGUIComponents, Variables aVariables){
        initializeReferenceParameters(aGUIComponents, aVariables);
    }

    /*---------------------------------------------------------------

     **                    sortedByLength()                       **

     ---------------------------------------------------------------*/
    public void sortedByLength(){
        /*** Default sorted ASC***/
        ArrayList<Double>[] shapeletDoubleTemp = this.aVariables.Shapelet_double;

        int[] myArray = new int[this.aVariables.Shapelet_double.length];

        for(int i = 0; i < this.aVariables.Shapelet_double.length; i++){
//            System.out.println("shapelet [" + i + "] length: "+ shapeletDouble[i].size());
            myArray[i] = this.aVariables.Shapelet_double[i].size(); /*** Each shapelet length (size) to myArray ***/
        }

        /*** Binary Insertion Sort ***/
        for (int i = 1; i < myArray.length; i++)
        {
            int x = myArray[i];
            ArrayList<Double> x_shapelet = this.aVariables.Shapelet_double[i];

            // Find location to insert using binary search
            int j = Math.abs(Arrays.binarySearch(myArray, 0, i, x) + 1);

            //Shifting array to one location right
            System.arraycopy(myArray, j, myArray, j+1, i-j);

            //Placing element at its correct location
            myArray[j] = x;

            /*** Use j as a bridge to sort ***/
            System.arraycopy(shapeletDoubleTemp, j, shapeletDoubleTemp, j+1, i-j);
            shapeletDoubleTemp[j] = x_shapelet;
        }
        /*** ***/

//        System.out.println("shapelet length: "+currentShapelet.size());

        this.aVariables.Shapelet_double = shapeletDoubleTemp;

        for(int i = 0; i<this.aVariables.Shapelet_double.length; i++) {
            System.out.println("shapeletDouble[" + i + "] length: " + this.aVariables.Shapelet_double[i].size());
        }
    }

    /*---------------------------------------------------------------

     **                    sortedASC()                       **

     ---------------------------------------------------------------*/
    public void sortedASC(){

        DefaultListModel shapeletJListModelASC = new DefaultListModel();
        for(int i = 0; i < this.aVariables.Shapelet_double.length; i++)
        {
            shapeletJListModelASC.addElement(String.valueOf(i));
        }
        this.aGUIComponents.shapeletJList.setModel(shapeletJListModelASC);
        JOptionPane.showMessageDialog(this.aGUIComponents.frmTimeSeries,
                "Shapelets sorted by ascend successfully!");
    }

    /*---------------------------------------------------------------

     **                    sortedDESC()                       **

     ---------------------------------------------------------------*/
    public void sortedDESC(){

        DefaultListModel shapeletJListModelDESC = new DefaultListModel();
        for(int i = this.aVariables.Shapelet_double.length - 1; i >= 0; i--)
        {
            shapeletJListModelDESC.addElement(String.valueOf(i));
        }
        this.aGUIComponents.shapeletJList.setModel(shapeletJListModelDESC);
        JOptionPane.showMessageDialog(this.aGUIComponents.frmTimeSeries,
                "Shapelets sorted by descend successfully!");
    }
}
