package jbcodeforce;

import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jbcodeforce.app.person.domain.Meeting;
import jbcodeforce.app.person.domain.Person;
import jbcodeforce.app.person.domain.PersonService;

public class TestPersonService {
   
    @Test
    public void testExtractCompanyName(){
        PersonService serv = new PersonService();
        String cname = serv.buildCompanyName("ibm.com");
        Assertions.assertEquals("ibm", cname);
        cname = serv.buildCompanyName("company");
        Assertions.assertEquals("company", cname);
        Assertions.assertEquals("some.company", serv.buildCompanyName("some.company.com"));
    }

    @Test
    public void testExtractFirstName(){
        PersonService serv = new PersonService();
        Assertions.assertEquals("jane", serv.getFirstName("jane tonic"));
        Assertions.assertEquals("jane", serv.getFirstName("jane.tonic"));
        Assertions.assertEquals("ana", serv.getFirstName("ana jane tonic"));
        Assertions.assertEquals("anna", serv.getFirstName("anna"));
    }


    @Test
    public void testExtractLasttName(){
        PersonService serv = new PersonService();
        Assertions.assertEquals("tonic", serv.getLastName("jane tonic"));
        Assertions.assertEquals("tonic", serv.getLastName("jane.tonic"));
        Assertions.assertEquals("tonic", serv.getLastName("ana jane tonic"));
        Assertions.assertEquals("tonic", serv.getLastName("tonic"));
    }

    @Test
    public void testParseInvite(){
        Meeting m = new Meeting("travel","Event storming recap");
        m.attendees = "Tania.Whitby@company.com, Jennifer.Seidenwurm@company.com, Jerome Boyer, Lawrence P Norcini, Sathish.Saravanan@company.com, Mark.Freeman@company.com, Dave.Hollander@company.com";
        String[] invites = m.splitAttendees();
        Assertions.assertNotNull(invites);
        Assertions.assertTrue(invites.length > 3);
        PersonService serv = new PersonService();
        List<Person> invitedPersons = serv.buildInvitedPersonList(invites);
        Assertions.assertNotNull(invitedPersons);
        Jsonb parser = JsonbBuilder.create();
        for (Person s : invitedPersons) {
            System.out.println(parser.toJson(s));
        }
    }
}
