package models;

import static com.google.common.base.Objects.toStringHelper;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

public class Activity 
{ 
  static Long   counter = 0l;
  
  public Long   id;
  public String type;
  public String location;
  public double distance;
  public String start;
  DateTime startTime = new DateTime();
  //DateTime startTime = DateTime.parse(start);

  
  public List<Location> route = new ArrayList<>();
 
  public Activity()
  {
  }
  
  public Activity(String type, String location, double distance, DateTime startTime)
  {
    this.id        = counter++;
    this.type      = type;
    this.location  = location;
    this.distance  = distance;
    this.startTime = startTime; 
  }
  
  @Override
  public String toString()
  {
    return toStringHelper(this).addValue(id)
                               .addValue(type)
                               .addValue(location)
                               .addValue(distance)
                               .addValue(startTime)
                               .addValue(route)
                               .toString();
  }
  
  @Override  
  public int hashCode()  
  {  
     return Objects.hashCode(this.id, this.type, this.location, this.distance, this.startTime);  
  } 
  
  @Override
  public boolean equals(final Object obj)
  {
    if (obj instanceof Activity)
    {
      final Activity other = (Activity) obj;
      return Objects.equal(type, other.type) 
          && Objects.equal(location,  other.location)
          && Objects.equal(distance,  other.distance)
          && Objects.equal(startTime, other.startTime)
          && Objects.equal(route,     other.route);    
    }
    else
    {
      return false;
    }
  }
}