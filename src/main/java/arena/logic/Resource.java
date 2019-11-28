package arena.logic;

import static arena.logic.ArenaConstants.INITIAL_RESOURCE_NUM;

/**
 * Class to store the Resources uses Singleton Design Pattern
 * @author Aaron WANG
 */
public final class Resource {
    private int resourceAmount = INITIAL_RESOURCE_NUM;
    private static final Resource INSTANCE = new Resource();
    private Resource() {}


    /**
     * Getter method for resourceAmount
     * @return current resource amount
     */
    public static int getResourceAmount() {
        return INSTANCE.resourceAmount;
    }

    /**
     * Deduct amount from resource object
     * @param amount amount to be deducted
     * @return boolean of whether it can be deducted (true if yes)
     */
    public static boolean deductAmount(int amount) {
        if (canDeductAmount(amount)) {
            INSTANCE.resourceAmount -= amount;
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
        return INSTANCE.resourceAmount >= am && am >=0;
    }

    /**
     * Setter function for resourceAmount
     * @param resourceAmount new value for resourceAmount
     */
    public static void setResourceAmount(int resourceAmount) {
        INSTANCE.resourceAmount = resourceAmount;
    }

    /**
     * Add a certain amount to the resource
     * @param resourceAmount amount to be added
     */
    public static void addResourceAmount(int resourceAmount) {
        INSTANCE.resourceAmount += resourceAmount;
    }
}
