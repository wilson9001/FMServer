package DataServices;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import DBActionObjects.AuthTokenDAO;
import DBActionObjects.DAO;
import Models.AuthTokenModel;
import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;
import Requests.ClearRequest;
import Requests.EventRequest;
import Requests.FillRequest;
import Requests.LoadRequest;
import Requests.LoginRequest;
import Requests.PersonRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.FillResponse;
import Responses.LoadResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServicesTest
{
    private Services services;
    @Before
    public void setUp() throws Exception
    {
        services = new Services();
        UserModel newUser = new UserModel("username", "password", "email", "firstname", "lastname", "m", "personID");

        //new DAO().register(newUser);
    }

    @After
    public void tearDown() throws Exception
    {
        //new DAO().clear();
    }

    @Test
    public void AprocessRequest() throws Exception
    {
        //UserModel newUser = new UserModel("username", "password", "email", "firstname", "lastname", "m", "personID");

        UserModel userModel = new UserModel("username", "password", "email", "firstName", "lastName", "m", "personID");
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

        UserModel newUserModel = new UserModel("username1", "password", "email", "firstname", "lastname", "f", "personID1");
        userToAdd.add(newUserModel);

        dataGenerator = new DataGenerator();
        dataGenerator.generateGenerationsOfPeople(newUserModel, 4);

        peopleToAdd.addAll(dataGenerator.getGenerationsOfPeople());
        eventModels.addAll(dataGenerator.getListOfEvents());

        LoadRequest loadRequest = new LoadRequest(userToAdd, peopleToAdd, eventModels);

        LoadResponse loadResponse = services.processRequest(loadRequest);

        String result = loadResponse.getResult();

        assertNotEquals(result, Services.internalSeverError);
       assertNotEquals(result, Services.badParameters);
    }

    @Test
    public void BprocessRequest1() throws Exception
    {
        LoginResponse loginResponse = services.processRequest(new LoginRequest("username", "password"));

        assertNull(loginResponse.getErrorMessage());
        assertEquals(loginResponse.getPersonID(), "personID");
        assertEquals(loginResponse.getUserName(), "username");
        assertNotNull(loginResponse.getAuthToken());
    }

    @Test
    public void CprocessRequest2() throws Exception
    {
        AuthTokenModel authTokenModel = new AuthTokenDAO().selectAuthTokenByUser("username");

        PersonResponse personResponse = services.processRequest(new PersonRequest(authTokenModel.getToken(), "personID"));

        assertNull(personResponse.getErrorMessage());
        assertEquals(personResponse.getPersonID(), "personID");
        assertEquals(personResponse.getDescendant(), "username");
        assertEquals(personResponse.getFirstName(), "firstName");
        assertEquals(personResponse.getLastName(), "lastName");
        assertEquals(personResponse.getGender(), "m");
        assertNotNull(personResponse.getFather());
        assertNotNull(personResponse.getMother());
        assertNull(personResponse.getPeople());

        personResponse = services.processRequest(new PersonRequest(authTokenModel.getToken(), null));

        assertNull(personResponse.getErrorMessage());
        assertNull(personResponse.getFather());
        assertNull(personResponse.getMother());
        assertNull(personResponse.getGender());
        assertNull(personResponse.getFirstName());
        assertNull(personResponse.getLastName());
        assertNull(personResponse.getDescendant());
        assertNull(personResponse.getPersonID());
        assertNull(personResponse.getSpouse());
        assertNotNull(personResponse.getPeople());
        assertFalse(personResponse.getPeople().isEmpty());

        personResponse = services.processRequest(new PersonRequest("badAuthToken", null));

        assertEquals(personResponse.getErrorMessage(), Services.invalidAuthToken);

        RegisterRequest registerRequest = new RegisterRequest("username1", "psword", "email", "first", "last", "m");

        new Services().processRequest(registerRequest);

        LoginResponse loginResponse = new Services().processRequest(new LoginRequest("username1", "psword"));

        services = new Services();

        String authTokenString = loginResponse.getAuthToken(); //authTokenModel.getToken();

        personResponse = services.processRequest(new PersonRequest(authTokenString, "personID"));
//
        assertEquals(personResponse.getErrorMessage(), Services.badParameters);
    }

    @Test
    public void DprocessRequest3() throws Exception
    {
        RegisterResponse registerResponse = services.processRequest(new RegisterRequest("userName2", "password", "email", "firstname", "lastname", "f"));

        assertNull(registerResponse.getErrorMessage());

        assertNotNull(registerResponse.getPersonID());
        assertEquals(registerResponse.getUserName(), "userName2");

        registerResponse = services.processRequest(new RegisterRequest("userName2", "password", "email", "firstname", "lastname", "f"));

        assertEquals(registerResponse.getErrorMessage(), Services.userNameAlreadyUsed);
    }

    @Test
    public void EprocessRequest5() throws Exception
    {
        FillResponse fillResponse = services.processRequest(new FillRequest("username1"));

        assertNotEquals(fillResponse.getResult(), Services.internalSeverError);
        assertNotEquals(fillResponse.getResult(), Services.badParameters);

    }

    @Test
    public void FprocessRequest6() throws Exception
    {
        AuthTokenModel authTokenModel = new AuthTokenDAO().selectAuthTokenByUser("username");
        EventResponse eventResponse = services.processRequest(new EventRequest(authTokenModel.getToken()));

        assertNull(eventResponse.getMessage());
        assertNotNull(eventResponse.getEvents());
    }

    @Test
    public void GprocessRequest4() throws Exception
    {
        assertEquals(DAO.clearSuccess, services.processRequest(new ClearRequest()).getResponse());
    }
}