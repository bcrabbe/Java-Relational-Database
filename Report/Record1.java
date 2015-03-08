import java.util.*;

class Record
{
   public class RecordException extends Exception
   {
      static final long serialVersionUID = 42L;
      public RecordException()
      {
         super();
      }

      public RecordException(String message)
      {
         super(message);
      }

      public RecordException(String message, Throwable cause)
      {
        super(message, cause);
      }

      public RecordException(Throwable cause)
      {
         super(cause);
      }
   }

   class Attribute
   {
      String name;
      String value;

      Attribute(String nameInput, String valueInput)
      {
         name = nameInput;
         value = valueInput;
      }
   }

    Set<Attribute> data;

    Record() {
      data = new LinkedHashSet<Attribute>();
    }

   void addField(String fieldName, String fieldValue) throws RecordException
   {
      for(Attribute field: data) {
         if(field.name==fieldName) {
            throw new RecordException("There already exists a field named " + fieldName + "in record.");
         }
      }
      Attribute newField = new Attribute(fieldName,fieldValue);
      this.data.add(newField);
   }

   String getFieldValue(String fieldName) throws RecordException
   {
      for(Attribute field: data) {
         if(field.name==fieldName) {
            return field.value;
         }
      }
      throw new RecordException("No field named " + fieldName + " in record.");
   }

   void setFieldValue(String fieldName, String fieldValue) throws RecordException
   {
      for(Attribute field: data) {
         if(field.name==fieldName) {
            field.value = fieldValue;
            return;
         }
      }
      throw new RecordException("No field named " + fieldName+ " in record.");
   }

   int countFields()
   {
      return data.size();
   }

   public static void main(String[] args)
   {
      Record row = new Record();
      row.testRecord();
   }

   void testRecord()
   {
      try {
         this.addField("Field1","value1");
         this.addField("Field2","value2");
         this.setFieldValue("Field2","value2.2");
         System.out.println(this.getFieldValue("Field1"));
         System.out.println(this.getFieldValue("Field2"));
         System.out.println( this.countFields() );
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
}
