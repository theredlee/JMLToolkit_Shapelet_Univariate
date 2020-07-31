/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeries;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import Utilities.Logging;
import Utilities.StatisticalUtilities;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author josif
 */
public class TransformationFieldsGenerator
{
    // the dataset under consideration
    DataSet dataSet;
    String dsName;

    public double transformationScale = 0.5;

    public boolean useBestWarpingPath = false;

    public double transformationRate;

    public double warpingWindow;

    // the control points used for creating transformation
    public List<TimeSeriesControlPoints> controlPoints;

    DecimalFormat df = new DecimalFormat("#.#");
    PrintStream tfPs;

    // the transformation fields used to store the transformation
    // fields after reading from file
    List<List<TimeSeriesControlPoints>> transformationFields;

    // a random number generator
    Random rand;

    private static TransformationFieldsGenerator instance = null;

    public static TransformationFieldsGenerator getInstance()
    {
        if(instance == null)
            instance = new TransformationFieldsGenerator();

        return instance;
    }

    // initialize the instance and create the control points
    private TransformationFieldsGenerator()
    {
        // get a sub-dataset holding the time series having only one label

        transformationFields = new ArrayList<List<TimeSeriesControlPoints>>();

        warpingWindow = 0.1;

        rand = new Random();

        dsName="";

        transformationRate = 1.0;
    }

    // initialize the control points
    public void InitializeTheControlPoints()
    {
        controlPoints = new ArrayList<TimeSeriesControlPoints>();

        // initialize the control points on a few positions on the series
        double [] percentagePositions = new double[]{
                0.10, 0.20, 0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90
                // (1.0/6.0), (3.0/6.0), (5.0/6.0)
                // 0.10, 0.30, 0.50, 0.70, 0.90
        };
            /*
            0.1, 0.15,
            0.2, 0.25,
            0.3, 0.35,
            0.4, 0.45,
            0.5, 0.55,
            0.6, 0.65,
            0.7, 0.75,
            0.8, 0.85,
            0.9, 0.95 };
            */

        for( double pctPos : percentagePositions )
        {
            TimeSeriesControlPoints cp = new TimeSeriesControlPoints();
            cp.position = (int)(pctPos * dataSet.numFeatures);
            controlPoints.add(cp);
        }


    }


    public void CreateTransformationFields(DataSet dataset, String transformationsFieldsFolder)
    {
        dataSet = dataset;

        // initialize the control points
        InitializeTheControlPoints();

        // first create the file to print the tranformation fields
        CreateTransformationFieldDataSetFile(transformationsFieldsFolder);

        // read the nominal labels
        dataSet.ReadNominalTargets();


        // initialize the warping window
        if(useBestWarpingPath)
            warpingWindow = DTW.getInstance().getWarpingWindow(dsName);
        else
            warpingWindow = 1.0;

        // check if the warping window is 0, then no transformation
        // is needed at all
        if(warpingWindow == 0)
            return;

        // iterate through the read nominal labels and create the
        // transformation field per each label
        for( double label : dataSet.nominalLabels )
        {
            Logging.println("Transformations of label: " + label, Logging.LogLevel.DEBUGGING_LOG);

            DataSet ds = dataSet.FilterByLabel(label);

            for(int i = 0; i < controlPoints.size(); i++)
            {
                for(int j = 0; j < 2; j++)
                {
                    int pos = controlPoints.get(i).position;

                    boolean rightOrLeft = j == 0;

                    Logging.println("Label: " + label + ", Position: " + pos + ", Direction: " + (rightOrLeft?"right":"left"), Logging.LogLevel.DEBUGGING_LOG);

                    ApplyFilteredWarpingPaths(ds, pos, 0, rightOrLeft);
                    UpdateControlPointStatistics();
                    PrintControlPointsStatistics(label);

                    System.gc();
                }

            }

        }

        // close the file stream
        tfPs.flush();
        tfPs.close();

    }

    // update the control point statistics based on a set of warping paths
    public void UpdateControlPointStatistics()
    {
        // update the statistics (mean,std) of the control points
        for(int cpIndex = 0; cpIndex < controlPoints.size(); cpIndex++)
        {
            TimeSeriesControlPoints cp = controlPoints.get(cpIndex);

            cp.mean = StatisticalUtilities.Mean( cp.warpedPts );
            //cp.mean = Math.round(cp.mean);
            // cp.std = StatisticalUtilities.StandardDeviation( cp.warpedPts );
        }

    }

    // apply the warping paths directly on the control points
    public void ApplyFilteredWarpingPaths( DataSet ds, int position, int warpingOffset, boolean warpingRight )
    {
        // clear the warped points from the control points
        for(int i = 0; i < controlPoints.size(); i++)
            controlPoints.get(i).warpedPts.clear();

        int warpingWindowPts = (int)Math.ceil(ds.numFeatures * warpingWindow);

        for(int i = 0; i < ds.instances.size(); i++)
        {
            for(int j = 0; j < ds.instances.size(); j++)
            {
                if(i == j)
                    continue;

                List<Double> ins1 = ds.instances.get(i).GetFeatureValues();
                List<Double> ins2 = ds.instances.get(j).GetFeatureValues();
                // compute the warping path of the two instances
                List<List<Integer>> wp = DTW.getInstance().CalculateWarpingPath(ins1, ins2, warpingWindowPts);

                // if it is warped more than a filter offset
                boolean doesWarpingExceedsOffset = false;

                // check if any of the warpings exceeds the offset
                for( int k = 0; k < wp.get(position).size(); k++ )
                {
                    boolean warpingExceeded = wp.get(position).get(k)- position  >= warpingOffset;

                    if( !warpingRight )
                        warpingExceeded = wp.get(position).get(k) - position <= warpingOffset;

                    if( warpingExceeded )
                    {
                        doesWarpingExceedsOffset = true;
                        break;
                    }
                }

                // if the warping path exceeds the warping window barrier
                // then add the warping alignments to the control points
                if( doesWarpingExceedsOffset )
                {
                    for(int l = 0; l < controlPoints.size(); l++)
                    {
                        TimeSeriesControlPoints cp = controlPoints.get(l);
                        int pos = cp.position;

                        for( int m = 0; m < wp.get(pos).size(); m++ )
                        {
                            cp.warpedPts.add(wp.get(pos).get(m) - pos);
                        }

                    }
                }
            }
        }

    }

    // get filtered warping paths based on the position, an offset and direction
    // the function will return all the warping paths that apply
    // for example 5,0,true return all the warping paths that are warped to the right at position 5
    // for example 5,3,false return all the warping paths that are warped to the left at position 5,
    // by at least 3 points from the diagonal
    public List<List<List<Integer>>> GetFilteredWarpingPaths( DataSet ds, int position, int warpingOffset, boolean warpingRight )
    {
        List<List<List<Integer>>> wps = new ArrayList<List<List<Integer>>>();

        int warpingWindowPts = (int)(ds.numFeatures * warpingWindow);

        for(int i = 0; i < ds.instances.size(); i++)
        {
            for(int j = 0; j < ds.instances.size(); j++)
            {
                if(i == j)
                    continue;

                List<Double> ins1 = ds.instances.get(i).GetFeatureValues();
                List<Double> ins2 = ds.instances.get(j).GetFeatureValues();
                // compute the warping path of the two instances
                List<List<Integer>> wp = DTW.getInstance().CalculateWarpingPath(ins1, ins2, warpingWindowPts);

                // if it is warped more than a filter offset
                boolean doesWarpingExceedsOffset = false;

                // check if any of the warpings exceeds the offset
                for( int k = 0; k < wp.get(position).size(); k++ )
                {
                    boolean warpingExceeded = wp.get(position).get(k)- position  >= warpingOffset;

                    if( !warpingRight )
                        warpingExceeded = wp.get(position).get(k) - position <= warpingOffset;

                    if( warpingExceeded )
                    {
                        doesWarpingExceedsOffset = true;
                        break;
                    }
                }

                if( doesWarpingExceedsOffset )
                {
                    // add the warping into the list
                    wps.add(wp);
                }
            }
        }

        return wps;
    }


    public List<List<List<Integer>>> GetAllWarpingPaths( DataSet ds)
    {
        List<List<List<Integer>>> wps = new ArrayList<List<List<Integer>>>();

        int warpingWindowPts = (int)(ds.numFeatures * warpingWindow);

        for(int i = 0; i < ds.instances.size(); i++)
        {
            for(int j = 0; j < ds.instances.size(); j++)
            {
                if(i == j)
                    continue;

                List<Double> ins1 = ds.instances.get(i).GetFeatureValues();
                List<Double> ins2 = ds.instances.get(j).GetFeatureValues();
                // compute the warping path of the two instances
                List<List<Integer>> wp = DTW.getInstance().CalculateWarpingPath(ins1, ins2, warpingWindowPts);

                wps.add(wp);
            }
        }

        return wps;
    }



    private void PrintControlPointsStatistics(double label)
    {
        boolean noChange = true;

        for(int cpIndex = 0; cpIndex < controlPoints.size(); cpIndex++)
        {
            TimeSeriesControlPoints cp = controlPoints.get(cpIndex);

            if(cp.mean > 0)
                noChange = false;
        }

        if(noChange)
        {
            Logging.println( "Transformation no change, skipping...", Logging.LogLevel.DEBUGGING_LOG);
            return;
        }

        // first print the label
        tfPs.print( label );

        // then print all the control points and the variation on those
        for(int cpIndex = 0; cpIndex < controlPoints.size(); cpIndex++)
        {
            TimeSeriesControlPoints cp = controlPoints.get(cpIndex);
            Logging.println( "CP: " + cp.position + " aligned with mean: " + cp.mean, Logging.LogLevel.DEBUGGING_LOG);

            tfPs.print("," + cp.position + ":" + df.format(cp.mean ));
        }

        tfPs.println("");
    }


    private void CreateTransformationFieldDataSetFile(String transformationsFieldsFolder)
    {
        String dsNameFull=dataSet.name;

        if(dsNameFull.indexOf("TRAIN") >= 0)
            dsName = dsNameFull.split("_TRAIN")[0];
        else if(dsNameFull.indexOf("TEST") >= 0)
            dsName = dsNameFull.split("TEST")[0];
        else if(dsNameFull.indexOf("VALIDATION") >= 0)
            dsName = dsNameFull.split("VALIDATION")[0];


        String tsDsFile = transformationsFieldsFolder + "/" + dsName;

        try
        {
            tfPs = new PrintStream(new File (tsDsFile));
        }
        catch(Exception exc)
        {
            Logging.println(exc.getMessage(), Logging.LogLevel.ERROR_LOG);
        }

    }


    public void LoadTransformationFields(DataSet dataset, String transformationsFieldsFolder)
    {
        dataSet = dataset;

        String dsNameFull=dataSet.name;

        if(dsNameFull.indexOf("TRAIN") >= 0)
            dsName = dsNameFull.split("_TRAIN")[0];
        else if(dsNameFull.indexOf("TEST") >= 0)
            dsName = dsNameFull.split("TEST")[0];
        else if(dsNameFull.indexOf("VALIDATION") >= 0)
            dsName = dsNameFull.split("VALIDATION")[0];

        // initialize the warping window
        if(useBestWarpingPath)
            warpingWindow = DTW.getInstance().getWarpingWindow(dsName);
        else
            warpingWindow = 1.0;

        String tsDsFile = transformationsFieldsFolder + "/" + dsName;

        transformationFields = new ArrayList<List<TimeSeriesControlPoints>>();

        try
        {
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(tsDsFile);

            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null)
            {
                String[] tokens = strLine.split(",");

                List<TimeSeriesControlPoints> tf = new ArrayList<TimeSeriesControlPoints>();
                double label = Double.valueOf(tokens[0]);

                Logging.print(""+label, Logging.LogLevel.DEBUGGING_LOG);

                for(int i = 1; i < tokens.length; i++)
                {
                    TimeSeriesControlPoints cp = new TimeSeriesControlPoints();
                    cp.label = label;

                    String [] tokens2 = tokens[i].split(":");

                    cp.position =  Integer.valueOf(tokens2[0]);
                    cp.mean =  Double.valueOf(tokens2[1]);

                    Logging.print(","+cp.position+":"+cp.mean, Logging.LogLevel.DEBUGGING_LOG);

                    tf.add(cp);
                }

                Logging.print("\n", Logging.LogLevel.DEBUGGING_LOG);

                transformationFields.add(tf);
            }

            //Close the input stream
            in.close();

        }
        catch (Exception e)
        {//Catch exception if any
            Logging.println("Exception: " + e.getMessage(), Logging.LogLevel.ERROR_LOG);
        }
    }


    // transform the data instance

    public List<DataInstance> Transform(DataInstance ins)
    {

        List<DataInstance> transformedInstances = new ArrayList<DataInstance>();

        for(int i = 0; i < transformationFields.size(); i++)
        {
            List<TimeSeriesControlPoints> transformationField = transformationFields.get(i);

            double label = transformationField.get(0).label;

            // check if the label of the transformation field matches
            // the label of the instance, if so create transformed instance
            if( label == ins.target )
            {
                // randomly apply only a portion of the transformations
                if( rand.nextDouble() <= transformationRate)
                {
                    // the old and new control points
                    List<Integer> oldCP = new ArrayList<Integer>();
                    List<Integer> newCP = new ArrayList<Integer>();

                    // add extreme points as handles
                    oldCP.add(0);
                    newCP.add(0);

                    for(TimeSeriesControlPoints pc : transformationField)
                    {
                        oldCP.add(pc.position);
                        int newPosition = pc.position +
                                //         (int)Math.round(pc.mean);
                                (int)Math.round(pc.mean*transformationScale);
                        newCP.add(newPosition);
                    }

                    oldCP.add(ins.features.size()-1);
                    newCP.add(ins.features.size()-1);

                    boolean anyChange = false;

                    for(int j = 0; j < oldCP.size(); j++)
                    {
                        if( oldCP.get(j) != newCP.get(j))
                        {
                            anyChange = true;
                            break;
                        }
                    }

                    if(!anyChange)
                    {
                        Logging.println("Transformation skipped, no change.", Logging.LogLevel.DEBUGGING_LOG);
                    }
                    else
                    {
                        Logging.println("Label: " + label + ", Transformation applied:", Logging.LogLevel.DEBUGGING_LOG);
                        Logging.println(oldCP, Logging.LogLevel.DEBUGGING_LOG);
                        Logging.println(newCP, Logging.LogLevel.DEBUGGING_LOG);

                        DataInstance transformed = MLS.getInstance().Transform(
                                ins, oldCP, newCP);

                        transformedInstances.add(transformed);
                    }
                }
            }
        }


        return transformedInstances;
    }


    public BufferedImage CreateImage(DataSet ds, List<List<List<Integer>>> wps)
    {
        int width = ds.numFeatures;
        int height = ds.numFeatures;

        int [][] warpingMap = CreateWarpingMatrix(ds, wps);

        BufferedImage seriesImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // We need its raster to set the pixels' values.
        WritableRaster raster = seriesImage.getRaster();

        //dsMin = GetMinFeatureValue();
        //dsMax = GetMaxFeatureValue();

        // get the maximum
        double min = 0;
        double max = 0;
        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                if( warpingMap[i][j] > max )
                    max = warpingMap[i][j];


        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                double val = warpingMap[i][j];

                //double imVal = ((val-min)/(max-min))*255;

                raster.setSample(i,j,0,val);
            }
        }


        return seriesImage;
    }



    // create a warping matrix of a warping path list
    public int[][] CreateWarpingMatrix(DataSet ds, List<List<List<Integer>>> wps)
    {
        int width = ds.numFeatures;
        int height = ds.numFeatures;

        int [][] warpingMap = new int[width][height];

        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                warpingMap[i][j] = 0;

        for(int i = 0; i < wps.size(); i++)
        {
            for(int j = 0; j < wps.get(i).size(); j++)
            {
                int xPos = j;

                for(int k = 0; k < wps.get(i).get(j).size(); k++)
                {
                    int yPos = wps.get(i).get(j).get(k);

                    warpingMap[xPos][yPos]++;
                }
            }
        }


        return warpingMap;
    }

    // save the warping matrix to a file
    public void SaveWarpingMatrix(int [][] wm, String fileName)
    {
        try
        {
            PrintStream ps = new PrintStream(new File (fileName));

            for(int i = 0; i < wm.length; i++)
            {
                for(int j = 0; j < wm[i].length; j++)
                {
                    ps.print(wm[i][j]);
                    if(j < wm[i].length -1 ) ps.print("\t");
                }
                ps.println("");
            }

            ps.flush();
            ps.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
