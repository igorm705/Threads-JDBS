package hac;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** The main class of this program: it must get name file with maximum amount of
 * threads, delay, retries, URL's list.
 *
 */
public class Crawl {

    public static void main(String[] args) throws Exception {

        int amount_threads, retries;//amount of threades and retrties
        long delay; //maximal delay
        String FilesName;


        //----------------------------------------------------
        //checking for correctness of the arguments
        try {
            if (args.length != 6){
                throw new NotEnoughtArgumentsException();
            }
            try {
                amount_threads = Integer.parseInt(args[2]);
                retries = Integer.parseInt(args[4]);
                if (amount_threads <= 0 || retries <= 0) {
                    throw new LessThenZeroExcepton();
                }

            } catch (LessThenZeroExcepton e) {
                System.out.println("Amount of threads must be more than zero!");
            }

        }
        catch (NotEnoughtArgumentsException e) {
            System.out.println("Amount of arguments must be exactly 5!");
        }

        //----------------------------------------------------------
        //initialization variables
        amount_threads = Integer.parseInt(args[2]);
        ExecutorService pool = Executors.newFixedThreadPool(amount_threads);


        retries = Integer.parseInt(args[4]);
        delay = Long.parseLong(args[3]);

        FilesName = args[5];


        //---------------------------------------------------
       // read urls from the file
        List<String> list_url = new ArrayList<>();
        ReadFromFile rdf = new ReadFromFile();

        list_url = rdf.return_file(FilesName);


        //define arrayList of threads
        ArrayList<Runnable> array_threads = new ArrayList<Runnable>();

        //create and initializate boolean array
        boolean indexes[] = new boolean[list_url.size()];
        for (int i =0; i < list_url.size(); i ++) {
            indexes[i] = false;
        }

        LineOfThreads lineofthreads = new LineOfThreads(list_url.size());

        //initialization the array of threads
        for (int i = 0; i < list_url.size(); i ++) {

            //create new object of thread and passing arguments
            Runnable temp = new ReadURL(list_url.get(i),
                                        delay,
                                        retries,
                                        i,
                                        lineofthreads);


            //add to array list new thread
            array_threads.add(i, temp);
        }

       // execute the threads
        for (int i = 0; i < array_threads.size(); i++) {
            pool.execute(array_threads.get(i));

            if (i == array_threads.size() - 1) {
                pool.shutdown();
            }

        }

    }

}
