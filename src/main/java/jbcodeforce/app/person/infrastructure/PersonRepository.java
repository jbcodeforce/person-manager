package jbcodeforce.app.person.infrastructure;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.views.AllDocsResponse;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jbcodeforce.app.person.domain.Person;

@ApplicationScoped
public class PersonRepository {

    @ConfigProperty(name = "app.db.create", defaultValue = "true")
    boolean dbCreate;

    @ConfigProperty(name = "app.datasource.url")
    String dbURL;

    @ConfigProperty(name = "app.datasource.db.person.name")
    String dbName;

    @ConfigProperty(name = "app.datasource.username")
    String dbUserName;

    @ConfigProperty(name = "app.datasource.password")
    String dbPassword;

    public CloudantClient client;
    private Database db;

    public PersonRepository() {
    }
 
    public List<Person> getPersons() {
        try {
            AllDocsResponse resp= db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse();
            return resp.getDocsAs(Person.class);
          } catch(IOException e) {
            System.err.println(e.getMessage());
            return new ArrayList<Person>();
        }
       
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
            System.err.println("Continue with existing DB");
        }
         db = client.database(dbName, false);
    }

	public Person save(Person person) {
        if (person._rev == null) {
            person.creationDate = LocalDate.now().toString();
            db.save(person);
        } else {
            person.updateDate = LocalDate.now().toString();  
            db.update(person);
        }
        return person;
    }
    
    public Person update(Person person) {
        if (person._rev == null) {
            person.creationDate = LocalDate.now().toString();
            db.save(person);
        } else {
            person.updateDate = LocalDate.now().toString();  
            db.update(person);
        }
        return person;
	}

	public void shutdown() {
        client.shutdown();
	}
}
