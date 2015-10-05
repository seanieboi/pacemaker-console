package controllers;

import java.io.IOException;
import asg.cliche.Command;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import models.User;
import asg.cliche.Param;

public class Main
{
  PacemakerAPI paceApi = new PacemakerAPI();

  @Command(description="Create a new User")
  public void createUser (@Param(name="first name") String firstName, @Param(name="last name") String lastName, 
                          @Param(name="email")      String email,     @Param(name="password")  String password)
  {
    paceApi.createUser(firstName, lastName, email, password);
  }

  @Command(description="Get a Users details")
  public void getUser (@Param(name="email") String email)
  {
    User user = paceApi.getUser(email);
    System.out.println(user);
  }

  @Command(description="Get all users details")
  public void getUsers ()
  {
    java.util.List<User> users = paceApi.getUsers();
    System.out.println(users);
  }

  @Command(description="Delete a User")
  public void deleteUser (@Param(name="email") String email)
  {
    paceApi.deleteUser(email);
  }

  public static void main(String[] args) throws IOException
  {
    Shell shell = ShellFactory.createConsoleShell("pc", "Welcome to pcemaker-console - ?help for instructions", new Main());
    shell.commandLoop(); 
  }
}