package models.requestObjects;

import models.Photo;

public class PhotoObject {
    public String name;
    public String src;

    public Photo toPhoto(){
        return new Photo(name, src);
    }
}
