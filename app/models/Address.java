package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;

/**
 * Created by rudy on 07/01/17.
 */
public class Address extends Model {

    @Id
    private long id;

    private String address;
}
