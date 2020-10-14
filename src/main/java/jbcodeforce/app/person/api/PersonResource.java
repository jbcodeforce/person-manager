package jbcodeforce.app.person.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jbcodeforce.app.person.domain.Person;
import jbcodeforce.app.person.infrastructure.PersonRepository;

@Path("/api/v1/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    PersonRepository personRepository;

    public PersonResource(){}
    

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Multi<Person> getAllThePersons(){
       return Multi.createFrom().items(personRepository.getPersons().stream());
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Uni<Person> savePerson(Person person) {
       return Uni.createFrom().item(personRepository.save(person));
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Uni<Person> updatePerson(Person person) {
       return Uni.createFrom().item(personRepository.update(person));
   }
}