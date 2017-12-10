package Models;

/**
 * Represents a User. Used for transmitting data within server.
 * @author ajw9001
 */
public class UserModel
{
    //localhost:[port number] will access running server on this machine.

    UserModel()
    {/*Used for Gson*/}

    /**
     * Constructor
     * @param userName
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     * @param personID
     */
    public UserModel(String userName, String password, String email, String firstName, String lastName, String gender, String personID)
    {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }


    private String userName, password, email, firstName, lastName, gender, personID;

    /**
     * @return User name.
     */
    public String getUserName()
    {
        return userName;
    }
/*
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
*/

    /**
     * @return Password.
     */
    public String getpassword()
    {
        return password;
    }

  /*  public void setPassword(String password)
    {
        Password = password;
    }
*/

    /**
     * @return Email.
     */
    public String getEmail()
    {
        return email;
    }
/*
    public void setEmail(String email)
    {
        this.email = email;
    }
*/

    /**
     * @return First name.
     */
    public String getFirstName()
    {
        return firstName;
    }
/*
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
*/

    /**
     * @return Last name.
     */
    public String getLastName()
    {
        return lastName;
    }
/*
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
*/

    /**
     * @return Gender.
     */
    public String getGender()
    {
        return gender;
    }
/*
    public void setGender(String gender)
    {
        this.gender = gender;
    }
*/

    /**
     * @return Person ID.
     */
    public String getPersonID()
    {
        return personID;
    }
/*
    public void setPersonID(String personID)
    {
        this.personID = personID;
    }*/
}
