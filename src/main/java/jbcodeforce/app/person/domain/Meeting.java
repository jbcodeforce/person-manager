package jbcodeforce.app.person.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Meeting {
    public String id;
    public String title;
    public String creationDate;
    public String updateDate;
    public String attendees;
    public String context;
    public String customer;
    public boolean active=true;

    public Meeting(){}

    public Meeting(String c,String t){
        this.customer = c;
        this.title = t;
    }

    public String[] splitAttendees(){
        String[] inv = {};
        if (this.attendees != null) {
            inv = this.attendees.split(",");
        }
        return inv;
    }

    public String toString(){
        return this.customer +  " - " + this.title;
    }
}
