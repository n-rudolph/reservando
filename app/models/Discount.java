package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Discount extends Model{

    @Id
    private Long id;
    private String code;
    private int discount;
    private boolean isUsed;


    public Discount(String code, int discount){
        this.code = code;
        this.discount = discount;
        this.isUsed = false;
    }

    public Discount setUsed() {
        isUsed = true;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getDiscount() {
        return discount;
    }

    public boolean isUsed() {
        return isUsed;
    }

    private static Finder<Long, Discount> finder = new Finder<>(Discount.class);

    public static boolean isCodeValid(String code) {
        final Discount discount1 = finder.where().eq("code", code).findUnique();
        return discount1 != null && !discount1.isUsed();
    }

    public static Discount byCode(String code) {
        return finder.where().eq("code", code).findUnique();
    }

    public static List<Discount> all() {
        return finder.all();
    }

    public static Discount byId(Long id) {
        return finder.byId(id);
    }
}
