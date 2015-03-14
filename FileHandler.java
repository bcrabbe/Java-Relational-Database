
//import java.util.*;
//import java.nio.file;
//import java.nio.charset.StandardCharsets;

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
    public enum Format
    {
        TXT, TEX
    }
    
    final static private String encoding = "UTF-8";
    final static String newLineDelim = Pattern.quote("||");
    final static String columnDelim = Pattern.quote("|");
    
    FileHandler()
    {
        
    }
    
    public exportTable(Table t, Format ext, String fpath)
    {
        if(ext==TXT) {
            exportTableAsTxt(t, fpath);
        }
        else if(ext==TEX)
        {
            
        }
    }
    
    private void exportTableAsTxt(Table t, String fpath)
    {
        
    }
    
    public void saveTableToFile(Table t)
    {
        stringToFile(t.toString());
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
    
    static private List<String> trimWhiteSpace(String[] oldStrings)
    {
        List<String> newStrings = new ArrayList<String>();
        for(String s: oldStrings) {
            newStrings.add(s.trim());
        }
        return newStrings;
    }
    
    Table loadTableFromFile(String fpath)
    {
        try {
            String tableString = new String(readFile(fpath));
            String[] tableRowStrings = tableString.split(newLineDelim);
            String[] tableHeadings = tableRowStrings[1].split(columnDelim);
           // List<String> tableHeadingsTrimed = trimWhiteSpace(tableHeadings);
            
            Table newTable = new Table(tableRowStrings[0],//[0] has table name
                                       Arrays.asList(tableHeadings));
            for(int i=2; i<tableRowStrings.length; ++i) {
                newTable.addTuple(tableRowStrings[i].split(columnDelim));
            }
            return newTable;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }
    
    static String readFile(String fpath) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(fpath));
        return new String(encoded, encoding);
    }
    
    void testReadFile()
    {
        testSaveTable();
        try {
            String file = readFile("./saves/test.txt");
            System.out.println(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }
    
    void testSaveTable()
    {
        List<String> attributeNames2 = new ArrayList<String>(3);
        attributeNames2.add("Attribute1");
        attributeNames2.add("Attribute2");
        attributeNames2.add("Attribute3");
        try {
            Table t2 = new Table("testTable2",attributeNames2);
            t2.addTuple("value1","value1","value1");
            t2.addTuple("value2","value2","value2");
            String tableString = t2.toString();
            saveTableToFile(tableString,"./saves/test.txt");
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }
    
    void testLoadTableFromFile()
    {
        try {
            testSaveTable();//make sure we have a file there
            Table testT = loadTableFromFile("./saves/test.txt");
            String stringInFile = readFile("./saves/test.txt");
            is(testT.toString(),stringInFile);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }

    static void is(Object x, Object y)
    {
        System.out.print("testing " + x.toString() + " = " + y.toString() );
        
        if (x==y || (x != null && x.equals(y)) ) {
            System.out.println("...pass");
            return;
        }
        System.out.print("...fail");
    }
    
    public static void main(String[] args)
    {
        FileHandler fh = new FileHandler();
        fh.testSaveTable();
        fh.testReadFile();
        fh.testLoadTableFromFile();

    }
}