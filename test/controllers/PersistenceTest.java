package controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;

import models.Activity;
import models.Location;
import models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.Serializer;
import utils.XMLSerializer;
import controllers.PacemakerAPI;
import static models.Fixtures.users;
import static models.Fixtures.activities;
import static models.Fixtures.locations;

public class PersistenceTest
{
  PacemakerAPI pacemaker;
  
  void populate (PacemakerAPI pacemaker)
  {
    for (User user : users)
    {
      pacemaker.createUser(user.firstName, user.lastName, user.email, user.password);
    }
    User user1 = pacemaker.getUserByEmail(users[0].email);
    Activity activity = pacemaker.createActivity(user1.id, activities[0].type, activities[0].location, activities[0].distance, activities[0].start);
    pacemaker.createActivity(user1.id, activities[1].type, activities[1].location, activities[1].distance, activities[0].start);
    User user2 = pacemaker.getUserByEmail(users[1].email);
    pacemaker.createActivity(user2.id, activities[2].type, activities[2].location, activities[2].distance, activities[0].start);
    pacemaker.createActivity(user2.id, activities[3].type, activities[3].location, activities[3].distance, activities[0].start);
    
    for (Location location : locations)
    {
      pacemaker.addLocation(activity.id, location.latitude, location.longitude);
    }
  }
  
  void deleteFile(String fileName)
  {
    File datastore = new File ("testdatastore.xml");
    if (datastore.exists())
    {
      datastore.delete();
    }
  }
  
  @Test
  public void testPopulate()
  { 
    pacemaker = new PacemakerAPI(null);
    assertEquals(0, pacemaker.listUsers().size());
    populate (pacemaker);
    
    assertEquals(users.length, pacemaker.listUsers().size());
    assertEquals(2, pacemaker.getUserByEmail(users[1].email).activities.size());
    assertEquals(2, pacemaker.getUserByEmail(users[2].email).activities.size());   
    Long activityID = pacemaker.getUserByEmail(users[1].email).activities.keySet().iterator().next();
    assertEquals(locations.length, pacemaker.listActivty(activityID).route.size());   
  }

  
  @Test
  public void testXMLSerializer() throws Exception
  { 
    String datastoreFile = "testdatastore.xml";
    deleteFile (datastoreFile);
    
    Serializer serializer = new XMLSerializer(new File (datastoreFile));
    
    pacemaker = new PacemakerAPI(serializer); 
    populate(pacemaker);
    pacemaker.store();
    
    PacemakerAPI pacemaker2 =  new PacemakerAPI(serializer);
    pacemaker2.load();
    
    assertEquals (pacemaker.listUsers().size(), pacemaker2.listUsers().size());
    for (User user : pacemaker.listUsers())
    {
      Collection<User> users = pacemaker2.listUsers();
      System.out.println("User to search for:");
      System.out.println(user);
      System.out.println("Collection");
      System.out.println(users);
      assertTrue (users.contains(user));
    //  assertTrue (pacemaker2.getUsers().contains(user));
    }
    deleteFile ("testdatastore.xml");
  }
}