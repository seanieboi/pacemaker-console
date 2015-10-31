package models;

public class Fixtures
{
  public static User[] users =
  {
    new User ("marge", "simpson", "marge@simpson.com",  "secret"),
    new User ("lisa",  "simpson", "lisa@simpson.com",   "secret"),
    new User ("bart",  "simpson", "bart@simpson.com",   "secret"),
    new User ("maggie","simpson", "maggie@simpson.com", "secret")
  };

  public static Activity[] activities =
  {
    new Activity ((long) 1, "walk",  "fridge", 0.001, "time"),
    new Activity ((long) 2, "walk",  "fridge", 4.5, "time"),
    new Activity ((long) 3, "run",   "work",   2.2, "time"),
    new Activity ((long) 4, "walk",  "shop",   2.5, "time"),
    new Activity ((long) 5, "cycle", "school", 4.5, "time")
  };

  public static Location[]locations =
  {
    new Location(23.3f, 33.3f),
    new Location(34.4f, 45.2f),  
    new Location(25.3f, 34.3f),
    new Location(44.4f, 23.3f)       
  };
}