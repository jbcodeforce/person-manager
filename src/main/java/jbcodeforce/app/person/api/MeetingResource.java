package jbcodeforce.app.person.api;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jbcodeforce.app.person.domain.Meeting;
import jbcodeforce.app.person.domain.MeetingService;
import jbcodeforce.app.person.infrastructure.MeetingRepository;

@ApplicationScoped
@Path("/api/v1/meetings")
public class MeetingResource {
    
    @Inject
    public MeetingService meetingService;
    
    @Inject MeetingRepository meetingRepository;

    public MeetingResource(){}
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Meeting> getAllActiveMeetings(){
        return Multi.createFrom().items(meetingRepository.getActiveMeetings().stream());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Uni<Meeting> getMeetingById(@PathParam String id){
        return Uni.createFrom().item(meetingRepository.getById(id));
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Meeting> processAndSaveMeeting(Meeting meeting) {
        return meetingService.processAndSave(meeting);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Meeting> updateMeeting(Meeting meeting) {
        return Uni.createFrom().item(meetingRepository.update(meeting));
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> deleteMeeting(Meeting meeting) {
        return Uni.createFrom().item(meetingRepository.delete(meeting));
    }

}
