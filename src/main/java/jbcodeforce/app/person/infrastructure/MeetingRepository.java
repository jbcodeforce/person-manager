package jbcodeforce.app.person.infrastructure;

import java.time.LocalDate;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.mutiny.Multi;
import jbcodeforce.app.person.domain.Meeting;

@ApplicationScoped
public class MeetingRepository {

    @ConfigProperty(name = "app.db.create", defaultValue = "true")
    boolean schemaCreate;
    
    static HashMap<String,Meeting> meetings = new HashMap<String,Meeting>();;

    public MeetingRepository(){
    }

    public Multi<Meeting> getActiveMeetings(){
        return Multi.createFrom().items(meetings.values().stream());
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

	public Meeting save(Meeting meeting) {
     
        if (meetings.get(meeting.title) == null) {
            meeting.creationDate = LocalDate.now().toString();
            meetings.put(meeting.title,meeting);
        } else {
            meeting.updateDate = LocalDate.now().toString();
            meetings.replace(meeting.title, meeting);
        }
        return meeting;
	}
}
