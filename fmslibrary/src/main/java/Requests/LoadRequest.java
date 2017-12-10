package Requests;

import java.util.ArrayList;

import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;

/**
 * Request object for clearing all tables and then loading new data from JSON into tables.
 * @author ajw9001
 */
public class LoadRequest
{
    /**
     * Constructor.
     * @param usersToAdd
     * @param peopleToAdd
     * @param eventsToAdd
     */
    public LoadRequest(ArrayList<UserModel> usersToAdd, ArrayList<PersonModel> peopleToAdd, ArrayList<EventModel> eventsToAdd)
    {
        this.users = usersToAdd;
        this.persons = peopleToAdd;
        this.events = eventsToAdd;
    }

    public LoadRequest(){}

    private ArrayList<UserModel> users;
    private ArrayList<PersonModel> persons;
    private ArrayList<EventModel> events;

    /**
     * @return List of users to add.
     */
    public ArrayList<UserModel> getUsersToAdd()
    {
        return users;
    }

    /**
     * @return List of people to add.
     */
    public ArrayList<PersonModel> getPeopleToAdd()
    {
        return persons;
    }

    /**
     * @return List of events to add.
     */
    public ArrayList<EventModel> getEventsToAdd()
    {
        return events;
    }
}
