package DataServices;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alex on 10/31/2017.
 */

public class FemaleNames
{
    FemaleNames(){}

    public String getName()
    {
        return data.get(new Random().nextInt(data.size()));
    }

    ArrayList<String> data;
}
