package models.requestObjects;

import models.Client;
import models.Discount;
import models.Local;
import models.Reservation;
import org.joda.time.DateTime;

public class ReservationObject {
    public long localId;
    public String discountCode;
    public DateTime date;
    public int amount;

    public Reservation toReservation(Client client) {
        final Local local = Local.getLocalById(localId);
        final Discount discount = Discount.byCode(discountCode);
        if(discount != null){
            discount.setUsed();
            discount.update();
        }
        return new Reservation(client, local, amount, date, discount);
    }
}
