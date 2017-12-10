package Requests;

/**
 * Request object to remove data from the Person and Event tables associated with a specific user and then fill them with new data.
 * @author ajw9001
 */
public class FillRequest
{
    /**
     * Basic constructor. Used when generations are not specified. Instead, the default number of generation data is used.
     * @param userName Associated with fill request.
     */
    public FillRequest(String userName)
    {
        this.userName = userName;
        generations = defaultGenerationNumber;
    }

    FillRequest(){}

    /**
     * Overloaded constructor. Used when the number of generations to fill data for is specified.
     * @param userName Associated with fill request
     * @param generations
     */
    public FillRequest(String userName, int generations)
    {
        this.userName = userName;
        this.generations = generations;
    }

    private String userName;
    private int generations;

    /**
     * @return User Name associated with request.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @return Generations associated with request.
     */
    public int getGenerations()
    {
        return generations;
    }

    private static final int defaultGenerationNumber = 4;
}
