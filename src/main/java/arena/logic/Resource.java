package arena.logic;

public class Resource {
    private static int amount;


    public Resource(int initAmount) {
        amount = initAmount;
    }

    public static int getAmount() {
        return amount;
    }

    public static void setAmount(int amount) {
        Resource.amount = amount;
    }
}
