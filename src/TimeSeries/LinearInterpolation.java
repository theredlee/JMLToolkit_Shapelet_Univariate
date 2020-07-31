/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeries;

import DataStructures.DataInstance;
import DataStructures.DataSet;
import DataStructures.FeaturePoint.PointStatus;
import Utilities.Logging;
import java.awt.geom.Point2D;
import java.util.List;

/**
 *
 * @author Josif Grabocka
 */
public class LinearInterpolation
{
    double gapRatio = 0.1;

    public LinearInterpolation(double missingGapRatio)
    {
        gapRatio = missingGapRatio;
    }

    // linearly interpolate the missing points of every trajectory in the dataset
    public void Interpolate(DataSet dataSet)
    {
        int noTimeSeries = dataSet.instances.size();

        // iterate through all affected trajectories
        for(int i = 0; i < noTimeSeries; i++)
        {
            Interpolate(dataSet.instances.get(i));
        }
    }

    // interpolate a trajectory
    public void Interpolate(DataInstance timeSeries)
    {
        int noPoints = timeSeries.features.size();

        for( int j = 0; j < noPoints; j++ )
        {
            if( timeSeries.features.get(j).status == PointStatus.MISSING  )
            {
                // interpolate linearly the missing point
                LinearlyInterpolateMissingPoint(timeSeries, j);
            }
        }
    }

    /*
     *  calculate the linearly interpolated estimate of a missing value
     */

    public void LinearlyInterpolateMissingPoint(DataInstance timeSeries, int missingIndex)
    {
        int rightNonMissingIndex = FindNeighborNonMissingIndex(timeSeries, missingIndex, true);
        int leftNonMissingIndex = FindNeighborNonMissingIndex(timeSeries, missingIndex, false);

        double missingTime = missingIndex;
        double missingValue = 0;

        double leftTime = leftNonMissingIndex;
        double rightTime = rightNonMissingIndex;
        double leftValue = timeSeries.features.get(leftNonMissingIndex).value;
        double rightValue = timeSeries.features.get(rightNonMissingIndex).value;

        // if no more missing index is on the left then pick missings on the right and then extrapolate
        if( leftNonMissingIndex == missingIndex || rightNonMissingIndex == missingIndex )
        {
            boolean directionToRight = leftNonMissingIndex == missingIndex ? true : false;
            missingValue = LinearlyExtrapolate(timeSeries, missingIndex, directionToRight);
        }
        else
        {
            missingValue = leftValue + (missingTime - leftTime) / (rightTime - leftTime)
                    * (rightValue-leftValue);
        }

        timeSeries.features.get(missingIndex).value = missingValue;
        timeSeries.features.get(missingIndex).status = PointStatus.PRESENT;

    }


    /*
     * Find the first next non missing neighbor on either side of search
     * if direction is true search toward left, otherwise search toward right
     */

    public int FindNeighborNonMissingIndex(DataInstance trajectory, int indexMissing, boolean direction)
    {
        int nonMissingIndex = indexMissing;

        int advanceStep = direction ? 1 : -1;
        int noPoints = trajectory.features.size();

        int i = indexMissing + advanceStep;

        for( ; i >= 0 && i < noPoints; i += advanceStep )
        {
            if( trajectory.features.get(i).status == PointStatus.PRESENT )
            {
                nonMissingIndex = i;
                break;
            }
        }

        return nonMissingIndex;
    }


    public double LinearlyExtrapolate( DataInstance timeSeries, int missingIndex, boolean nonMissingDirectionToRight )
    {
        boolean directionToRight = nonMissingDirectionToRight ? true : false;

        double missingTime = missingIndex;
        double missingValue = 0;

        int numNeighborsToSelect = ((int)(timeSeries.features.size() * gapRatio))/2;
        if( numNeighborsToSelect < 2) numNeighborsToSelect = 2;

        List<Integer> neighboursIndexes = timeSeries.GetNeighboursIndexes(missingIndex, directionToRight, numNeighborsToSelect);

        // point 1 is the closest neighbour
        double pt1Time = neighboursIndexes.get(0),
                pt1Value = timeSeries.features.get( neighboursIndexes.get(0) ).value;

        // point 2 is the average of the neighbours
        double pt2Time = 0, pt2Value = 0;

        for( int i = 0; i < neighboursIndexes.size(); i++)
        {
            pt2Value += timeSeries.features.get( neighboursIndexes.get(i) ).value;

            if( i == 0 || i == neighboursIndexes.size()-1 )
                pt2Time += neighboursIndexes.get(i);
        }
        pt2Value /= neighboursIndexes.size();
        pt2Time /= 2;

        double slope = (pt2Value-pt1Value)/(pt2Time-pt1Time);
        missingValue = pt1Value + slope*(missingTime-pt1Time);

        return missingValue;
    }
}
