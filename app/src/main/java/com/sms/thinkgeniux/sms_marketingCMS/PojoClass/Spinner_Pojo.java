package com.sms.thinkgeniux.sms_marketingCMS.PojoClass;

/**
 * Created by CH-Hamza on 2/26/2018.
 */

public class Spinner_Pojo {
    private String id;
    private String name;

    public Spinner_Pojo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Spinner_Pojo){
            Spinner_Pojo c = (Spinner_Pojo)obj;
            if(c.getName().equals(name) && c.getId()==id )
                return true;
        }

        return false;
    }


}
