package models;

import com.avaje.ebean.Model;
import org.joda.time.DateTime;
import play.libs.Time;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.List;

@Entity
public class Reservation extends Model {

    @Id
    private long id;
    private int amount;
    private DateTime date;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Local local;
    @OneToOne(cascade = CascadeType.REMOVE)
    @Nullable
    private Discount discount;

    private static Finder<Long, Reservation> finder = new Finder<Long,Reservation>(Reservation.class);
    private boolean active;

    public Reservation() {
    }

    public Reservation(Client client, Local local, int amount, DateTime date, @Nullable Discount discount) {
        this.client = client;
        this.local = local;
        this.amount = amount;
        this.date = date;
        this.discount = discount;
        active = true;
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

    @Nullable
    public Discount getDiscount() {
        return discount;
    }

    public Reservation setDiscount(@Nullable Discount discount) {
        this.discount = discount;
        return this;
    }

    public static Reservation byId(long id) {
        return finder.byId(id);
    }

    public static List<Reservation> byClient(Client client) {
        return finder.where().eq("client", client).findList();
    }

    public static List<Reservation> byLocal(Local local) {
        return finder.where().eq("local", local).findList();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
