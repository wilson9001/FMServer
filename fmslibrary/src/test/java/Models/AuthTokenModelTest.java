package Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/2/2017.
 */
public class AuthTokenModelTest
{
    private AuthTokenModel authTokenModel;

    @Before
    public void setUp() throws Exception
    {
        authTokenModel = new AuthTokenModel("token", "userID", 1);
    }

    @Test
    public void getToken() throws Exception
    {
        assertEquals(authTokenModel.getToken(), "token");
    }

    @Test
    public void getDateTimeCreated() throws Exception
    {
        assertEquals(authTokenModel.getDateTimeCreated(), 1);
    }

    @Test
    public void getUserID() throws Exception
    {
        assertEquals(authTokenModel.getUserID(), "userID");
    }

}