

import static java.nio.file.StandardOpenOption.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;
import java.lang.Object;
import java.util.regex.Pattern;

class FileHandler
{
    final static private String encoding = "UTF-8";
    final static String newTableDelim = Pattern.quote("|||");
    final static String newLineDelim = Pattern.quote("||");
    final static String columnDelim = Pattern.quote("|");
    
    FileHandler()
    {
        
    }
    
    void exportTableAsTxt(Table t, String fpath)
    {
        List<String> rowStrings = t.presentTableForPrinting();
        stringListToFile(rowStrings, fpath);
    }
    
    private void stringListToFile(List<String> strList, String fpath)
    {
        String combined = "";
        for(String str: strList) {
            combined = combined.concat(str);
        }
        stringToFile(combined,fpath);
    }
    
    void saveTableToFile(Table t, String fpath)
    {
        stringToFile( t.toSaveString(), fpath);
    }
    
    void saveDatabaseToFile(Database d, String fpath)
    {
        stringToFile( d.toSaveString(), fpath);
    }
    
    private void stringToFile(String s, String fpath)
    {
        Writer writer = null;
        try {
            writer = new BufferedWriter(
                        new OutputStreamWriter(
                            new FileOutputStream(fpath), encoding));
            writer.write(s);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    static Database loadDataBaseFromFile(String fpath)
    {
        try {
            String dbString = new String(readFile(fpath));
            String[] tableStrings = dbString.split(newTableDelim);
            List<Table> tables =  new ArrayList<Table>();
            for(String tableString: tableStrings) {
                tables.add(loadTableFromString(tableString));
            }
            Database db = new Database(tables);
            return db;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }
    
    static private List<String> trimWhiteSpace(String[] oldStrings)
    {
        List<String> newStrings = new ArrayList<String>();
        for(String s: oldStrings) {
            newStrings.add(s.trim());
        }
        return newStrings;
    }
                          
    static Table loadTableFromString(String tableString) throws Exception
    {
        try {
            String[] tableRowStrings = tableString.split(newLineDelim);
            String[] tableHeadings = tableRowStrings[2].split(columnDelim);
            String[] primaryKey = tableRowStrings[1].split(columnDelim);
            String tableName = tableRowStrings[0];
            Table newTable = new Table(tableName, primaryKey, tableHeadings,
                                       tableRowStrings);
            return newTable;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("could not load table");
        }
    }
                          
    static Table loadTableFromFile(String fpath) throws Exception
    {
        String tableString = new String(readFile(fpath));
        return loadTableFromString(tableString);
    }
    
    static String readFile(String fpath) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(fpath));
        return new String(encoded, encoding);
    }
    
    
    void testSaveTable()
    {
        System.out.println("Testing : testSaveTable()\n ");

        String[] attributeNames2 = new String[]{"Attribute1","Attribute2","Attribute3"};

        try {
            Table t2 = new Table("testTable2",attributeNames2);
            t2.addTuple("value1","value1","value1");
            t2.addTuple("value2","value2","value2");
            String tableString = t2.toSaveString();
            saveTableToFile(t2,"./saves/test.txt");
            is(readFile("./saves/test.txt"), "testTable2||Attribute1|Attribute2|Attribute3||Attribute1|Attribute2|Attribute3||value1|value1|value1||value2|value2|value2||");
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }
    
    void testLoadTableFromFile()
    {
        System.out.println("Testing : testLoadTableFromFile()\n ");

        try {
            Table testT = loadTableFromFile("./saves/test.txt");
            String stringInFile = readFile("./saves/test.txt");
            is(testT.toSaveString(),stringInFile);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }

    void testExportTableAsTxt()
    {
        System.out.println("Testing : testExportTableAsTxt() ");

        String[] attributeNames = new String[]{"Attribute1","Att2","A3"};
        try {
            Table t2 = new Table("testTable2",attributeNames);
            t2.addTuple("value1","val1","v1");
            t2.addTuple("value2","val2","v2");
            exportTableAsTxt(t2, "./exports/test.txt");
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }
    
    static void is(Object x, Object y)
    {
        System.out.print("testing " + x.toString() + " = " + y.toString() );
        
        if (x==y || (x != null && x.equals(y)) ) {
            System.out.println("...pass\n");
            return;
        }
        System.out.print("...fail\n");
    }
    
    public static void main(String[] args)
    {
        FileHandler fh = new FileHandler();
        fh.testSaveTable();
        fh.testLoadTableFromFile();
        fh.testExportTableAsTxt();

    }
}