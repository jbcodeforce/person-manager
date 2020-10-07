package jbcodeforce.app.person.infrastructure;

import java.time.LocalDate;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.mutiny.Multi;
import jbcodeforce.app.person.domain.Person;

@ApplicationScoped
public class PersonRepository {

    @Inject
    io.vertx.mutiny.pgclient.PgPool client;
    @Inject
    @ConfigProperty(name = "app.schema.create", defaultValue = "true")
    boolean schemaCreate;
    
    static HashMap<String,Person> persons = new HashMap<String,Person>();;

    public PersonRepository(){
        Person bk= new Person("spory@us.ibm.com","Bob","Spory");
        bk.company="IBM";
        bk.role="NA DBA Technical Sales Leader";
        bk.helpNeeds.add("Get efficient demos of DBA product on OpenShift");
        bk.contexts.add("Meet him from his community call and 1x1- 10/05/2020");
        bk.skills.add("DBA product porfolio");
        bk.skills.add("Technical Sale");
        persons.put(bk.email,bk);
    }

    public Multi<Person> getPersons(){
        return Multi.createFrom().items(persons.values().stream());
    }

    @PostConstruct
    void config() {
        if (schemaCreate) {
            initdb();
        }
    }

    private void initdb() {
        // TODO
    }

	public static Person save(Person person) {
        if (persons.get(person.email) == null) {
            person.creationDate = LocalDate.now().toString();
            persons.put(person.email,person);
        } else {
            person.updateDate = LocalDate.now().toString();
            persons.replace(person.email, person);
        }
        return person;
	}
}
