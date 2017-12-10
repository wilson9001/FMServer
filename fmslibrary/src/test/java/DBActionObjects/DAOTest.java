package DBActionObjects;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import DataServices.DataGenerator;
import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;
import Models.UserModelTest;
import Requests.LoginRequest;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOTest
{
    private DAO dao;

    @Before
    public void setUp() throws Exception
    {
        dao = new DAO();
    }

    @Test
    public void AopenConnection() throws Exception
    {
            dao.openConnection();
            dao.closeConnection(false);
    }

    @Test
    public void Bclear() throws Exception
    {
        assertEquals(dao.clear(), dao.clearSuccess);
    }

    @Test
    public void Cfill() throws Exception
    {
        UserModel userModel = new UserModel("userName", "password", "email", "firstName", "lastName", "m", "personID");
        ArrayList<UserModel> userToAdd = new ArrayList<>();
        userToAdd.add(userModel);
        UserDAO userDAO = new UserDAO();
        userDAO.insertUsersIntoTable(userToAdd);
        ArrayList<PersonModel> personModels = new ArrayList<>();
        personModels.add(new PersonModel("personID", "userName", "firstName", "lastName", "m", null, null, null));
        new PersonDAO().insertPeopleIntoDatabase(personModels);
        userDAO.updateUserPersonID("userName", "personID");

        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generateGenerationsOfPeople(userModel, 4);

        ArrayList<PersonModel> peopleToAdd = dataGenerator.getGenerationsOfPeople();
        ArrayList<EventModel> eventModels = dataGenerator.getListOfEvents();

        assertNotNull(peopleToAdd);
        assertNotNull(eventModels);

        assertNotEquals(dao.fill(userModel.getUserName(), peopleToAdd, eventModels), dao.SQL_errorMsg);
    }

    @Test
    public void Dlogin() throws Exception
    {
        dao = new DAO();

        ArrayList<String> results = dao.login("userName", "password");
        assertNotNull(results);
        assertEquals(results.get(1), "userName");
        assertEquals(results.get(2), "personID");
        assertNotNull(results.get(0));

        assertNull(dao.login("username1", "password"));
        assertNull(dao.login("userName", "wrongpassword"));
    }

    @Test
    public void Eload() throws Exception
    {
        UserModel userModel = new UserModel("userName", "password", "email", "firstName", "lastName", "m", "personID");
        ArrayList<UserModel> userToAdd = new ArrayList<>();
        userToAdd.add(userModel);

       // ArrayList<PersonModel> personModels = new ArrayList<>();
        //personModels.add(new PersonModel("personID", "userName", "firstName", "lastName", "m", null, null, null));

        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generateGenerationsOfPeople(userModel, 4);

        ArrayList<PersonModel> peopleToAdd = dataGenerator.getGenerationsOfPeople();
        ArrayList<EventModel> eventModels = dataGenerator.getListOfEvents();

        assertNotNull(peopleToAdd);
        assertNotNull(eventModels);

        UserModel newUserModel = new UserModel("userName1", "password", "email", "firstname", "lastname", "f", "personID1");
        userToAdd.add(newUserModel);

        dataGenerator = new DataGenerator();
        dataGenerator.generateGenerationsOfPeople(newUserModel, 4);

        peopleToAdd.addAll(dataGenerator.getGenerationsOfPeople());
        eventModels.addAll(dataGenerator.getListOfEvents());

        assertNotEquals(dao.load(userToAdd, peopleToAdd, eventModels), DAO.SQL_errorMsg);
    }

    /*
    @Test
    public void Fregister() throws Exception
    {
        UserModel newUser = new UserModel("username2", "password", "email", "firstname", "lastname", "m", "personID2");

        assertNotEquals(dao.register(newUser), DAO.SQL_errorMsg);

        assertEquals(dao.register(newUser), DAO.SQL_errorMsg);
    }
*/
    @Test
    public void GcloseConnection() throws Exception
    {
        dao.openConnection();
        dao.closeConnection(false);
        dao.clear();
    }
}