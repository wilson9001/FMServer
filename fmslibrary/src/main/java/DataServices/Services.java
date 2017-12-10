package DataServices;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.crypto.Data;

import DBActionObjects.*;
import Models.*;
import Requests.*;
import Responses.*;

/**
 * This class handles the requests coming from the server and returns the appropriate response object.
 * @author ajw9001
 */
public class Services
{
    /**
     * Overloaded service method. Returns response based on request object type.
     * @param loadReq
     * @return Load response object.
     */
    public LoadResponse processRequest(LoadRequest loadReq)
    {
        if(loadReq == null)
        {
            return new LoadResponse(internalSeverError);
        }

        ArrayList<UserModel> usersToAdd = loadReq.getUsersToAdd();
        ArrayList<PersonModel> peopleToAdd = loadReq.getPeopleToAdd();
        ArrayList<EventModel> eventsToAdd = loadReq.getEventsToAdd();

        DAO database = new DAO();

        if(usersToAdd == null || peopleToAdd == null || eventsToAdd == null || usersToAdd.size() == 0 || peopleToAdd.size() == 0 || eventsToAdd.size() == 0)
        {
            return new LoadResponse(badParameters);
        }

        String results = database.load(usersToAdd, peopleToAdd, eventsToAdd);

        if(results.equals(DAO.SQL_errorMsg))
        {
            results = badParameters;
        }

        return new LoadResponse(results);
    }

    /**
     * Overloaded service method. Returns response based on request object type.
     * @param loginReq
     * @return Login response object.
     */
    public LoginResponse processRequest(LoginRequest loginReq)
    {
        if (loginReq == null)
        {
            return new LoginResponse(internalSeverError);
        }

        String userName = loginReq.getUserName();
        String password = loginReq.getPassword();

        if (userName == null || password == null || userName.isEmpty() || password.isEmpty())
        {
            return new LoginResponse(badParameters);
        }

        DAO database = new DAO();

        ArrayList<String> result = database.login(userName, password);

        if (result == null)
        {
            return new LoginResponse(badParameters);
        }
        else
        {
            if (result.size() == 1)
            {
                return new LoginResponse(internalSeverError);
            }

            else
            {
                return new LoginResponse(result.get(0), result.get(1), result.get(2));
            }
        }
    }

    /**
     * Overloaded service method. Returns response based on request object type.
     * @param personReq
     * @return Person response object.
     */
    public PersonResponse processRequest (PersonRequest personReq)
    {
        if(personReq == null)
        {
            return new PersonResponse(internalSeverError);
        }

        String tokenIn = personReq.getAuthToken();
        String personIn = personReq.getPersonID();

        if(tokenIn == null || tokenIn.isEmpty())
        {
            return new PersonResponse(badParameters);
        }

        AuthTokenDAO tokenTable = new AuthTokenDAO();

        AuthTokenModel token = tokenTable.selectAuthToken(tokenIn);

        if(token == null)
        {
            if(tokenTable.isSQLError())
            {
                return new PersonResponse(internalSeverError);
            }

            return new PersonResponse(invalidAuthToken);
        }

        String userName = token.getUserID();

        PersonDAO personTable = new PersonDAO();

        if(personIn == null)
        {
            ArrayList<PersonModel> peopleResults = personTable.selectPeople(userName);

            if(peopleResults == null)
            {
                return new PersonResponse(internalSeverError);
            }

            return new PersonResponse(peopleResults);
        }

        if(personIn.isEmpty())
        {
            return new PersonResponse(badParameters);
        }

        PersonModel personResult = personTable.selectPerson(personIn,userName);

        if(personResult == null)
        {
            if(personTable.isInvalidPersonID())
            {
                return new PersonResponse(badParameters);
            }
            if(personTable.isPersonNotAssignedToUser())
            {
                return new PersonResponse(notAssociatedWithUser);
            }
            if(personTable.hasSQLError())
            {
                return new PersonResponse(internalSeverError);
            }
        }
        return new PersonResponse(personResult);
    }

    /**
     * Overloaded service method. Returns response based on request object type.
     * @param regReq
     * @return Registration response object.
     */
    public RegisterResponse processRequest (RegisterRequest regReq)
    {
        System.out.println("Register service called.");

        if(regReq == null)
        {
            return new RegisterResponse(internalSeverError);
        }

        String userName, password, email, firstName, lastName, gender;

        userName = regReq.getUserName();
        password = regReq.getPassword();
        email = regReq.getEmail();
        firstName = regReq.getFirstName();
        lastName = regReq.getLastName();
        gender = regReq.getGender();

        if(userName == null || password == null || email == null || firstName == null || lastName == null || gender == null || userName.isEmpty() || password.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty())
        {
            return new RegisterResponse(badParameters);
        }

        UserModel newUser = new UserModel(userName, password, email, firstName, lastName, gender, DataGenerator.generateUUID());

        UserDAO userTable = new UserDAO();

        ArrayList<UserModel> userToAdd = new ArrayList<>();
        userToAdd.add(newUser);

        String result = userTable.insertUsersIntoTable(userToAdd);

        if(result.equals(DAO.SQL_errorMsg))
        {
            UserModel userCheck = userTable.selectUser(userName);

            //Either there was a SQL error, or the previous insert failed to add the userName even though it wasn't in the database.
            if(userCheck == null)
            {
                System.out.println("User was not added into database or lookup of user failed!");
                return new RegisterResponse(internalSeverError);
            }

            //The previous insert failed because the username already exists in the database.
            return new RegisterResponse(userNameAlreadyUsed);
        }

        System.out.println("User inserted successfully");

        DataGenerator dataGenerator = new DataGenerator();

        dataGenerator.generateGenerationsOfPeople(newUser, 0);

        ArrayList<PersonModel> peopleToAdd = dataGenerator.getGenerationsOfPeople();

        PersonDAO personTable = new PersonDAO();

        boolean updateSucess = false;

        //entry point
        try
        {
            result = personTable.insertPeopleIntoDatabase(peopleToAdd);
            updateSucess = userTable.updateUserPersonID(newUser.getUserName(), newUser.getPersonID());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if(result.equals(DAO.SQL_errorMsg) || !updateSucess)
        {
            System.out.println("Inserting person for user or updating user with personID failed!");
            return new RegisterResponse(internalSeverError);
        }

        FillRequest fillRequest = new FillRequest(newUser.getUserName());

        FillResponse fillResponse = processRequest(fillRequest);

        if(fillResponse.getResult().equals(DAO.SQL_errorMsg))
        {
            System.out.println("Fill in DB failed!");
            return new RegisterResponse(internalSeverError);
        }

        //create new AuthToken for user
        AuthTokenModel newToken = dataGenerator.generateAuthToken(userName);

        AuthTokenDAO tokenTable = new AuthTokenDAO();

        String tokenResults = tokenTable.insertToken(newToken);

        if(tokenResults.equals(DAO.SQL_errorMsg))
        {
            System.out.println("Inserting authToken failed!");
            return new RegisterResponse(internalSeverError);
        }

        return new RegisterResponse(newToken.getToken(), userName, newUser.getPersonID());
    }

    public ClearResponse processRequest (ClearRequest clearReq)
    {
        DAO database = new DAO();

        String results = database.clear();

        if(results.equals(DAO.SQL_errorMsg))
        {
            results = internalSeverError;
        }

        return new ClearResponse(results);
    }

    public FillResponse processRequest(FillRequest fillReq)
    {
        System.out.println("Fill service called.");

        DataGenerator dataGenerator = new DataGenerator();

        UserModel seedForTree = new UserDAO().selectUser(fillReq.getUserName());

        //PersonID already set
        dataGenerator.generateGenerationsOfPeople(seedForTree, fillReq.getGenerations());

        DAO database = new DAO();

        ArrayList<PersonModel> peopleToInsert = dataGenerator.getGenerationsOfPeople();
        ArrayList<EventModel> eventsToAdd = dataGenerator.getListOfEvents();

        String result = database.fill(fillReq.getUserName(), peopleToInsert, eventsToAdd);

        if(result.equals(DAO.SQL_errorMsg))
        {
            result = internalSeverError;
        }

        return new FillResponse(result);
    }

    public EventResponse processRequest(EventRequest eventReq)
    {
        if(eventReq == null)
        {
            return new EventResponse(internalSeverError);
        }

        String tokenIn = eventReq.getAuthToken();

        if(tokenIn == null || tokenIn.isEmpty())
        {
            return new EventResponse(invalidAuthToken);
        }

        AuthTokenDAO tokenTable = new AuthTokenDAO();

        AuthTokenModel token = tokenTable.selectAuthToken(tokenIn);

        if(token == null)
        {
            if(tokenTable.isSQLError())
            {
                return new EventResponse(internalSeverError);
            }

            EventResponse eventRes = new EventResponse(invalidAuthToken);
            return eventRes;
        }

        String userName = token.getUserID();

        String eventID = eventReq.getEventID();

        EventDAO eventTable = new EventDAO();

        if(eventID == null)
        {
            ArrayList<EventModel> events = eventTable.selectAllEvents(userName);

            if (events == null && eventTable.isSQLError())
            {
                    return new EventResponse(DAO.SQL_errorMsg);
            }

            return new EventResponse(events);
        }
        else
        {
            EventModel event = eventTable.selectEvent(eventID, userName);

            if(event == null)
            {
                EventResponse eventResponse = new EventResponse(badParameters);

                if(eventTable.isEventIsNotAssignedToUser())
                {
                    eventResponse.setMessage(notAssociatedWithUser);
                }
                if(eventTable.isSQLError())
                {
                    eventResponse.setMessage(DAO.SQL_errorMsg);
                }

                return eventResponse;
            }

            return new EventResponse(event);
        }
    }

    private static final int defaultGenerationsToCreate = 4;
   // public static final String SQL_errorMsg = "SQL Error!";
    public static final String badParameters = "Bad parameters.";
    public static final String internalSeverError = "Internal Sever Error!";
    public static final String invalidAuthToken = "Invalid AuthToken!";
    public static final String notAssociatedWithUser = "The requested item does not belong to this user!";
    public static final String userNameAlreadyUsed = "Username already in use.";
}
