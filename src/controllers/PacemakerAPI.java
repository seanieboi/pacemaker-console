package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.User;

public class PacemakerAPI
{
  //private List <User> users = new ArrayList<User>();

  private Map<String, User> users = new HashMap<String, User>();
  
  public Collection<User> getUsers ()
  {
    return users.values();
  }

  public  void deleteUsers() 
  {
    users.clear();
  }

  public User createUser(String firstName, String lastName, String email, String password) 
  {
    User user = new User (firstName, lastName, email, password);
    users.put(email, user);
    return user;
  }

  public User getUser(String email) 
  {
    return users.get(email);
  }

  public void deleteUser(String email) 
  {
    users.remove(email);
  }
}