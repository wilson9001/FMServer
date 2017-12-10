package Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alex on 11/3/2017.
 */
public class PersonModelTest
{
    private PersonModel personModel;

    @Before
    public void setUp() throws Exception
    {
        personModel = new PersonModel("personID", "descendant", "firstName", "lastName", "gender", "father", "mother", "spouse");
    }

    @Test
    public void getPersonID() throws Exception
    {
        assertEquals(personModel.getPersonID(),"personID");
    }

    @Test
    public void getDescendant() throws Exception
    {
        assertEquals(personModel.getDescendant(),"descendant");
    }

    @Test
    public void getFirstName() throws Exception
    {
        assertEquals(personModel.getFirstName(),"firstName");
    }

    @Test
    public void getLastName() throws Exception
    {
        assertEquals(personModel.getLastName(),"lastName");
    }

    @Test
    public void getGender() throws Exception
    {
        assertEquals(personModel.getGender(),"gender");
    }

    @Test
    public void getFather() throws Exception
    {
        assertEquals(personModel.getFather(),"father");
    }

    @Test
    public void setFather() throws Exception
    {
        personModel.setFather("Father");
        assertEquals(personModel.getFather(),"Father");
    }

    @Test
    public void getMother() throws Exception
    {
        assertEquals(personModel.getMother(),"mother");
    }

    @Test
    public void setMother() throws Exception
    {
        personModel.setMother("Mother");
        assertEquals(personModel.getMother(),"Mother");
    }

    @Test
    public void getSpouse() throws Exception
    {
        assertEquals(personModel.getSpouse(),"spouse");
    }

    @Test
    public void setSpouse() throws Exception
    {
        personModel.setSpouse("Spouse");
        assertEquals(personModel.getSpouse(),"Spouse");
    }

}