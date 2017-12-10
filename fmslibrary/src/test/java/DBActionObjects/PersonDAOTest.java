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
public class PersonDAOTest
{
    private PersonDAO personDAO;
    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception
    {
        personDAO = new PersonDAO();
        userDAO = new UserDAO();
    }

    @After
    public void tearDown() throws Exception
    {
        //new DAO().clear();
    }

    @Test
    public void AinsertPeopleIntoDatabase() throws Exception
    {
        ArrayList<UserModel> usersToadd = new ArrayList<>();

        usersToadd.add(new UserModel("descendant", "password", "email", "firstName", "lastName", "m", "person"));
        usersToadd.add(new UserModel("descendant1", "password", "email", "firstName", "lastName", "f", "person1"));

        userDAO.insertUsersIntoTable(usersToadd);

        ArrayList<PersonModel> peopleToAdd = new ArrayList<>();

        peopleToAdd.add(new PersonModel("person", "descendant", "firstName", "lastName", "m", null, null, null));
        peopleToAdd.add(new PersonModel("person1", "descendant1", "firstName", "lastName", "f", null, null, null));

        assertEquals(personDAO.insertPeopleIntoDatabase(peopleToAdd), DAO.insertPeopleSuccess);

        peopleToAdd.clear();

        peopleToAdd.add(new PersonModel("person", "descendant", "firstName", "lastName", "m", null, null, null));

        //assertEquals(personDAO.insertPeopleIntoDatabase(peopleToAdd), DAO.SQL_errorMsg);

        peopleToAdd.clear();

        peopleToAdd.add(new PersonModel("person2", "descendant", "firstName", "lastName", "m", "person", "person1", "person3"));
        peopleToAdd.add(new PersonModel("person3", "descendant", "firstName", "lastName", "f", "person", "person1", "person2"));

        assertEquals(personDAO.insertPeopleIntoDatabase(peopleToAdd), DAO.insertPeopleSuccess);
    }

    @Test
    public void BselectPerson() throws Exception
    {
        PersonModel personModel = personDAO.selectPerson("person1", "descendant1");
        assertNotNull(personModel);

        personModel = personDAO.selectPerson("person", "descendant1");
        assertNull(personModel);
        assertTrue(personDAO.isPersonNotAssignedToUser());
        assertFalse(personDAO.isInvalidPersonID());
        assertFalse(personDAO.hasSQLError());

        personModel = personDAO.selectPerson("person4", "descendant");
        assertNull(personModel);
        assertTrue(personDAO.isInvalidPersonID());
        assertFalse(personDAO.hasSQLError());
    }

    @Test
    public void CselectPeople() throws Exception
    {
        ArrayList<PersonModel> people = personDAO.selectPeople("descendant");
        assertNotNull(people);
        assertEquals(people.size(), 3);
        people = personDAO.selectPeople("descendant1");
        assertNotNull(people);
        assertEquals(people.size(), 1);
        personDAO.clear();
    }

}