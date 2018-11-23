package com.sms.thinkgeniux.sms_marketingCMS.PojoClass;

public class GroupItem
{
    private String Group_Id;
    private String Group_Name;

    public GroupItem(String group_Id, String group_Name) {
        Group_Id = group_Id;
        Group_Name = group_Name;
    }

    public String getGroup_Id() {
        return Group_Id;
    }

    public void setGroup_Id(String group_Id) {
        Group_Id = group_Id;
    }

    public String getGroup_Name() {
        return Group_Name;
    }

    public void setGroup_Name(String group_Name) {
        Group_Name = group_Name;
    }
}
