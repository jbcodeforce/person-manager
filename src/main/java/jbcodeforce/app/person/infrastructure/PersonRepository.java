package jbcodeforce.app.person.infrastructure;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.mutiny.Multi;
import jbcodeforce.app.person.domain.Item;
import jbcodeforce.app.person.domain.Person;

@ApplicationScoped
public class PersonRepository {

    @ConfigProperty(name = "app.db.create", defaultValue = "true")
    boolean dbCreate;

    @ConfigProperty(name = "app.datasource.url")
    String dbURL;

    @ConfigProperty(name = "app.datasource.db.name")
    String dbName;

    @ConfigProperty(name = "app.datasource.username")
    String dbUserName;

    @ConfigProperty(name = "app.datasource.password")
    String dbPassword;

    public CloudantClient client;
    private Database db;

    static HashMap<String, Person> persons = new HashMap<String, Person>();;

    public PersonRepository() {
        Person bk = new Person("spory@us.ibm.com", "Bob", "Spory");
        bk.company = "IBM";
        bk.role = "NA DBA Technical Sales Leader";
        bk.needs.add(new Item(1,"Get efficient demos of DBA product on OpenShift"));
        bk.contexts.add(new Item(1,"Meet him from his community call and 1x1- 10/05/2020"));
        bk.skills.add(new Item(0,"DBA product porfolio"));
        bk.skills.add(new Item(1,"Technical Sale"));
        persons.put(bk.email, bk);
    }
 
    public Multi<Person> getPersons() {
        return Multi.createFrom().items(persons.values().stream());
    }

    @PostConstruct
    void config() {
        try {
            client = ClientBuilder.url(new URL(dbURL))
            .username(dbUserName)
            .password(dbPassword)
            .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ;
        }
 
        if (dbCreate) {
            initdb();
        }
    }

    private void initdb() {
        try {
            client.createDB(dbName);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
         db = client.database(dbName, false);
    }

	public Person save(Person person) {
        if (persons.get(person.email) == null) {
            person.creationDate = LocalDate.now().toString();
            db.save(person);
        } else {
            person.updateDate = LocalDate.now().toString();  
            db.save(person);
        }
        return person;
	}

	public void shutdown() {
        client.shutdown();
	}
}
