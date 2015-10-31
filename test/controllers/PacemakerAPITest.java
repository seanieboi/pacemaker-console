package controllers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.PacemakerAPI;
import models.Activity;
import models.Location;
import models.User;

import static models.Fixtures.users;
import static models.Fixtures.activities;
import static models.Fixtures.locations;

public class PacemakerAPITest
{
  private PacemakerAPI pacemaker;

  @Before
  public void setup()
  {
    pacemaker = new PacemakerAPI(null);
    for (User user : users)
    {
      pacemaker.createUser(user.firstName, user.lastName, user.email, user.password);
    }
  }

  @After
  public void tearDown()
  {
    pacemaker = null;
  }

  @Test
  public void testUser()
  {
    assertEquals (users.length, pacemaker.listUsers().size());
    pacemaker.createUser("homer", "simpson", "homer@simpson.com", "secret");
    assertEquals (users.length+1, pacemaker.listUsers().size());
    assertEquals (users[1], pacemaker.getUserByEmail(users[1].email));
  }  

  @Test
  public void testUsers()
  {
    assertEquals (users.length, pacemaker.listUsers().size());
    for (User user: users)
    {
      User eachUser = pacemaker.getUserByEmail(user.email);
      assertEquals (user, eachUser);
      assertNotSame(user, eachUser);
    }
  }

  @Test
  public void testDeleteUsers()
  {
    assertEquals (users.length, pacemaker.listUsers().size());
    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    pacemaker.deleteUser(marge.id);
    assertEquals (users.length-1, pacemaker.listUsers().size());    
  }  
  
  @Test
  public void testAddActivity()
  {
    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    Activity activity = pacemaker.createActivity(marge.id, activities[1].type, activities[0].location, activities[0].distance, activities[0].start);
    Activity returnedActivity = pacemaker.listActivty(activity.id);
    assertEquals(activities[1],  returnedActivity);
    assertNotSame(activities[1], returnedActivity);
  }  
  
  @Test
  public void testAddActivityWithMultipleLocation()
  {
    User marge = pacemaker.getUserByEmail("marge@simpson.com");
    Long activityId = pacemaker.createActivity(marge.id, activities[0].type, activities[0].location, activities[0].distance, activities[0].start).id;

    for (Location location : locations)
    {
      pacemaker.addLocation(activityId, location.latitude, location.longitude);      
    }

    Activity activity = pacemaker.listActivty(activityId);
    assertEquals (locations.length, activity.route.size());
    int i = 0;
    for (Location location : activity.route)
    {
      assertEquals(location, locations[i]);
      i++;
    }
  } 
  
}