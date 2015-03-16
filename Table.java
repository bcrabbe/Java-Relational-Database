import java.util.*;

class Table
{
    String name;
    List<Attribute> tableHeading;// attribute names, types, constraints etc
    Set<Tuple> tableBody;// a set of tuples
    List<Attribute> primaryKey;

    Table(String tableName, String[] primaryKey, String[] tableHeadings, String[] tableRows) throws Exception
    {
        this(tableName, tableHeadings);
        try {
            for(int i=3; i<tableRows.length; ++i) {
                addTuple(tableRows[i].split(FileHandler.columnDelim));
            }
            setPrimaryKey(primaryKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("Could not load table");
        }
    }

    Table(String name, String[] attributeNames) throws Exception
    {
        try {
            setNameIfValid(name);
            tableHeading = new ArrayList<Attribute>();
            tableBody = new LinkedHashSet<Tuple>();
            for(String attributeName: attributeNames) {
                addAttribute(attributeName);
            }
            primaryKey = new ArrayList<Attribute>();
            setPrimaryKey(attributeNames);
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void setNameIfValid(String name) throws Exception
    {
        if(isValidName(name)) {
            this.name = name;
        }
    }
    
    static boolean isValidName(String name) throws Exception
    {
        if( name.contains(FileHandler.columnDelim) ||
           name.contains(FileHandler.newLineDelim) ) {
            throw new Exception(name + " is invalid. names cannot contain any of: " +
                                FileHandler.columnDelim + FileHandler.newLineDelim);
        }
        else return true;
    }
    
    void setPrimaryKey(String... attributeNames) throws Exception
    {
        if( countColumns()==0 ) {
            throw new Exception("There are no attributes.");
        }
        if( countRows()>0 )  {
            if( !isAttributeSetUnique(attributeNames) ) {
                throw new Exception("Suggested PK is not unique. Cannot set.");
            }
        }
        primaryKey.clear();
        for(String attributeName: attributeNames) {
            primaryKey.add(getAttributeFromName(attributeName));
        }
    }
    
    private boolean isAttributeSetUnique(String... attributeNames) throws Exception
    {
        List<String> allValues = getAllValuesOfAttribute(attributeNames);
        Set<String> allValuesSet = new HashSet<String>(allValues);
        if( allValuesSet.size() < allValues.size() ) return false;
        else return true;
    }

    String toSaveString()
    {
        String tableString = name + "||";
        try {
            for(Attribute key: primaryKey) {
                tableString = tableString.concat(key.name);
                tableString = tableString.concat("|");
            }
            tableString = tableString.concat("|");
            List<String> attNames = getAttributeNames();
            for(String column: attNames) {
                tableString = tableString.concat(column);
                tableString = tableString.concat("|");
            }
            tableString = tableString.concat("|");
            for(Tuple row: tableBody) {
                for(String attName: attNames) {
                    tableString = tableString.concat(row.getAttributeValue(attName));
                    tableString = tableString.concat("|");
                }
                tableString =  tableString.concat("|");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
        return tableString;
    }
    
    boolean attributeExists(String attributeName)
    {
        for(Attribute a: tableHeading) {
            if(a.name.equals(attributeName)) {
                return true;
            }
        }
        return false;
    }
    
    void addAttribute(String attributeName) throws Exception
    {
        if(isValidAttribute(attributeName)) {
            Attribute newAtt = new Attribute(attributeName);
            tableHeading.add(newAtt);
        }
    }
    
    private boolean isValidAttribute(String attributeName) throws Exception
    {
        if(isValidName(attributeName)) {
            if(attributeExists(attributeName)) {
                throw new Exception("attribute named " + attributeName +
                                    " already exists in table");
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    int countColumns()
    {
       return tableHeading.size();
    }
    
    int countRows()
    {
        return tableBody.size();
    }

    List<String> getAttributeNames()
    {
        List<String> attributeNames = new ArrayList<String>();
        for(Attribute a: tableHeading) {
            attributeNames.add(a.name);
        }
        return attributeNames;
    }

    void editAttributeName(String oldName, String newName) throws Exception
    {
        if(isValidName(newName)) {
            if(attributeExists(oldName)) {
                Attribute editMyName = getAttributeFromName(oldName);
                editMyName.name = newName;
            }
            else {
                throw new Exception("Could not edit attribute name. No attribute named " +
                                    oldName + " in table.");
            }
        }
       
    }

    private Attribute getAttributeFromName(String name) throws Exception
    {
        for(Attribute field: tableHeading) {
            if(field.name.equals(name)) {
                return field;
            }
        }
        throw new Exception("Could not get attribute. None named " + name + " in table.");
    }
    
    private boolean doesTupleExist(String... values)
    {
        List<String> newTupleValues = Arrays.asList(values);
        for(Tuple row: tableBody) {
            List<String> rowValues = row.getAllValuesOfTuple();
            if(rowValues.size() == newTupleValues.size() && rowValues.containsAll(newTupleValues)) {
                return true;
            }
        }
        return false;
    }
    
    void addTuple(String... values) throws Exception
    {
        if(doesTupleExist(values)) {
            throw new Exception("Duplicate tuple discarded");
        }
        if(values.length != tableHeading.size()) {
            throw new Exception("addTuple() for Table " + name + " expects " +
                                    tableHeading.size() + " data values. It received " +
                                    values.length);
        }
        else {
            Tuple newTuple = new Tuple(tableHeading, Arrays.asList(values));
            tableBody.add(newTuple);
        }
    }
    
    void deleteTuple(String attributeName, String value) throws Exception
    {
        tableBody.remove(getTupleFromSelect(attributeName,value));
    }
    
    private Tuple getTupleFromSelect(String selectAttributeName, String selectValue) throws Exception
    {
        Attribute selectedAttribute = getAttributeFromName(selectAttributeName);
        for(Tuple row: tableBody) {
            if(row.getAttributeValue(selectedAttribute.name)==selectValue) {
                return row;
            }
        }
        throw new Exception("There are no tuples where " +
                                selectAttributeName + " has a value of " + selectValue );
    }
    

    
    private List<String> getAllValuesOfAttribute(String... attributeNames) throws Exception
    {
        List<String> allValues = new ArrayList<String>();
        for(Tuple row: tableBody) {
            String combinedValue = "";
            for(String attributeName: attributeNames) {
                combinedValue += row.getAttributeValue(attributeName);
            }
            allValues.add(combinedValue);
        }
        return allValues;
    }
    
    
    
    // printing functions
    
    private int spacesBetweenCols = 4;
    List<String> presentTableForPrinting()
    {
        List<String> presentedTable = new ArrayList<String>();
        List<String> headings = getAttributeNames();
        List<Integer> headingWidths = getColumnWidths(headings);
        
        presentedTable.add(name);
        presentedTable.add("\n\n");
        
        presentedTable.add(presentTableRow(headings, headingWidths));
        presentedTable.add( makeStringOfChar( getTableWidth(headingWidths), '-') + "\n" );
        
        for(Tuple row: tableBody) {
            List<String> rowString = row.toListOfStrings();
            presentedTable.add(presentTableRow(rowString, headingWidths));
        }
        return presentedTable;
    }
    
    private int getTableWidth(List<Integer> headingWidths)
    {
        int sum = 0;
        for(int headingWidth : headingWidths) {
            sum += headingWidth;
        }
        sum +=spacesBetweenCols*countColumns();
        return sum;
    }
    
    private String presentTableRow(List<String> rowStrings, List<Integer> headingWidths)
    {
        String presentedRow = "";
        for(int i=0; i<rowStrings.size(); ++i)
        {
            int strWidth = getWidthOfString(rowStrings.get(i));
            int spacesNeeded = headingWidths.get(i) - strWidth;
            presentedRow = presentedRow.concat(rowStrings.get(i));
            presentedRow = presentedRow.concat(makeStringOfChar(spacesNeeded+spacesBetweenCols, ' '));
        }
        presentedRow += '\n';
        return presentedRow;
    }
    
    private String makeStringOfChar(int number, char c)
    {
        String s = "";
        for(int i=0; i<number; ++i)
        {
            s = s + c;
        }
        return s;
    }
    
    private List<Integer> getColumnWidths(List<String> headings)
    {
        List<Integer> headingWidths = new ArrayList<Integer>();
        for(String heading: headings) {
            headingWidths.add(getWidthOfString(heading));
        }
        return headingWidths;
    }
    
    private int getColumnWidth(String attributeName) throws Exception
    {
        Attribute a = getAttributeFromName(attributeName);
        int maxwidth = getWidthOfString(attributeName);
        for(Tuple row: tableBody) {
            String valueString = row.getAttributeValue(attributeName);
            int width = getWidthOfString(valueString);
            maxwidth = width > maxwidth ? width : maxwidth;
        }
        return maxwidth;
    }

    static int getWidthOfString(String str)
    {
        int width=0, maxwidth=0;
        for( char c: str.toCharArray() ) {
            if(c=='\n' || c=='\r') {
                maxwidth = width > maxwidth ? width : maxwidth;
                width = 0;
            }
            else {
                ++width;
            }
        }
        maxwidth = width > maxwidth ? width : maxwidth;
        return maxwidth;
    }
    
    void testGetWidthOfString()
    {
        is(getWidthOfString("123456"),6);
        is(getWidthOfString("123\n123456\n123"),6);
        is(getWidthOfString("123\n\r123456\n\r123456"),6);
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
    
    void viewTable()
    {
        List<String> table = presentTableForPrinting();
        for(String s: table) {
            System.out.print(s);
        }
    }
    
    void testCreateTable()
    {
        System.out.println("testing testCreateTable");
        try {
            viewTable();
            System.out.print(getAttributeNames());
            is( attributeExists("Attribute1"), true);
            is( attributeExists("Attribute2"), true);
            is( attributeExists("Attribute3"), true);
            is( countColumns(), 3);
            is(toSaveString(),
               "testTable2||Attribute1|Attribute2|Attribute3||Attribute1|Attribute2|Attribute3||value1|value1|value1||value2|value2|value2||");
            addTuple("value3","value3","value3");
            addTuple("value4","value4","value4");
            is(countRows(),4);
            is(primaryKey.containsAll(tableHeading),true );
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }
    
    void testSetPrimaryKey()
    {   try {
            System.out.println("testing testSetPrimaryKey");
            setPrimaryKey("Attribute1");
            is(primaryKey.get(0), getAttributeFromName("Attribute1"));
            setPrimaryKey("Attribute2");
            is(primaryKey.size(),1);
            is(primaryKey.get(0), getAttributeFromName("Attribute2"));
            List<Attribute> compoundPK = new ArrayList<Attribute>();
            compoundPK.add(getAttributeFromName("Attribute2"));
            compoundPK.add(getAttributeFromName("Attribute3"));
            setPrimaryKey("Attribute2","Attribute3");
            is(primaryKey.containsAll(compoundPK), true);
            addTuple("value2","value5","value5");
            addTuple("value1","value6","value6");
            setPrimaryKey("Attribute1");
        } catch(Exception e) {
            is(e.getMessage(),"Suggested PK is not unique. Cannot set.");
            System.out.println(e.getMessage());
        }
    }
    
    void testUniquenessConstraint()
    {
        System.out.println("testing testUniquenessConstraint");
        viewTable();
        int countRowsB4 = countRows();
        try {
            addTuple("value3","value3","value3");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        is(countRows(),countRowsB4);

    }
    
    public static void main(String[] args)
    {
        try {
            Table t2 = FileHandler.loadTableFromFile("./saves/test.txt");
            t2.testCreateTable();
            t2.testGetWidthOfString();
            t2.testSetPrimaryKey();
            t2.testUniquenessConstraint();

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }


}


/*
 if( isAttributeSetNotNull(AttributeName) ) {
 } else {
 throw new Exception(attributeName + " has null values. Cannot set PK.");
 }
 private boolean isAttributeSetNotNull(String... attributeNames) throws Exception
 {
 
 for(String attributeName: attributeNames) {
 List<String> allValues = getAllValuesOfAttribute(attributeName);
 for(String val: allValues) {
 if( val.size() == 0 || val.toLowerCase() == "null") {
 return false;
 }
 }
 return true;
 }
 return true;
 }
 
 private boolean isAttributeNotNull(String attributeName) throws Exception
 {
 List<String> allValues = getAllValuesOfAttribute(attributeName);
 for(String val: allValues) {
 if( val.size() == 0 || val.toLowerCase() == "null") {
 return false;
 }
 }
 return true;
 }*/