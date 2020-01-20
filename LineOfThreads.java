package hac;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * This class cares about threads output: they have to print their outpu according to order in
 * a list.
 */

public class LineOfThreads {

    int counter= 1;
    private int amount_threads;

    //-----------constructor-----------------
    LineOfThreads(int amount_threads){this.amount_threads = amount_threads;}


    // creating a hash table
    Hashtable<Integer, String> h = new Hashtable<>();
    Hashtable<Integer, String> h1 = new Hashtable<>();

    //save duration of current thread
    List<double[]> duration = new ArrayList<>();


    //-----------------------------------------------
    public void getready(int num, String urlName, double threadDuration){

        h.put(num, urlName);
        if (threadDuration == -1){
            h1.put(num, " timeout");
        }
        else if (threadDuration == -2){
            h1.put(num, " failed");
        }
        else {
            h1.put(num, String.format("%.0f", threadDuration) + " ms");
        }

        if (h.size() == amount_threads && counter==1) {
            counter++;
                 for (int i = 0; i < h.size(); i++) {
                        System.out.println(h.get(i) + " " + h1.get(i));
                 }
        }
    }
    //-----------------------------------------------
}
