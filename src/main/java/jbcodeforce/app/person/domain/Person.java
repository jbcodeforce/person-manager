package jbcodeforce.app.person.domain;

import java.util.ArrayList;
import java.util.List;

public class Person {
    public String id;
    public String firstname;
    public String lastname;
    public String email;
    public String organization;
    public String company;
    public String role;
    public String creationDate;
    public String updateDate;
    public List<String> skills;
    public List<String> helpNeeds;
    public List<String> contexts;

    public Person(){
        skills = new ArrayList<String>();
        helpNeeds = new ArrayList<String>();
        contexts =  new ArrayList<String>();
    }
    
    public Person(String email, String fn, String ln) {
        this.email = email;
        this.firstname = fn;
        this.lastname = ln;
        skills = new ArrayList<String>();
        helpNeeds = new ArrayList<String>();
        contexts =  new ArrayList<String>();
    }

    public String toString(){
        return this.email +  " - " + this.lastname;
    }
}
