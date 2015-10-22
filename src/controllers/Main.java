package controllers;

import java.io.File;
import java.util.Collection;
import models.Activity;
import models.User;
import utils.Serializer;
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
    if (datastore.isFile())
    {
      paceApi.load();
    }
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
  ///
  
  @Command(description="List a Users detail by Email")
  public void listUser (@Param(name="email") String email)
  {
    User user = paceApi.getUserByEmail(email);
    System.out.println(user);
  }
  
  @Command(description="List all users details")
  public void listUsers ()
  {
    Collection<User> users = paceApi.listUsers();
    System.out.println(users);
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

  //List activities
  @Command(description="Get an Activities detail")
  public void listActivities(@Param(name="id") Long id)
  {
    Activity activity = paceApi.listActivty(id);
    System.out.println(activity);
  }
  
  public static void main(String[] args) throws Exception
  {
    Main main = new Main();

    Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions", main);
    shell.commandLoop();

    main.paceApi.store();
  }
}