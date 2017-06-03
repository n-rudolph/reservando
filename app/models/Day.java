package models;

import com.avaje.ebean.Model;
import play.api.libs.json.Writes;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Day extends Model{
    @Id
    private long id;
    private String day;

    private static Finder<Long, Day> finder = new Finder<Long,Day>(Day.class);

    public Day(String day){
        this.day = day;
    }

    public long getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public Day setId(long id) {
        this.id = id;
        return this;
    }

    public Day setDay(String day) {
        this.day = day;
        return this;
    }

    public static Day byId(long id){return finder.byId(id);}
    public static Day getDay(String dayName) {return finder.where().eq("day",dayName).findUnique();}

    public static List<Day> all() {
        return finder.all();
    }
}

