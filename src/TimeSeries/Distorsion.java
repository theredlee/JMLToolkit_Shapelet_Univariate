/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeries;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import DataStructures.FeaturePoint.PointStatus;
//import MatrixFactorization.CollaborativeImputation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import weka.estimators.NDConditionalEstimator;

/**
 *
 * @author josif
 */
public class Distorsion {
    /*
     * singleton implementation
     */

    private static Distorsion instance = null;

    private Distorsion() {
    }

    public static Distorsion getInstance() {
        if (instance == null) {
            instance = new Distorsion();
        }

        return instance;
    }

    // add uniform distortion to every instance
    public DataSet Distort(DataSet ds, double epsilon) {
        int numInstances = ds.instances.size();
        int numFeatures = ds.numFeatures;

        DataSet distortedDataSet = new DataSet();
        distortedDataSet.name = ds.name;
        distortedDataSet.numFeatures = numFeatures;

        for (int i = 0; i < numInstances; i++) {
            distortedDataSet.instances.addAll(Distort(ds.instances.get(i), epsilon));
        }

        /*
         * CollaborativeImputation ci = new CollaborativeImputation(); ci.k =
         * (int) (0.05*distortedDataSet.numFeatures); ci.lambda = 0.0001;
         * ci.learnRate = 0.01;
         *
         * ci.Impute(distortedDataSet);
         */

        return distortedDataSet;
    }

    // add uniform distortion to every instance
    public List<DataInstance> Distort(DataInstance ins, double epsilon) {

        int numFeatures = ins.features.size();

        List<DataInstance> distortedInstances = new ArrayList<DataInstance>();
        distortedInstances.add(ins);

        //double [] distortionPercentages = {0.01, 0.03, 0.05, 0.07};
        double[] distortionPercentages = {epsilon};

        for (int j = 0; j < distortionPercentages.length; j++)
        {
            double distorsionAmount = distortionPercentages[j];

            int numPointsToShift = (int) (numFeatures * distortionPercentages[j]);

            /*
             * shift forward
             */
            DataInstance shiftedForwardInstance = new DataInstance(ins);
            DataInstance shiftedBackwardInstance = new DataInstance(ins);

            for (int k = 0; k < numFeatures; k++)
            {
                shiftedForwardInstance.features.get(k).status = PointStatus.MISSING;
                shiftedBackwardInstance.features.get(k).status = PointStatus.MISSING;
            }

            // set the first points as missing
            for (int k = 0; k < numFeatures; k++)
            {
                int shiftedForwardIndex = (int) (Math.ceil((double) k * (1.0 + distorsionAmount)));

                if (shiftedForwardIndex < numFeatures) {
                    shiftedForwardInstance.features.get(shiftedForwardIndex).value = ins.features.get(k).value;
                    shiftedForwardInstance.features.get(shiftedForwardIndex).status = PointStatus.PRESENT;
                }
            }

            // shift backward
            for (int k = numFeatures-1; k >= 0; k--)
            {
                int kp = numFeatures-k-1;
                int shiftedBackwardIndexp = (int) (Math.ceil((double) kp * (1.0 + distorsionAmount)));
                int shiftedBackwardIndex = numFeatures-1-shiftedBackwardIndexp;

                if (shiftedBackwardIndex >= 0)
                {
                    shiftedBackwardInstance.features.get(shiftedBackwardIndex).value = ins.features.get(k).value;
                    shiftedBackwardInstance.features.get(shiftedBackwardIndex).status = PointStatus.PRESENT;
                }
            }

            LinearInterpolation li = new LinearInterpolation(0);
            li.Interpolate(shiftedForwardInstance);
            li.Interpolate(shiftedBackwardInstance);

            // add the distorted instances
            //distortedInstances.add(shiftedForwardInstance);
            //distortedInstances.add(shiftedBackwardInstance);

            DataInstance scaledForward = new DataInstance(ins);
            scaledForward.Scale(1+epsilon);
            DataInstance scaledBackward = new DataInstance(ins);
            scaledBackward.Scale(1-epsilon);

            distortedInstances.add(scaledForward);
            distortedInstances.add(scaledBackward);

        }



        return distortedInstances;
    }

    //
    public DataSet DistortMLS(DataSet ds, double eps) {
        int numInstances = ds.instances.size();
        int numFeatures = ds.numFeatures;

        DataSet distortedDataSet = new DataSet();
        distortedDataSet.name = ds.name;
        distortedDataSet.numFeatures = numFeatures;

        for (int i = 0; i < numInstances; i++) {
            DataInstance ins = ds.instances.get(i);

            distortedDataSet.instances.add(ins);

            distortedDataSet.instances.addAll(TranslateMLS(ins, eps));

        }

        return distortedDataSet;
    }

    public List<DataInstance> TranslateMLS(DataInstance ins, double epsilon)
    {
        List<DataInstance> distortedInstances = new ArrayList<DataInstance>();

        List<List<List<Integer>>> transformations = new ArrayList<List<List<Integer>>>();

        int numFeatures = ins.features.size();
        // number of points to translate


        // for all transformation amounts less then epsilon
        for (double eps = epsilon; eps > 0; eps -= 0.02)
        {
            int epsilonPts =  (int) Math.ceil((double)numFeatures * eps);

            int frequency = 4*epsilonPts;

            // for both directions of translation left and right
            for (int t = 0; t < 2; t++) {
                // either move points backwards x -1 or forward
                epsilonPts = epsilonPts * (t == 0 ? 1 : -1);

                // the list of transformation control points
                List<List<Integer>> transformation = new ArrayList<List<Integer>>();
                List<Integer> oldCP = new ArrayList<Integer>();
                List<Integer> newCP = new ArrayList<Integer>();

                for (int p = 0; p < numFeatures; p++)
                {
                    // add extreme points unchanged as control points
                    if ((p == 0) || (p == numFeatures - 1))
                    {
                        oldCP.add(p);
                        newCP.add(p);
                    }
                    else
                    {
                        if( p % frequency == 0)
                        {
                            int newP = p+epsilonPts;
                            int newP2 = p+3*epsilonPts;

                            if( newP2 >= 0 && newP2 < numFeatures)
                            {
                                oldCP.add(p);
                                newCP.add(newP);
                            }
                        }
                    }
                }

                transformation.add(oldCP);
                transformation.add(newCP);

                transformations.add(transformation);
            }
        }

        for (List<List<Integer>> transformation : transformations)
        {
            List<Integer> oldCP = transformation.get(0);
            List<Integer> newCP = transformation.get(1);

            DataInstance distIns = MLS.getInstance().Transform(
                    ins, oldCP, newCP);

            distortedInstances.add(distIns);

            /*
             * DataInstance distIns1 = new DataInstance(distIns);
             * distIns1.Scale(0.9); DataInstance distIns2 = new
             * DataInstance(distIns); distIns2.Scale(1.1);
             * distortedInstances.add(distIns1);
             * distortedInstances.add(distIns2);
             *
             */
        }

        return distortedInstances;
    }

    public DataSet DistortMLS2(DataSet ds, double eps) {
        int numInstances = ds.instances.size();
        int numFeatures = ds.numFeatures;

        DataSet distortedDataSet = new DataSet();
        distortedDataSet.name = ds.name;
        distortedDataSet.numFeatures = numFeatures;

        for (int i = 0; i < numInstances; i++) {
            DataInstance ins = ds.instances.get(i);

            distortedDataSet.instances.add(ins);

            distortedDataSet.instances.addAll(TranslateMLS2(ins, eps));

        }

        return distortedDataSet;
    }


    public DataSet DistortTransformationField(DataSet ds, String tfFolder)
    {
        TransformationFieldsGenerator.getInstance().LoadTransformationFields(ds, tfFolder);

        int numInstances = ds.instances.size();
        int numFeatures = ds.numFeatures;

        DataSet distortedDataSet = new DataSet();
        distortedDataSet.name = ds.name;
        distortedDataSet.numFeatures = numFeatures;

        for (int i = 0; i < numInstances; i++) {
            DataInstance ins = ds.instances.get(i);

            distortedDataSet.instances.add(ins);

            distortedDataSet.instances.addAll(
                    TransformationFieldsGenerator.getInstance().Transform(ins));

        }

        return distortedDataSet;
    }

    public List<DataInstance> TranslateMLS2(DataInstance ins, double epsilon) {
        List<DataInstance> distortedInstances = new ArrayList<DataInstance>();

        List<List<List<Integer>>> transformations = new ArrayList<List<List<Integer>>>();

        int numFeatures = ins.features.size();
        // number of points to translate


        // for all transformation amounts less then epsilon
        for (double eps = epsilon; eps >= epsilon; eps -= 0.01)
        {
            int epsilonPts =  (int) Math.ceil((double)numFeatures * eps);

            //int frequency = 2*epsilonPts;
            int frequency = (int)((double)numFeatures * 0.25);

            // for both directions of translation left and right
            for (int t = 0; t < 2; t++) {
                // either move points backwards x -1 or forward
                epsilonPts = epsilonPts * (t == 0 ? 1 : -1);

                // the list of transformation control points
                List<List<Integer>> transformation = new ArrayList<List<Integer>>();
                List<Integer> oldCP = new ArrayList<Integer>();
                List<Integer> newCP = new ArrayList<Integer>();

                for (int p = 0; p < numFeatures; p++)
                {
                    // add extreme points unchanged as control points
                    if ((p == 0) || (p == numFeatures - 1))
                    {
                        oldCP.add(p);
                        newCP.add(p);
                    }
                    else
                    {
                        if( p % frequency == 0)
                        {
                            oldCP.add(p);
                            newCP.add(p + epsilonPts);
                        }
                    }
                }

                transformation.add(oldCP);
                transformation.add(newCP);

                transformations.add(transformation);
            }
        }

        for (List<List<Integer>> transformation : transformations)
        {
            List<Integer> oldCP = transformation.get(0);
            List<Integer> newCP = transformation.get(1);

            DataInstance distIns = MLS.getInstance().Transform(
                    ins, oldCP, newCP);

            distortedInstances.add(distIns);

            /*
             * DataInstance distIns1 = new DataInstance(distIns);
             * distIns1.Scale(0.9); DataInstance distIns2 = new
             * DataInstance(distIns); distIns2.Scale(1.1);
             * distortedInstances.add(distIns1);
             * distortedInstances.add(distIns2);
             *
             */
        }

        return distortedInstances;
    }
}
