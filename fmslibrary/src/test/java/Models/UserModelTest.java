package Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
public class UserModelTest
{
    private UserModel userModel;

    @Before
    public void setUp() throws Exception
    {
        userModel = new UserModel("userName", "password", "email", "firstName", "lastName", "gender", "personID");
    }

    @Test
    public void getUserName() throws Exception
    {
        assertEquals(userModel.getUserName(), "userName");
    }

    @Test
    public void getpassword() throws Exception
    {
        assertEquals(userModel.getpassword(), "password");
    }

    @Test
    public void getEmail() throws Exception
    {
        assertEquals(userModel.getEmail(), "email");
    }

    @Test
    public void getFirstName() throws Exception
    {
        assertEquals(userModel.getFirstName(), "firstName");
    }

    @Test
    public void getLastName() throws Exception
    {
        assertEquals(userModel.getLastName(), "lastName");
    }

    @Test
    public void getGender() throws Exception
    {
        assertEquals(userModel.getGender(), "gender");
    }

    @Test
    public void getPersonID() throws Exception
    {
        assertEquals(userModel.getPersonID(), "personID");
    }

}