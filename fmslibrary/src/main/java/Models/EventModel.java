package Models;

/**
 * Represents an event. Used for transmitting data within the server.
 * @author ajw9001
 */
public class EventModel
{

    /**
     * Constructor.
     * @param eventID
     * @param descendant
     * @param person
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public EventModel(String eventID, String descendant, String person, double latitude, double longitude, String country, String city, String eventType, int year)
    {
        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = person;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    private String eventID, descendant, personID, country, city, eventType;
    private double latitude, longitude;
    private int year;

    /**
     * @return Event ID.
     */
    public String getEventID()
    {
        return eventID;
    }

    /*
    public void setEventID(String eventID)
    {
        this.eventID = eventID;
    }*/

    /**
     * @return Descendant.
     */
    public String getDescendant()
    {
        return descendant;
    }
/*
    public void setDescendant(String descendant)
    {
        this.descendant = descendant;
    }
*/

    /**
     * @return Person.
     */
    public String getPerson()
    {
        return personID;
    }
/*
    public void setPerson(String person)
    {
        this.person = person;
    }
*/

    /**
     * @return Latitude.
     */
    public double getLatitude()
    {
        return latitude;
    }

    /*
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }*/

    /**
     * @return Longitude.
     */
    public double getLongitude()
    {
        return longitude;
    }
/*
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
*/

    /**
     * @return Country.
     */
    public String getCountry()
    {
        return country;
    }
/*
    public void setCountry(String country)
    {
        this.country = country;
    }
*/

    /**
     * @return City.
     */
    public String getCity()
    {
        return city;
    }
/*
    public void setCity(String city)
    {
        this.city = city;
    }
*/

    /**
     * @return Event type.
     */
    public String getEventType()
    {
        return eventType;
    }
/*
    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }
*/

    /**
     * @return Year.
     */
    public int getYear()
    {
        return year;
    }
/*
    public void setYear(String year)
    {
        this.year = year;
    }*/
}
