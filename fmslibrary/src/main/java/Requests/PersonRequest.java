package Requests;

/**
 * Request object for a specific person.
 * @author ajw9001
 */
public class PersonRequest
{
    /**
     * Constructor.
     * @param authToken AuthToken to make sure that the user is logged in.
     * @param personID
     */
    public PersonRequest(String authToken, String personID)
    {
        this.authToken = authToken;
        this.personID = personID;
    }

    private String authToken, personID;

    /**
     * @return AuthToken associated with request.
     */
    public String getAuthToken()
    {
        return authToken;
    }

    /**
     * @return Person ID associated with request.
     */
    public String getPersonID()
    {
        return personID;
    }
}
