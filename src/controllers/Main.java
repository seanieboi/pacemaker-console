package controllers;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateTime;

import models.Activity;
import models.User;
import utils.Serializer;
import utils.XMLSerializer_ISer;
import utils.XMLSerializer;
import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;

import com.bethecoder.ascii_table.ASCIITable;
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
  
  //Add a new user
  @Command(description="Create a new User")
  public void createUser (@Param(name="first name") String firstName, @Param(name="last name") String lastName, 
                          @Param(name="email")      String email,     @Param(name="password")  String password)
  {
    User user = paceApi.createUser(firstName, lastName, email, password);
    
    listUser(user.id);
  }

  //Get User by ID
  @Command(description="List a Users detail by ID", name="List-user", abbrev="lius")
  public void listUser (@Param(name="id") Long id)
  {
    User user = paceApi.listUser(id);
    String[] header = { "ID", "FIRSTNAME", "LASTNAME", "EMAIL", "PASSWORD" };
    String [][]data = new String[1][header.length];
    
    data[0][0] = user.id.toString();
    data[0][1] = user.firstName;
    data[0][2] = user.lastName;
    data[0][3] = user.email;
    data[0][4] = user.password;
       
    System.out.println("ok");

    ASCIITable.getInstance().printTable(header, data);

  }

  ///List User by searching for an email address 
  @Command(description="List a Users detail by Email")
  public void listUser (@Param(name="email") String email)
  {
    User user = paceApi.getUserByEmail(email);

    String[] header = { "ID", "FIRSTNAME", "LASTNAME", "EMAIL", "PASSWORD" };
    String [][]data = new String[1][header.length];
    
    data[0][0] = user.id.toString();
    data[0][1] = user.firstName;
    data[0][2] = user.lastName;
    data[0][3] = user.email;
    data[0][4] = user.password;
       
    ASCIITable.getInstance().printTable(header, data);
  }
  
  //List activities by User ID
  @Command(description="Get an Activities by User ID")
  public void listActivities(@Param(name="user-id") Long id)
  {
    
    Collection<Activity> activities = paceApi.listActivities(id);
    //Activity activity = paceApi.getActivityByUser(id);
    
    //System.out.println(activities);
    
    String[] header = { "ID", "TYPE", "LOCATION", "DISTANCE", "STARTTIME", "ROUTE" };
    String [][]data = new String[activities.size()][header.length];
    
    int row = 0;
    
    for(Activity activity1:activities)
    {
      if(activity1.userId == id){
        data[row][0] = activity1.id.toString();
        data[row][1] = activity1.type.toString();
        data[row][2] = activity1.location.toString();
        data[row][3] = String.valueOf(activity1.distance);
        data[row][4] = activity1.startTime.toString();
        data[row][5] = activity1.route.toString();
        
      }
      row++;
    }
    
    System.out.println("ok");
    
    ASCIITable.getInstance().printTable(header, data);
    }
  
  //List all users
  @Command(description="List all users")
  public void listUsers()
  {
    Collection<User> users = paceApi.listUsers();
          
    String [] header = { "ID", "First Name", "Last Name", "Email", "Password"};
    
    String [][]data = new String[users.size()][header.length];
    
    int row = 0;
    
    for(User user:users)
    {
    data[row][0] = user.id.toString();
    data[row][1] = user.firstName.toString();
    data[row][2] = user.lastName.toString();
    data[row][3] = user.email.toString();
    data[row][4] = user.password.toString();
    
    row++;
    }
    
    System.out.println("ok");
    
    ASCIITable.getInstance().printTable(header, data);
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
  
  //Add activity for User
  @Command(description="Create a new Activity")
  public void addActivity (@Param(name="user-id")  Long   id,       @Param(name="type") String type, 
          @Param(name="location") String location, @Param(name="distance") double distance,
          @Param(name="start-time") String start)
  {
    Optional<User> user = Optional.fromNullable(paceApi.listUser(id));
    if (user.isPresent())
    {
      Activity activity = paceApi.createActivity(id, type, location, distance, start);
    
      System.out.println("ok");
    }
    //listUser(user.id);
  }

  //Add location to Activity
  @Command(description="Add Location to an activity")
  public void addLocation (@Param(name="activity-id")  Long  id,   
                           @Param(name="latitude")     float latitude, @Param(name="longitude") float longitude)
  {
    Optional<Activity> activity = Optional.fromNullable(paceApi.listActivty(id));
    if (activity.isPresent())
    {
      paceApi.addLocation(activity.get().id, latitude, longitude);
      System.out.println("ok");
    }
  }

  //Load stored data from xml 
  @Command(description="Load File")
  public void load () throws Exception
  {
    paceApi.load();
  }

  //write data to xml store
  @Command(description="Store User, Activity and Location Details")
  public void store () throws Exception
  {
    paceApi.store();
  }

  //Creates shell 
  public static void main(String[] args) throws Exception
  {
    Main main = new Main();

    Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions", main);
    shell.commandLoop();

    main.paceApi.store();
  }
}