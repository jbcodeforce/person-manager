package jbcodeforce.app.person.domain;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import jbcodeforce.app.person.infrastructure.PersonRepository;

@ApplicationScoped
public class PersonService {
    private Logger logger = Logger.getLogger(PersonService.class);

    @Inject
    PersonRepository personRepository;
    
    public PersonService() {
        super();
    }

	public List<Person> buildInvitedPersonList(String[] invites) {
        List<Person> persons = new ArrayList<Person>();
        for (String email : invites) {
            Person p = new Person();
            email=email.trim();
            if (email.contains("@")) {
                String[] parts = email.split("@");
                p.company = buildCompanyName(parts[1]);
                p.firstname = getFirstName(parts[0]);
                p.lastname = getLastName(parts[0]);
            } else {
                p.firstname = getFirstName(email);
                p.lastname = getLastName(email);
                p.company = "ibm";
            }
            persons.add(p);
        }
		return persons;
    }
    
    public String getFirstName(String namePart){
        if (namePart != null && namePart.length() > 0) {
            return getPart(namePart,0);
        }
        return "";
    }

    public String getLastName(String namePart){
        if (namePart != null && namePart.length() > 0) {
            return getPart(namePart,1);
        }
        return "";
    }

    private String getPart(String namePart, int idx){
        if (namePart.contains(".")) {
            if (idx == 0)
                return namePart.substring(0,namePart.indexOf("."));
            else 
                return namePart.substring(namePart.lastIndexOf(".")+1,namePart.length());
        } else if (namePart.contains(" ")) {
            if (idx == 0)
                return namePart.substring(0,namePart.indexOf(" "));
            else 
                return namePart.substring(namePart.lastIndexOf(" ")+1,namePart.length());
          } else {
            return namePart;
          }
    }

    public String buildCompanyName(String dns){
        String company = "ibm";
        if (dns != null && dns.length() > 0) {
            if (dns.indexOf(".")> 0) {
                company = dns.substring(0, dns.lastIndexOf("."));
            } else {
                company = dns;
            } 
        }
        return company;
    }

	public void updateInvitedPersonContext(String[] attendees,String meetingContext) {
        for (Person attendee : buildInvitedPersonList(attendees)) {
            attendee.contexts.add(new Item(0,meetingContext));
            personRepository.save(attendee);
        };
	}


}
