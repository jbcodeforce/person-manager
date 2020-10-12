package jbcodeforce.app.person.domain;

import java.util.ArrayList;
import java.util.List;

public class Person {
    public String _id;
    public String _rev;
    public String firstname;
    public String lastname;
    public String email;
    public String organization;
    public String company;
    public String role;
    public String creationDate;
    public String updateDate;
    public List<Item> skills;
    public List<Item> needs;
    public List<Item> contexts;

    public Person(){
        init();
    }
    
    public Person(String email, String fn, String ln) {
        this.email = email;
        this.firstname = fn;
        this.lastname = ln;
        init();
    }

    public void init(){
        skills = new ArrayList<Item>();
        needs = new ArrayList<Item>();
        contexts =  new ArrayList<Item>();
    }

    public String toString(){
        return this.email +  " - " + this.lastname;
    }
}
