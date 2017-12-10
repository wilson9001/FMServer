package Requests;

/**
 * Request object for a specific event.
 */
public class EventRequest
{
    /**
     * Constructor
     * @param eventID ID of event this request is asking for.
     * @param authToken AuthToken to make sure that user is logged in.
     */
    public EventRequest(String eventID, String authToken)
    {
        this.eventID = eventID;
        this.authToken = authToken;
    }

    public EventRequest(String authToken)
    {
        this.authToken = authToken;
        this.eventID = null;
    }

    private String eventID, authToken;

    /**
     * @return Event ID.
     */
    public String getEventID()
    {
        return eventID;
    }

    /**
     * @return AuthToken associated with request.
     */
    public String getAuthToken()
    {
        return authToken;
    }
}
