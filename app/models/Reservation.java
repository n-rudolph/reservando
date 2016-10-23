package models;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;
import play.libs.Time;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Reservation extends Model {

    @Id
    private long id;
    private int amount;
    private DateTime date;
    @OneToOne
    private Client client;
    @OneToOne
    private Local local;

    public static Finder<Long, Reservation> find = new Finder<Long,Reservation>(Reservation.class);

    public Reservation() {
    }

    public Reservation(long id, int amount, DateTime date, Client client, Local local) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.client = client;
        this.local = local;
    }

    public long getId() {
        return id;
    }

    public Reservation setId(long id) {
        this.id = id;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Reservation setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public DateTime getDate() {
        return date;
    }

    public Reservation setDate(DateTime date) {
        this.date = date;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public Reservation setClient(Client client) {
        this.client = client;
        return this;
    }

    public Local getLocal() {
        return local;
    }

    public Reservation setLocal(Local local) {
        this.local = local;
        return this;
    }
}
