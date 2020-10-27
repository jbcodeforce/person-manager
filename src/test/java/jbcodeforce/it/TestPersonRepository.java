package jbcodeforce.it;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import jbcodeforce.app.person.domain.Item;
import jbcodeforce.app.person.domain.Person;
import jbcodeforce.app.person.infrastructure.PersonRepository;

public class TestPersonRepository {

    @Container
    public static GenericContainer container = new GenericContainer("couchdb").withExposedPorts(5984)
            .withEnv("COUCHDB_USER", "admin").withEnv("COUCHDB_PASSWORD", "password");
    public static  PersonRepository repo = new PersonRepository();

    @BeforeAll
    public static void initCouchDB() {
        container.start();
        repo.dbURL = "http://" + container.getHost() + ":" + container.getFirstMappedPort();
        repo.dbCreate = true;
        repo.dbName = "persons";
        repo.dbUserName = "admin";
        repo.dbPassword = "password";
        repo.init();
    }

    @AfterAll
    public static void closeAll() {
        container.close();
    }

    @Order(1)
    @Test
    public void persistedPersonShouldHaveIdAndRev() {
        Person bk = new Person("builder@ibm.com", "Bob", "Builder");
        bk.company = "IBM";
        bk.role = "NA DBA Technical Sales Leader";
        bk.needs.add(new Item(1, "Get efficient demos of DBA product on OpenShift"));
        bk.contexts.add(new Item(1, "Meet him from his community call and 1x1- 10/05/2020"));
        bk.skills.add(new Item(0, "DBA product porfolio"));
        bk.skills.add(new Item(1, "Technical Sale"));
        Person out = repo.save(bk);
        Assertions.assertNotNull(out._id);
        Assertions.assertNotNull(out._rev);
        Assertions.assertEquals("builder@ibm.com",out._id);
        System.out.println(out._rev);
    }

    @Order(2)
    @Test
    public void shouldFindPersonByFirstAndLastNames() {
        List<Person> out = repo.getPersonByName("Bob", "Builder");
        Assertions.assertNotNull(out);
        Assertions.assertEquals(1,out.size());
        Assertions.assertEquals("builder@ibm.com",out.get(0)._id);
    }

    @Order(3)
    @Test
    public void shouldFindPersonById() {
        Person out = repo.getPersonById("builder@ibm.com");
        Assertions.assertNotNull(out);
        Assertions.assertEquals("builder@ibm.com",out._id);
    }

    @Order(4)
    @Test
    public void addMoreUsers() {
        Person bk = new Person("tester@ibm.com", "Rahul", "Tester");
        bk.company = "IBM";
        bk.role = "tester";
        bk.contexts.add(new Item(1, "Customer call on test"));
        bk.skills.add(new Item(0, "Integration product porfolio"));
        bk.skills.add(new Item(1, "Performance test"));
        repo.save(bk);
        List<Person> out = repo.getPersonsBySkill("Integration");
        Assertions.assertNotNull(out);
        Assertions.assertEquals(1,out.size());
        Assertions.assertEquals("tester@ibm.com",out.get(0)._id);
    }
}
