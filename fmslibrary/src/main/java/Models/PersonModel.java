package Models;

/**
 * Created by Alex on 10/13/2017.
 */

public class PersonModel
{
    private String personID, descendant, firstName, lastName, gender,
            father, mother, spouse;

    public PersonModel(String PersonID,String Descendant, String FirstName, String LastName, String Gender, String Father, String Mother, String Spouse)
    {
        this.personID = PersonID;
        this.descendant = Descendant;
        this.firstName = FirstName;
        this.lastName = LastName;
        this.gender = Gender;
        this.father = Father;
        this.mother = Mother;
        this.spouse = Spouse;
    }

    public String getPersonID()
    {
        return personID;
    }

    public String getDescendant()
    {
        return descendant;
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

    public void setFather(String father)
    {
        this.father = father;
    }

    public String getMother()
    {
        return mother;
    }

    //Changed these...
    public void setMother(String mother)
    {
        this.mother = mother;
    }

    public String getSpouse()
    {
        return spouse;
    }

    public void setSpouse(String spouse)
    {
        this.spouse = spouse;
    }
}
