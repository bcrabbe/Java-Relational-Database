class Driver
{
    void test()
    {
        Tuple row = new Tuple();
        row.testTuple();
        
        FileHandler fh = new FileHandler();
        fh.testSaveTable();
        fh.testLoadTableFromFile();
        fh.testExportTableAsTxt();
        
        try {
            Table t2 = FileHandler.loadTableFromFile("./test.txt");
            t2.testCreateTable();
            t2.testGetWidthOfString();
            t2.testSetPrimaryKey();
            t2.testUniquenessConstraint();
            
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            Database db = FileHandler.loadDataBaseFromFile("./testDB.txt");
            db.testLoadDB();
            Database db2 = FileHandler.loadDataBaseFromFile("./testDBaddTable.txt");
            db2.testLoadDbWithNameClash();
        } catch(Exception e) {
            is(e.getMessage(),"Table called testTable already exists in Database");
        }
    }

    static void is(Object x, Object y)
    {
        System.out.print("testing " + x.toString() + " = " + y.toString() );

        if (x==y || (x != null && x.equals(y)) ) {
            System.out.println("...pass");
            return;
        }
        System.out.println("...fail");
    }


   public static void main(String[] args)
   {
      Driver program = new Driver();
      program.test();
   }
}
