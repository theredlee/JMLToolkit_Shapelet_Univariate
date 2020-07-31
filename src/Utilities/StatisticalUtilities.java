package Utilities;

import java.util.List;

public class StatisticalUtilities 
{
    // the mean of a list of values
    public static double Mean(List<Integer> values)
    {
        double sum = 0;
		
        for( int i = 0; i < values.size(); i++ )
        {
                sum += values.get(i);
        }
		
        return sum / (double)values.size(); 
    }
    
    // the mean of a list of values
    public static double Mean(double [] values)
    {
        double sum = 0;
		
        for( int i = 0; i < values.length; i++ )
                sum += values[i];
		
        return sum / (double)values.length; 
    }
    
    public static double StandardDeviation(double [] values)
    {
        double mean = Mean(values);
        double sumSquaresDiffs = 0; 
		
        for( int i = 0; i < values.length; i++ )
        {
                double diff = values[i] - mean;
                
                sumSquaresDiffs += diff*diff; 
        }

        return Math.sqrt( sumSquaresDiffs / (values.length-1) );  
    }
    
    // computes the standard deviation of a list of numbers
    public static double StandardDeviation(List<Integer> values)
    {
        double mean = Mean(values);
        double sumSquaresDiffs = 0; 
		
        for( int i = 0; i < values.size(); i++ )
        {
                double diff = values.get(i) - mean;
                
                sumSquaresDiffs += diff*diff; 
        }

        return Math.sqrt( sumSquaresDiffs / (values.size()-1) ); 
    }
    
    public static double Power(double val, int pow)
    {
    	double result = 1; 
    	
    	for(int i = 0; i < pow; i++)
    		result *= val;
    	
    	return result;
    }
    
    public static int PowerInt(int val, int pow)
    {
    	int result = 1; 
    	
    	for(int i = 0; i < pow; i++)
    		result *= val;
    	
    	return result;
    }
    
    // normalize the vector to mean 0 and std 1
    public static double [] Normalize(double [] vector)
    {
    	double mean = Mean(vector);
    	double std = StandardDeviation(vector);
    	
    	double [] normalizedVector = new double[vector.length];
    	
    	for(int i = 0; i < vector.length; i++)
    		if( std != 0 )
    			normalizedVector[i] = (vector[i]-mean)/std;
    	
    	return normalizedVector;
    }
    
    public static double [] MeanRemoval(double [] vector)
    {
    	double mean = Mean(vector);
    	
    	double [] normalizedVector = new double[vector.length];
    	
    	for(int i = 0; i < vector.length; i++)
			normalizedVector[i] = (vector[i]-mean);
    	
    	return normalizedVector;
    }
    
    public static double exp(double val) {
        final long tmp = (long) (1512775 * val + 1072632447);
        return Double.longBitsToDouble(tmp << 32);
    }
    
    
    public static double SumOfSquares(double [] v1, double [] v2)
    {
    	double ss = 0;
    	
    	int N = v1.length;
    	
    	double err = 0;
    	for(int i = 0; i < N; i++)
    	{
    		err = v1[i]-v2[i];
    		ss+=err*err;
    	}
    	
    	return ss;
    }
}
