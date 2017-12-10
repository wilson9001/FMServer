package DataServices;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import Models.*;

/**
 * Created by Alex on 10/28/2017.
 */

public class DataGenerator
{
    public static final String generateUUID()
    {
        return UUID.randomUUID().toString().replace("-","");
    }

    public DataGenerator()
    {
        generationsOfPeople = new ArrayList<>();
        listOfEvents = new ArrayList<>();
        generationsToCreate = -1;
        locations = null;
        maleNames = null;
        femaleNames = null;
        lastNames = null;
    }

    public AuthTokenModel generateAuthToken(String userID)
    {
        String newToken = generateUUID();

        LocalDateTime dateCreated = LocalDateTime.now();

        StringBuilder dayOfYearTimeString = new StringBuilder();

        dayOfYearTimeString.append(dateCreated.getDayOfYear());
        dayOfYearTimeString.append(dateCreated.getHour());
        dayOfYearTimeString.append(dateCreated.getMinute());

        int dayOfYearTimeInt = Integer.valueOf(dayOfYearTimeString.toString());

        return new AuthTokenModel(newToken, userID, dayOfYearTimeInt);
    }

    private boolean keepGoing(int generationDistance)
    {
        return generationsToCreate > generationDistance;
    }

    public boolean generateGenerationsOfPeople(UserModel seedForTree, int generations2Create)
    {
        try
        {
            Gson gson = new Gson();

            Reader fileReader = new FileReader("C:\\Users\\Alex\\AndroidStudioProjects\\Application\\fmslibrary\\libs\\json\\locations.json");

            locations = gson.fromJson(fileReader, LocationData.class);

            fileReader.close();

            fileReader = new FileReader("C:\\Users\\Alex\\AndroidStudioProjects\\Application\\fmslibrary\\libs\\json\\mnames.json");

            maleNames = gson.fromJson(fileReader, MaleNames.class);

            fileReader.close();

            fileReader = new FileReader("C:\\Users\\Alex\\AndroidStudioProjects\\Application\\fmslibrary\\libs\\json\\fnames.json");

            femaleNames = gson.fromJson(fileReader, FemaleNames.class);

            fileReader.close();

            fileReader = new FileReader("C:\\Users\\Alex\\AndroidStudioProjects\\Application\\fmslibrary\\libs\\json\\snames.json");

            lastNames = gson.fromJson(fileReader, LastNames.class);

            fileReader.close();

            fileReader = null;
        }
        catch (IOException ex)
        {
            System.out.println("At least one JSON data file is missing!\n");
            ex.printStackTrace();
            return false;
        }

        if(seedForTree == null || generations2Create < 0)
        {
            return false;
        }

        generationsToCreate = generations2Create;

        //create new person representing user.
       String descendant = seedForTree.getUserName();

        PersonModel userPerson = new PersonModel(seedForTree.getPersonID(), descendant, seedForTree.getFirstName(), seedForTree.getLastName(), seedForTree.getGender(), null, null, null);

        generationsOfPeople.add(userPerson);

        int generationDistance = 0;

        if(keepGoing(generationDistance))
        {
            //generate names and UUID's
            String firstNameMale = maleNames.getName();
            String lastNameMale = lastNames.getName();
            String personIDMale = generateUUID();

            String firstNameFemale = femaleNames.getName();
            String lastNameFemale = lastNames.getName();
            String personIDFemale = generateUUID();

            userPerson.setFather(personIDMale);
            userPerson.setMother(personIDFemale);

            PersonModel male = new PersonModel(personIDMale, descendant, firstNameMale, lastNameMale, maleConst, null, null, personIDFemale);
            PersonModel female = new PersonModel(personIDFemale, descendant, firstNameFemale, lastNameFemale, femaleConst, null, null, personIDMale);

            generationsOfPeople.add(male);
            generationsOfPeople.add(female);

            /////////////////////////////////////Generate events for these two people, passing in descendant, personIDMale, personIDFemale, generation distance, and base year.
            int currentYear = Year.now().getValue();

            generationDistance++;

            int baseYearForEvents = generateEvents(descendant, personIDMale, personIDFemale, generationDistance, currentYear);

            if(keepGoing(generationDistance))
            {
              String personIDFather1 = generateUUID();
              String personIDMother1 = generateUUID();

              male.setFather(personIDFather1);
              male.setMother(personIDMother1);

              String personIDFather2 = generateUUID();
              String personIDMother2 = generateUUID();

              female.setFather(personIDFather2);
              female.setMother(personIDMother2);

              generateParents(descendant, personIDFather1, personIDMother1, generationDistance, baseYearForEvents);
              generateParents(descendant, personIDFather2, personIDMother2, generationDistance, baseYearForEvents);
            }
        }
        return true;
    }

    private void generateParents(String descendant, String personIDMale, String personIDFemale, int generationDistance, int baseYearForEvents)
    {
        //generate names
        String firstNameMale = maleNames.getName();
        String lastNameMale = lastNames.getName();

        String firstNameFemale = femaleNames.getName();
        String lastNameFemale = lastNames.getName();

        PersonModel male = new PersonModel(personIDMale, descendant, firstNameMale, lastNameMale, maleConst, null, null, personIDFemale);
        PersonModel female = new PersonModel(personIDFemale, descendant, firstNameFemale, lastNameFemale, femaleConst, null, null, personIDMale);

        generationsOfPeople.add(male);
        generationsOfPeople.add(female);

        generationDistance++;

        /////////////////////////////////////Generate events for these two people, passing in descendant, personIDMale, personIDFemale, generation distance, and base year.
        baseYearForEvents = generateEvents(descendant, personIDMale, personIDFemale, generationDistance, baseYearForEvents);

        if(keepGoing(generationDistance))
        {
            String personIDFather1 = generateUUID();
            String personIDMother1 = generateUUID();

            male.setFather(personIDFather1);
            male.setMother(personIDMother1);

            String personIDFather2 = generateUUID();
            String personIDMother2 = generateUUID();

            female.setFather(personIDFather2);
            female.setMother(personIDMother2);

            generateParents(descendant, personIDFather1, personIDMother1, generationDistance, baseYearForEvents);
            generateParents(descendant, personIDFather2, personIDMother2, generationDistance, baseYearForEvents);
        }
    }

    private int generateEvents(String descendant, String personIDMale, String personIDFemale, int generationDistance, int baseYear)
    {
        int currentYear = Year.now().getValue();

        Random randomNumberGenerator = new Random();
        //---Birth events---
        int baseYearBirths;

        if(generationDistance == 1)
        {
            baseYearBirths = baseYear - (upperBoundOfFirstGenerationAge - randomNumberGenerator.nextInt(maxVariationOf1GenBirths));
        }
        else
        {
            baseYearBirths = baseYear - baseBirthSubtractionAfter1Gen;
        }

        int birthYearMale = baseYearBirths + randomNumberGenerator.nextInt(maxBirthDistance);
        int birthYearFemale = baseYearBirths + randomNumberGenerator.nextInt(maxBirthDistance);

        //get locations for birth events
        Location eventLocation = null;
        try
        {
            eventLocation = locations.getLocation();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        double latitudeMale = eventLocation.latitude;
        double longitudeMale = eventLocation.longitude;
        String countryMale = eventLocation.country;
        String cityMale = eventLocation.city;

        eventLocation = locations.getLocation();

        double latitudeFemale = eventLocation.latitude;
        double longitudeFemale = eventLocation.longitude;
        String countryFemale = eventLocation.country;
        String cityFemale = eventLocation.city;

        EventModel birthMale = new EventModel(generateUUID(), descendant, personIDMale, latitudeMale, longitudeMale, countryMale, cityMale, birthEventConst, birthYearMale);
        EventModel birthFemale = new EventModel(generateUUID(), descendant, personIDFemale, latitudeFemale, longitudeFemale, countryFemale, cityFemale, birthEventConst, birthYearFemale);

        listOfEvents.add(birthMale);
        listOfEvents.add(birthFemale);

        //---Baptism events---//
        EventModel baptismMale = new EventModel(generateUUID(), descendant, personIDMale, latitudeMale, longitudeMale, countryMale, cityMale, baptismEventConst, birthYearMale + randomNumberGenerator.nextInt(maxAgeOfBaptism));
        EventModel baptismFemale = new EventModel(generateUUID(), descendant, personIDFemale, latitudeFemale, longitudeFemale, countryFemale, cityFemale, baptismEventConst, birthYearFemale + randomNumberGenerator.nextInt(maxAgeOfBaptism));

        listOfEvents.add(baptismMale);
        listOfEvents.add(baptismFemale);

        //---Marriage event---//
        Integer birthIntegerMale = birthYearMale;
        Integer birthIntegerFemale = birthYearFemale;

        ArrayList<Integer> birthYears = new ArrayList<>();
        birthYears.add(birthIntegerMale);
        birthYears.add(birthIntegerFemale);

        Collections.sort(birthYears);

        int moreRecentBirthYear = birthYears.get(0);

        int marriageBaseYear = moreRecentBirthYear + baseMarriageAddition;

        int marriageYear = marriageBaseYear + randomNumberGenerator.nextInt(maxMarriageVariation);

        //get location for marriage event
        eventLocation = locations.getLocation();

        double latitudeMarriage = eventLocation.latitude;
        double longitudeMarriage = eventLocation.longitude;
        String countryMarriage = eventLocation.country;
        String cityMarriage = eventLocation.city;

        EventModel marriageMale = new EventModel(generateUUID(), descendant, personIDMale, latitudeMarriage, longitudeMarriage, countryMarriage, cityMarriage, marriageEventConst, marriageYear);
        EventModel marriageFemale = new EventModel(generateUUID(), descendant, personIDFemale, latitudeMarriage, longitudeMarriage, countryMarriage, cityMarriage, marriageEventConst, marriageYear);

        listOfEvents.add(marriageMale);
        listOfEvents.add(marriageFemale);

        int deathYearMale, deathYearFemale;

        //---DEATH---//
        if(generationDistance <= lastGenerationWithLongerLifeSpan)
        {
            deathYearMale = birthYearMale + (maxLifeRecent - randomNumberGenerator.nextInt(maxVariationForLongLifeRecent));
            deathYearFemale = birthYearFemale + (maxLifeRecent - randomNumberGenerator.nextInt(maxVariationForLongLifeRecent));
        }
        else
        {
            deathYearMale = birthYearMale + (maxLifeHistoric - randomNumberGenerator.nextInt(maxVariationForLongLifeHistoric));
            deathYearFemale = birthYearFemale + (maxLifeHistoric - randomNumberGenerator.nextInt(maxVariationForLongLifeHistoric));
        }

        //get locations for death events

        eventLocation = locations.getLocation();

        latitudeMale = eventLocation.latitude;
        longitudeMale = eventLocation.longitude;
        countryMale = eventLocation.country;
        cityMale = eventLocation.city;

        eventLocation = locations.getLocation();
        latitudeFemale = eventLocation.latitude;
        longitudeFemale = eventLocation.longitude;
        countryFemale = eventLocation.country;
        cityFemale = eventLocation.city;

        EventModel deathMale = new EventModel(generateUUID(), descendant, personIDMale, latitudeMale, longitudeMale, countryMale, cityMale, deathEventConst, deathYearMale);
        EventModel deathFemale = new EventModel(generateUUID(), descendant, personIDFemale, latitudeFemale, longitudeFemale, countryFemale, cityFemale, deathEventConst, deathYearFemale);

        if(deathYearMale <= currentYear)
        {
            listOfEvents.add(deathMale);
        }

        if(deathYearFemale <= currentYear)
        {
            listOfEvents.add(deathFemale);
        }

        return baseYearBirths;
    }

    private ArrayList<PersonModel> generationsOfPeople;
    private ArrayList<EventModel> listOfEvents;
    private static final String maleConst = "m";
    private static final String femaleConst = "f";
    private int generationsToCreate;
    private static final int upperBoundOfFirstGenerationAge = 70;
    private static final int maxVariationOf1GenBirths = 31;
    private static final int baseBirthSubtractionAfter1Gen = 22;
    private static final int maxBirthDistance = 5;
    private static final int maxAgeOfBaptism = 3;
    private static final int baseMarriageAddition = 18;
    private static final int maxMarriageVariation = 5;
    private static final int lastGenerationWithLongerLifeSpan = 3;
    private static final int maxLifeRecent = 90;
    private static final int maxVariationForLongLifeRecent = 31;
    private static final int maxLifeHistoric = 60;
    private static final int maxVariationForLongLifeHistoric = 21;
    private static final String birthEventConst = "Birth";
    private static final String baptismEventConst = "Baptism";
    private static final String marriageEventConst = "Marriage";
    private static final String deathEventConst = "Death";
    private LocationData locations;
    private MaleNames maleNames;
    private FemaleNames femaleNames;
    private LastNames lastNames;

    public ArrayList<PersonModel> getGenerationsOfPeople()
    {
        return generationsOfPeople;
    }
    public ArrayList<EventModel> getListOfEvents()
    {
        return listOfEvents;
    }
}
