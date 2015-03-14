import java.util.*;

class Table
{
    String name;
    List<Attribute> tableHeading;// attribute names, types, constraints etc
    Set<Tuple> tableBody;// a set of tuples
    
    Table(String name)
    {
        this.name = name;
        tableHeading = new ArrayList<Attribute>();//columns
        tableBody = new LinkedHashSet<Tuple>();//rows
    }

    Table(String name, List<String> fieldNames)
    {
        this.name = name;
        tableHeading = new ArrayList<Attribute>();
        tableBody = new LinkedHashSet<Tuple>();
        for(String newField: fieldNames) {
            try {
                addAttribute(newField);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public String toString()
    {
        String tableString = name + "||\n";
        try {
            List<String> attNames = getAttributeNames();
            for(String column: attNames) {
                tableString.concat(column);
                tableString.concat(" | ");
            }
            tableString.concat(" ||\n");
            for(Tuple row: tableBody) {
                for(String attName: attNames) {
                    tableString.concat(row.getAttributeValue(attName));
                    tableString.concat(" | ");
                }
                tableString.concat(" ||\n");
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
        if(attributeExists(attributeName)){
            throw new Exception("attribute " + attributeName + " already exists");
        }
        Attribute newAtt = new Attribute(attributeName);
        tableHeading.add(newAtt);
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
        if(attributeExists(oldName)) {
            Attribute editMyName = getAttributeFromName(oldName);
            editMyName.name = newName;
        }
        else {
            throw new Exception("Could not edit attribute name. No attribute named " +
                                oldName + " in table.");
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
    
    static void is(Object x, Object y)
    {
        System.out.println("testing " + x.toString() + " = " + y.toString() );

        if (x==y || (x != null && x.equals(y)) ) {
            System.out.println("pass");
            return;
        }
        System.out.println("fail");
    }

    public static void main(String[] args)
    {
        Table t = new Table("testTable");
        try {
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
        Table t2 = new Table("testTable2",attributeNames2);
        is( t2.attributeExists("Attribute1"), true);
        is( t2.attributeExists("Attribute2"), true);
        is( t2.attributeExists("Attribute3"), true);
        is(t2.countColumns(),3);
        try {
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
