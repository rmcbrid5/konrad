package com.konrad.mail.models;

import com.google.gson.internal.LinkedTreeMap;

public class Header {

    private String name;
    private String value;

    public Header(LinkedTreeMap headerLtm){

        name = headerLtm.containsKey("name") ? headerLtm.get("name").toString() : "";
        value = headerLtm.containsKey("value") ? headerLtm.get("value").toString() : "";
    }

    public String getName(){
        return name;
    }

    public String getValue(){
        return value;
    }
}
