package DBActionObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Models.AuthTokenModel;

/**
 * Interacts with AuthToken table in database.
 * @author ajw9001
 */
public class AuthTokenDAO extends DAO
{
    //If this is true and a null is returned from a SELECT operation, there was an error.
    private boolean SQLError;

    public AuthTokenDAO()
    {
        SQLError = false;
    }

    /**
     * Queries the AuthToken table to see if a particular token exists.
     * @param token AuthToken to select.
     * @return Model of token or <code>null</code> if the token was not found in the table.
     */
    public AuthTokenModel selectAuthToken (String token)
    {
        boolean success = false;
        PreparedStatement stmt = null;
        ResultSet results;
        AuthTokenModel resultModel = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("SELECT * FROM AUTHTOKEN WHERE TOKEN = ? ");

            stmt.setString(1, token);

            results = stmt.executeQuery();

            if(results.next())
            {
                resultModel = new AuthTokenModel(results.getString(1), results.getString(2), results.getInt(3));
                success = true;
            }
            else
            {
                success = false;
            }

            //resultModel = new UserModel(results.getString(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7));
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
     * Queries the AuthToken table to see if a particular token exists.
     * @param userName AuthToken to select.
     * @return Model of token or <code>null</code> if the token was not found in the table.
     */
    public AuthTokenModel selectAuthTokenByUser (String userName)
    {
        boolean success = false;
        PreparedStatement stmt = null;
        ResultSet results;
        AuthTokenModel resultModel = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("SELECT * FROM AUTHTOKEN WHERE USERID = ? ");

            stmt.setString(1, userName);

            results = stmt.executeQuery();

            if(results.next())
            {
                resultModel = new AuthTokenModel(results.getString(1), results.getString(2), results.getInt(3));
                success = true;
            }
            else
            {
                success = false;
            }

            //resultModel = new UserModel(results.getString(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7));
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
     * Creates the AuthToken table in the database. <strong>Note: If the authToken table already exists in the database, it will be dropped and re-created.</strong>
     * @return If the table was successfully created.
     */
    public boolean createTokenTable() {return false;}

    /**
     * Inserts new token into AuthToken table.
     * @param token Value to insert.
     * @return Success or error message.
     */
    public String insertToken(AuthTokenModel token)
    {
        boolean success = false;
        PreparedStatement stmt = null;

        try
        {
            openConnection();

            String tokenToInsert, userID;
            int dateTimeCreated;

            stmt = dbConnection.prepareStatement("INSERT INTO AUTHTOKEN (TOKEN, USERID, DATETIMECREATED) VALUES (?, ?, ?)");

            tokenToInsert = token.getToken();
            userID = token.getUserID();
            dateTimeCreated = token.getDateTimeCreated();

            stmt.setString(1, tokenToInsert);
            stmt.setString(2, userID);
            stmt.setInt(3, dateTimeCreated);

            success = (stmt.executeUpdate() == 1);
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
        return success ? insertAuthTokenSuccess : SQL_errorMsg;
    }

    public boolean deleteAuthToken(String tokenToDelete)
    {
        boolean success = false;
        PreparedStatement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("DELETE FROM AUTHTOKEN WHERE TOKEN = ? ");

            stmt.setString(1, tokenToDelete);

            success = (stmt.executeUpdate() == 1);
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
