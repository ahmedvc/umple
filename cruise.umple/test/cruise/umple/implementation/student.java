/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.16.0.2388 modeling language!*/



// line 1 "BasicConstraint1.ump"
public class student
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //student Attributes
  private int age;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public student(int aAge)
  {
    if ( !(aAge>18))
    { 
     throw new RuntimeException("Please provide a valid age"); 
    }
    age = aAge;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAge(int aAge)
  {
    boolean wasSet = false;
    if (aAge>18)
    {
    age = aAge;
    wasSet = true;
    }
    return wasSet;
  }

  public int getAge()
  {
    return age;
  }

  public void delete()
  {}


  public String toString()
  {
	  String outputString = "";
	  
    return super.toString() + "["+
            "age" + ":" + getAge()+ "]"
     + outputString;
  }
}