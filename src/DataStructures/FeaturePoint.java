package DataStructures;

import java.util.*;

import Utilities.Logging;

public class FeaturePoint 
{
	/*
	 * FeaturePoint coordinate
	 */
	public double value; 

	boolean isMultivariate;
	public List<Double> values;

	public enum PointStatus { PRESENT, MISSING };
	
	public PointStatus status;
	
	/*
	 * Constructor
	 */
	public FeaturePoint()
	{
		value = 0;
		status = PointStatus.PRESENT;
		
		isMultivariate = false;
		values = null;
	}
	
	public FeaturePoint(FeaturePoint p)
	{
		value = p.value;
		status = p.status;
	}
        
    public FeaturePoint(double v)
	{
		value = v;
		status = PointStatus.PRESENT;
	}
        
    public double distanceSquare(FeaturePoint p)
    {
        if( status == PointStatus.MISSING || p.status == PointStatus.MISSING )
        {
            return Double.MAX_VALUE;
        }
        
        double distance = 0;
        
        // for single-valued time series 
        if( ! isMultivariate)
        {
        	distance = Math.pow(value - p.value, 2);
        }
        // for multivariate time series
        else 
        {
        	for( int i = 0; i < values.size(); i++ )
        		distance += Math.pow(values.get(i) - p.values.get(i), 2);
        }
        
        return distance;
    }
	
}
