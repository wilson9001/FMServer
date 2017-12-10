package DBActionObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import Models.PersonModel;
import Models.UserModel;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserDAOTest
{
    private UserDAO userDAO;
    private PersonDAO personDAO;

    @Before
    public void setUp() throws Exception
    {
        userDAO = new UserDAO();
        personDAO = new PersonDAO();
    }

    @After
    public void tearDown() throws Exception
    {
        //new DAO().clear();
    }

    @Test
    public void AinsertUsersIntoTable() throws Exception
    {
        ArrayList<UserModel> usersToadd = new ArrayList<>();

        usersToadd.add(new UserModel("descendant", "password", "email", "firstName", "lastName", "m", "person"));
        usersToadd.add(new UserModel("descendant1", "password", "email", "firstName", "lastName", "f", "person1"));

        assertEquals(userDAO.insertUsersIntoTable(usersToadd), DAO.insertUserSuccess);

        usersToadd.clear();

        usersToadd.add(new UserModel("descendant", "password", "email", "firstName", "lastName", "m", "person"));

        assertEquals(userDAO.insertUsersIntoTable(usersToadd), DAO.SQL_errorMsg);
    }

    @Test
    public void BupdateUserPersonID() throws Exception
    {
        ArrayList<PersonModel> peopleToAdd = new ArrayList<>();

        peopleToAdd.add(new PersonModel("person", "descendant", "firstName", "lastName", "m", null, null, null));
        peopleToAdd.add(new PersonModel("person1", "descendant1", "firstName", "lastName", "f", null, null, null));

        personDAO.insertPeopleIntoDatabase(peopleToAdd);

        assertTrue(userDAO.updateUserPersonID("descendant", "person"));
        assertTrue(userDAO.updateUserPersonID("descendant1", "person1"));

        //assertFalse(userDAO.updateUserPersonID("descendant", "person2"));
    }

    @Test
    public void CselectUser() throws Exception
    {
        UserModel user = userDAO.selectUser("descendant");
        assertNotNull(user);
        assertEquals(user.getPersonID(), "person");

        user = userDAO.selectUser("descendant2");
        assertNull(user);
        assertFalse(userDAO.isSQLError());
    }

    @Test
    public void DclearUserTable() throws Exception
    {
        assertTrue(userDAO.clearUserTable());

        UserModel user = userDAO.selectUser("descendant");
        assertNull(user);

        userDAO.clear();
    }
}