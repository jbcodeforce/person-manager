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
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.views.AllDocsResponse;
import static com.cloudant.client.api.query.Expression.eq;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.vertx.core.json.JsonObject;
import jbcodeforce.app.person.domain.Meeting;

@ApplicationScoped
public class MeetingRepository {

    @ConfigProperty(name = "app.db.create", defaultValue = "true")
    boolean dbCreate;

    @ConfigProperty(name = "app.datasource.url")
    String dbURL;

    @ConfigProperty(name = "app.datasource.db.meeting.name")
    String dbName;

    @ConfigProperty(name = "app.datasource.username")
    String dbUserName;

    @ConfigProperty(name = "app.datasource.password")
    String dbPassword;

    public CloudantClient client;
    private Database db;
    
    public MeetingRepository(){
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
    
    public List<Meeting> getActiveMeetings(){
        try {
            QueryResult<Meeting> meetings = db.query(new QueryBuilder(
                    eq("active", true)).
                build(), Meeting.class);
           // AllDocsResponse resp= db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse();
           // return resp.getDocsAs(Meeting.class);
           return meetings.getDocs();
          } catch(Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<Meeting>();
        }
       
    }

	public Meeting save(Meeting meeting) {    
        if (meeting._rev == null) {
            meeting.creationDate = LocalDate.now().toString();
            db.save(meeting);
        } else {
            return update(meeting);
        }
        return meeting;
    }
    
    public Meeting update(Meeting meeting) {
        if (meeting._rev == null) {
            return save(meeting);
        } else {
            meeting.updateDate = LocalDate.now().toString();  
            db.update(meeting);
        }
        return meeting;
	}

	public void shutdown() {
        client.shutdown();
	}

	public String delete(Meeting meeting) {
		try {
            meeting.updateDate = LocalDate.now().toString();
            meeting.active = false;
            db.update(meeting);
        } catch(Exception e) {
            e.printStackTrace();
            return "Failure";
        }
        return "Success";
	}
}
