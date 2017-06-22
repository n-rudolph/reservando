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
    private String date;
    private String turn;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Local local;
    @OneToOne
    @Nullable
    private Discount discount;

    private static Finder<Long, Reservation> finder = new Finder<Long,Reservation>(Reservation.class);

    public Reservation() {
    }

    public Reservation(Client client, Local local, int amount, String date, String turn, @Nullable Discount discount) {
        this.client = client;
        this.local = local;
        this.amount = amount;
        this.date = date;
        this.turn = turn;
        this.discount = discount;
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

    public String getDate() {
        return date;
    }

    public Reservation setDate(String date) {
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

    public String getTurn() {
        return turn;
    }

    public Reservation setTurn(String turn) {
        this.turn = turn;
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
}
