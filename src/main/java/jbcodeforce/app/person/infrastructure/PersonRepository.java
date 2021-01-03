package jbcodeforce.app.person.infrastructure;

import static com.cloudant.client.api.query.Expression.eq;
import static com.cloudant.client.api.query.Expression.regex;
import static com.cloudant.client.api.query.Operation.and;

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
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.views.AllDocsResponse;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jbcodeforce.app.person.domain.Person;

@ApplicationScoped
public class PersonRepository {

    @ConfigProperty(name = "app.db.create", defaultValue = "true")
    public boolean dbCreate;

    @ConfigProperty(name = "app.datasource.url")
    public String dbURL;

    @ConfigProperty(name = "app.datasource.db.person.name")
    public String dbName;

    @ConfigProperty(name = "app.datasource.username")
    public String dbUserName;

    @ConfigProperty(name = "app.datasource.password")
    public String dbPassword;

    @ConfigProperty(name= "app.datasource.iam.apikey")
    public String iamApiKey;

    public CloudantClient client;
    private Database db;

    public PersonRepository() {
    }


    @PostConstruct
    public void init() {
        try {
            if (iamApiKey == null || iamApiKey.isEmpty() ) {
                client = ClientBuilder.url(new URL(dbURL)).username(dbUserName).password(dbPassword).build();    
            } else {
                client = ClientBuilder.account(dbUserName).iamApiKey(iamApiKey).build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
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


    public void shutdown() {
        client.shutdown();
    }

    public Person save(Person person) {
        if (person._rev == null) {
            if (person.email != null) {
                person._id = person.email;
            } else {
                person._id = person.firstname + "_" + person.lastname;
            }
            person.creationDate = LocalDate.now().toString();
            Response rep = db.save(person);
            person._rev = rep.getRev();
            person._id = rep.getId();
        } else {
            person.updateDate = LocalDate.now().toString();
            Response rep = db.update(person);
            person._rev = rep.getRev();
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

    public List<Person> getPersons() {
        try {
            AllDocsResponse resp = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse();
            return resp.getDocsAs(Person.class);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ArrayList<Person>();
        }

    }
    

    public List<Person> getPersonByName(String firstname, String lastname) {
        try {
            QueryResult<Person> persons = db.query(
                    new QueryBuilder(and(eq("firstname", firstname), eq("lastname", lastname))).build(), Person.class);
            return persons.getDocs();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Person getPersonById(String id) {
        try {
            return db.find(Person.class, id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

	public List<Person> getPersonsBySkill(String skillSearch) {
        try {
            skillSearch = skillSearch.toLowerCase();
            QueryResult<Person> persons = db.query(
                    new QueryBuilder(regex("skills", skillSearch)).build(), Person.class);
            return persons.getDocs();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    
	}
}
