package controllers;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import models.Activity;
import models.User;
import utils.Serializer;
import utils.XMLSerializer_ISer;
import utils.XMLSerializer;
import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import com.google.common.base.Optional;

public class Main
{
  public PacemakerAPI paceApi;

  public Main() throws Exception
  {
    File  datastore = new File("datastore.xml");
    Serializer serializer = new XMLSerializer(datastore);
    
    paceApi = new PacemakerAPI(serializer);
    
    /* Disabled to use load command
    if (datastore.isFile())
    {
      paceApi.load();
    }
    */
  }
  
  @Command(description="Create a new User")
  public void createUser (@Param(name="first name") String firstName, @Param(name="last name") String lastName, 
                          @Param(name="email")      String email,     @Param(name="password")  String password)
  {
    paceApi.createUser(firstName, lastName, email, password);
  }

  //Get User by ID
  @Command(description="List a Users detail by ID")
  public void listUser (@Param(name="id") Long id)
  {
    User user = paceApi.listUser(id);
    System.out.println(user);
  }

  ///List User by searching for an email address 
  @Command(description="List a Users detail by Email")
  public void listUser (@Param(name="email") String email)
  {
    User user = paceApi.getUserByEmail(email);
    System.out.println(user);
  }
  
  //List activities
  @Command(description="Get an Activities by User ID")
  public void listActivities(@Param(name="user-id") Long id)
  {
	  
	//Collection<Activity> activities = paceApi.listActivities();
	User activity = paceApi.getActivityByUser(id);
    //User user = paceApi.listUser(id);
    //Activity activity = paceApi.listActivty(id);
    System.out.println(activity);
  }
  
  //List all users 
  @Command(description="List all users details")
  public void listUsers ()
  {
    Collection<User> users = paceApi.listUsers();
    
    //adds a new line between users
    Iterator newLine = users.iterator();
    while (newLine.hasNext())
      {
        String name = newLine.next().toString();

        System.out.println(name);
      } 
  }

  /*
   * Not needed
  @Command(description="Delete a User")
  public void deleteUser (@Param(name="email") String email)
  {
    Optional<User> user = Optional.fromNullable(paceApi.getUserByEmail(email));
    if (user.isPresent())
    {
      paceApi.deleteUser(user.get().id);
    }
  }
  */
  
  //Delete User by ID
  @Command(description="Delete a User")
  public void deleteUser (@Param(name="id") Long id)
  {
    Optional<User> user = Optional.fromNullable(paceApi.listUser(id));
    if (user.isPresent())
    {
      paceApi.deleteUser(user.get().id);
    }
  }
  
  @Command(description="Add an activity")
  public void addActivity (@Param(name="user-id")  Long   id,       @Param(name="type") String type, 
                           @Param(name="location") String location, @Param(name="distance") double distance)
  {
    Optional<User> user = Optional.fromNullable(paceApi.listUser(id));
    if (user.isPresent())
    {
      paceApi.createActivity(id, type, location, distance);
    }
  }

  @Command(description="Add Location to an activity")
  public void addLocation (@Param(name="activity-id")  Long  id,   
                           @Param(name="latitude")     float latitude, @Param(name="longitude") float longitude)
  {
    Optional<Activity> activity = Optional.fromNullable(paceApi.listActivty(id));
    if (activity.isPresent())
    {
      paceApi.addLocation(activity.get().id, latitude, longitude);
    }
  }
  
  @Command(description="Load File")
  public void load () throws Exception
  {
    paceApi.load();
  }

  @Command(description="Store User, Activity and Location Details")
  public void store () throws Exception
  {
	  paceApi.store();
  }

  public static void main(String[] args) throws Exception
  {
    Main main = new Main();

    Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions", main);
    shell.commandLoop();

    main.paceApi.store();
  }
}