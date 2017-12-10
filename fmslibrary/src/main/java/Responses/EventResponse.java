package Responses;

import java.util.ArrayList;

import Models.EventModel;

/**
 * Response object for event requests.
 * @author ajw9001
 */
public class EventResponse
{
    /**
     * Constructor. Used when only a single event was requested.
     * @param event Event to be used in response.
     */
    public EventResponse(EventModel event)
    {
        data = null;
        message = null;
        descendant = event.getDescendant();
        eventID = event.getEventID();
        personID = event.getPerson();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        country = event.getCountry();
        city = event.getCity();
        eventType = event.getEventType();
        year = event.getYear();
    }

    /**
     * Overloaded constructor. Used when all events for a specific user were requested.
     * @param events List of events corresponding to the specified user.
     */
    public EventResponse(ArrayList<EventModel> events)
    {
        this.data = events;
        message = null;
        descendant = null;
        eventID = null;
        personID = null;
        latitude = null;
        longitude = null;
        country = null;
        city = null;
        eventType = null;
        year = null;
    }

    public EventResponse(String errorMsg)
    {
        message = errorMsg;
        data = null;
        descendant = null;
        eventID = null;
        personID = null;
        latitude = null;
        longitude = null;
        country = null;
        city = null;
        eventType = null;
        year = null;
    }

    private ArrayList<EventModel> data;
    private String message, descendant, eventID, personID, country, city, eventType;
    private Double latitude, longitude;
    private Integer year;

    /**
     * @return Events requested. If only one event was requested, the size of this list will be 1.
     */
    public ArrayList<EventModel> getEvents()
    {
        return data;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
