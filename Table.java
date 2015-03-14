import java.util.*;

class Table
{
    String name;
    List<Attribute> tableHeading;// attribute names, types, constraints etc
    Set<Tuple> tableBody;// a set of tuples
    
    
    Table(String name) throws Exception
    {
        setNameIfValid(name);
        tableHeading = new ArrayList<Attribute>();//columns
        tableBody = new LinkedHashSet<Tuple>();//rows
    }

    Table(String name, List<String> fieldNames) throws Exception
    {
        setNameIfValid(name);
        tableHeading = new ArrayList<Attribute>();
        tableBody = new LinkedHashSet<Tuple>();
        for(String newField: fieldNames) {
            addAttribute(newField);
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
    
    public String toString()
    {
        String tableString = name + "||";
        try {
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
            if(a.name==attributeName) {
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
                throw new Exception("attribute named " + attributeName + " already exists in table");
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

    public List<String> getAttributeNames()
    {
        List<String> attributeNames = new ArrayList<String>();
        for(Attribute a: tableHeading) {
            attributeNames.add(a.name);
        }
        return attributeNames;
    }

    public void editAttributeName(String oldName, String newName) throws Exception
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
            if(field.name==name) {
                return field;
            }
        }
        throw new Exception("Could not get attribute. None named " + name + " in table.");
    }

    
    void addTuple(String... values) throws Exception
    {
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
        if(attributeExists(attributeName)) {
            Attribute attributeToDelete = getAttributeFromName(attributeName);
            try {
                for(Tuple row: tableBody) {
                    if(row.getAttributeValue(attributeName)==value) {
                        tableBody.remove(row);
                        return;
                    }
                }
            } catch(Exception e) {
                throw new Exception("deleteTuple() there are no tuples where " +
                                    attributeName + " has a value of " + value );
            }
        } else {
            throw new Exception("deleteTuple()  No attribute named " + attributeName +
                                " in table " + name);
        }
    }
    
    public int getColumnWidth(String attributeName) throws Exception
    {
        getAttributeFromName(attributeName);
        
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
        try {
            Table t = new Table("testTable");
            t.addAttribute("Attribute1");
            is( t.attributeExists("Attribute1"), true);
            is( t.getAttributeFromName("Attribute1"), t.tableHeading.iterator().next() );
            List<String> attributeNames = new ArrayList<String>(1);
            attributeNames.add("Attribute1");
            is(t.getAttributeNames(),attributeNames);
            t.addTuple("value1");
            is(t.countRows(),1);
            //t.deleteTuple("Attribute1","value1");
           // is(t.countRows(),0);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
        List<String> attributeNames2 = new ArrayList<String>(3);
        attributeNames2.add("Attribute1");
        attributeNames2.add("Attribute2");
        attributeNames2.add("Attribute3");
        try {
            Table t2 = new Table("testTable2",attributeNames2);
            is( t2.attributeExists("Attribute1"), true);
            is( t2.attributeExists("Attribute2"), true);
            is( t2.attributeExists("Attribute3"), true);
            is(t2.countColumns(),3);
            t2.addTuple("value1","value1","value1");
            t2.addTuple("value2","value2","value2");
            System.out.println(t2.getAttributeNames());
            System.out.print(t2.toString());
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new Error();
        }
    }


}
