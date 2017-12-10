package Responses;

/**
 * Response object for load requests.
 * @author ajw9001
 */
public class LoadResponse
{
    /**
     * Constructor
     * @param message
     */
    public LoadResponse(String message)
    {
        this.message = message;
    }

    private String message;

    /**
     * @return Result message. Could be an error or success message.
     */
    public String getResult()
    {
        return message;
    }
}
