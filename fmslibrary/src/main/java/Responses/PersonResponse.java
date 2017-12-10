package Responses;

import java.util.ArrayList;

import Models.PersonModel;
import Requests.PersonRequest;

/**
 * Response object for person requests.
 * @author ajw9001
 */
public class PersonResponse
{
    /**
     * Constructor. Used when a single person was requested.
     * @param person
     */
    public PersonResponse(PersonModel person)
    {
        data = null;
        message = null;
        descendant = person.getDescendant();
        personID = person.getPersonID();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        gender = person.getGender();
        father = person.getFather();
        mother = person.getMother();
        spouse = person.getSpouse();
    }

    /**
     * Overloaded constructor. Used when multiple people were requested.
     * @param people
     */
    public PersonResponse(ArrayList<PersonModel> people)
    {
        this.data = new ArrayList<>(people);
        message = null;
        descendant = null;
        personID = null;
        firstName = null;
        lastName = null;
        gender = null;
        father = null;
        mother = null;
        spouse = null;
    }

    /**
     * Overloaded constructor. Used when there was an error processing the requests.
     * @param errorMessage
     */
    public PersonResponse(String errorMessage)
    {
        this.message = errorMessage;
    }

    private ArrayList<PersonModel> data;
    private String message, descendant, personID, firstName, lastName, gender, father, mother, spouse;

    /**
     * @return Error Message
     */
    public String getErrorMessage()
    {
        return message;
    }

    /**
     * @return List of people from request. May be only 1 if a single person was requested.
     */
    public ArrayList<PersonModel> getPeople()
    {
        return data;
    }

    public String getDescendant()
    {
        return descendant;
    }

    public String getPersonID()
    {
        return personID;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getGender()
    {
        return gender;
    }

    public String getFather()
    {
        return father;
    }

    public String getMother()
    {
        return mother;
    }

    public String getSpouse()
    {
        return spouse;
    }
}
