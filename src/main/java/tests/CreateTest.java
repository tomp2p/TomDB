package tests;

import uzh.tomdb.api.Connection;
import uzh.tomdb.api.Statement;
import uzh.tomdb.main.TomDB;


public class CreateTest {
    
    public static void main(String[] args) {
        test();
    }
    
    public static void test() {
      
                    TomDB.createLocalPeers(100);
                    Connection con = TomDB.getConnection();
                    Statement stmt = con.createStatement();
                    
                    long now = System.currentTimeMillis();

                    stmt.execute("CREATE TABLE tabone (id, nameone, address) OPTIONS (uniqueindex:id, blocksize:2, storage:fullblocks)");
                    
//                    stmt.execute("CREATE TABLE tabtwo (idtwo, nametwo, salary) OPTIONS (uniqueindex:idtwo, blocksize:20, storage:insertionorder)").start();
                    
                    stmt.start();
                    
                    System.out.println("CREATE TABLE done in " + (System.currentTimeMillis() - now));

    }

}
