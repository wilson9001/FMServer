package Requests;

/**
 * Request object for logging in a returning user.
 * @author ajw9001
 */
public class LoginRequest
{
    /**
     * Constructor.
     * @param userName
     * @param password
     */
    public LoginRequest(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    LoginRequest() {}

    private String userName, password;

    /**
     * @return User name.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @return Password.
     */
    public String getPassword()
    {
        return password;
    }
}
