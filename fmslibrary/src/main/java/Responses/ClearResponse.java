package Responses;

/**
 * Response object for clear requests.
 * @author ajw9001
 */
public class ClearResponse
{
    /**
     * Constructor
     * @param message Response message.
     */
    public ClearResponse(String message)
    {
        this.message = message;
    }

    private String message;

    /**
     * @return Response. Can be success or error message.
     */
    public String getResponse()
    {
        return message;
    }
}
