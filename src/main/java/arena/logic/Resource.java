package arena.logic;

public class Resource {
    private static int resourceAmount;


    public Resource(int initAmount) {
        resourceAmount = initAmount;
    }

    public static int getResourceAmount() {
        return resourceAmount;
    }
    public static boolean deductAmount(int amount) {
        if (canDeductAmount(amount)) {
            resourceAmount -= amount;
            return true;
        }
        return false;
    }
    public static boolean canDeductAmount(int am) {
        return resourceAmount >= am;
    }
    public static void setResourceAmount(int resourceAmount) {
        Resource.resourceAmount = resourceAmount;
    }
    public static void addResourceAmount(int resourceAmount) {
        Resource.resourceAmount += resourceAmount;
    }
}
