package DBActionObjects;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import DataServices.DataGenerator;
import Models.*;

/**
 * Generic Database Access Object. Used as a base class for more specific DAO subclasses, as well as for handling operations on the tables that do not correspond to an exact object represented said tables.
 * All methods of this class are final. The inherited data is the Java DB connection object.
 *@author ajw9001
 */
public class DAO
{
    static
    {
        try
        {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException ex)
        {
            System.out.println("SQLite browser failed!");

            ex.printStackTrace();
        }
    }


    protected Connection dbConnection;

    public void openConnection() throws SQLException
    {
        //String dbName = /*"db" + File.separator +*/ "newFamilyMapDB.sqlite";
        //final String CONNECTION_URL = "JDBC:sqlite:C:/Users/Alex/AndroidStudioProjects/Application/fmslibrary/newFamilyMapDB.sqlite";
        final String CONNECTION_URL = "JDBC:sqlite:FamilyMapDB.db";

        //final String CONNECTION_URL = "jbdc:sqlite:db" + File.separator + "FamilyMapDB.db";

        try
        {
            dbConnection = DriverManager.getConnection(CONNECTION_URL);
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }

        dbConnection.setAutoCommit(false);
        try
        {
            PreparedStatement stmt = dbConnection.prepareStatement("PRAGMA foreign_keys = ON;");
            stmt.execute();
            dbConnection.commit();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void closeConnection(boolean transactionSuccessful) throws SQLException
    {
        if(transactionSuccessful)
        {
            dbConnection.commit();
        }
        else
        {
            dbConnection.rollback();
        }

        dbConnection.close();
        dbConnection = null;
    }

    /**
     * Clears all data from the tables.
     * @return Success or error message.
     */
    public final String clear()
    {
        UserDAO userTable = new UserDAO();

        boolean success = false;

        Statement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.createStatement();

            if (userTable.clearUserTable())
            {
                stmt.execute("DELETE FROM PERSON");
                stmt.execute("DELETE FROM EVENT");
                stmt.execute("DELETE FROM AUTHTOKEN");
                success = true;
            }
        }
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (stmt != null)
                {
                    stmt.close();
                }
                closeConnection(success);
            }
            catch (SQLException ex)
            {
                success = false;
            }
        }

        return success ? clearSuccess : SQL_errorMsg;
    }

    /**
     * Removes any existing Person and Event data for a username and fills the table with the default number of generations' worth of data for said username.
     * @param username ID of user to alter data.
     * @return Success or error message.
     */
    public final String fill(String username, ArrayList<PersonModel> peopleToInsert, ArrayList<EventModel> eventsToAdd)
    {
        PersonDAO personTable = new PersonDAO();
        String resultString = null;

        //This also clears the Event table due to foreign keys.
        if(personTable.deletePeopleFromUser(username))
        {
            //create people. Remember to re-add person representing user as well <--create in Services and pass in.
            //ArrayList<PersonModel> peopleToInsert = new ArrayList<>();

            if(personTable.insertPeopleIntoDatabase(peopleToInsert).equals(insertPeopleSuccess))
            {
                UserDAO userTable = new UserDAO();
                if(userTable.updateUserPersonID(username, peopleToInsert.get(0).getPersonID()))
                {
                    //create events.
                    //ArrayList<EventModel> eventsToAdd = new ArrayList<>();

                    EventDAO eventTable = new EventDAO();
                    if(eventTable.insertEventsIntoTable(eventsToAdd).equals(insertEventSuccess))
                    {
                        StringBuilder successMessage = new StringBuilder();

                        successMessage.append("Successfully added ");
                        successMessage.append(peopleToInsert.size());
                        successMessage.append(" persons and ");
                        successMessage.append(eventsToAdd.size());
                        successMessage.append(" events to the database");

                        resultString = successMessage.toString();
                    }
                    else
                    {
                        resultString = SQL_errorMsg;
                    }
                }
                else
                {
                    resultString = SQL_errorMsg;
                }
            }
            else
            {
                resultString = SQL_errorMsg;
            }
        }
        else
        {
            resultString = SQL_errorMsg;
        }

        return resultString;
    }

    /**
     * Overloaded basic fill method. This version specifies the number of generations' worth of data to add to the table.
     * @param username ID of user to alter data.
     * @param generations Number of generations' worth of data to add.
     * @return Success or error message.
     */
/*    public final String fill(FillRequest req)
    {
        String username = req.getUserName();
        int generations = req.getGenerations();

        PersonDAO personTable = new PersonDAO();
        String resultString = null;

        int personCount = -1;
        int eventCount = -1;

        //This also clears the Event table due to foreign keys.
        if(personTable.deletePeopleFromUser(username))
        {
            //create people. Remember to re-add person representing user as well
            ArrayList<PersonModel> peopleToInsert = new ArrayList<>();

            personCount = peopleToInsert.size();

            if(personTable.insertPeopleIntoDatabase(peopleToInsert).equals(insertPeopleSuccess))
            {
                UserDAO userTable = new UserDAO();
                if(userTable.updateUserPersonID(username, peopleToInsert.get(0).getPersonID()))
                {
                    //create events.
                    ArrayList<EventModel> eventsToAdd = new ArrayList<>();

                    eventCount = eventsToAdd.size();

                    EventDAO eventTable = new EventDAO();
                    if(eventTable.insertEventsIntoTable(eventsToAdd).equals(insertEventSuccess))
                    {
                        StringBuilder successMessage = new StringBuilder();

                        successMessage.append("Successfully added ");
                        successMessage.append(personCount);
                        successMessage.append(" persons and ");
                        successMessage.append(eventCount);
                        successMessage.append(" events to the database");

                        resultString = successMessage.toString();
                    }
                    else
                    {
                        resultString = SQL_errorMsg;
                    }
                }
                else
                {
                    resultString = SQL_errorMsg;
                }
            }
            else
            {
                resultString = SQL_errorMsg;
            }
        }
        else
        {
            resultString = SQL_errorMsg;
        }

        return resultString;
    }*/

    /**
     * Clears all tables and then loads the provided User, Person, and Event objects.
     * @param users Array of User objects to add.
     * @param people Array of Person objects to add.
     * @param events Array of Event objects to add.
     * @return Success or error message.
     */
    public final String load(ArrayList<UserModel> users, ArrayList<PersonModel> people, ArrayList<EventModel> events)
    {
        boolean success;

        if(clear().equals(clearSuccess))
        {
            UserDAO userTable = new UserDAO();

            HashMap<String, String> userIDtoPersonID = new HashMap<>();

            for(UserModel user : users)
            {
                userIDtoPersonID.put(user.getUserName(), user.getPersonID());
            }

            if(userTable.insertUsersIntoTable(users).equals(insertUserSuccess))
            {
                PersonDAO personTable = new PersonDAO();

                if(personTable.insertPeopleIntoDatabase(people).equals(insertPeopleSuccess))
                {
                    Set<String> userNameList = userIDtoPersonID.keySet();

                    success = true;

                    for(String user : userNameList)
                    {
                        if(!userTable.updateUserPersonID(user, userIDtoPersonID.get(user)))
                        {
                            success = false;
                            break;
                        }
                    }

                    if(success)
                    {
                        EventDAO eventTable = new EventDAO();
                        if(!eventTable.insertEventsIntoTable(events).equals(insertEventSuccess))
                        {
                            success = false;
                        }
                    }
                }
                else
                {
                    success = false;
                }
            }
            else
            {
                success = false;
            }
        }
        else
        {
            success = false;
        }
        if(success)
        {
            StringBuilder successMessage = new StringBuilder();

            successMessage.append("Successfully added ");
            successMessage.append(users.size());
            successMessage.append(" users, ");
            successMessage.append(people.size());
            successMessage.append(" persons, and ");
            successMessage.append(events.size());
            successMessage.append(" events to the database.");

            return successMessage.toString();
        }
        else
        {
            return SQL_errorMsg;
        }
    }

    /**
     * Checks if a User exists in the User table.
     * @param username Username to check.
     * @param password Password to check.
     * @return If the User exists, this returns the AuthToken, UserName, and PersonID. If the person does not exist it returns <code>null</code>. Otherwise, this returns an error message.<br><strong>NOTE: This function does not return an AuthToken object. It returns the AuthToken as a string.</strong>
     */
    public final ArrayList<String> login(String username, String password)
    {
        UserDAO userTable = new UserDAO();

        UserModel resultUserModel = userTable.selectUser(username);

        ArrayList<String> resultSet = new ArrayList<>();
        boolean success = true;

        if(resultUserModel != null)
        {
            if(password.equals(resultUserModel.getpassword()))
            {
                AuthTokenDAO tokenTable = new AuthTokenDAO();

                AuthTokenModel resultToken = tokenTable.selectAuthTokenByUser(resultUserModel.getUserName());

                if(resultToken == null)
                {
                    if(tokenTable.isSQLError())
                    {
                        resultSet.add(SQL_errorMsg);
                    }
                    else
                    {
                        //create new authToken and insert into database.
                        DataGenerator dataGenerator = new DataGenerator();

                        AuthTokenModel newToken = dataGenerator.generateAuthToken(resultUserModel.getUserName());

                        if(tokenTable.insertToken(newToken).equals(SQL_errorMsg))
                        {
                            resultSet.add(SQL_errorMsg);
                        }

                        else
                        {
                            //changed
                            resultSet.add(newToken.getToken());
                            resultSet.add(resultUserModel.getUserName());
                            resultSet.add(resultUserModel.getPersonID());
                        }
                    }
                }
                else
                {
                    resultSet.add(resultToken.getToken());
                    resultSet.add(resultUserModel.getUserName());
                    resultSet.add(resultUserModel.getPersonID());
                }
            }
            else
            {
                success = false;
            }
        }
        else
        {
            if(userTable.isSQLError())
            {
                resultSet.add(SQL_errorMsg);
            }
            else
            {
                success = false;
            }
        }
        return success ? resultSet : null;
    }

    /**
     * Creates a new user in the User table.
     * @param newUser User to add.
     * @return If successful, this returns an AuthToken, the Username, and the PersonID. Otherwise, this returns an error message.<br><strong>NOTE: This function does not return an AuthToken object. It returns the AuthToken as a string.</strong>
     */
    /*public final ArrayList<String> register(UserModel newUser)
    {
        UserDAO userTable = new UserDAO();

        ArrayList<String> resultMessages = new ArrayList<>();
        //generate authToken and Person to insert after User is created.

        ArrayList<UserModel> userToAdd = new ArrayList<>();

        userToAdd.add(newUser);

        String insertResults = userTable.insertUsersIntoTable(userToAdd);

        //add person and authToken models and then update User table to show appropriate personID.

        if (insertResults.equals(insertUserSuccess))
        {
            //generate authToken
            AuthTokenModel newToken = null;

            AuthTokenDAO tokenTable = new AuthTokenDAO();

            if(tokenTable.insertToken(newToken).equals(insertAuthTokenSuccess))
            {
                //createPerson

                PersonModel newPerson = null;

                PersonDAO personTable = new PersonDAO();

                ArrayList<PersonModel> personToInsert = new ArrayList<>();

                personToInsert.add(newPerson);

                if(personTable.insertPeopleIntoDatabase(personToInsert).equals(insertPeopleSuccess))
                {
                    if(userTable.updateUserPersonID(newPerson.getPersonID(), newPerson.getDescendant()))
                    {
                        resultMessages.add(newToken.getToken());
                        resultMessages.add(newUser.getUserName());
                        resultMessages.add(newPerson.getPersonID());
                    }
                    else
                    {
                        resultMessages.add(SQL_errorMsg);
                    }
                }
                else
                {
                    resultMessages.add(SQL_errorMsg);
                }
            }
            else
            {
                resultMessages.add(SQL_errorMsg);
            }
        }
        else
        {
            resultMessages.add(SQL_errorMsg);
        }

        return resultMessages;
    }*/

    public static final String clearSuccess = "Tables cleared.";

    public static final String SQL_errorMsg = "SQL Error!";
    protected static final String insertPeopleSuccess =  "All people inserted into database";
    protected static final String insertEventSuccess = "All events inserted into database";
    protected static final String insertUserSuccess = "All users inserted into database";
    protected static final String insertAuthTokenSuccess = "AuthToken successfully inserted";
    private static final int defaultGenerations = 3;
}
