package TimeSeries;

import java.util.ArrayList;
import java.util.List;

import Utilities.Logging;
import Utilities.Logging.LogLevel;
import Utilities.StatisticalUtilities;
import DataStructures.DataInstance;
import DataStructures.DataSet;
import DataStructures.FeaturePoint;
import DataStructures.Matrix;

public class SAXRepresentation 
{
	public boolean showDebugMsgs;
	
	String alphabet = "abcdefghijklmnopqr";
	
	public SAXRepresentation()
	{
		showDebugMsgs = false;
	}
	
    public DataSet generatePAA( DataSet ds, double reducedDimensionalityFraction)
    {
        DataSet paaDs = new DataSet();
        paaDs.name = ds.name;
        
        int numInstances = ds.instances.size();
        int numFeatures = ds.numFeatures;
        
        int newTotalLength = (int)(reducedDimensionalityFraction * numFeatures);
        int k = (int)((double)numFeatures / (double)newTotalLength);

        // initialize the num features
        paaDs.numFeatures = newTotalLength;
        
        // iterate over all the instances
        for(int i = 0; i < numInstances; i++) {
            DataInstance paaInstance = new DataInstance();
            paaInstance.name = ds.instances.get(i).name;
            paaInstance.target = ds.instances.get(i).target;
            
            // keep an aggregate sum of every k instances
            double aggregatedPointsSum = 0;
            int remainingPointsToMerge = 0;
            
            // iterate through all the features
            for( int j = 0; j< numFeatures; j++ )
            {
                double value = ds.instances.get(i).features.get(j).value; 
                
                aggregatedPointsSum += value;
                remainingPointsToMerge = j % k;
                
                // every multiple of k'th feature, add the average of the aggregated 
                // values in the last k features, not the first number
                if( remainingPointsToMerge == 0 && j > 0 )
                {
                    FeaturePoint p = new FeaturePoint();
                    p.status = FeaturePoint.PointStatus.PRESENT;
                    p.value = aggregatedPointsSum/k;
                    
                    paaInstance.features.add(p);
                    aggregatedPointsSum = 0;
                    remainingPointsToMerge = 0;
                }
                    
                
            }
            
            // if remaining points are left add them as the last point
            if( aggregatedPointsSum > 0 )
            {
	            FeaturePoint p = new FeaturePoint();
	            p.status = FeaturePoint.PointStatus.PRESENT;
	            p.value = aggregatedPointsSum / remainingPointsToMerge;

	            paaInstance.features.add(p);
            }
            
            
            // add the instance to the PAA dataset
            paaDs.instances.add(paaInstance);
            
            paaDs.ReadNominalTargets();
        }
        
        return paaDs;
    }

    public Matrix generatePAAToMatrix( DataSet ds, double reducedDimensionalityFraction)
    {
        int numSeries = ds.instances.size();
        int numPoints = ds.numFeatures;

        // the desired reduction of length
        int desiredReductionLength = (int)(reducedDimensionalityFraction * numPoints);
        // how many points will be aggregated
        int k = (int)((double)numPoints / (double)desiredReductionLength);

        // reduce the length to the new realistically possible number of points
        int reducedLength =  numPoints / k;

        // if there are points remaining then increase the reduced series by one
        int remainingPoints = numPoints % k;
        if(remainingPoints > 0)
        	reducedLength++;

        Matrix paaDs = new Matrix(numSeries, reducedLength);

        //Logging.println("Num Points " + numPoints );
        //Logging.println("Desired Reduction " + desiredReductionLength  );
        //Logging.println("Reduce from " + numPoints + " to " + reducedLength );
        //Logging.println("Aggregate every " + k + " points, Remaining " + remainingPoints );

        // iterate over all the instances
        for(int i = 0; i < numSeries; i++)
        {
            // keep an aggregate sum of every k instances
            double aggregatedPointsSum = 0;
            int aggregatedNumPoints = 0;
            int paaPointIndex = 0;

            //Logging.println("---->");

            // iterate through all the features
            for( int j = 0; j< numPoints; j++ )
            {
                double value = ds.instances.get(i).features.get(j).value;

                aggregatedPointsSum += value;
                aggregatedNumPoints++;

                // every multiple of k'th feature, add the average of the aggregated
                // values in the last k features, not the first number
                if( aggregatedNumPoints == k && j > 0 )
                {
                	//Logging.println("orig series " + j + " aggregated to new index " + paaPointIndex );

                	paaDs.set(i, paaPointIndex, aggregatedPointsSum/(double)aggregatedNumPoints);

                    // increment the index of the reduced paa series
                    paaPointIndex++;
                    // reset the aggregations
                    aggregatedPointsSum = 0;
                    aggregatedNumPoints = 0;
                }


            }

            // if remaining points are left add them as the last point
            if( aggregatedNumPoints > 0 )
            {
            	double remainingValue = aggregatedPointsSum/(double)aggregatedNumPoints;

            	//Logging.println("remaining " + aggregatedNumPoints + " points aggregated to new index" + paaPointIndex );
            	//Logging.println("remaining value " + remainingValue + " points aggregated to new index" + paaPointIndex );

	            paaDs.set(i, paaPointIndex, remainingValue);
            }

        }

        return paaDs;
    }

    public double[] GeneratePAA(double [] series, int w)
    {
    	double [] seriesPAA = new double[w];

    	// move through all the subsequences
    	int pieceId = 0;
    	int step = series.length/w;

    	for(int i = 0; i < series.length; i += step )
    	{
    		double numPointsInSubsequence = 0;
    		double sumPointsInSubsequence = 0;

    		for(int j = i; j < i+step && j < series.length; j++)
    		{
    			sumPointsInSubsequence += series[j];
    			numPointsInSubsequence += 1.0;

    			if(showDebugMsgs)
    				System.out.print("+"+series[j]);

    		}

    		seriesPAA[pieceId] = sumPointsInSubsequence / numPointsInSubsequence;

    		if(showDebugMsgs)
    			System.out.println("=paa["+pieceId+"]"+seriesPAA[pieceId]);

    		pieceId++;
    	}

    	// normalize vector to 0 mean and 1 std
    	return seriesPAA; //StatisticalUtilities.Normalize(seriesPAA);
    }
    
    public double[] GetQuantileThresholds(int alphabetSize, double midData, double spaceData)
    {

    	double maxVal = Double.MAX_VALUE;
    	
    	double[] quantileThresholds = null;

		switch(alphabetSize)
		{
			case 2:
			{ quantileThresholds  = new double[]{ 0 + midData, maxVal }; break; }
			case 3:
			{ quantileThresholds  = new double[]{-0.43 * spaceData + midData, 0.43 * spaceData + midData, maxVal }; break; }
			case 4:
			{ quantileThresholds  = new double[]{-0.67 * spaceData + midData, 0 + midData, 0.67 * spaceData + midData, maxVal }; break; }
			case 5:
			{ quantileThresholds  = new double[]{-0.84 * spaceData + midData, -0.25 * spaceData + midData,
					0.25 * spaceData + midData, 0.84 * spaceData + midData, maxVal }; break; }
			case 6:
			{ quantileThresholds  = new double[]{-0.97 * spaceData + midData, -0.43 * spaceData + midData,
					0 * spaceData + midData, 0.43 * spaceData + midData, 0.97 * spaceData + midData, maxVal }; break; }
			case 7:
			{ quantileThresholds  = new double[]{-1.07 * spaceData + midData, -0.57 * spaceData + midData,
					-0.18 * spaceData + midData, 0.18 * spaceData + midData, 0.57 * spaceData + midData, 1.07 * spaceData + midData, maxVal }; break; }
			case 8:
			{ quantileThresholds  = new double[]{-1.15 * spaceData + midData, -0.67 * spaceData + midData, -0.32 * spaceData + midData,
					0 * spaceData + midData, 0.32 * spaceData + midData, 0.67 * spaceData + midData, 1.15 * spaceData + midData, maxVal}; break; }
			case 9:
			{ quantileThresholds  = new double[]{-1.22 * spaceData + midData, -0.76 * spaceData + midData, -0.43 * spaceData + midData,
					-0.14 * spaceData + midData, 0.14 * spaceData + midData, 0.43 * spaceData + midData, 0.76 * spaceData + midData, 1.22 * spaceData + midData, maxVal}; break; }
			case 10:
			{ quantileThresholds  = new double[]{-1.28 -0.84 -0.52 -0.25, 0.0, 0.25, 0.52, 0.84, 1.28, maxVal}; break; }
		}
    	
    	return quantileThresholds;
    }


    public String ConvertToSAX(double [] paaValues, int alphabetSize, double midData, double spaceData)
    {
    	String saxCodes = "";
    	double  [] cutPoints = GetQuantileThresholds(alphabetSize, midData, spaceData);


    	for(int i = 0; i < paaValues.length; i++) {
    		int alphabetLetterIndex = -1;

    		for(int cutpointsIndex = 0; cutpointsIndex < cutPoints.length; cutpointsIndex++)
    		{
    			/*** Threshold **/
    			if( paaValues[i] < cutPoints[cutpointsIndex])
    			{
    				alphabetLetterIndex = cutpointsIndex;
    				break;
    			}
    		}

    		String saxLetter = alphabet.substring(alphabetLetterIndex, alphabetLetterIndex+1);

    		//Logging.println(paaValues[i]+"<"+cutPoints[alphabetLetterIndex] + ", so alphabet["+alphabetLetterIndex+"]=" + saxLetter, LogLevel.DEBUGGING_LOG);

    		saxCodes += saxLetter;
    	}
    	
    	return saxCodes;
    }


	/*** --------------------------------------------> **/
	/*** Output **/
    public double[] ReturnPAA(){
    	double[] anArray = new double[1000];
		/***** Create an output from PAA transformation ***/




		/*** **/

		return anArray;
	}


    // extract the bag of patterns from a single data instance
    public List<String> ExtractBagOfPatterns(
    		double [] ins, 
    		int slidingWindowSize, 
    		int innerDimension, 
    		int alphabetSize )
    {
    	List<String> bagOfPatterns = new ArrayList<String>();
    	
    	// initialize a holder of the previous sax code 
    	// in order to compute similarity between the 
    	String previousSubSeriesSAX = "zzzzzz";
    	int numPoints = ins.length;
    	int i = 0;
    	
    	// iterate over all sliding windows
    	for(i = 0; numPoints - i >= slidingWindowSize ; i++)
    	{
    		// set the number of points to either the sliding window size
    		// or the remaining ones until the end
    		double [] subSeries = new double[slidingWindowSize];
    		for(int j = 0; j < slidingWindowSize; j++)
    			subSeries[j] = ins[i+j];
    		
    		// normalize the sub series 
    		double [] subSeriesNorm = StatisticalUtilities.Normalize(subSeries);
    		
    		// convert the normalized subseries to PAA form
    		double [] subSeriesPAA = GeneratePAA(subSeriesNorm, innerDimension);
    		
    		// convert the PAA form into SAX
    		String subSeriesSAX = ConvertToSAX(subSeriesPAA, alphabetSize,0,0);
    		
    		// check if current sax code is different to the previous
    		// in order to avoid the numerosity problem of closeby 
    		if( previousSubSeriesSAX.compareTo(subSeriesSAX) != 0)
    		{
    			bagOfPatterns.add(subSeriesSAX);
    			previousSubSeriesSAX = subSeriesSAX;
    			
    			if(showDebugMsgs)
    			{
	    			System.out.print("("+i+":"+(i+slidingWindowSize)+")" + "=[" + subSeriesSAX + ", ");
	    			Logging.print(subSeriesPAA, LogLevel.DEBUGGING_LOG); 
	    			System.out.println("] ");
    			}
    		}
    		
    	}
    	
    	if(showDebugMsgs)
    		System.out.println("");
    	
    	
    	return bagOfPatterns;
    }
    
    
    // extract the bag of patterns from a dataset
    public List<List<String>> ExtractBagOfPatterns(
				Matrix ds, 
				int slidingWindowSize, 
				int innerDimension, 
				int alphabetSize )
	{
		List<List<String>> bagOfPatterns = new ArrayList<List<String>>();
		
		for(int i = 0; i < ds.getDimRows(); i++)
			bagOfPatterns.add( ExtractBagOfPatterns(ds.getRow(i), slidingWindowSize, innerDimension, alphabetSize) );
		
		return bagOfPatterns;
	}
    
    public double [] ComputeQuantileMidpoints(int alphabetSize, double midData, double spaceData)
    {
    	double [] quantileThresholds = GetQuantileThresholds(alphabetSize, midData, spaceData);
    	
    	double [] quantileMidpoints = new double[alphabetSize]; 
    	
    	
    	for(int charIdx = 0; charIdx < alphabetSize; charIdx++)
    	{
    		if( charIdx == 0 )
    		{
    			// since the quantile threshold at 0 is open ended, then set its mid value 
    			// at the same distance from quantile 0 as the midpoint between from the quantile thresholds 
    			// at index 0 and 1
    			double delta_Quantile0_To_Midpoint01 = 0.5*(quantileThresholds[1]-quantileThresholds[0]);
    			quantileMidpoints[0] =  quantileThresholds[0] - delta_Quantile0_To_Midpoint01;
    		}
    		else if(charIdx == alphabetSize-1)
    		{
    			double delta_QuantileLast_To_MidpointOneBeforeLast = 
    					0.5*(quantileThresholds[alphabetSize-2]-quantileThresholds[alphabetSize-3]);
    			
    			quantileMidpoints[alphabetSize-1] = quantileThresholds[alphabetSize-2] 
    					+ delta_QuantileLast_To_MidpointOneBeforeLast;
    		}
    		else
    		{
    			quantileMidpoints[charIdx] = 0.5*(quantileThresholds[charIdx-1]+quantileThresholds[charIdx]);
    		}
    		
    		//System.out.println(charIdx+", Quantile="+quantileThresholds[charIdx]+", Midpoint="+quantileMidpoints[charIdx]);
    	}
    	
    	return quantileMidpoints;
    }
    
    // recreate a series approximation from a sax word
    public double [] RestoreSeriesFromSax( String saxWord, int slidingWindowSize, int alphabetSize, double midData, double spaceData )
    {
    	double [] series = new double[slidingWindowSize];
    	
    	double [] quantileMidpoints = ComputeQuantileMidpoints(alphabetSize, midData, spaceData);
    	
    	int innerDimensionality = saxWord.length();
    	int charSegmentSize = slidingWindowSize/innerDimensionality;
    	
    	// go to every character
    	for(int chIdx = 0; chIdx < saxWord.length(); chIdx++)
    	{
    		// compute the PAA value of the character
    		double charValue = quantileMidpoints[ alphabet.indexOf(saxWord.charAt(chIdx)) ]; 
    		
    		//System.out.println( "Char:" + saxWord.charAt(chIdx) + ", CharVal=" + charValue ); 
    		
    		// pump the character segment with the value
    		for(int charSegmentIdx = 0; charSegmentIdx < charSegmentSize; charSegmentIdx++)
    		{
    			series[chIdx*charSegmentSize + charSegmentIdx] = charValue;
    		}
    	}
    	
    	return series;
    }

    
}
