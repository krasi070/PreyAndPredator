import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

	private static final int DEFAULT_BREEDING_AGE = 5;
    private static final int DEFAULT_MAX_AGE = 40;
    private static final double DEFAULT_BREEDING_PROBABILITY = 0.12;
    private static final int DEFAULT_MAX_LITTER_SIZE = 4;
	
	// The age at which a rabbit can start to breed.
    public static int breedingAge = DEFAULT_BREEDING_AGE;
    // The age to which a rabbit can live.
    public static int maxAge = DEFAULT_MAX_AGE;
    // The likelihood of a rabbit breeding.
    public static double breedingProbability = DEFAULT_BREEDING_PROBABILITY;
    // The maximum number of births.
    public static int maxLitterSize = DEFAULT_MAX_LITTER_SIZE;
	// A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
	
    // Individual characteristics (instance fields).

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setAge(0);
        if(randomAge) {
            setAge(rand.nextInt(maxAge));
        }
    }
    
    public static void setDefaultValues() {
    	breedingAge = DEFAULT_BREEDING_AGE;
    	maxAge = DEFAULT_MAX_AGE;
    	breedingProbability = DEFAULT_BREEDING_PROBABILITY;
    	maxLitterSize = DEFAULT_MAX_LITTER_SIZE;
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Animal> newRabbits)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {
        setAge(getAge() + 1);
        if(getAge() > maxAge) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Animal> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc);
            newRabbits.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= breedingProbability) {
            births = rand.nextInt(maxLitterSize) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return getAge() >= breedingAge;
    }
}
