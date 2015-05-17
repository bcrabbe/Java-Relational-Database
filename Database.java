import java.util.*;


class Database
{
    Set<Table> tables;

    Database()
    {
        tables = new LinkedHashSet<Table>();
    }
    
    Database(List<Table> tables) throws Exception
    {
        this();
        for(Table t: tables) {
            addTable(t);
        }
    }
    
    List<String> showTables()
    {
        List<String> tableNames = new ArrayList<String>();
        for(Table t: tables) {
            tableNames.add(t.name);
        }
        return tableNames;
    }
    
    boolean isNameClash(String name)
    {
        for(Table t: tables) {
            if(t.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    void addTable(Table t) throws Exception
    {
        if(isNameClash(t.name)) {
            throw new Exception("Table called " + t.name + " already exists in Database");
        } else {
            tables.add(t);
        }
    }
    
    String toSaveString()
    {
        String saveString = new String();
        for(Table t: tables) {
            saveString += t.toSaveString();
            saveString += "|";
        }
        return saveString;
    }
    
    void testLoadDB()
    {
        Driver.is(showTables().contains("testTable2"),true);
        Driver.is(showTables().contains("testTable3"),true);
        Driver.is(showTables().contains("testTable"),true);
    }
    
    void testLoadDbWithNameClash()
    {
        Driver.is(tables.size(),2);
        Driver.is(showTables().contains("testTable2"),true);
        Driver.is(showTables().contains("testTable"),true);
    }
    
    public static void main(String[] args)
    {
        try {
            Database db = FileHandler.loadDataBaseFromFile("./testDB.txt");
            db.testLoadDB();
            Database db2 = FileHandler.loadDataBaseFromFile("./testDBaddTable.txt");
            db2.testLoadDbWithNameClash();
        } catch(Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
