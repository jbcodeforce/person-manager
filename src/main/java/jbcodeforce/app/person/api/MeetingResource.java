package jbcodeforce.app.person.api;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jbcodeforce.app.person.domain.Meeting;
import jbcodeforce.app.person.domain.MeetingService;

@ApplicationScoped
@Path("/api/v1/meetings")
public class MeetingResource {
    
    @Inject
    public MeetingService meetingService;
    
    public MeetingResource(){}
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Meeting> getAllActiveMeetings(){
        return meetingService.getActiveMeetings();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Meeting> processAndSaveMeeting(Meeting meeting) {
        return meetingService.processAndSave(meeting);
    }
}
