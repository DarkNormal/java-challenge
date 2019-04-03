package com.marklordan.model;

public abstract class BaseObject {


    private long id;
    private int version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    protected String trimAndCapitalise(String valueToModify){
        if(valueToModify == null){
            return "";
        }
        return valueToModify.trim().toUpperCase();
    }
}
