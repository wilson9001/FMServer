package DBActionObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Models.EventModel;

/**
 * Interacts with Event table in database.
 * @author ajw9001
 */
public class EventDAO extends DAO
{
    //If a query returns no results but this is set to true, then there was an error in the SELECT.
    private boolean SQLError, eventIsNotAssignedToUser;

    public EventDAO()
    {
        SQLError = false;
        eventIsNotAssignedToUser = false;
    }

    /**
     * Creates new Event table in database. <strong>Note: If the table already exists in the database, it will be dropped before it is re-created.</strong>
     * @return Whether the table was created or not.
     */
    public boolean createEventTable(){return false;}

    /**
     * Inserts 1 or more event rows into the Events table.
     * @param eventsToInsert ArrayList of Event Model objects to translate and insert into the database.
     * @return A success or error message.
     */
    public String insertEventsIntoTable(ArrayList<EventModel> eventsToInsert)
    {
        boolean success = false;
        PreparedStatement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("INSERT INTO EVENT (EVENTID, DESCENDANT, PERSON, LATITUDE, LONGITUDE, COUNTRY, CITY, EVENTTYPE, YEAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            success = true;
            String eventID, descendant, person, country, city, eventType;
            double latitude, longitude;
            int year;

            for(EventModel event : eventsToInsert)
            {
                eventID = event.getEventID();
                descendant = event.getDescendant();
                person = event.getPerson();
                latitude = event.getLatitude();
                longitude = event.getLongitude();
                country = event.getCountry();
                city = event.getCity();
                eventType = event.getEventType();
                year = event.getYear();

                stmt.setString(1, eventID);
                stmt.setString(2, descendant);
                stmt.setString(3, person);
                stmt.setDouble(4, latitude);
                stmt.setDouble(5, longitude);
                stmt.setString(6, country);
                stmt.setString(7, city);
                stmt.setString(8, eventType);
                stmt.setInt(9, year);

                if(stmt.executeUpdate() != 1)
                {
                    success = false;
                    break;
                }
            }
        }
        catch(SQLException ex)
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
        return success ? insertEventSuccess : SQL_errorMsg;
    }

    /**
     * Queries Event table for a specific event.
     * @param eventID ID of event to query.
     * @return If the event is found, this return an Event object. Otherwise it returns <code>null</code>.
     */
    public EventModel selectEvent (String eventID, String userID)
    {
        boolean success = false;
        PreparedStatement stmt = null;
        ResultSet results;
        EventModel resultModel = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("SELECT * FROM EVENT WHERE EVENTID = ? ");

            stmt.setString(1, eventID);

            results = stmt.executeQuery();

            if(results.next())
            {
                resultModel = new EventModel(results.getString(1), results.getString(2),results.getString(3),results.getDouble(4),results.getDouble(5),results.getString(6),results.getString(7),results.getString(8),results.getInt(9));

                stmt.close();
                stmt = null;

                stmt = dbConnection.prepareStatement("SELECT * FROM EVENT WHERE EVENTID = ? AND DESCENDANT = ?");

                stmt.setString(1, eventID);
                stmt.setString(2, userID);

                results = stmt.executeQuery();

                if(results.next())
                {
                    success = true;
                }
                else
                {
                    success = false;
                    eventIsNotAssignedToUser = true;
                }
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
     * Queries Event table for all events related to a specific user. This means all of their ancestor's events are being queried.
     * @param userID ID of user to query in Events table.
     * @return If the user has events, this will return an arrayList of events. Otherwise this returns <code>null</code>.
     */
    public ArrayList<EventModel> selectAllEvents (String userID)
    {
        boolean success = false;
        PreparedStatement stmt = null;
        ResultSet results;
        ArrayList<EventModel> resultModelArray = new ArrayList<>();

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("SELECT * FROM EVENT WHERE DESCENDANT = ? ");

            stmt.setString(1, userID);

            results = stmt.executeQuery();

            if(results.next())
            {
                resultModelArray.add(new EventModel(results.getString(1), results.getString(2),results.getString(3),results.getDouble(4),results.getDouble(5),results.getString(6),results.getString(7),results.getString(8),results.getInt(9)));

                while(results.next())
                {
                    resultModelArray.add(new EventModel(results.getString(1), results.getString(2),results.getString(3),results.getDouble(4),results.getDouble(5),results.getString(6),results.getString(7),results.getString(8),results.getInt(9)));
                }

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
                if (stmt != null)
                {
                    stmt.close();
                }
                closeConnection(success);
            }
            catch (SQLException ex)
            {
                success = false;
                SQLError = false;
            }
        }
        return success ? resultModelArray : null;
    }

    public boolean isSQLError()
    {
        return SQLError;
    }

    public boolean isEventIsNotAssignedToUser()
    {
        return eventIsNotAssignedToUser;
    }
}