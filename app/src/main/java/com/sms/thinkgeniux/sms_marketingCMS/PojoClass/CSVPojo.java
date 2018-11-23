package com.sms.thinkgeniux.sms_marketingCMS.PojoClass;
/*
 * Basic Data class to hold a state name and the state capital.
 */
public class CSVPojo {

    private String number;

    public CSVPojo(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}