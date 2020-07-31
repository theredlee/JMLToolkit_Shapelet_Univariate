/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TimeSeries;

import DataStructures.DataInstance;
import DataStructures.FeaturePoint;
import java.util.List;
import umontreal.iro.lecuyer.functionfit.BSpline;

/**
 *
 * @author josif
 */
public class MLS
{
    // singleton implementation
    private static MLS instance = null;

    private MLS()
    {
    }

    public static MLS getInstance()
    {
        if( instance == null)
            instance = new MLS();

        return instance;
    }

    // an alpha of 1 corresponds to Euclidean distance
    public double alpha = 1.0f;

    // the number of control points
    int n;

    // the control points "p transformed to q"
    double[] pX, pY, qX, qY;

    // the centroids
    double pCX, pCY, qCX, qCY;

    // the new transformation result coordinates
    double resultX, resultY;

    // the transformation matrix
    double m11, m12, m21, m22;

    // calculate the transformed value of a point x,y
    public void calculate(double x, double y)
    {
        calculateCentroids(x, y);
        calculateMRigid(x, y);
        resultX = qCX + m11 * (x - pCX)
                + m12 * (y - pCY);
        resultY = qCY + m21 * (x - pCX)
                + m22 * (y - pCY);
    }

    // x, y is supposed to be absolute
    public void calculateCentroids(double x, double y) {
        pCX = pCY = qCX = qCY = 0;
        double total = 0;
        for (int i = 0; i < n; i++) {
            double w = w(x, y, pX[i], pY[i]);
            total += w;
            pCX += w * pX[i];
            pCY += w * pY[i];
            qCX += w * qX[i];
            qCY += w * qY[i];
        }
        pCX /= total;
        pCY /= total;
        qCX /= total;
        qCY /= total;
    }

    public void calculateMSimilarity(double x, double y) {
        double mu = 0;
        m11 = m12 = m21 = m22 = 0;
        for (int i = 0; i < n; i++) {
            double w = w(x, y, pX[i], pY[i]);
            double pXi = pX[i] - pCX, pYi = pY[i] - pCY;
            double qXi = qX[i] - qCX, qYi = qY[i] - qCY;
            m11 += w * (pXi * qXi + pYi * qYi);
            m12 += w * (pYi * qXi - pXi * qYi);
            mu += w * (pXi * pXi + pYi * pYi);
        }
        m11 /= mu;
        m12 /= mu;
        m21 = -m12;
        m22 = m11;
    }

    // calculate rigid transformation matrix M_i
    public void calculateMRigid(double x, double y)
    {
        double mu1 = 0, mu2 = 0;
        m11 = m12 = m21 = m22 = 0;

        for (int i = 0; i < n; i++)
        {
            double w = w(x, y, pX[i], pY[i]);
            double pXi = pX[i] - pCX, pYi = pY[i] - pCY;
            double qXi = qX[i] - qCX, qYi = qY[i] - qCY;
            m11 += w * (pXi * qXi + pYi * qYi);
            m12 += w * (pYi * qXi - pXi * qYi);
        }

        double mu = (double)Math.sqrt(m11 * m11 + m12 * m12);

        m11 /= mu;
        m12 /= mu;
        m21 = -m12;
        m22 = m11;
    }

    public void calculateMAffine(double x, double y) {
        float a11, a12, a22;
        float b11, b12, b21, b22;
        a11 = a12 = a22 = b11 = b12 = b21 = b22 = 0;
        for (int i = 0; i < n; i++) {
            double w = w(x, y, pX[i], pY[i]);
            double pXi = pX[i] - pCX, pYi = pY[i] - pCY;
            double qXi = qX[i] - qCX, qYi = qY[i] - qCY;
            a11 += w * pXi * pXi;
            a12 += w * pXi * pYi;
            a22 += w * pYi * pYi;
            b11 += w * pXi * qXi;
            b12 += w * pXi * qYi;
            b21 += w * pYi * qXi;
            b22 += w * pYi * qYi;
        }
        float detA = a11 * a22 - a12 * a12;
        m11 = (a22 * b11 - a12 * b21) / detA;
        m12 = (-a12 * b11 + a11 * b21) / detA;
        m21 = (a22 * b12 - a12 * b22) / detA;
        m22 = (-a12 * b12 + a11 * b22) / detA;
    }

    // the weight w
    public double w(double x, double y, double px, double py)
    {
        x -= px;
        y -= py;
        if (x == 0 && y == 0)
            return 1e10f;
        x = 1 / (x * x + y * y);
        if (alpha == 1)
            return x;
        return (double)Math.exp(Math.log(x) * alpha);
    }

    public DataInstance Transform( DataInstance ins,
                                   List<Integer> oldControlPts, List<Integer> newControlPts)
    {


        // initialize the control points
        n = oldControlPts.size();
        pX = new double[n]; pY = new double[n];
        qX = new double[n]; qY = new double[n];

        for(int i = 0; i < n; i++)
        {
            pX[i] = (double)oldControlPts.get(i);
            qX[i] = (double)newControlPts.get(i);
            pY[i] = qY[i] = 1;
        }

        // distort every point of series
        DataInstance distortedInstance = new DataInstance(ins);
        int numFeatures = distortedInstance.features.size();

        for( int i = 0; i < numFeatures; i++)
        {
            distortedInstance.features.get(i).value = 0;
            distortedInstance.features.get(i).status = FeaturePoint.PointStatus.MISSING;
        }

        for( int i = 0; i < numFeatures; i++)
        {
            double x = i, y = 1;
            double value = ins.features.get(i).value;

            // calculate the new position of x and y
            calculate(x, y);


            int newX = (int)resultX;
            int newY = (int)resultY;

            // correct new indexes if wrong

            if(newX >= numFeatures)
                newX = numFeatures-1;
            else if (newX < 0)
                newX = 0;

            distortedInstance.features.set(newX, new FeaturePoint(value));
            distortedInstance.features.get(newX).status = FeaturePoint.PointStatus.PRESENT;

            //System.out.println( x+":"+y+" - "+newX+":"+newY );
        }

        // linearly interpolate distorted instance

        LinearInterpolation li = new LinearInterpolation(0);
        li.Interpolate(distortedInstance);

        return distortedInstance;
    }


}
