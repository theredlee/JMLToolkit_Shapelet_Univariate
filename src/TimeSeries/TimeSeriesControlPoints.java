/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * The control points of time series
 * @author josif
 */
public class TimeSeriesControlPoints
{
    // point position of control point
    int position;

    // the aligned points to this position on all the
    // time series
    public List<Integer> warpedPts;

    // the average variation of a dataset around this point
    double mean;
    // the average variation of a dataset around this point
    double std;

    // the label of this control point transformation
    double label;

    //
    public TimeSeriesControlPoints()
    {
        warpedPts = new ArrayList<Integer>();

    }

}
