package jbcodeforce.app.person.api;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Multi;
import jbcodeforce.app.person.domain.Person;
import jbcodeforce.app.person.infrastructure.PersonRepository;

@Path("/api/v1/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

 

    @Inject
    PersonRepository personRepository;

    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }


   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Multi<Person> getAllThePersons(){
       return personRepository.getPersons();
   }
}