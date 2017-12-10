package DBActionObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Models.UserModel;

/**
 * Created by Alex on 10/13/2017.
 */

/**
 * Interacts with User table in database.
 * @author ajw9001
 */
public class UserDAO extends DAO
{
    //If this is true and a null is returned from a SELECT operation, there was an error.
    private boolean SQLError;

    public UserDAO()
    {
        SQLError = false;
    }

    /**
     * Creates user table in database. <strong>Note: If the table already exists, it will be dropped and re-created. This will also cause all People, AuthTokens, and Events to be deleted from their respective tables to prevent orphaned records. It will also remove foreign key constraints on the other tables, and you will need to re-create them as well to re-add the foreign key constraints.</strong>
     * @return Whether or not the table was successfully created.
     */
    public boolean createUserTable()
    {
        boolean success = false;
        Statement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.createStatement();

            stmt.executeUpdate("DROP TABLE IF EXISTS USER");
            stmt.executeUpdate("CREATE TABLE `USER` (`PERSONID` TEXT UNIQUE,`USERNAME` TEXT NOT NULL UNIQUE,`PASSWORD` TEXT NOT NULL,`EMAIL` TEXT NOT NULL,`FIRSTNAME` TEXT NOT NULL,`LASTNAME` TEXT NOT NULL,`GENDER` TEXT NOT NULL CHECK(GENDER = 'm' OR GENDER = 'f'), FOREIGN KEY(`PERSONID`) REFERENCES `PERSON`(`PERSONID`) ON DELETE SET NULL, PRIMARY KEY(`USERNAME`))");

            success = true;
        }
        catch (SQLException ex)
        {
            success = false;
        }
        finally
        {
                try
                {
                    if(stmt != null)
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
        return success;
    }

    /**
     * Clears User table. <strong>NOTE: To prevent orphaned records, this operation will also delete all rows in the AuthToken, Person, and Event tables.</strong>
     * @return If the transaction was successful.
     */
    public boolean clearUserTable()
    {
        boolean success = false;
        Statement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.createStatement();

            stmt.executeUpdate("DELETE FROM USER");
            success = true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();

            success = false;
        }
        finally
        {
                try
                {
                    if(stmt != null)
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
        return success;
    }

    /**
     * Queries the User table in the database for a specified user.
     * @param userName The user to query in the table.
     * @return If the User is found, this returns a User object. Otherwise, this returns <code>null</code>.
     */
    public UserModel selectUser (String userName)
    {
        boolean success = false;
        PreparedStatement stmt = null;
        ResultSet results;
        UserModel resultModel = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("SELECT * FROM USER WHERE USERNAME = ? ");

            stmt.setString(1, userName);

            results = stmt.executeQuery();

            if(results.next())
            {
                resultModel = new UserModel(results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getString(1));
                success = true;
            }
            else
            {
                success = false;
            }
        }
        catch (SQLException ex)
        {
            success = false;
            SQLError = true;
        }
        finally
        {
                try
                {
                    if(stmt != null)
                    {
                        stmt.close();
                    }
                    closeConnection(success);
                }
                catch (SQLException ex)
                {
                    success = false;
                    SQLError = true;
                }
        }
        return success ? resultModel : null;
    }

    /**
     * Inserts 1 or more new users into the User table.
     * @param usersToAdd A list of User objects to be inserted into the table. If only 1 user is being inserted into the table, then the list will only contain 1 object.
     * @return A message containing a success or error status.
     */
    public String insertUsersIntoTable(ArrayList<UserModel> usersToAdd)
    {
        boolean success = false;
        PreparedStatement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("INSERT INTO USER (PERSONID, USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, GENDER) VALUES (NULL, ?, ?, ?, ?, ?, ?)");

            String userName, password, email, firstName, lastName, gender;

            for (UserModel user : usersToAdd)
            {
                userName = user.getUserName();
                password = user.getpassword();
                email = user.getEmail();
                firstName = user.getFirstName();
                lastName = user.getLastName();
                gender = user.getGender();

                stmt.setString(1, userName);
                stmt.setString(2, password);
                stmt.setString(3, email);
                stmt.setString(4, firstName);
                stmt.setString(5, lastName);
                stmt.setString(6, gender);

                if (stmt.executeUpdate() != 1)
                {
                    success = false;
                    break;
                }
            }

            success = true;
        }
        catch (SQLException ex)
        {
            success = false;
        }
        finally
        {
                try
                {
                    if(stmt != null)
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
        return success ? insertUserSuccess : SQL_errorMsg;
    }

    public boolean updateUserPersonID(String userName, String personID)
    {
        boolean success = false;
        PreparedStatement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("UPDATE USER SET PERSONID = ? WHERE USERNAME = ?");

            stmt.setString(1,personID);
            stmt.setString(2, userName);

            int rowsAffected = stmt.executeUpdate();

            success = (rowsAffected == 1);
        }
        catch (SQLException ex)
        {
            success = false;
        }
        finally
        {
                try
                {
                    if(stmt != null)
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
        return success;
    }

    public boolean isSQLError()
    {
        return SQLError;
    }
}
