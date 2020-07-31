package DataClean;

import Utilities.Logging;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataClean_Good {

    private String filedir = System.getProperty("user.dir");

    /*** Constructor -----------------------------! **/
    public DataClean_Good(){
        clean();
    }

    /*** clean() **/
    public void clean(){

        File file = new File(filedir + "/experiment/Grace-java");
        File[] files = file.listFiles();
        String ds;
        String sp = File.separator;

        for(int i = 0; i < files.length; i++){
            ds = files[i].getName();
            String trainSetPath = files[i]  + sp + ds + sp + ds + "_TRAIN";
// The traps any possible read/write exceptions which might occur
            try {
                File inputFile = new File(trainSetPath);
                // Open the reader/writer, this ensure that's encapsulated
                // in a try-with-resource block, automatically closing
                // the resources regardless of how the block exists
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){
                    // Read each line from the reader and compare it with
                    // with the line to remove and write if required
                    String line = null;
                    String line_I = null;

                    int rowStart = 1;
                    ArrayList<Integer> rowStartArrayList = new ArrayList<>();

                    while ((line = reader.readLine()) != null) {
                        line_I = line;
                        ArrayList<String> tsElementList = new ArrayList<String>(Arrays.asList(line_I.split(",")));

                        boolean nonZeroIndex = false;

                        for(int cutpointsIndex = 1; cutpointsIndex < tsElementList.size(); cutpointsIndex++)
                        {
                            if( Double.valueOf(tsElementList.get(cutpointsIndex)) > 0.0)
                            {
                                nonZeroIndex = true;
                                break;
                            }
                        }

                        if(!nonZeroIndex){
                            rowStartArrayList.add(rowStart);
                        }

                        rowStart++;
                    }
                    reader.close();

                    /*** Print all empty rows in Arraylist**/
                    for(int j = 0; j < rowStartArrayList.size(); j++) {
                        Logging.println(trainSetPath + " - " +"rowStartArrayList [" + j +"]: " + rowStartArrayList.get(j), Logging.LogLevel.INFORMATIVE_LOG);
                    }

                    cleanSingleRow(rowStartArrayList, i);
                }
            } catch (IOException ex) {
                // Handle any exceptions
                ex.printStackTrace();
            }
        }
    }

    /*** cleanSingleRow(): Only clean the its self dataset **/
    public void cleanSingleRow(ArrayList<Integer> row, int fileIndex){

        File file = new File(filedir + "/experiment/Grace-java");
        File[] files = file.listFiles();
        String ds;
        String sp = File.separator;

            ds = files[fileIndex].getName();
            String inputFileName = files[fileIndex] + sp + ds + sp + ds + "_TRAIN";
            String outputFileName = files[fileIndex]  + sp + ds + sp + ds + "_TRAIN" + "_COPY";
// The traps any possible read/write exceptions which might occur
            try {
                File inputFile = new File(inputFileName);
                File outputFile = new File(outputFileName);
                // Open the reader/writer, this ensure that's encapsulated
                // in a try-with-resource block, automatically closing
                // the resources regardless of how the block exists
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    // Read each line from the reader and compare it with
                    // with the line to remove and write if required
                    String line = null;

                    int rowStart = 1;
                    boolean skip = false;
                    while ((line = reader.readLine()) != null){
                        skip = false;
                        for(int arrayListIndex = 0; arrayListIndex < row.size(); arrayListIndex++){
                            if (rowStart == row.get(arrayListIndex)){
                                skip = true;
                                Logging.println("rowStart: " + rowStart, Logging.LogLevel.INFORMATIVE_LOG);
                                break;
                            }
                        }
                        if(!skip){
                            if(rowStart > 1){
                                writer.newLine();
                            }
                            writer.write(line);
                        }
                        rowStart++;
                    }
                    reader.close();
                    writer.close();
                }
                // This is some magic, because of the compounding try blocks
                // this section will only be called if the above try block
                // exited without throwing an exception, so we're now safe
                // to update the input file

                // If you want two files at the end of his process, don't do
                // this, this assumes you want to update and replace the
                // original file

                // Delete the original file, you might consider renaming it
                // to some backup file
                if (inputFile.delete()) {
                    // Rename the output file to the input file
                    if (!outputFile.renameTo(inputFile)) {
                        throw new IOException("Could not rename " + outputFileName + " to " + inputFileName);
                    }
                } else {
                    throw new IOException("Could not delete original input file " + inputFileName);
                }
            } catch (IOException ex) {
                // Handle any exceptions
                ex.printStackTrace();
            }

    }

    /*** main() **/
    public static void main(String[] args){
        DataClean_Good myDataClean_II = new DataClean_Good();
    }
}
