package models;

import static org.junit.Assert.*;
import org.junit.Test;

public class ActivityTest
{ 
  Activity test = new Activity ((long) 1,"walk", "fridge", 0.001, "2015-10-31T14:58:10.237Z");

  @Test
  public void testCreate()
  {
    assertEquals ("walk",          test.type);
    assertEquals ("fridge",        test.location);
    assertEquals (0.0001, 0.001,   test.distance);  
    assertEquals ("2015-10-31T14:58:10.237Z",   test.start); 
  }

  @Test //Not working correctly as DateTime is incorrect
  public void testToString()
  {
    assertEquals ("Activity{" + test.id + ", walk, fridge, 0.001, 2015-10-31T14:58:10.237Z, []}", test.toString());
  }
}