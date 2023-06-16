package classes;

import java.util.LinkedList;

public class Order {
    private int id;
    private LinkedList<Food> foodList;
    private int orderOwnerID;
    private String deliveryID;

    public Order() {
        foodList= new LinkedList<>();
    }



    public Order(LinkedList<Food> foodList, int orderOwnerID, String deliveryID,int id) {
        this.foodList = foodList;
        this.orderOwnerID = orderOwnerID;
        this.deliveryID = deliveryID;
        this.id=id;
    }



    public double genreteFinalPrice(){
        double price=0;
        for (int i = 0; i < foodList.size(); i++) {
            price+=foodList.get(i).getFoodPrice()*foodList.get(i).getFoodQuantity();
        }
        return price;
    }



    // Setters and Getters
    public LinkedList<Food> getFoodList() {
        return foodList;
    }

    public void addToFoodList(Food food) {
        this.foodList.add(food);
    }

    public int getOrderOwnerID() {
        return orderOwnerID;
    }

    public void setOrderOwnerID(int orderOwnerID) {
        this.orderOwnerID = orderOwnerID;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
