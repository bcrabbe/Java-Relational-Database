class Driver
{
    void run()
    {

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
      program.run();
   }
}
