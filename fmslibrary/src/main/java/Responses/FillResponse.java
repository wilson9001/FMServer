package Responses;

/**
 * Response object for fill requests.
 * @author ajw9001
 */
public class FillResponse
{
    public FillResponse(String message)
    {
        this.message = message;
    }

    private String message;

    /**
     * @return A success or error message.
     */
    public String getResult()
    {
        return message;
    }
}
