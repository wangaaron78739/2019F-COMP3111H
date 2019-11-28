package arena.logic;

/**
 * Class to store the Resources
 * @author Aaron WANG
 */
public class Resource {
    private static int resourceAmount;

    /**
     * Constructor for Resource Object
     * @param initAmount initial resource amount
     */
    public Resource(int initAmount) {
        resourceAmount = initAmount;
    }

    /**
     * Getter method for resourceAmount
     * @return current resource amount
     */
    public static int getResourceAmount() {
        return resourceAmount;
    }

    /**
     * Deduct amount from resource object
     * @param amount amount to be deducted
     * @return boolean of whether it can be deducted (true if yes)
     */
    public static boolean deductAmount(int amount) {
        if (canDeductAmount(amount)) {
            resourceAmount -= amount;
            return true;
        }
        return false;
    }

    /**
     * Determine whether there is enough resource to deduct a certain amount
     * @param am amount to be duducted
     * @return boolean of whether it can be deducted (true if yes)
     */
    public static boolean canDeductAmount(int am) {
        return resourceAmount >= am && am >=0;
    }

    /**
     * Setter function for resourceAmount
     * @param resourceAmount new value for resourceAmount
     */
    public static void setResourceAmount(int resourceAmount) {
        Resource.resourceAmount = resourceAmount;
    }

    /**
     * Add a certain amount to the resource
     * @param resourceAmount amount to be added
     */
    public static void addResourceAmount(int resourceAmount) {
        Resource.resourceAmount += resourceAmount;
    }
}
