package hac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**This class reads from getting file list of URLs and moves it back.
 *
 */

public class ReadFromFile {
    private List<String> urls_list = new ArrayList<>();

            public List<String> return_file (String FileName) {

                try {
                    URL path = ReadFromFile.class.getResource(FileName);
                    File f = new File(path.getFile());
                    BufferedReader reader = new BufferedReader(new FileReader(f));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        urls_list.add(line);
                    }
                    reader.close();
                    return urls_list;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read file");
           // System.err.format("Exception occurred trying to read '%s'.", FileName);
            e.printStackTrace();
            return null;
        }
    }
}
