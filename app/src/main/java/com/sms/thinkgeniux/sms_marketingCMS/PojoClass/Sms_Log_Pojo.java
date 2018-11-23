package com.sms.thinkgeniux.sms_marketingCMS.PojoClass;

public class Sms_Log_Pojo
{
//    private String Id;
    private String To;
    private String From;
    private String Message;
    private String Time;

    public Sms_Log_Pojo( String to, String from, String message, String time) {
        To = to;
        From = from;
        Message = message;
        Time = time;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
