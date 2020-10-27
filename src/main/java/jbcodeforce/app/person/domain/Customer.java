package jbcodeforce.app.person.domain;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    public String name;
    public List<Meeting> meetings;
    
    public Customer() {
        super();
        meetings = new ArrayList<Meeting>();
    }

	public Customer(String customer) {
	}
}
