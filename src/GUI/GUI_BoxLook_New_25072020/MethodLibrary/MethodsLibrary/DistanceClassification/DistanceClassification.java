package GUI.GUI_BoxLook_New_25072020.MethodLibrary.MethodsLibrary.DistanceClassification;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import GUI.GUI_BoxLook_New_25072020.GUIComponents.GUIComponents;
import GUI.GUI_BoxLook_New_25072020.Variables.Variables;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DistanceClassification extends DistanceClassification_abstract {

    public DistanceClassification(){}

    public DistanceClassification(GUIComponents aGUIComponents, Variables aVariables){
        initialize(aGUIComponents, aVariables);
    }

    private void initialize(GUIComponents aGUIComponents, Variables aVariables){
        initializeReferenceParameters(aGUIComponents, aVariables);
    }

    public void classificationTest() throws FileNotFoundException, UnsupportedEncodingException {
        //[0]: d0, [1]: d1, [2]: label
//        outputArray = new ArrayList[3];

//        for(int i=0; i<outputArray.length; i++){
//            outputArray[i] = new ArrayList<>();
//        }

        int TP_0 = 0, FN_0 = 0, FP_0 = 0, TN_0 = 0;
        int TP_1 = 0, FN_1 = 0, FP_1 = 0, TN_1 = 0;
        DataSet aDataSet = this.aVariables.dataSet;
        DataSet datasetLabeled;
        ArrayList<Integer> TS_labelList = this.aVariables.TS_labelArryList;

        ArrayList<Double>[] aShapeletDouble = this.aVariables.shapeletDouble;
        ArrayList<Integer> shapelet_Labellist = this.aVariables.shapeletLabelArrayList;

        double minDistance;
        double distance;
        double[] distanceSum = new double[shapelet_Labellist.size()];

        int labelIndex = 0;

        double shapelet_count_0=0;
        double shapelet_count_1=0;

        for(ArrayList<Double> aShapelet: aShapeletDouble){
            int shapelet_Label = aShapelet.get(labelIndex).intValue();
            if(shapelet_Label==0){
                shapelet_count_0++;
            }else if(shapelet_Label==1){
                shapelet_count_1++;
            }else{
                Logger logger
                        = Logger.getLogger(
                        DistanceClassification.class.getName());
                // log messages using log(Level level, String msg)
                logger.log(Level.WARNING, "Number of shapelet labels error I. ");
            }
        }
        System.out.println("shapelet_count_0: " + shapelet_count_0);
        System.out.println("shapelet_count_1: " + shapelet_count_1);

        for(double TS_label: TS_labelList){
            datasetLabeled = aDataSet.FilterByLabel(TS_label);
            for(DataInstance aInstance: datasetLabeled.instances){

                for(int k=0; k< distanceSum.length; k++){
                    distanceSum[k]=0;
                }

                double[] sqrSum = {0, 0};

                for(ArrayList<Double> aShapelet: aShapeletDouble){
                    int shapelet_Label = aShapelet.get(labelIndex).intValue();
    //                    System.out.println("shapelet_Label: " + shapelet_Label);
                    distance = getShortestDistance_unit(aInstance, aShapelet);
                    distanceSum[shapelet_Label] += distance;
                    sqrSum[shapelet_Label] += distance*distance;
                }

                // /*** After get sum distance of all labels shapelet **/
                // This loop is designed for only two labels dataset: shapelet_Label==0->{positive} shapelet_Label==1->{negative}
                double distance_0=0, distance_1=0;
                int labelGuess;

                System.out.println( "TS_Label: " +  "-----------------------------------------> "+ TS_label );
    //                outputArray[2].add(TS_label);
                for(int shapelet_Label: shapelet_Labellist){
    //                    System.out.println("shapelet_Label: " + shapelet_Label);
                    if(shapelet_Label==0){
                        distance_0 = distanceSum[shapelet_Label]/(shapelet_count_0*1.0)/Math.sqrt(sqrSum[shapelet_Label]);
                        System.out.println("distance_0: " + distance_0);
    //                        outputArray[shapelet_Label].add(distance_0);
                    }else if(shapelet_Label==1){
                        distance_1 = distanceSum[shapelet_Label]/(shapelet_count_1*1.0)/Math.sqrt(sqrSum[shapelet_Label]);
                        System.out.println("distance_1: " + distance_1);
    //                        outputArray[shapelet_Label].add(distance_1);
                    }else{
                        Logger logger
                                = Logger.getLogger(
                                DistanceClassification.class.getName());
                        // log messages using log(Level level, String msg)
                        logger.log(Level.WARNING, "Number of shapelet labels error II. ");
                    }

                }


                if(distance_0<distance_1){
                    labelGuess=0;
                }else if(distance_1<distance_0){
                    labelGuess=1;
                }else{
                    labelGuess=-1;
                }

                // label of current time series: TS_label
                if(TS_label==0 && labelGuess==0){
                    TP_0++;
                }else if(TS_label==0 && labelGuess==1){
                    FN_0++;
                }else if(TS_label==1 && labelGuess==0){
                    FP_0++;
                }else if(TS_label==1 && labelGuess==1){
                    TN_0++;
                }else{
                    Logger logger
                            = Logger.getLogger(
                            DistanceClassification.class.getName());
                    // log messages using log(Level level, String msg)
                    logger.log(Level.WARNING, "Number of shapelet labels error III. ");
                }
            }
        }

//        System.out.println("True positive: " + TP);
//        System.out.println("False positive: " + PF);
//        System.out.println("True negative: " + NT);
//        System.out.println("False negative " + FN);
//        System.out.println("Accuracy:" + (double)((TP+FN)*1.0/(TP+PF+NT+FN)));

        String str_0 = "\nTrue positive: " + TP_0 + "\nFalse negative: " + FN_0 + "\nFalse positive: " + FP_0 + "\nTrue negative: " + TN_0 + "\nAccuracy: " + (TP_0+TN_0)*1.0/(TP_0+FN_0+FP_0+TN_0) + "\n";
//        String str_1 = "\nTrue positive: " + TP_1 + "\nFalse negative: " + FN_1 + "\nFalse positive: " + FP_1 + "\nTrue negative: " + TN_1 + "\nAccuracy: " + (TP_1+TN_1)*1.0/(TP_1+FN_1+FP_1+TN_1) + "\n";
        infoClassificaationTest("\n" + str_0);

//        outputFile(outputArray);
    }

    public void classificationTest_v2(){

        int TP = 0, FN = 0, FP = 0, TN = 0;
        DataSet aDataSet = this.aVariables.dataSet;
        DataSet datasetLabeled;
        ArrayList<Integer> TS_labelList = this.aVariables.TS_labelArryList;

        ArrayList<Double> [] aShapeletDouble = this.aVariables.shapeletDouble;
        ArrayList<Integer> shapelet_Labellist = this.aVariables.shapeletLabelArrayList;

        double[] distance = new double[shapelet_Labellist.size()];
        double[] minDistance = new double[shapelet_Labellist.size()];
        double[] distanceSum = new double[shapelet_Labellist.size()];

        int labelIndex = 0;

        double shapelet_count_0=0;
        double shapelet_count_1=0;

        for(ArrayList<Double> aShapelet: aShapeletDouble){
            int shapelet_Label = aShapelet.get(labelIndex).intValue();
            if(shapelet_Label==0){
                shapelet_count_0++;
            }else{
                shapelet_count_1++;
            }
        }

        int aCount = 0;
        for(double TS_label: TS_labelList){
            datasetLabeled = aDataSet.FilterByLabel(TS_label);
            for(DataInstance aInstance: datasetLabeled.instances){

                for(int i=0; i< distanceSum.length; i++){ // Initialize sumArray
                    distanceSum[i]=0;
                }

                for(int i=0; i< minDistance.length; i++){ // Initialize sumArray
                    minDistance[i]=Double.MAX_VALUE;
                }

                for(ArrayList<Double> aShapelet: aShapeletDouble){
                    int shapelet_Label = aShapelet.get(labelIndex).intValue();
//                    System.out.println("shapelet_Label: " + shapelet_Label);
                    distance[shapelet_Label] = getShortestDistance_unit(aInstance, aShapelet);
                    if(minDistance[shapelet_Label]>distance[shapelet_Label]){
                        minDistance[shapelet_Label]=distance[shapelet_Label];
                    }
                    distanceSum[shapelet_Label] = minDistance[shapelet_Label];
                }

                // /*** After get sum distance of all labels shapelet **/
                // This loop is designed for only two labels dataset: shapelet_Label==0->{positive} shapelet_Label==1->{negative}
                double distance_0=0, distance_1=0;
                int labelGuess;

                System.out.println( "TS_Label: " + "-----------------------------------------> "  + TS_label);
                for(int shapelet_Label: shapelet_Labellist){
//                    System.out.println("shapelet_Label: " + shapelet_Label);
                    if(shapelet_Label==0){
                        distance_0 = distanceSum[shapelet_Label]/(shapelet_count_0*1.0);;
                        System.out.println("distance_0: " + distance_0);
                    }else if(shapelet_Label==1){
                        distance_1 = distanceSum[shapelet_Label]/(shapelet_count_1*1.0);;
                        System.out.println("distance_1: " + distance_1);
                    }else{
                        Logger logger
                                = Logger.getLogger(
                                DistanceClassification.class.getName());
                        // log messages using log(Level level, String msg)
                        logger.log(Level.WARNING, "Number of shapelet labels error I. ");
                    }
                }

                if(distance_0<distance_1){
                    labelGuess=0;
                }else{
                    labelGuess=1;
                }

                // label of current time series: TS_label
                if(TS_label==0 && labelGuess==0){
                    TP++;
                }else if(TS_label==0 && labelGuess==1){
                    FN++;
                }else if(TS_label==1 && labelGuess==0){
                    FP++;
                }else if(TS_label==1 && labelGuess==1){
                    TN++;
                }else{
                    Logger logger
                            = Logger.getLogger(
                            DistanceClassification.class.getName());
                    // log messages using log(Level level, String msg)
                    logger.log(Level.WARNING, "Number of shapelet labels error II. ");
                }
            }
        }

//        System.out.println("True positive: " + TP);
//        System.out.println("False positive: " + PF);
//        System.out.println("True negative: " + NT);
//        System.out.println("False negative " + FN);
//
//        System.out.println("Accuracy:" + (double)((TP+FN)*1.0/(TP+PF+NT+FN)));
        String str = "\nTrue positive: " + TP + "\nFalse negative: " + FN + "\nFalse positive: " + FP + "\nTrue negative: " + TN + "\nAccuracy: " + (TP+TN)*1.0/(TP+FN+FP+TN) + "\n";
        infoClassificaationTest("\n" + str);
    }

    public ArrayList<ArrayList<ArrayList<double[]>>> classificationTest_v3() throws FileNotFoundException, UnsupportedEncodingException {
        //[0]: d0, [1]: d1, [2]: label

//        int TP_0 = 0, FN_0 = 0, FP_0 = 0, TN_0 = 0;
//        int TP_1 = 0, FN_1 = 0, FP_1 = 0, TN_1 = 0;
        DataSet aDataSet = this.aVariables.dataSet;
        DataSet datasetLabeled;
        ArrayList<Integer> TS_labelList = this.aVariables.TS_labelArryList;

        ArrayList<Double>[] aShapeletDouble = this.aVariables.shapeletDouble;
        ArrayList<Integer> shapelet_Labellist = this.aVariables.shapeletLabelArrayList;

        if(TS_labelList.size()!=shapelet_Labellist.size()){
            Logger logger
                    = Logger.getLogger(
                    DistanceClassification.class.getName());
            // log messages using log(Level level, String msg)
            logger.log(Level.WARNING, "Labels error -0: Number of shapelet labels error don't equals to number of time series labels.");
        }

        double distance;
        int labelIndex = 0;
        double shapelet_count_0=0;
        double shapelet_count_1=0;

        for(ArrayList<Double> aShapelet: aShapeletDouble){
            int shapelet_Label = aShapelet.get(labelIndex).intValue();
            if(shapelet_Label==0){
                shapelet_count_0++;
            }else if(shapelet_Label==1){
                shapelet_count_1++;
            }else{
                Logger logger
                        = Logger.getLogger(
                        DistanceClassification.class.getName());
                // log messages using log(Level level, String msg)
                logger.log(Level.WARNING, "Number of shapelet labels error I. ");
            }
        }
        System.out.println("shapelet_count_0: " + shapelet_count_0);
        System.out.println("shapelet_count_1: " + shapelet_count_1);

        ArrayList<ArrayList<ArrayList<double[]>>> distance_all = new ArrayList<>();

        for(int numLabels = 0; numLabels<TS_labelList.size(); numLabels++){
            ArrayList<ArrayList<double[]>> outputArray_ = new ArrayList<>();
            // Each class of shapelett output a file
            int shapelet_Label = -1;
            for(ArrayList<Double> aShapelet: aShapeletDouble){
                shapelet_Label = aShapelet.get(labelIndex).intValue();
                ArrayList<double[]> myArrayList = new ArrayList<>();
                for(double TS_label: TS_labelList){
                    datasetLabeled = aDataSet.FilterByLabel(TS_label);
                    int TSnum = 0; // The retrieving makes sense only if all classes follow the same order: label order -> TS default order from this.aVariables.dataSet
                    for(DataInstance aInstance: datasetLabeled.instances){
                        int dimensions = 3; // dimensions = 3 [0]: distance, [1]: label, [2]: time series number
                        double[] ary = new double[dimensions];
                        distance = getShortestDistance_unit(aInstance, aShapelet);
                        ary[0] = distance;
                        ary[1] = TS_label;
                        ary[2] = TSnum;
                        myArrayList.add(ary);
                        TSnum++;
                    }
                }
                outputArray_.add(myArrayList);
            }
            if(shapelet_Label==-1){
                Logger logger
                        = Logger.getLogger(
                        DistanceClassification.class.getName());
                // log messages using log(Level level, String msg)
                logger.log(Level.WARNING, "Shapelet labels has not been initialized. Error I. ");
            }else{
                distance_all.set(shapelet_Label, outputArray_);
            }
        }

        return distance_all;
    }

    public double getShortestDistance_unit(DataInstance aInstance, ArrayList<Double> currentShapelet){ /*** Every plot after loading shapelet should calculate the distance between shapelet and TS **/
        double distanceBetweenST = 0;
        double distanceMin = Double.MAX_VALUE;;
        for(int i=0; i<(aInstance.features.size()-(currentShapelet.size()-1)); i++ ){ // Discard first label
            // index in indexcurrentShapelet
            distanceBetweenST = 0;
            for(int j=1; j< currentShapelet.size(); j++){ // j=1 -> discard first label
                // index in indexcurrentShapelet
                distanceBetweenST += Math.pow(aInstance.features.get(j+i).value - currentShapelet.get(j), 2.0);
            }
            distanceBetweenST = Math.sqrt(distanceBetweenST);
            if(distanceBetweenST < distanceMin){
                distanceMin = distanceBetweenST;
            }
        }
        /*** After get a distanceMin **/
//        return distanceMin;
        return distanceMin/((currentShapelet.size()-1)*1.0);
    }

    public void outputFile(ArrayList<Double>[] anArray) throws FileNotFoundException, UnsupportedEncodingException {
            String root = System.getProperty("user.dir");
            String subroot_I = "/datasets/Grace_dataset/v_2/";
            String path = root + subroot_I + "outputfile.txt";

            int rows = anArray[0].size();
            int col = anArray.length;

            PrintWriter writer = new PrintWriter(path, "UTF-8");

            for(int k=0; k<rows ;k++){
                System.out.println("Outout: --> ");
                for(int l=0; l<col ;l++){
                    /*** ---------------------------- **/
                    writer.append(Double.toString(anArray[l].get(k)));
                    /*** **/
                    if(l < (col-1)){
                        writer.append(",");
                    }
                }
                writer.println();
            }

            writer.close();
    }

    public void outputFile_v2(ArrayList<ArrayList<double[]>> anArray, String name) throws FileNotFoundException, UnsupportedEncodingException {
        String root = System.getProperty("user.dir");
        String subroot_I = "/datasets/Grace_dataset/v_2/";
        String path = root + subroot_I + name + ".txt";

        int col = anArray.size();
        int rows = anArray.get(0).size();

        PrintWriter writer = new PrintWriter(path, "UTF-8");

        System.out.println("Outout: --> ");

        System.out.println("rows: " + rows);

        for(int k=0; k<rows; k++){
            for(int l=0; l<col; l++){
                /*** ---------------------------- **/
                for(int i=0; i<2; i++){
                    if(i==0){
                        writer.append(Double.toString(anArray.get(l).get(k)[i]));
                        /*** **/
                        writer.append(",");
                        /*** **/
                    }else if(l==col-1 && i==1){
//                        if(anArray.get(l).get(k)[i]==0.0){
////                            System.out.println("Tell me!");
//                        }
                        writer.append(Double.toString(anArray.get(l).get(k)[i]));
                        writer.println();
                    }
                }
            }

        }

        System.out.println("Outout done!");

        infoClassificaationTest("\n" + "Outout done!");
        writer.close();
    }
}
