package Models;

/**
 * Represents an AuthToken. Used for transmitting data within the server.
 * @author ajw9001
 */
public class AuthTokenModel
{
    /**
     * Constructor.
     * @param token AuthToken value.
     * @param dateTimeCreated Date and time the authToken was created in an array of ints. The values are [year, month, day, hour, minute, second].
     */
    public AuthTokenModel(String token, String userID, int dateTimeCreated)
    {
        this.token = token;
        this.userID = userID;
        this.dateTimeCreated = dateTimeCreated;
    }

    private String token, userID;
    private int dateTimeCreated;

    /**
     * @return AuthToken value.
     */
    public String getToken()
    {
        return token;
    }

    /**
     * @return Date and time the authToken was created in an array of ints. The values are [year, month, day, hour, minute, second].
     */
    public int getDateTimeCreated()
    {
        return dateTimeCreated;
    }

    public String getUserID()
    {
        return userID;
    }
}
