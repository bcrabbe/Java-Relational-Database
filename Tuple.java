import java.util.*;


class Tuple
{
   List<AttributeValue> attributes;

   class AttributeValue
   {
        String value;
        Attribute type;

        AttributeValue(Attribute attributeType, String value)
        {
           this.value = value;
           this.type = attributeType;
        }
    }

    Tuple()
    {
        attributes = new ArrayList<AttributeValue>();
    }

    Tuple( List<Attribute> tableHeading, List<String> values)
    {
        attributes = new ArrayList<AttributeValue>();
        for(int i=0; i<values.size(); ++i) {
            AttributeValue newAttribute = new AttributeValue(tableHeading.get(i), values.get(i));
            attributes.add(newAttribute);
        }
    }
    
//    String getAllValuesOfTuple()
//    {
//        String allValuesOfTuple = new String();
//        for(AttributeValue av: attributes) {
//            allValuesOfTuple += av.value;
//        }
//        return allValuesOfTuple;
//    }
    List<String> getAllValuesOfTuple()
    {
        List<String> allValuesOfTuple = new ArrayList<String>();
        for(AttributeValue av: attributes) {
            allValuesOfTuple.add(av.value);
        }
        return allValuesOfTuple;
    }
    
    private AttributeValue getAttributeValueObjFromName(String attributeName) throws Exception
    {
        for(AttributeValue attribute: attributes) {
            if(attribute.type.name==attributeName) {
                return attribute;
            }
        }
        throw new Exception("No attribute named " + attributeName + " in tuple." );
    }
    
    boolean attributeExists(String name)
    {
        for(AttributeValue attribute: attributes) {
            if(attribute.type.name==name) {
                return true;
            }
        }
        return false;
    }

    String getAttributeValue(String attributeName) throws Exception
    {
        AttributeValue attribute = getAttributeValueObjFromName(attributeName);
        return attribute.value;
    }


    void setAttributeValue(String attributeName, String attributeValue) throws Exception
    {
        AttributeValue attribute =  getAttributeValueObjFromName(attributeName);
        attribute.value=attributeValue;
    }

    int countAttributes()
    {
        return attributes.size();
    }

    void addAttribute(Attribute attributeType, String value) throws Exception
    {
        if(attributeExists(attributeType.name)) {
            throw new Exception("addAttribute() attribute named " + attributeType.name +
                                " already exists in tuple.");
        }
        AttributeValue newAttribute = new AttributeValue(attributeType,value);
        attributes.add(newAttribute);
    }
    
    List<String> toListOfStrings()
    {
        List<String> rowString = new ArrayList<String>();
        for(AttributeValue av: attributes) {
            rowString.add(av.value);
        }
        return rowString;
    }
    
    static void is(Object x, Object y)
    {
        if (x==y || (x != null && x.equals(y)) ) {
            System.out.println("pass");
            return;
        }
        System.out.println("fail");
        throw new Error("Error:\n" + x + "\n" + y);
    }

    public static void main(String[] args)
    {
        Tuple row = new Tuple();
        row.testTuple();
    }

    void testTuple()
    {
        try {
            Attribute newAttribute = new Attribute("test");
            addAttribute(newAttribute,"value1");
            is(1,countAttributes());
            is("value1",getAttributeValue("test"));
            setAttributeValue("test", "value2");
            is("value2",getAttributeValue("test"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
