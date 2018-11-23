package com.sms.thinkgeniux.sms_marketingCMS.PojoClass;

public class ContactItem
{
    private String Group_Name;
    private String Contact_Number;

    public ContactItem(String group_Name, String contact_Number) {
        Group_Name = group_Name;
        Contact_Number = contact_Number;
    }

    public String getGroup_Name() {
        return Group_Name;
    }

    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }

    public String getContact_Number() {
        return Contact_Number;
    }

    public void setContact_Number(String contact_Number) {
        Contact_Number = contact_Number;
    }
}
