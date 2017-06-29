package models.Response;

import models.Discount;
import models.Local;
import models.Reservation;
import org.joda.time.DateTime;


public class ReservationResponse {
    
    public long id;
    public int amount;
    public DateTime date;
    public String clientName;
    public Local local;
    public Discount discount;

    public ReservationResponse(Reservation r){
        this.id = r.getId();
        this.amount = r.getAmount();
        this.date = r.getDate();
        this.clientName = r.getClient().getFirstName() + " " + r.getClient().getlastName();
        this.local = r.getLocal();
        if (r.getDiscount() != null){
            this.discount = Discount.byId(r.getDiscount().getId());
        } else {
            this.discount = null;
        }
    }
}
