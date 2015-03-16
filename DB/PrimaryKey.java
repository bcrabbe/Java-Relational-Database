import java.util.*;


class PrimaryKey
{
    Table table
    Attribute key;
    
    PrimaryKey(Table table, Attribute key) throws Exception
    {
        this.table = table;
        if(isValidPK(key))
        this.key = key;
    }
    
    boolean isValidPK()
    {
        
    }
    
   
    
}

