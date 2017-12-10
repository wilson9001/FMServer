package DBActionObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventDAOTest
{
    private EventDAO eventDAO;
    private UserDAO userDAO;
    private PersonDAO personDAO;

    @Before
    public void setUp() throws Exception
    {
        /*
       eventDAO = new EventDAO();
        userDAO = new UserDAO();
        personDAO = new PersonDAO();

       ArrayList<UserModel> usersToadd = new ArrayList<>();
       ArrayList<PersonModel> peopleToAdd = new ArrayList<>();

       usersToadd.add(new UserModel("descendant", "password", "email", "firstName", "lastName", "m", "person"));
       usersToadd.add(new UserModel("descendant1", "password", "email", "firstName", "lastName", "f", "person1"));

       peopleToAdd.add(new PersonModel("person", "descendant", "firstName", "lastName", "m", null, null, null));
       peopleToAdd.add(new PersonModel("person1", "descendant1", "firstName", "lastName", "f", null, null, null));

       userDAO.insertUsersIntoTable(usersToadd);
       personDAO.insertPeopleIntoDatabase(peopleToAdd);
    */
    }
	
	@After
    public void tearDown() throws Exception
    {
     //   new DAO().clear();
    }
	
    @Test
    public void AinsertEventsIntoTable() throws Exception
    {
        eventDAO = new EventDAO();
        userDAO = new UserDAO();
        personDAO = new PersonDAO();

        ArrayList<UserModel> usersToadd = new ArrayList<>();
        ArrayList<PersonModel> peopleToAdd = new ArrayList<>();

        usersToadd.add(new UserModel("descendant", "password", "email", "firstName", "lastName", "m", "person"));
        usersToadd.add(new UserModel("descendant1", "password", "email", "firstName", "lastName", "f", "person1"));

        peopleToAdd.add(new PersonModel("person", "descendant", "firstName", "lastName", "m", null, null, null));
        peopleToAdd.add(new PersonModel("person1", "descendant1", "firstName", "lastName", "f", null, null, null));

        userDAO.insertUsersIntoTable(usersToadd);
        personDAO.insertPeopleIntoDatabase(peopleToAdd);

        ArrayList<EventModel> eventsToAdd = new ArrayList<>();

        eventsToAdd.add(new EventModel("eventID", "descendant", "person", 0,0,"country", "city", "eventType", 1));
        eventsToAdd.add(new EventModel("eventID1", "descendant", "person", 0,0,"country", "city", "eventType", 1));
        eventsToAdd.add(new EventModel("eventID2", "descendant1", "person1", 0,0,"country1", "city", "eventType1", 2));
        eventsToAdd.add(new EventModel("eventID3", "descendant1", "person1", 0,0,"country1", "city", "eventType1", 2));

        assertEquals(eventDAO.insertEventsIntoTable(eventsToAdd), DAO.insertEventSuccess);

        eventsToAdd.clear();

        eventsToAdd.add(new EventModel("eventID", "descendant", "person", 0,0,"country", "city", "eventType", 1));

        assertEquals(eventDAO.insertEventsIntoTable(eventsToAdd), DAO.SQL_errorMsg);
    }

    @Test
    public void BselectEvent() throws Exception
    {
        eventDAO = new EventDAO();
        //userDAO = new UserDAO();
        //personDAO = new PersonDAO();

        EventModel eventModel = eventDAO.selectEvent("eventID", "descendant");

        assertNotNull(eventModel);
        assertEquals(eventModel.getDescendant(), "descendant");
        assertEquals(eventModel.getPerson(), "person");
        assertEquals(eventModel.getLatitude(), 0, 0);
        assertEquals(eventModel.getLongitude(), 0, 0);
        assertEquals(eventModel.getCountry(), "country");
        assertEquals(eventModel.getCity(), "city");
        assertEquals(eventModel.getEventType(), "eventType");
        assertEquals(eventModel.getYear(), 1);

        eventModel = eventDAO.selectEvent("event", "descendant");
        assertNull(eventModel);
        assertFalse(eventDAO.isSQLError());
        assertFalse(eventDAO.isEventIsNotAssignedToUser());

        eventModel = eventDAO.selectEvent("eventID", "descendant1");
        assertNull(eventModel);
        assertFalse(eventDAO.isSQLError());
        assertTrue(eventDAO.isEventIsNotAssignedToUser());
    }

    @Test
    public void CselectAllEvents() throws Exception
    {
        eventDAO = new EventDAO();

        ArrayList<EventModel> eventsForUser = eventDAO.selectAllEvents("descendant1");

        assertNotNull(eventsForUser);
        assertFalse(eventDAO.isSQLError());
        assertEquals(eventsForUser.size(), 2);

        EventModel result1 = eventsForUser.get(0);
        EventModel result2 = eventsForUser.get(1);

        assertNotNull(result1);
        assertNotNull(result2);

        assertEquals(result1.getEventID(), "eventID2");
        assertEquals(result2.getEventID(), "eventID3");

        eventsForUser = eventDAO.selectAllEvents("descendant2");
        assertNull(eventsForUser);
        assertFalse(eventDAO.isSQLError());

        eventDAO.clear();
    }
}