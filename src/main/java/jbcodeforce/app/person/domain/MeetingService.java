package jbcodeforce.app.person.domain;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jbcodeforce.app.person.infrastructure.MeetingRepository;

@ApplicationScoped
public class MeetingService {
    private Logger logger = Logger.getLogger(MeetingService.class);

    @Inject
    public MeetingRepository meetingRepository;
    
    @Inject
    public PersonService personService;

    public MeetingService() {
        super();
    }


    /**
     * Save meeting and process the invited persons with person service
     * @param m
     */
	public Uni<Meeting> processAndSave(Meeting meeting) {
        logger.info(meeting.toString());
        meeting = meetingRepository.save(meeting);
        personService.updateInvitedPersonContext(meeting.splitAttendees(),meeting.context);
        return Uni.createFrom().item(meeting);
    }
    
    public Multi<Customer> findCustomer(String name) {
       return  Multi.createFrom().items(meetingRepository.findCustomer(name).stream());
    } 
}
