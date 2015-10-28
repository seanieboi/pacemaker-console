package models;

import static org.junit.Assert.*;
import org.junit.Test;

public class ActivityTest
{ 
  Activity test = new Activity ("walk", "fridge", 0.001, "12:10:2013 9:00:00");

  @Test
  public void testCreate()
  {
    assertEquals ("walk",          test.type);
    assertEquals ("fridge",        test.location);
    assertEquals (0.0001, 0.001,   test.distance);  
    assertEquals ("12:10:2013 9:00:00",   test.start); 
  }

  @Test
  public void testToString()
  {
    assertEquals ("Activity{" + test.id + ", walk, fridge, 0.001, 12:10:2013 9:00:00}", test.toString());
  }
}