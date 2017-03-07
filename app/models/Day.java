package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Day extends Model{
    @Id
    private long id;
    private String day;

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
}
