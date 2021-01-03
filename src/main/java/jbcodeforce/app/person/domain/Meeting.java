package jbcodeforce.app.person.domain;

import java.util.List;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Indexed
public class Meeting {
    public String _id;
    public String _rev;
    public String title;
    public String creationDate;
    public String updateDate;
    public String attendees;
    public String context;
    public String customer;
    public List<Item> todos;
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
