package jbcodeforce.app.person.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@Indexed
public class Person {
    public String _id;
    public String _rev;
    @FullTextField(analyzer = "name")
    @KeywordField(name = "firstname_sort", sortable = Sortable.YES, normalizer = "sort")
    public String firstname;
    @FullTextField(analyzer = "name")
    @KeywordField(name = "lastname_sort", sortable = Sortable.YES, normalizer = "sort")
    public String lastname;
    public String email;
    public String organization;
    public String company;
    public String role;
    public String location;
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
