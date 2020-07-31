package TimeSeries;

import DataStructures.BloomFilter;
import DataStructures.DataSet;
import DataStructures.Matrix;
import Utilities.Logging;
import Utilities.Sigmoid;
import org.happy.commons.concurrent.loops.ForEachTask_1x0;
import org.happy.commons.concurrent.loops.Parallel_1x0;

import java.io.*;
import java.util.*;

public class EfficientLTS_PAASAX {
    // number of training and testing instances
    public int ITrain, ITest;
    // length of a time-series
    public int Q;
    // length of shapelet
    public int L[];
    public int slidingWindow_miniunit;
    // number of latent patterns
    public int K[];
    // scales of the shapelet length
    public int R;
    // number of classes
    public int C; //C_new;
    // number of segments
    public int J[];

    // shapelets
    double Shapelets[][][];

    // classification weights
    double W[][];
    double biasW[];

    // accumulate the gradients
    double GradHistShapelets[][][];
    double GradHistW[][];
    double GradHistBiasW[];

    // the softmax parameter
    public double alpha;

    // the number of iterations
    public int maxIter;
    // the learning rate
    public double eta;

    public int kMeansIter;

    // the regularization parameters
    public double lambdaW;

    public List<Double> nominalLabels;

    // structures for storing the precomputed terms
    double D[][][][];
    double E[][][][];
    double M[][][];
    double Psi[][];
    double sigY[][];

    Random rand = new Random(1);

    List<Integer> instanceIdxs;
    List<Integer> rIdxs;

    // time series data and the label
    /*** Tp = sax.generatePAAToMatrix(dataSet, paaRatio); **/
    public Matrix T,Tp,Ts;
    public Matrix Y, Y_b;

    // time series max and min data
    public double maxData;
    public double minData;
    public double midData;
    public double spaceData;

    public int alphabetSize;
    public int pcover;

    //BloomFilter
    public BloomFilter[] BF;
    public double falsePositiveProbability = 0.0001;
    public int expectedNumberOfElements = 60000;

    // the paa ratio, i.e. 0.25 reduces the length of series by 1/4
    public double paaRatio = 0.5;
    // the step ration of subsequence
    public double stepRatio;
    public int slidingWindow;
    //public int slidingWindow_miniunit;
    public int numSubsequence;

    // initialize the data structures
    public void Initialize(DataSet dataSet) {

        // set the labels to be binary 0 and 1, needed for the logistic loss
        CreateOneVsAllTargets();

        // initialize the shapelets (complete initialization during the clustering)
        Shapelets = new double[C][][];

        //GradHistShapelets = new double[R][][];
        GradHistShapelets = new double[C][][];

        long discover_startTime = System.currentTimeMillis();
        TransferPAASAX(dataSet);
        long discover_endTime = System.currentTimeMillis();
        Logging.println("Discover Time = " + (discover_endTime-discover_startTime), Logging.LogLevel.DEBUGGING_LOG);

        K = new int[C];

        // initialize the terms for pre-computation
        D = new double[ITrain+ITest][C][][];
        E = new double[ITrain+ITest][C][][];

        // initialize the placeholders for the precomputed values
        M = new double[ITrain+ITest][][];
        Psi = new double[ITrain+ITest][];
        sigY = new double[ITrain+ITest][C];

        // initialize the weights
        W = new double[C][];
        biasW = new double[C];

        GradHistW = new double[C][];
        GradHistBiasW = new double[C];

        for(int c=0; c<C; c++){
            K[c] = Tp.hatOmegaLable[c].size();

            // initialize the weights
            W[c] = new double[K[c]];

            GradHistW[c] = new double[K[c]];
        }
        int tempK=0;

        for(int c=0; c<C; c++){
            if(K[c] > tempK){
                tempK = K[c];
            }
        }

        for(int i=0; i<ITrain+ITest; i++){
            M[i] = new double[C][];

            for(int c=0; c<C; c++){
                // initialize the terms for pre-computation
                D[i][c] = new double[K[c]][];
                E[i][c] = new double[K[c]][];

                M[i][c] = new double[K[c]];

                // initialize the placeholders for the precomputed values
                Psi[i] = new double[tempK];

                for (int k = 0; k < K[c]; k++) {
                    D[i][c][k] = new double[T.getDimColumns()-Tp.hatOmegaLable[c].get(k).length()];
                    E[i][c][k] = new double[T.getDimColumns()-Tp.hatOmegaLable[c].get(k).length()];
                }

            }
        }

        for(int c = 0; c < C; c++)
        {
            for(int k = 0; k < Tp.hatOmegaLable[c].size(); k++)
            {
                W[c][k] = 2*rand.nextDouble()-1;
                GradHistW[c][k] = 0;
            }

            biasW[c] = 2*rand.nextDouble()-1;
            GradHistBiasW[c] = 0;
        }

        // precompute the M, Psi, sigY, used later for setting initial W
        for(int i=0; i < ITrain+ITest; i++) {
            PreCompute(i);
        }
        // store all the instances indexes for
        instanceIdxs = new ArrayList<Integer>();
        for(int i = 0; i < ITrain; i++)
            instanceIdxs.add(i);
        // shuffle the order for a better convergence
        Collections.shuffle(instanceIdxs, rand);
        Logging.println("Initializations Completed!", Logging.LogLevel.DEBUGGING_LOG);
    }

    // create one-cs-all targets
    public void CreateOneVsAllTargets() {
        C = nominalLabels.size();

        Y_b = new Matrix(ITrain+ITest, C);

        // initialize the extended representation
        for(int i = 0; i < ITrain+ITest; i++)
        {
            // firts set everything to zero
            for(int c = 0; c < C; c++)
                Y_b.set(i, c, 0);

            // then set the real label index to 1
            int indexLabel = nominalLabels.indexOf( Y.get(i, 0) );
            Y_b.set(i, indexLabel, 1.0);
        }

    }

    // predict the label value vartheta_i
    public double Predict(int i, int c) {
        double Y_hat_ic = biasW[c];

        //for(int r = 0; r < R; r++)
        for(int k = 0; k < K[c]; k++)
            Y_hat_ic += M[i][c][k] * W[c][k];

        return Y_hat_ic;
    }

    // precompute terms
    public void PreCompute(int i) {
        // precompute terms
        for(int c=0; c<C; c++){
            for(int k=0; k<Tp.hatOmegaLable[c].size(); k++){
                for(int j=0; j<T.getDimColumns()-Tp.hatOmegaLable[c].get(k).length(); j++){
                    // precompute D
                    D[i][c][k][j] = 0;
                    double err = 0;
                    for(int l=0; l<Tp.hatOmegaLable[c].get(k).length(); l++){
                        err = T.get(i, j+l)- Shapelets[c][k][l];
                        D[i][c][k][j] += err*err;
                    }
                    D[i][c][k][j] /= (double) Tp.hatOmegaLable[c].get(k).length();

                    // precompute E
                    E[i][c][k][j] = Math.exp(alpha * D[i][c][k][j]);
                }

                // precompute Psi
                Psi[i][k] = 0;
                for(int j = 0; j < T.getDimColumns()-Tp.hatOmegaLable[c].get(k).length(); j++)
                    Psi[i][k] +=  Math.exp( alpha * D[i][c][k][j] );

                // precompute M
                M[i][c][k] = 0;

                for(int j = 0; j < T.getDimColumns()-Tp.hatOmegaLable[c].get(k).length(); j++)
                    M[i][c][k] += D[i][c][k][j]* E[i][c][k][j];

                M[i][c][k] /= Psi[i][k];
            }

        }

        for(int c = 0; c < C; c++)
            sigY[i][c] = Sigmoid.Calculate( Predict(i, c) );
    }

    // compute the MCR on the test set
    public double GetMCRTrainSet() {
        int numErrors = 0;

        for(int i = 0; i < ITrain; i++)
        {
            PreCompute(i);

            double max_Y_hat_ic = Double.MIN_VALUE;
            int label_i = -1;

            for(int c = 0; c < C; c++)
            {
                double Y_hat_ic = Sigmoid.Calculate( Predict(i, c) );

                if(Y_hat_ic > max_Y_hat_ic)
                {
                    max_Y_hat_ic = Y_hat_ic;
                    label_i = (int)Math.ceil(c);
                }
            }

            if( nominalLabels.indexOf(Y.get(i)) != label_i )
                numErrors++;
        }

        return (double)numErrors/(double)ITrain;
    }

    // compute the MCR on the test set
    public double ReduceGetMCRTestSet() {
        int numErrors = 0;

        for(int i = ITrain; i < ITrain+ITest; i++)
        {
            PreCompute(i);

            double max_Y_hat_ic = Double.MIN_VALUE;
            int label_i = -1;

            for(int c = 0; c < C; c++)
            {
                double Y_hat_ic = Sigmoid.Calculate( Predict(i, c) );

                if(Y_hat_ic > max_Y_hat_ic)
                {
                    max_Y_hat_ic = Y_hat_ic;
                    label_i = (int)Math.ceil(c);
                }
            }

            if( nominalLabels.indexOf(Y.get(i)) != label_i )
                numErrors++;
        }

        return (double)numErrors/(double)ITest;
    }

    // compute the accuracy loss of instance i according to the
    // smooth hinge loss
    public double AccuracyLoss(int i, int c) {
        double Y_hat_ic = Predict(i, c);
        double sig_y_ic = Sigmoid.Calculate(Y_hat_ic);

        return -Y_b.get(i,c)*Math.log( sig_y_ic ) - (1-Y_b.get(i, c))*Math.log(1-sig_y_ic);
    }

    // compute the accuracy loss of the train set
    public double AccuracyLossTrainSet() {
        double accuracyLoss = 0;

        for(int i = 0; i < ITrain; i++)
        {
            PreCompute(i);

            for(int c = 0; c < C; c++)
                accuracyLoss += AccuracyLoss(i, c);
        }

        return accuracyLoss;
    }
    // compute the accuracy loss of the train set

    public double AccuracyLossTestSet() {
        double accuracyLoss = 0;

        for(int i = ITrain; i < ITrain+ITest; i++)
        {
            PreCompute(i);

            for(int c = 0; c < C; c++)
                accuracyLoss += AccuracyLoss(i, c);
        }
        return accuracyLoss;
    }

    public int HammingDistance(String One, String Two) {
        if (One.length() != Two.length())
            return -1;

        int counter = 0;

        for (int i = 0; i < One.length(); i++)
        {
            if ( (One.charAt(i) != Two.charAt(i)) && Math.abs(One.charAt(i) - Two.charAt(i)) < 2 ){
                counter++;
            }
            else if(Math.abs(One.charAt(i) - Two.charAt(i)) > 1){
                if( (DTW(One, Two)==0) || (DTW(Two, One)==0) ){
                    return One.length()/slidingWindow_miniunit;
                }else {
                    return -1;
                }
            }
        }

        return counter;
    }

    public int DTW(String One, String Two) {
        int numCut = One.length()/slidingWindow_miniunit;
        String tempOne, tempTwo;
        for(int i=1; i<=numCut; i++){
            tempOne = One.substring(i);
            tempTwo = Two.substring(0, One.length()-i);
            for(int j=0; j<tempOne.length(); j++){
                if( (tempOne.charAt(j) != tempTwo.charAt(j)) ){
                    break;
                }
                if( j == (tempOne.length()-1) ){
                    return 0;
                }
            }
        }
        return -1;
    }

    public boolean pCover(int [] coverSet, int p){
        for(int i=0; i<coverSet.length; i++){
            if(coverSet[i]<p){
                return true;
            }
        }
        return false;
    }

    public void TransferPAASAX(DataSet dataSet) {
        //determine the paaRatio
//        if(Tp.getDimColumns()>500){
//            paaRatio = 0.2;
//        }else if(Tp.getDimColumns()<100){
//            paaRatio = 1.0;
//        }
//        else {
//            paaRatio = 1.0/(Tp.getDimColumns()/100);
//        }

        // determine the stepRatio
//        stepRatio =  5.0 / Tp.getDimColumns() * paaRatio;

        // apply the PAA
        SAXRepresentation sax = new SAXRepresentation();
        //SAXRepresentation rawsax = new SAXRepresentation();
        Tp = sax.generatePAAToMatrix(dataSet, paaRatio);

        minData = Tp.getMinValue();
        maxData = Tp.getMaxValue();
        midData = (minData + maxData)/2.0;
        spaceData = (maxData - minData)/2.0;
        Logging.println(minData+ "  "+ maxData+  "  "+spaceData, Logging.LogLevel.DEBUGGING_LOG);

        //Transfer PAA to SAX
        Tp.SAX  = new String[ITrain];
        for(int i=0; i < ITrain; i++){
//            Logging  .println(i +" " + Tp.getRow(i), Logging.LogLevel.DEBUGGING_LOG);
            Tp.SAX[i] = sax.ConvertToSAX(Tp.getRow(i),alphabetSize, midData, spaceData);
        }

        //Construct the Bloom Filters
        {
            int originalLength = Tp.getDimColumns();
            int tmpSlide = (int) (stepRatio * Tp.getDimColumns());
            for(int i=1; i <= originalLength/tmpSlide; i++){
                numSubsequence = numSubsequence + (originalLength + 1 - tmpSlide * i);
            }
        }

        slidingWindow_miniunit = (int) (stepRatio * Tp.getDimColumns());
        C = nominalLabels.size();
        BF = new BloomFilter[C];
        Tp.OmegaLable = new TreeSet[C];
        Tp.starOmegaLable = new TreeSet[C];
        Tp.hatOmegaLable = new ArrayList[C];
        Tp.starOmegaArrayList = new ArrayList[C];
        Tp.starOmegaLinkedList = new LinkedList[C];
        Tp.labelNum = new int[C];
        Tp.subSAX = new String[C][][];

        // unify the label
        TreeSet<Integer> originalLabelSet = new TreeSet<Integer>();
        for(int i=0;i<ITrain;i++){
            if (originalLabelSet.size()<C) {
                originalLabelSet.add((int)(Y.get(i,0)));
            }else {
                break;
            }
        }
        List<Integer> originalLabelList = new ArrayList<Integer>(originalLabelSet);

        //the number of instance for each class
        int C_new;
        for(int i=0;i<ITrain;i++){
//            int C_new;
//            if( (int)(Y.get(i,0)) == -1 ){
//                C_new = 0;
//            }else{
//                C_new = (int)(Y.get(i,0));
//            }
            //C_new = (int)(Y.get(i,0)-1);
            C_new = originalLabelList.indexOf((int)(Y.get(i,0)));
            //int C_new = (int)(Y.get(i,0));
            ++Tp.labelNum[C_new];
        }

        for(int c=0; c<C;c++){
            BF[c] = new BloomFilter(falsePositiveProbability,expectedNumberOfElements);
            Tp.OmegaLable[c] = new TreeSet<String>();
            Tp.starOmegaLable[c] = new TreeSet<String>();
            Tp.hatOmegaLable[c] = new ArrayList<String>();
            Tp.starOmegaArrayList[c] = new ArrayList<String>();
            Tp.starOmegaLinkedList[c] = new LinkedList<String>();
            Tp.subSAX[c] = new String[Tp.labelNum[c]][numSubsequence];
        }
        int [] classNumTemp;
        classNumTemp = new int[C];
        for(int i=0; i< ITrain; i++){
//            int C_new;
//            if( (int)(Y.get(i,0)) == -1 ){
//                C_new = 0;
//            }else{
//                C_new = (int)(Y.get(i,0));
//            }
            //C_new = (int)(Y.get(i,0)-1);
            C_new = originalLabelList.indexOf((int)(Y.get(i,0)));
            //int C_new = (int)(Y.get(i,0));
            int slideStep = 1;
            int subScript = numSubsequence-1;
            for(slidingWindow = slidingWindow_miniunit; slidingWindow <= Tp.getDimColumns(); slidingWindow=slidingWindow_miniunit * slideStep){
                for(int v = 0; Tp.getDimColumns() - v >= slidingWindow ; v++){
                    Tp.subSAX[C_new][classNumTemp[C_new]][subScript] = Tp.SAX[i].substring(v,v+slidingWindow);
                    Tp.OmegaLable[C_new].add(Tp.subSAX[C_new][classNumTemp[C_new]][subScript]);
                    BF[C_new].add(Tp.subSAX[C_new][classNumTemp[C_new]][subScript]);
                    subScript--;
                }
                ++slideStep;
            }
            ++classNumTemp[C_new];
        }

        //excluding the subsequences in opposite side  with C classes
        LinkedList<Integer> [] BfExLinked = new LinkedList [C];
        for(int c=0; c<C; c++){
            BfExLinked[c] = new LinkedList<>();
            for(int i=0; i<C; i++){
                BfExLinked[c].add(i);
            }
        }
        for(int c=0; c<C; c++){
            if(BfExLinked[c].contains(c)){
                BfExLinked[c].remove(c);
            }
            int q=0;
            for(String s: Tp.OmegaLable[c]){
                for(int j=0; j<BfExLinked[c].size(); j++){
                    if(!BF[BfExLinked[c].get(j)].contains(s)){
                        //if(BF[BfExLinked[c].get(j)].contains(s)){
                        if(j == (BfExLinked[c].size()-1) ){
                            Tp.starOmegaLable[c].add(s);
                            Tp.starOmegaArrayList[c].add(s);
                            Tp.starOmegaLinkedList[c].add(s);
                        }
                        else{
                            continue;
                        }
                    }
                    else{
                        break;
                    }
                }
            }
        }

        /*** Csonstructing the bitmap **/
        int match=0;
        Tp.BitMap = new BitSet[C][];
        Tp.weightBitmap = new int[C][];
        for(int c=0; c<C; c++){
            Tp.BitMap[c] = new BitSet[Tp.starOmegaArrayList[c].size()];
            Tp.weightBitmap[c] = new int[Tp.starOmegaArrayList[c].size()];
            String s;
            for(int i=0; i< Tp.starOmegaArrayList[c].size();i++) {
                Tp.BitMap[c][i] = new BitSet(Tp.labelNum[c]);
                Tp.weightBitmap[c][i] = 0;
                s = Tp.starOmegaArrayList[c].get(i);
                int startPoint = 0;
                if( Tp.getDimColumns() == s.length() ){
                    startPoint = 0;
                }else {
                    for(int q=(Tp.getDimColumns()/slidingWindow_miniunit); q>s.length()/slidingWindow_miniunit; --q){
                        startPoint +=  (Tp.getDimColumns()-slidingWindow_miniunit *q + 1);
                    }
                }
                int candidateNum = Tp.getDimColumns()-s.length()+1;
                int endPoint = startPoint + candidateNum;
                for(int j=0; j<Tp.labelNum[c]; j++){
                    for(int l= startPoint; l< endPoint; l++){
                        if(s.equals(Tp.subSAX[c][j][l])){
                            Tp.BitMap[c][i].set(j);
                            ++match;
                            break;
                        }
                    }
                }
            }
        }

        /*** Before processing similar subsequences **/
        for(int c=0; c<C; c++){
            String s1, s2;
            int numSim = 0;
            int excludeNum = 0;
            for(int i=0; i<Tp.starOmegaArrayList[c].size(); i++){
                s1 = Tp.starOmegaArrayList[c].get(i);
                Tp.weightBitmap[c][i]++;
                for(int j=i+1; j<Tp.starOmegaArrayList[c].size(); j++){
                    s2 = Tp.starOmegaArrayList[c].get(j);
                    if ( HammingDistance(s1, s2) == s1.length()/slidingWindow_miniunit ){
                        Tp.weightBitmap[c][i]++;
                        Tp.weightBitmap[c][j]++;
                        numSim++;
                    }
                }
            }
        }

        /*** Excluding similar sax words from other classes **/
        LinkedList<Integer> [] BfSimExLinked = new LinkedList [C];
        for(int c=0; c<C; c++){
            BfSimExLinked[c] = new LinkedList<>();
            for(int i=0; i<C; i++){
                BfSimExLinked[c].add(i);
            }
        }
        for(int c=0; c<C-1; c++){
            String s1, s2;
            int account = 0;
            if(BfSimExLinked[c].contains(c)){
                BfSimExLinked[c].remove(c);
            }
            for(int i=0; i<Tp.starOmegaArrayList[c].size(); i++){
                if(Tp.BitMap[c][i].cardinality() != 0){
                    s1 = Tp.starOmegaArrayList[c].get(i);
                    for(int j=0; j<BfSimExLinked[c].size(); j++) {
                        for(int k = 0; k < Tp.starOmegaArrayList[BfSimExLinked[c].get(j)].size(); k++) {
                            if( Tp.BitMap[BfSimExLinked[c].get(j)][k].cardinality() != 0 ) {
                                s2 = Tp.starOmegaArrayList[BfSimExLinked[c].get(j)].get(k);
                                if(s1.length() == s2.length()){
                                    if (HammingDistance(s1, s2) == s1.length() / slidingWindow_miniunit) {
                                        Tp.BitMap[c][i].clear();
                                        Tp.weightBitmap[c][i] = 0;
                                        Tp.BitMap[BfSimExLinked[c].get(j)][k].clear();
                                        Tp.weightBitmap[BfSimExLinked[c].get(j)][k] = 0;
                                        account++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //sort sax for each class
        for(int c=0; c<C; c++){
            sortSAX(Tp.starOmegaArrayList[c],Tp.BitMap[c],Tp.weightBitmap[c]);
        }

        /*** Select final SAX for LTS **/
        for(int p=0; p<pcover; p++){
            for(int c=0; c<C; c++){
                finalSAX(Tp.starOmegaArrayList[c],Tp.hatOmegaLable[c],Tp.BitMap[c],Tp.labelNum[c], Tp.weightBitmap[c]);
                modifyWeight(Tp.hatOmegaLable[c], Tp.starOmegaArrayList[c], Tp.BitMap[c], Tp.weightBitmap[c]);
            }
        }

        /*** transfer SAX  bcak to raw data **/
        // initialize the gradient history of shapelets
        Shapelets = new double[C][][];
        GradHistShapelets = new double[C][][];
        for(int c=0; c<C; c++){
            Shapelets[c] = new double[Tp.hatOmegaLable[c].size()][];
            GradHistShapelets[c] =new double[Tp.hatOmegaLable[c].size()][];
            for(int i=0; i<Tp.hatOmegaLable[c].size(); i++){
                Shapelets[c][i] = new double[Tp.hatOmegaLable[c].get(i).length()]; //size
                GradHistShapelets[c][i] = new double[Tp.hatOmegaLable[c].get(i).length()]; //size
                Shapelets[c][i] = sax.RestoreSeriesFromSax(Tp.hatOmegaLable[c].get(i),
                        Tp.hatOmegaLable[c].get(i).length(), alphabetSize, midData, spaceData);
                for(int j=0; j<Tp.hatOmegaLable[c].get(i).length(); j++){
                    GradHistShapelets[c][i][j] = 0.0;
                }
            }
        }
    }

    public void sortSAX(ArrayList al, BitSet [] bs, int [] wb){
        BitSet bsTemp;
        int wbTemp;
        String saxTemp;
        for(int i=0; i<al.size()-1; i++){
            boolean flag = true;
            for(int j=0; j<al.size()-1-i; j++){
                if(bs[j].cardinality() < bs[j+1].cardinality()){
                    bsTemp = bs[j];
                    wbTemp = wb[j];
                    saxTemp = al.get(j).toString();
                    bs[j] = bs[j+1];
                    wb[j] = wb[j+1];
                    al.set(j, al.get(j+1));
                    bs[j+1] = bsTemp;
                    wb[j+1] = wbTemp;
                    al.set(j+1, saxTemp);
                    flag = false;
                } else if( bs[j].cardinality() == bs[j+1].cardinality() &&
                        bs[j].cardinality() != 0 && wb[j] < wb[j+1]){
                    bsTemp = bs[j];
                    wbTemp = wb[j];
                    saxTemp = al.get(j).toString();
                    bs[j] = bs[j+1];
                    wb[j] = wb[j+1];
                    al.set(j, al.get(j+1));
                    bs[j+1] = bsTemp;
                    wb[j+1] = wbTemp;
                    al.set(j+1, saxTemp);
                    flag = false;
                }
            }
            if(flag){
                break;
            }
        }
    }

    public void finalSAX(ArrayList al, ArrayList sal, BitSet [] bs, int label, int[] wbs){
        //choosing the candidates for LTS
        BitSet bsTemp;
        int BitMapNoneSize=0;
        bsTemp = new BitSet(label);
        for(int i=0; i<al.size(); i++){
            if(bsTemp.cardinality() == label){
                break;
            }else {
                bsTemp.or(bs[i]);
                if(bsTemp.cardinality() > BitMapNoneSize){
                    sal.add(al.get(i).toString());
                    bs[i].clear();
                    BitMapNoneSize = bsTemp.cardinality();
                }
            }
        }
    }

    public void modifyWeight(ArrayList sal, ArrayList al, BitSet[] bs, int [] wbs){
        for(int i=0; i<sal.size(); i++){
            String s1 = sal.get(i).toString();
            for(int j=0; j<al.size(); j++){
                String s2 = al.get(j).toString();
                if(s1.length() != s2.length()){
                    continue;
                }else{
                    if(HammingDistance(s1, s2) <= s1.length()/slidingWindow_miniunit){
                        //wbs[j]--;
                        bs[j].clear();
                    }
                }
            }
        }
    }

    public void LearnF() {
        // parallel implementation of the learning, one thread per instance
        // up to as much threads as JVM allows
        //long startTime = System.currentTimeMillis();
        Parallel_1x0.ForEach(instanceIdxs, new ForEachTask_1x0<Integer>()
        {
            public void iteration(Integer i)
            {
                double regWConst = ((double)2.0*lambdaW) / ((double) ITrain*C);

                double tmp2 = 0, tmp1 = 0, dLdY = 0, dMdS=0, gradS_rkl = 0, gradBiasW_c = 0, gradW_crk = 0;
                double eps = 0.000001;

                for(int c = 0; c < C; c++)
                {
                    PreCompute(i);

                    dLdY = -(Y_b.get(i, c) - sigY[i][c]);
                    for(int k = 0; k < K[c]; k++)
                    {
                        // gradient with respect to W_crk
                        gradW_crk = dLdY*M[i][c][k] + regWConst*W[c][k];

                        // add gradient square to the history
                        GradHistW[c][k] += gradW_crk*gradW_crk;

                        // update the weights
                        W[c][k] -= (eta / ( Math.sqrt(GradHistW[c][k]) + eps))*gradW_crk;

                        tmp1 = ( 2.0 / ( (double) Tp.hatOmegaLable[c].get(k).length() * Psi[i][k]) );

                        for (int l = 0; l < Tp.hatOmegaLable[c].get(k).length(); l++){
                            tmp2=0;

                            for(int j = 0; j < T.getDimColumns()-Tp.hatOmegaLable[c].get(k).length(); j++)
                                tmp2 += E[i][c][k][j] * (1 + alpha * (D[i][c][k][j] - M[i][c][k])) *
                                        (Shapelets[c][k][l] - T.get(i, j+l));

                            gradS_rkl = dLdY * W[c][k] * tmp1 * tmp2;

                            // add the gradient to the history
                            GradHistShapelets[c][k][l] += gradS_rkl * gradS_rkl;

                            Shapelets[c][k][l] -= (eta / (Math.sqrt(GradHistShapelets[c][k][l]) + eps))
                                    * gradS_rkl;
                        }
                    }

                    // the gradient
                    gradBiasW_c = dLdY;

                    // add the gradient to the history
                    GradHistBiasW[c] += gradBiasW_c*gradBiasW_c;

                    biasW[c] -= (eta / ( Math.sqrt(GradHistBiasW[c]) + eps))*gradBiasW_c;
                }
            }
        });
    }

    // optimize the objective function
    public double Learn(DataSet dataSet) throws FileNotFoundException, UnsupportedEncodingException {
        Initialize(dataSet);

        List<Double> lossHistory = new ArrayList<Double>();
        lossHistory.add(Double.MIN_VALUE);

        long iter_startTime = System.currentTimeMillis();
        // apply the stochastic gradient descent in a series of iterations
        for(int iter = 0; iter <= maxIter; iter++)
        {
            long LearnF_startTime = System.currentTimeMillis();
            // learn the latent matrices
            LearnF();
            long LearnF_endTime = System.currentTimeMillis();

            // measure the loss
            if( iter % 500 == 0)
            {
                double mcrTrain = GetMCRTrainSet();
                double mcrTest = ReduceGetMCRTestSet();

                double lossTrain = AccuracyLossTrainSet();
                double lossTest = AccuracyLossTestSet();

                lossHistory.add(lossTrain);

                Logging.println("It=" + iter + ", alpha= "+alpha+", lossTrain="+ lossTrain + ", lossTest="+
                        lossTest  + ", MCRTrain=" +mcrTrain + ", MCRTest=" +mcrTest + ", LearnF time="+
                        (LearnF_endTime-LearnF_startTime), Logging.LogLevel.DEBUGGING_LOG);

                // if divergence is detected start from the beggining
                // at a lower learning rate
                if( Double.isNaN(lossTrain) || mcrTrain == 1.0 )
                {
                    iter = 0;

                    eta /= 3;

                    lossHistory.clear();

                    Initialize(dataSet);

                    Logging.println("Divergence detected. Restarting at eta=" + eta, Logging.LogLevel.DEBUGGING_LOG);
                }

                if( lossHistory.size() > 100 )
                    if( lossTrain > lossHistory.get( lossHistory.size() - 2  )  )
                        break;
            }
        }

        //output shapelet

        /*** ----------------------------------------------------------- ***/
        /**********************************************************
         ****** The shapelets output path is here! ******
         *********************************************************/

        String subroot_I = "/datasets/Grace_dataset/v_2/shapelet-original.txt";
        String subroot_II = "/datasets/ItalyPowerDemand_dataset/v_1/shapelet-original.txt";

        String root = System.getProperty("user.dir");
        String path = root + subroot_I;
        //PrintWriter writer = new PrintWriter("experiment/shapelet/shapelet-original.txt", "UTF-8");
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        for(int c=0; c<C; c++){
            //System.out.println("Class: "+c);
            for(int k=0; k<K[c] ;k++){
                //System.out.println("shapelet: "+k);
                writer.append(c +",");
                for(int l=0; l<Tp.hatOmegaLable[c].get(k).length() ;l++){
//                    Logging.println("Shapelets["+c+"]["+k+"]["+l+"]" + Shapelets[c][k][l], Logging.LogLevel.DEBUGGING_LOG);
                    //writer.append(String.valueOf(Shapelets[c][k][l] * Tp.getMeanValue() + Math.sqrt(Tp.getVariance())));
                    writer.append(String.valueOf(Shapelets[c][k][l]));
                    if(l != (Tp.hatOmegaLable[c].get(k).length()-1)){
                        writer.append(",");
                    }
                }
                writer.println();
            }
        }
        writer.close();

        for(int c=0; c<C; c++){
            System.out.println("Class: "+c+" number of shapelets: "+K[c]);
        }

        return ReduceGetMCRTestSet();
    }

//    public static void main(String []args) throws Exception {
////        String filedir = "/home/comp/csgzli/experiment/";
////        String filedir = "D:\\RA\\Task4-ML\\experiment\\";
//
//        //test path
//        String filedir = "experimentI/shapetes";
//        File file = new File(filedir);
//        File[] files = file.listFiles();
//        String ds;
//        String sp = File.separator;
//
//        //Default parameter
//        int maxEpochs=1000;
//        int alphabetSize=4;
//        double stepRatio = 0.5;
//        double paaRatio = 0.5;
//        int pcover =1;
//
//        //read parameter file
//        //String parameter = "experiment\\parameter.txt";
//        BufferedReader br = new BufferedReader(new FileReader("experimentI/parameter.txt"));
//        String parameter;
//        while((parameter = br.readLine()) != null) {
//            String[] tokens = parameter.split(",");
//
//            maxEpochs = Integer.parseInt(tokens[0].trim());
//            alphabetSize = Integer.parseInt(tokens[1].trim());
//            stepRatio = Double.parseDouble(tokens[2].trim());
//            paaRatio = Double.parseDouble(tokens[3].trim());
//            pcover = Integer.parseInt(tokens[4].trim());
//        }
//                for (int j = 0; j < files.length; j++) {
//                    ds = files[j].getName();
//
////            try {
////                PrintStream print = new PrintStream("/home/comp/csgzli/experiment/ResultsTXT/experimentResult\\" + files[j].getName() + ".txt");
////                System.setOut(print);
////            } catch (FileNotFoundException e) {
////                e.printStackTrace();
////            }
//
//                    // values of hyperparameters
//                    double eta = 0.1, lambdaW = 0.01, alpha = -25, L = 0.2, K = -1;
////                    double eta = 0.1, alpha = -25, L = 0.2;
////            int maxEpochs = 1000, R = 4;
//                    //int maxEpochs = 1000;
//                    //int R = 4;
//                    //int alphabetSize = 4;
//                    //double stepRatio = 0.5;
//                    //double paaRatio = 0.5;
//                    //int pcover = 1;
//                    double falsePositiveProbability = 0.0001;
//                    int expectedNumberOfElements = 60000;
//                    String trainSetPath = filedir + ds + sp + ds + "_TRAIN", testSetPath = filedir + ds + sp + ds + "_TEST";
//                    //String trainSetPath = filedir + ds, testSetPath = filedir + ds;
//
//                    Logging.println("dataset: " + files[j].getName() + " stepRatio: " + stepRatio + " alphabetSize: "
//                            + alphabetSize, Logging.LogLevel.DEBUGGING_LOG);
//
//                    long startTime = System.currentTimeMillis();
//
//                    // load dataset
//                    DataSet trainSet = new DataSet();
//                    trainSet.LoadDataSetFile(new File(trainSetPath));
//                    DataSet testSet = new DataSet();
//                    testSet.LoadDataSetFile(new File(testSetPath));
//
//                    // normalize the data instance
//                    trainSet.NormalizeDatasetInstances();
//                    testSet.NormalizeDatasetInstances();
//
//                    // predictor variables T
//                    Matrix T = new Matrix();
//                    T.LoadDatasetFeatures(trainSet, false);
//                    T.LoadDatasetFeatures(testSet, true);
//                    // outcome variable O
//                    Matrix O = new Matrix();
//                    O.LoadDatasetLabels(trainSet, false);
//                    O.LoadDatasetLabels(testSet, true);
//
//                    EfficientLTS eLTS = new EfficientLTS();
//                    // initialize the sizes of data structures
//                    eLTS.ITrain = trainSet.GetNumInstances();
//                    eLTS.ITest = testSet.GetNumInstances();
//                    eLTS.Q = T.getDimColumns();
//                    //Logging.println(eLTS.ITrain+" "+eLTS.ITest+" "+eLTS.Q);
//                    // set the time series and labels
//                    eLTS.T = T;
//                    eLTS.Y = O;
//
//                    eLTS.alphabetSize = alphabetSize;
//                    eLTS.stepRatio = stepRatio;
//                    eLTS.paaRatio = paaRatio;
//                    eLTS.pcover = pcover;
//
//                    eLTS.falsePositiveProbability = falsePositiveProbability;
//                    eLTS.expectedNumberOfElements = expectedNumberOfElements;
//
//                    // set the learn rate and the number of iterations
//                    eLTS.maxIter = maxEpochs;
//
//                    eLTS.slidingWindow_miniunit = (int) (L * T.getDimColumns());
//                    //eLTS.R = R;
//                    // set the regularization parameter
//                    eLTS.lambdaW = lambdaW;
//                    eLTS.eta = eta;
//                    eLTS.alpha = alpha;
//                    trainSet.ReadNominalTargets();
//                    eLTS.nominalLabels = new ArrayList<Double>(trainSet.nominalLabels);
//
//                    long before_learn = System.currentTimeMillis();
//
//                    eLTS.Learn(trainSet);
//                    long endTime = System.currentTimeMillis();
//
//                    Logging.println(
//                            String.valueOf(eLTS.ReduceGetMCRTestSet()) + " " + String.valueOf(eLTS.GetMCRTrainSet()) + " "
//                                    + String.valueOf(eLTS.AccuracyLossTrainSet()) + " "
//                                    + "learn time=" + (endTime - before_learn) + " "
//                                    + "before_learn time=" + (before_learn - startTime), Logging.LogLevel.DEBUGGING_LOG
//                    );
//
//                }
//            }
//        }catch (Exception e){
//            Logging.println("Exception thrown: " + e);
//        }

//    }

}