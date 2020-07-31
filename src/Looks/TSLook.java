package Looks;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import DataStructures.FeaturePoint;

import java.util.ArrayList;
import java.util.HashMap;

import Utilities.GlobalValues;

public class TSLook {

    DataSet aDataSet;

    HashMap<String, Double> aBoundset;

    public TSLook(DataSet aDataSet){
        this.aDataSet = aDataSet;
    }

    /*** ---------------------------> **/
    public void PAALike_initial() {
        /*** 1. Order all thr data in dataset **/

        ArrayList<Double> orderedArylist = aDataSet.getOrderedArylist();

        /*** 2. Divide the dataset into 7 intervals **/
        this.aBoundset = getBounds(orderedArylist);

    }
    /***  **/

    /*** 2. Divide the dataset into 7 intervals **/
    public HashMap<String, Double> getBounds(ArrayList<Double> aArylist){

        /*** Biinning values into 7 bins **/
        int val_bin_y = 7;

        /*** SAX: Alpha = 7 **/

        /*** Standard deviation percentages **/

        double one_std = 0.3413;

        double two_std = 0.4772;

        double three_std = 0.4987;

        HashMap<String, Double> bounds = new HashMap<String, Double>();

        double lowerbound, q3_left, q2_left, q1_left, q1_right, q2_right, q3_right, upperbound;

        int index;

        int aSize = aArylist.size();

        /*** Size problem ling -> int: If the size excesses the int, how to solve it? What is the maximum size of an Arraylist **/
        if(aSize>=Integer.MAX_VALUE){
            System.out.println("aSize from -> getBounds(ArrayList<Double> aArylist) -> TSRepresentation: " + aSize);
            IndexOutOfBoundsException exc = new IndexOutOfBoundsException();
            exc.printStackTrace();
        }

        try{

            index = 0;
            lowerbound = aArylist.get(index);
            bounds.put("lowerbound", lowerbound);

            index = (int) Math.round((1-three_std*2)*aSize);
            q3_left = aArylist.get(index);
            bounds.put("q3_left", q3_left);

            index = (int) Math.round((1-two_std*2)*aSize);
            q2_left = aArylist.get(index);
            bounds.put("q2_left", q2_left);

            index = (int) Math.round((1-one_std*2)*aSize);
            q1_left = aArylist.get(index);
            bounds.put("q1_left", q1_left);

            index = (int) Math.round((one_std*2)*aSize);
            q1_right = aArylist.get(index);
            bounds.put("q1_right", q1_right);

            index = (int) Math.round((two_std*2)*aSize);
            q2_right = aArylist.get(index);
            bounds.put("q2_right", q2_right);

            index = (int) Math.round((three_std*2)*aSize);
            q3_right = aArylist.get(index);
            bounds.put("q3_right", q3_right);

            /*** medium of the upper bound value and the maximum value [Extreme value affects the mean measurement ]**/
            index = (int) ((1-three_std*2)*aSize + aSize)/2;
            upperbound = aArylist.get(index);
            bounds.put("upperbound", upperbound);

        }catch (IndexOutOfBoundsException exc){
            exc.printStackTrace();
        }

        return bounds;
    };
    /***  **/

    /*** 3. Set the value of each interval **/  /*** 4. Change the value of dataset **/

    public ArrayList<Double> revalue(ArrayList<Double> aArylist){

        double lowerbound, q3_left, q2_left, q1_left, q1_right, q2_right, q3_right, upperbound;

        ArrayList<Double> aNewArylist = new ArrayList<>();

        lowerbound = aBoundset.get("lowerbound");
        q3_left = aBoundset.get("q3_left");
        q2_left = aBoundset.get("q2_left");
        q1_left = aBoundset.get("q1_left");
        q1_right = aBoundset.get("q1_right");
        q2_right = aBoundset.get("q2_right");
        q3_right = aBoundset.get("q3_right");
        upperbound = aBoundset.get("upperbound");

        for(double aVal: aArylist){
            double newVal;

            if(aVal<q3_left){
                newVal = (lowerbound+q3_left)/2.0;

            }else if(aVal<q2_left){
                newVal = (q3_left+q2_left)/2.0;

            }else if(aVal<q1_left){
                newVal = (q2_left+q1_left)/2.0;

            }else if(aVal<q1_right){
                newVal = (q1_left+q1_right)/2.0;

            }else if(aVal<q2_right){
                newVal = (q1_right+q2_right)/2.0;

            }else if(aVal<q3_right){
                newVal = (q2_right+q3_right)/2.0;

            }else{ // if(aVal>=q3_right)
                newVal = (q3_right+upperbound)/2.0;
            }

            aNewArylist.add(newVal);
        }

        return aNewArylist;

    };

    /*** **/


    public ArrayList<Double> arrayListTransfer(DataInstance aDataInstance){

        double aFeaturePointVal;

        ArrayList<Double> arraylist = new ArrayList<>();

        try{ // Be very careful of the index maximum bound
            DataInstance anDataInstance = aDataInstance;
            for(FeaturePoint aFeaturePoint: anDataInstance.features){
                if( aFeaturePoint.status == FeaturePoint.PointStatus.PRESENT && aFeaturePoint.value != GlobalValues.MISSING_VALUE ) {
                    aFeaturePointVal = aFeaturePoint.value;
                    arraylist.add(aFeaturePointVal);
                }
            }

        }catch(IndexOutOfBoundsException exc){
            exc.printStackTrace();
        }

        return arraylist;
    }





}
