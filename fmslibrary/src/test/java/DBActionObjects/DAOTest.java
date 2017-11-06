package DBActionObjects;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import DataServices.DataGenerator;
import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;
import Models.UserModelTest;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
public class DAOTest
{
    private DAO dao;

    @Before
    public void setUp() throws Exception
    {
        dao = new DAO();
    }

    @Test
    public void openConnection() throws Exception
    {
            dao.openConnection();
    }

    @Test
    public void clear() throws Exception
    {
        assertEquals(dao.clear(), dao.clearSuccess);
    }

    @Test
    public void fill() throws Exception
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

       // dataGenerator = new DataGenerator();

        peopleToAdd.clear();
        eventModels.clear();

        peopleToAdd.add(new PersonModel("couldBeValidID", "userName1", "firstName", "lastName", "m", null, null, null));
        eventModels.add(new EventModel("eventID1", "userName1", "couldBeValidID", 0,0,"country", "city", "eventType", 1));

        assertEquals(dao.fill("userName1", peopleToAdd, eventModels), dao.SQL_errorMsg);
    }

    @Test
    public void load() throws Exception
    {

    }

    @Test
    public void login() throws Exception
    {

    }

    @Test
    public void register() throws Exception
    {

    }

    @Test
    public void closeConnection() throws Exception
    {
        dao.closeConnection(false);
    }
}