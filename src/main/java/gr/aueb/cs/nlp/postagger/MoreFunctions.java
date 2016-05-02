/*
 * POStagger 2011
 * Athens University of Economics and Business
 * Department of Informatics
 * Koleli Evangelia
 */
package gr.aueb.cs.nlp.postagger;

import edu.stanford.nlp.CLclassify.*;
import edu.stanford.nlp.CLling.RVFDatum;
import edu.stanford.nlp.CLstats.ClassicCounter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoreFunctions {

    protected static HashMap<String, String> list;
    protected static HashMap<String, SmallSetWordWithCategories> words;
    protected static HashMap<String, SmallSetWordWithCategories> endings1;
    protected static HashMap<String, SmallSetWordWithCategories> endings2;
    protected static HashMap<String, SmallSetWordWithCategories> endings3; 
    protected static int corpus_used;

    //returns the 3 possible endings of a word
    protected static String getEnding(int numOfLetters, String word) {
    	
    	char[] array = word.toCharArray();

        if (numOfLetters == 1) {
            return Character.toString(array[array.length - 1]);
        }
        if (numOfLetters == 2) {
            return Character.toString(array[array.length - 2]) + Character.toString(array[array.length - 1]);
        }
        if (numOfLetters == 3) {
            return Character.toString(array[array.length - 3]) + Character.toString(array[array.length - 2]) + Character.toString(array[array.length - 1]);
        }
        return null;
    }

    //returns the length of a word
    protected static int findLength(String word) {
        char[] array = word.toCharArray();
        if (array.length == 1) {
            return 1;
        }
        if (array.length == 2) {
            return 2;
        }
        if (array.length > 2) {
            return 3;
        }
        return 0;
    }

    //opens a buffer in order to read the file with properties
    protected static void readFileWithProperties(String fileName, RVFDataset ds, boolean test) throws FileNotFoundException {
        FileInputStream fstream = new FileInputStream(fileName);
        DataInputStream in = new DataInputStream(fstream);
        if (test) {
            useRealValuedClassifierTest(new BufferedReader(new InputStreamReader(in)), ds);
        } else if (!test) {
            //useRealValuedClassifier(new BufferedReader(new InputStreamReader(in)), ds);//we use it only for cross validation
        }
    }

    //creates a data structure of train instances
    protected static void useRealValuedClassifierTest(BufferedReader br, RVFDataset ds) {
        String dataLine;
        String label;
        RVFDatum d;
        try {
            while ((dataLine = br.readLine()) != null) {
                ClassicCounter<String> cc = new ClassicCounter<String>();
                String[] data = dataLine.split(" ");
                label = data[0];
                for (int i = 1; i < data.length; i++) {
                    cc.incrementCount("feature" + i, Double.parseDouble(data[i]));
                }
                d = new RVFDatum(cc, label);
                ds.add(d);
            }
        } catch (IOException ex) {
            Logger.getLogger(MoreFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //returns the square of a number
    protected static double square(double a) {
        return a * a;
    }

    //creates a vector which contains the words to be classified
    protected static Vector<String> createVector(String wholeText) {
        Vector<String> justWords = new Vector<String>();
        StringTokenizer st = new StringTokenizer(wholeText, " ");
        justWords.add("null");
        while (st.hasMoreTokens()) {
            justWords.add(st.nextToken());
        }
        justWords.add("null");
        return justWords;
    }
}
