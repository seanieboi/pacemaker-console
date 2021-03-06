package controllers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import com.google.common.base.Optional;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import models.Activity;
import models.Location;
import models.User;
import utils.Serializer;

public class PacemakerAPI
{
  private Map<Long, User>     userIndex       = new HashMap<>();
  private Map<String, User>   emailIndex      = new HashMap<>();
  private Map<Long, Activity> activitiesIndex = new HashMap<>();
  
  private Serializer serializer;

  public PacemakerAPI(Serializer serializer)
  {
    this.serializer = serializer;
  }
  
  public Collection<User> listUsers()
  {
    return userIndex.values();
  }

  public Collection<Activity> listActivities(Long userId)
  {
      return activitiesIndex.values();
  }

  public void deleteUsers() 
  {
    userIndex.clear();
    emailIndex.clear();
  }

  public User createUser(String firstName, String lastName, String email, String password) 
  {
    User user = new User (firstName, lastName, email, password);
    user.id = (long) (userIndex.size() + 1);
    userIndex.put(user.id , user);
    emailIndex.put(email, user);
    return user;
  }

  public User getUserByEmail(String email) 
  {
    return emailIndex.get(email);
  }
  
  public Activity getActivityByUser(Long userId) 
  {
    return activitiesIndex.get(userId);
  }

  public User listUser(Long id) 
  {
    return userIndex.get(id);
  }

  public void deleteUser(Long id) 
  {
    User user = userIndex.remove(id);
    emailIndex.remove(user.email);
  }
  
  public Activity createActivity(Long id, String type, String location, double distance, String start)
  {
    Activity activity = null;
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent())
    {
      activity = new Activity (id, type, location, distance, start);
      activity.id = (long) (activitiesIndex.size()+1);      
      user.get().activities.put(activity.id, activity);
      activitiesIndex.put(activity.id, activity);
    }
    return activity;
  }

  public Activity listActivty(Long id)
  {
    Activity activity = null;
    Optional<User> user = Optional.fromNullable(userIndex.get(id));
    if (user.isPresent())
      {
          //activtyIndex
          user.get().activities.get(activity.userId);
          
      }
    return activity;
  }

  public void addLocation(Long id, float latitude, float longitude)
  {
    Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
    if (activity.isPresent())
    {
      activity.get().route.add(new Location(latitude, longitude));
    }
  }
  
  @SuppressWarnings("unchecked")
  public void load() throws Exception
  {
    serializer.read();
    activitiesIndex = (Map<Long, Activity>) serializer.pop();
    emailIndex      = (Map<String, User>)   serializer.pop();
    userIndex       = (Map<Long, User>)     serializer.pop();
  }

  public void store() throws Exception
  {
    serializer.push(userIndex);
    serializer.push(emailIndex);
    serializer.push(activitiesIndex);
    serializer.write(); 
  }
  
  @SuppressWarnings("unchecked")
  void load(File file) throws Exception
  {
    ObjectInputStream is = null;
    try
    {
      XStream xstream = new XStream(new DomDriver());
      is = xstream.createObjectInputStream(new FileReader(file));
      userIndex       = (Map<Long, User>)     is.readObject();
      emailIndex      = (Map<String, User>)   is.readObject();
      activitiesIndex = (Map<Long, Activity>) is.readObject();
    }
    finally
    {
      if (is != null)
      {
        is.close();
      }
    }
  }

  void store(File file) throws Exception
  {
    XStream xstream = new XStream(new DomDriver());
    ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(file));
    out.writeObject(userIndex);
    out.writeObject(emailIndex);
    out.writeObject(activitiesIndex);
    out.close(); 
  }
  

  public Activity createActivity2(Long userId, String type, String location,
    double distance, String start) {
      
    Activity activity = new Activity(userId, type, location, distance, start);
//      user.id = (long) (userIndex.size() + 1);
//      userIndex.put(user.id , user);
//      emailIndex.put(email, user);
      return activity;
}
}