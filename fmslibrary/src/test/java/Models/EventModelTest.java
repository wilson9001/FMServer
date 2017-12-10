package Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
public class EventModelTest
{
    private EventModel eventModel;
    @Before
    public void setUp() throws Exception
    {
        eventModel = new EventModel("eventID", "descendant", "person", 0,0,"country", "city", "eventType", 1);
    }

    @Test
    public void getEventID() throws Exception
    {
        assertEquals(eventModel.getEventID(), "eventID");
    }

    @Test
    public void getDescendant() throws Exception
    {
        assertEquals(eventModel.getDescendant(), "descendant");
    }

    @Test
    public void getPerson() throws Exception
    {
        assertEquals(eventModel.getPerson(), "person");
    }

    @Test
    public void getLatitude() throws Exception
    {
        assertEquals(eventModel.getLatitude(), 0,0);
    }

    @Test
    public void getLongitude() throws Exception
    {
        assertEquals(eventModel.getLongitude(), 0,0);
    }

    @Test
    public void getCountry() throws Exception
    {
        assertEquals(eventModel.getCountry(), "country");
    }

    @Test
    public void getCity() throws Exception
    {
        assertEquals(eventModel.getCity(), "city");
    }

    @Test
    public void getEventType() throws Exception
    {
        assertEquals(eventModel.getEventType(), "eventType");
    }

    @Test
    public void getYear() throws Exception
    {
        assertEquals(eventModel.getYear(), 1);
    }

}