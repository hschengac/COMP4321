/* A test program which read data from the RocksDB and
   outputs a plain-text file named spider_result.txt
   
   Format:
	Page title
	URL
	Last modification data, size of page
	Keyword1 freq1; Keyword2 freq2; Keyword3 greq3;...
	Child Link1
	Child Link2...
*/
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

public class test {
  
  public static void main (String[] args) {
    // a static method that loads the RocksDB C++ library
    RocksDB.loadLibrary();

    // the Options class contains a set of configurable DB options
    // that determines the behaviour of the database.
    try (final Options options = new Options().setCreateIfMissing(true)) {
    
      // a factory method that returns a RocksDB instance
      try (final RocksDB db = RocksDB.open(options, "/home/vm01/project/db")) {
          System.out.println("Connected to Database!");
          String docId = new String(db.get("http://www.cse.ust.hk".getBytes()));
          String detail = new String(db.get(docId.getBytes()));
          
          ArrayList<Integer> list1 = new ArrayList<Integer>();
          for (int index = detail.indexOf("#"); index >=0; index = detail.indexOf("#", index + 1)) {
              list1.add(index);
          }        
                    
          for (int i = 0; i < 8; i+=2) {
            //System.out.print(detail.substring(list1.get(i), list1.get(i+1)));
            System.out.println(detail.substring(list1.get(i), list1.get(i+2)));
          }
          
          String childlink = new String (detail.substring(list1.get(9), detail.length()));
          ArrayList<Integer>list2 = new ArrayList<Integer>();
          for (int index = childlink.indexOf(","); index >=0; index = childlink.indexOf(",", index + 1)) {
            list2.add(index);
          }
          
          System.out.println("Childlink");
          System.out.println(childlink.substring(0, list2.get(0)+1));
          for (int i = 0; i < list2.size(); i++) {
            System.out.println(childlink.substring(list2.get(i)+1, list2.get(i+1)));
          }
	
    	}
      } catch (RocksDBException e) {
          e.printStackTrace ();
      }
  }
}