package fooddeliverysystem.server;
import java.io.Serializable;
public class Food implements Serializable{
 private int id;
    private String foodName;
    private int foodQuantity;
    private double foodPrice;

    public Food() {

    }

    public Food(int id, String foodName, int foodQuantity, double foodPrice) {
        this.id = id;
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
        this.foodPrice = foodPrice;
    }
      public Food(Food food) {
        this.id = food.getId();
        this.foodName = food.getFoodName();
        this.foodQuantity = food.getFoodQuantity();
        this.foodPrice = food.getFoodPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }
}
