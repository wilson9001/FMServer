package DBActionObjects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import Models.PersonModel;

/**
 * Interacts with Person table in database.
 * @author ajw9001
 */
public class PersonDAO extends DAO
{
    private boolean SQLError, personNotAssignedToUser, invalidPersonID;

    public PersonDAO()
    {
        SQLError = false;
        personNotAssignedToUser = false;
        invalidPersonID = false;
    }

    /**
     * Creates person table in database. <strong>Note: If the table already exists in the database, it will be dropped and re-created</strong>
     * @return Whether the table was created.
     */
    public boolean createPersonTable(){

        return false;

    }

    /**
     * Queries the Person table for a specific person.
     * @param PersonID Person to query.
     * @return If found, this returns a Person object. Otherwise this returns <code>null</code>.
     */
    public PersonModel selectPerson (String PersonID, String userID)
    {
        boolean success = false;
        PreparedStatement stmt = null;
        ResultSet results;
        PersonModel resultModel = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("SELECT * FROM PERSON WHERE PERSONID = ? ");

            stmt.setString(1, PersonID);

            results = stmt.executeQuery();

            if(results.next())
            {
                resultModel = new PersonModel(results.getString(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getString(8));

                stmt.close();
                stmt = null;

                stmt = dbConnection.prepareStatement("SELECT * FROM PERSON WHERE PERSONID = ? AND DESCENDANT = ?");
                {
                    stmt.setString(1, PersonID);
                    stmt.setString(2, userID);

                    results = stmt.executeQuery();

                    if(results.next())
                    {
                        success = true;
                    }
                    else
                    {
                        success = false;
                        personNotAssignedToUser = true;
                    }
                }
            }
            else
            {
                success = false;
                invalidPersonID = true;
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
     * Queries the Person table for all persons associated with the current user.
     * @param userID ID of user to gather all data for.
     * @return All person objects associated with that user.
     */
    public ArrayList<PersonModel> selectPeople (String userID)
    {
        boolean success = false;
        PreparedStatement stmt = null;
        ResultSet results;
        ArrayList<PersonModel> resultModelArray = new ArrayList<>();

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("SELECT * FROM PERSON WHERE DESCENDANT = ? ");

            stmt.setString(1, userID);

            results = stmt.executeQuery();

            String personID, descendant, firstName, lastName, gender, father, mother, spouse;
            PersonModel personToAddToArray;

            try
            {
                while (results.next())
                {
                    personID = results.getString(1);
                    descendant = results.getString(2);
                    firstName = results.getString(3);
                    lastName = results.getString(4);
                    gender = results.getString(5);
                    father = results.getString(6);
                    mother = results.getString(7);
                    spouse = results.getString(8);

                    personToAddToArray = new PersonModel(personID, descendant, firstName, lastName, gender, father, mother, spouse);

                    //There will always be at least one person that represents the User.
                    resultModelArray.add(personToAddToArray);
                }

                success = true;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                success = false;
                SQLError = true;
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
                SQLError = true;
            }
        }
        return success ? resultModelArray : null;
    }

    /**
     * Inserts one or more new people rows into the table.
     * @param peopleToInsert A list of people to insert. If there is only 1 person to insert, the list will only contain 1 object.
     * @return A success or error message.
     */
    public String insertPeopleIntoDatabase(ArrayList<PersonModel> peopleToInsert)
    {
        HashMap<String, ArrayList<String>> PersonRelations = new HashMap<>();
        ArrayList<String> RelationsList;// = new ArrayList<>();

        boolean success = false;
        PreparedStatement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("INSERT INTO PERSON (PERSONID, DESCENDANT, FIRSTNAME, LASTNAME, GENDER, FATHER, MOTHER, SPOUSE) VALUES (?, ?, ?, ?, ?, NULL, NULL, NULL)");

            success = true;
            String userID, descendant, firstName, lastName, gender;

            for(PersonModel person : peopleToInsert)
            {
                RelationsList = new ArrayList<>();
                RelationsList.add(person.getFather());
                RelationsList.add(person.getMother());
                RelationsList.add(person.getSpouse());

                PersonRelations.put(person.getPersonID(), RelationsList);
                //RelationsList.clear();

                userID = person.getPersonID();
                descendant = person.getDescendant();
                firstName = person.getFirstName();
                lastName = person.getLastName();
                gender = person.getGender();

                stmt.setString(1, userID);
                stmt.setString(2, descendant);
                stmt.setString(3, firstName);
                stmt.setString(4, lastName);
                stmt.setString(5, gender);

                //System.out.println(stmt.toString());

                if(stmt.executeUpdate() != 1)
                {
                    success = false;
                    break;
                }
            }

            stmt.close();
            stmt = null;

            Set<String> peopleToUpdate = PersonRelations.keySet();

            String father, mother, spouse;

            stmt = dbConnection.prepareStatement("UPDATE PERSON SET FATHER = ?, MOTHER = ?, SPOUSE = ? WHERE PERSONID = ?");
            for(String personToUpdate : peopleToUpdate)
            {
                RelationsList = PersonRelations.get(personToUpdate);

                father = RelationsList.get(0);
                mother = RelationsList.get(1);
                spouse = RelationsList.get(2);

                stmt.setString(1, father);
                stmt.setString(2, mother);
                stmt.setString(3, spouse);
                stmt.setString(4, personToUpdate);

                //RelationsList.clear();

                if(stmt.executeUpdate() != 1)
                {
                    success = false;
                    break;
                }
            }
        }
        catch(SQLException ex)
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
                ex.printStackTrace();
                success = false;
            }
        }

        return success ? insertPeopleSuccess : SQL_errorMsg;
    }

    public boolean deletePeopleFromUser(String userName)
    {
        boolean success = false;
        PreparedStatement stmt = null;

        try
        {
            openConnection();

            stmt = dbConnection.prepareStatement("DELETE FROM PERSON WHERE DESCENDANT = ?");

            stmt.setString(1, userName);

            stmt.executeUpdate();

            stmt.close();

            stmt = dbConnection.prepareStatement("DELETE FROM EVENT WHERE DESCENDANT = ?");

            stmt.setString(1, userName);

            stmt.executeUpdate();

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
        return success;
    }

    public boolean hasSQLError()
    {
        return SQLError;
    }

    public boolean isPersonNotAssignedToUser()
    {
        return personNotAssignedToUser;
    }

    public boolean isInvalidPersonID()
    {
        return invalidPersonID;
    }
}
