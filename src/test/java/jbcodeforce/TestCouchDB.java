package jbcodeforce;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jbcodeforce.app.person.domain.Item;
import jbcodeforce.app.person.domain.Person;
import jbcodeforce.app.person.infrastructure.PersonRepository;

@QuarkusTest
public class TestCouchDB {

    @Inject
    public PersonRepository repo;

    @Test
    public void verifyPersonsDBExist() {
        List<String> databases = repo.client.getAllDbs();
        System.out.println("All my databases : ");
        for (String db : databases) {
            System.out.println(db);
        }
        Assertions.assertTrue(databases.contains("persons"));
       
    }

    
    @Test
    public void addADocument() {

        Person bk = new Person("spory@us.ibm.com", "Bob", "Spory");
        bk.company = "IBM";
        bk.role = "NA DBA Technical Sales Leader";
        bk.needs.add(new Item(1,"Get efficient demos of DBA product on OpenShift"));
        bk.contexts.add(new Item(1,"Meet him from his community call and 1x1- 10/05/2020"));
        bk.skills.add(new Item(0,"DBA product porfolio"));
        bk.skills.add(new Item(1,"Technical Sale"));
        Person out = repo.save(bk);
        System.out.println("You have inserted the document");
        System.out.println(out);
    }

    @Test
    public void getAllDocuments() {

        List<Person> out = repo.getPersons();
        out.stream().peek( p -> System.out.println(p.toString()));
    }

}
