package models.requestObjects;

import models.Photo;

public class PhotoObject {
    public String name;
    public String src;

    public Photo toPhoto(String path){
        return new Photo(name, path);
    }
}
