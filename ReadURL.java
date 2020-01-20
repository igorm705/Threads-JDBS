package hac;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;

/**
 * This is thread that works with specific URL. His purpose is to read URL and
 * check if it in SQl base. If yes,thread does anything.  If not, the thread checks if
 * URL contains images and puts in the base only URLs with images.
 * Output of thread: name of URL time of work in milliseconds (if thread did not
 * succes  to read an URL, it writes "timeout" and in case if URL wrong that writes
 * "failed".
 * The thread must get arguments: name of URL, delay between retries, amount retries,
 * number of URL in list of URL's, and object LineOfThreads that ordered all threads
 * according to this list.
 */

public class ReadURL implements Runnable{

    String str; //name of URL
    long delay;
    int retries;
    int index; //index number of the current thread
    LineOfThreads lineofthreads;
    URL url;

    //VERY IMPORTANT !!!
    //double constructor for passing arguments!!
    ReadURL(String s, long delay, int retries, int index, LineOfThreads lineofthreads) {str = s;
                                                this.delay = delay;
                                                this.retries = retries;
                                                this.index = index;
                                                this.lineofthreads = lineofthreads;}

    //----------------------------------------
    public void run(){
   keep_data(str);

     }

     //----------------------------------------
    public void insertThisThreadToList(double threadDuration) throws InterruptedException {


              synchronized (lineofthreads){
                  if (threadDuration == 0) {
                      lineofthreads.getready(index, str, System.currentTimeMillis());
                  }
                  else  {
                      lineofthreads.getready(index, str, threadDuration);
                  }
              }
    }
    //------------------------------------------------------------
    public URL return_url(String str) throws MalformedURLException {

            URL url = new URL(str);
            return url;
    }
    //-----------------------------------------
    //in case if URL dow not exist in Data base, check it
    public void check_url(String str, Statement statement) throws InterruptedException {


        while(retries !=0) {
            try {
                    try {url = return_url (str);}
                    catch (MalformedURLException em) {
                        insertThisThreadToList(-2);
                        retries=0;
                        break;
                    }

                    URLConnection con = url.openConnection();
                    String type = con.getContentType();


                    if (type.contains("image"))  {
                        String query_insert = "INSERT INTO images (date_added, url) VALUES (NULL, '" + str + "')";
                        statement.executeUpdate(query_insert);
                    }
                insertThisThreadToList(0);
                retries = 0;

            }


            catch (Exception e) {


                //another case: we continue to try to connect to URL
                retries--;
                //in case if we failed to connect to URL
                if(retries == 0) {
                    insertThisThreadToList(-1);
                    break;
                }

                try { sleep_function () ;}
                catch (InterruptedException ie) {}
            }
        }
    }

    //here the thread sleeps
    //------------------------------------------------------------------
    public void sleep_function () throws InterruptedException {
        Thread.sleep(delay);
    }

    //main operations with URLs
//------------------------------------------------------------------
    public void keep_data(String str) {

        // the url of the driver
        String url = "jdbc:mysql://localhost:3306/ex2?user=root&password=";
        Connection con = null; // The connection object to the database will be used to perform queries

        try {
            String odbcDriver = "com.mysql.jdbc.Driver"; // Load the Oracle driver
            Class.forName(odbcDriver);
        }

        catch (Exception e) {
            System.out.println("Failed to load the driver");
            return;
        }

        try {
            // Create the connection to the database
            con = DriverManager.getConnection(url);
            //a statement object from the connection will allow us to run queries
            Statement statement = con.createStatement();


            String query = "SELECT url FROM images WHERE EXISTS (SELECT url FROM images where url = '" + str + "')";
            ResultSet rs = statement.executeQuery(query);
            boolean found = false; //in case if the URL found in the Data Base

            while ( rs.next() ) {
                found = true;
                insertThisThreadToList(0);
            }

            //call function for processing current URL
            if (!found){
                check_url(str, statement);
            }


        } catch (Exception e) {
            // debuggin information
            System.out.println("Exception was thrown:\n");
            e.printStackTrace();
        }

//        // the finally clause insures us that we close after or without an exception
        finally {
            try {
                if (con != null) { con.close(); }
            } catch (SQLException e) {
                // debugging info
                e.printStackTrace();
            }
        }
    }


}