package DataServices;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alex on 10/31/2017.
 */

public class LocationData
{
    LocationData(){}

    public Location getLocation()
    {
        int locationsSize = data.size();
        Random randomInt = new Random();

        return data.get(randomInt.nextInt(locationsSize));
    }

   ArrayList<Location> data;
}
