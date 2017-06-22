package models.requestObjects;

import models.Client;
import models.Discount;
import models.Local;
import models.Reservation;

public class ReservationObject {
    public long localId;
    public String discountCode;
    public String date;
    public String turn;
    public int amount;

    public Reservation toReservation(Client client) {
        final Local local = Local.getLocalById(localId);
        final Discount discount = Discount.byCode(discountCode);
        return new Reservation(client, local, amount, date, turn, discount);
    }
}
