/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fooddeliverysystem.server;
import fooddeliverysystem.server.Food;
import fooddeliverysystem.server.javaconnect;
import java.net.*;
import java.sql.*;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pCd
 */
public class MyThread implements Runnable{
    Socket socket;
    Connection connection;
    String user;
    String action;
    int id;
    public MyThread(Socket socket, Connection connection) {
        this.socket = socket;
        this.connection = connection;
    }
    

    @Override
    public void run() {
        
        System.out.println("New client entered");
     
        
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;

         try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            user = in.readUTF().trim();
            if(!user.contains("login")&&!user.contains("regist")){
            action = in.readUTF().trim();
            }
            
            if (user.contains("customer")) {
                serveCustomer(socket, in, connection, preparedStatement, resultSet);
            } else if (user.contains("admin")) {
                serveAdmin(socket, in, connection, preparedStatement, resultSet);
            } else if (user.contains("login")) {
                login(socket, in, connection, preparedStatement, resultSet);
            } 
            else if (user.contains("regist")) {
                reister(socket, in, connection, preparedStatement, resultSet);
            }else {
                serveDelivery(socket, in, connection, preparedStatement, resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }

    }
    synchronized void reister(Socket s, DataInputStream in, Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws ClassNotFoundException, SQLException, IOException{
        String queryCustomer = in.readUTF();
        boolean existcustomer = isExist(queryCustomer);
        String QueryDelivery = in.readUTF();
        System.out.println(QueryDelivery);
        boolean existdelivery = isExist(QueryDelivery);
        String QueryAdmin = in.readUTF();
        boolean existadmin = isExist(QueryAdmin);

        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        
        out.writeBoolean(existcustomer);
        out.writeBoolean(existdelivery);
        out.writeBoolean(existadmin);
        String insert=in.readUTF();
        Statement statement = connection.createStatement();
         statement.executeUpdate(insert);
         
        
    }
    synchronized void login(Socket s, DataInputStream in, Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws ClassNotFoundException, SQLException, IOException {
        String queryCustomer = in.readUTF();
        boolean existcustomer = isExist(queryCustomer);
        String QueryDelivery = in.readUTF();
        System.out.println(QueryDelivery);
        boolean existdelivery = isExist(QueryDelivery);
        String QueryAdmin = in.readUTF();
        boolean existadmin = isExist(QueryAdmin);

        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        
        out.writeBoolean(existcustomer);
        out.writeBoolean(existdelivery);
        out.writeBoolean(existadmin);
        
        if (existadmin) {
            preparedStatement = connection.prepareStatement(QueryAdmin);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            out.writeUTF(resultSet.getString("password"));

        } else if (existdelivery) {
            preparedStatement = connection.prepareStatement(QueryDelivery);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            out.writeUTF(resultSet.getString("password"));

        } else if (existcustomer) {
            preparedStatement = connection.prepareStatement(queryCustomer);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            out.writeUTF(resultSet.getString("password"));
        }
    }
    synchronized void serveCustomer(Socket s,DataInputStream in,Connection connection,PreparedStatement preparedStatement,ResultSet resultSet) throws ClassNotFoundException,SQLException, IOException{
        ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
        if(action.contains("seeFood")){
            try {
            LinkedList<Food> foods=new LinkedList<Food>();
            String foodlist = "select * from food where stock_quanitity!=0 order by id";
            preparedStatement = connection.prepareStatement(foodlist);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String Food_id = resultSet.getString("id");
                String Food_name = resultSet.getString("food_name");
                String quantity = resultSet.getString("stock_quanitity");
                String price = resultSet.getString("price");
                foods.add(new Food(Integer.parseInt(resultSet.getString("id")), resultSet.getString("food_name"), Integer.parseInt(resultSet.getString("stock_quanitity")),
                        Double.parseDouble(resultSet.getString("price"))));
            }
            out.writeObject((LinkedList<Food>)foods);
            out.flush();
        } catch (SQLException e2) {
          e2.printStackTrace();
        } 
        }
        else if(action.contains("makeOrder")){
             DataOutputStream out2=new DataOutputStream(socket.getOutputStream());
                Order finalorder = new Order();
                String foodlist=in.readUTF().trim();
                preparedStatement = connection.prepareStatement(foodlist);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                id=Integer.parseInt(resultSet.getString("id"));
                
                ObjectInputStream in2=new ObjectInputStream(s.getInputStream());

                  finalorder.setOrderOwnerID(id);
                  finalorder.setId(finalorder.getOrderOwnerID() + 3410);
                 //here for error
             
                 LinkedList<Food> foodc=(LinkedList<Food>)in2.readObject();
                 LinkedList<Food> foodd=(LinkedList<Food>)in2.readObject();
                  for (int i = 0; i < foodc.size(); i++) {
                        finalorder.addToFoodList(foodc.get(i));
                    }
                 finalorder.genreteFinalPrice();
                 out2.close();
                 //add to the database
                  for (int i = 0; i < finalorder.getFoodList().size(); i++) {
                        String foodlist2 = "Insert into food_order values(" + finalorder.getId() + ",'" + finalorder.getFoodList().get(i).getFoodQuantity()
                                + "','" + finalorder.getFprice()+ "','" + finalorder.getFoodList().get(i).getId()
                                + "','" + finalorder.getOrderOwnerID() + "',null)";
                        System.out.println(foodlist2);
                        preparedStatement = connection.prepareStatement(foodlist2);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                    }
                  //update the database
                 for (int i = 0; i < foodd.size(); i++) {
                    String foodlist2 = "UPDATE food SET stock_quanitity=" + foodd.get(i).getFoodQuantity() + " WHERE id=" + foodd.get(i).getId();
                    preparedStatement = connection.prepareStatement(foodlist2);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                }
            
         
        }
        //start here
        else if(action.contains("changePass")){
                    String checkAccount=in.readUTF().trim();
                    preparedStatement = connection.prepareStatement(checkAccount);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    out.writeUTF(resultSet.getString("password"));
                    out.flush();
                    String changepassword=in.readUTF().trim();
                    preparedStatement = connection.prepareStatement(changepassword);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
        }
    }
    synchronized void serveAdmin(Socket s,DataInputStream in,Connection connection,PreparedStatement preparedStatement,ResultSet resultSet)throws ClassNotFoundException,SQLException, IOException{
        if(action.contains("addFood")) {
            Statement statement;
            String insert=in.readUTF().trim();
            statement = connection.createStatement();
                    statement.executeUpdate(insert);
        }
        else if(action.contains("adddeli")){
                    String queryCustomer = in.readUTF().trim();
                    String QueryDelivery = in.readUTF().trim();
                    String QueryAdmin = in.readUTF().trim();
                    
                    boolean existcustomer = isExist( queryCustomer);
                    boolean existdelivery = isExist(QueryDelivery);
                    boolean existadmin = isExist(QueryAdmin);
                    DataOutputStream out=new DataOutputStream(s.getOutputStream());
                    out.writeBoolean(existcustomer);
                    out.writeBoolean(existdelivery);
                    out.writeBoolean(existadmin);
                    
                    Statement statement = connection.createStatement();
                    String insert=in.readUTF().trim();
                    statement.executeUpdate(insert);
        }else if(action.contains("seeDelivery")){
             String foodlist=in.readUTF().trim();
                preparedStatement = connection.prepareStatement(foodlist);
                resultSet = preparedStatement.executeQuery();
                LinkedList<String> ids=new LinkedList<>();
                LinkedList<String> usernames=new LinkedList<>();
               LinkedList<String> passwords=new LinkedList<>();
                LinkedList<String> phones=new LinkedList<>();
                LinkedList<String>vehicles =new LinkedList<>();
                while (resultSet.next()) {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                String vehicle = resultSet.getString("vehicle");
                ids.add(id);
                usernames.add(username);
                passwords.add(password);
                phones.add(phone);
                vehicles.add(vehicle);
            }
            ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
            out.writeObject((LinkedList<String>)ids);
            out.writeObject((LinkedList<String>)usernames);
            out.writeObject((LinkedList<String>)passwords);
            out.writeObject((LinkedList<String>)phones);
            out.writeObject((LinkedList<String>)vehicles);
            out.flush();
        }else if (action.contains("seeFood")) {
            try {
                ObjectOutputStream out1=new ObjectOutputStream(s.getOutputStream());
                LinkedList<Food> foods = new LinkedList<Food>();
                String foodlist = "select * from food where stock_quanitity!=0 order by id";
                preparedStatement = connection.prepareStatement(foodlist);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String Food_id = resultSet.getString("id");
                    String Food_name = resultSet.getString("food_name");
                    String quantity = resultSet.getString("stock_quanitity");
                    String price = resultSet.getString("price");
                    foods.add(new Food(Integer.parseInt(resultSet.getString("id")), resultSet.getString("food_name"), Integer.parseInt(resultSet.getString("stock_quanitity")),
                            Double.parseDouble(resultSet.getString("price"))));
                }
                out1.writeObject((LinkedList<Food>) foods);
                out1.flush();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }else if(action.contains("seeCustomer")){
             try {     
                 String foodlist=in.readUTF().trim();
                preparedStatement = connection.prepareStatement(foodlist);
                resultSet = preparedStatement.executeQuery();
                LinkedList<String> ids=new LinkedList<>();
                LinkedList<String> usernames=new LinkedList<>();
               LinkedList<String> passwords=new LinkedList<>();
                LinkedList<String> phones=new LinkedList<>();
                LinkedList<String>locations =new LinkedList<>();
                while (resultSet.next()) {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                String location = resultSet.getString("location");
                ids.add(id);
                usernames.add(username);
                passwords.add(password);
                phones.add(phone);
                locations.add(location);
            }
            ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
            out.writeObject((LinkedList<String>)ids);
            out.writeObject((LinkedList<String>)usernames);
            out.writeObject((LinkedList<String>)passwords);
            out.writeObject((LinkedList<String>)phones);
            out.writeObject((LinkedList<String>)locations);
            out.flush();
            
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }
    synchronized void serveDelivery(Socket s,DataInputStream in,Connection connection,PreparedStatement preparedStatement,ResultSet resultSet) throws ClassNotFoundException,SQLException, IOException{
        if(action.contains("seeOrder")){
               String foodlist=in.readUTF().trim();
                preparedStatement = connection.prepareStatement(foodlist);
                resultSet = preparedStatement.executeQuery();
                LinkedList<String> ids=new LinkedList<>();
                LinkedList<String> usernames=new LinkedList<>();
                LinkedList<String> phones=new LinkedList<>();
                LinkedList<String> locations=new LinkedList<>();
                while (resultSet.next()) {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String phone = resultSet.getString("phone");
                String location = resultSet.getString("location");
                ids.add(id);
                usernames.add(username);
                phones.add(phone);
                locations.add(location);
            }
            ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
            out.writeObject((LinkedList<String>)ids);
            out.writeObject((LinkedList<String>)usernames);
            out.writeObject((LinkedList<String>)phones);
            out.writeObject((LinkedList<String>)locations);
            out.flush();
        }
         else if(action.contains("acceptOrder")){
            String foodlist=in.readUTF().trim();
            System.out.println(foodlist);
            preparedStatement = connection.prepareStatement(foodlist);
            resultSet = preparedStatement.executeQuery();
            DataOutputStream out=new DataOutputStream(s.getOutputStream());
            ObjectOutputStream out2=new ObjectOutputStream(s.getOutputStream());
            LinkedList<String> foodname=new LinkedList<>();
            LinkedList<String> foodqun=new LinkedList<>();
            int finalprice=0;
            String username="";
            String phone="";
            String location="";
            while (resultSet.next()) {
                if (resultSet != null) {
                 username=resultSet.getString("username");
                System.out.println(username);
                
                  phone=resultSet.getString("phone");
                 System.out.println(phone);
               
                  location=resultSet.getString("location");
                 System.out.println(location);
               
                
                    finalprice += Integer.parseInt(resultSet.getString("final_price"));
                
                    foodname.add(resultSet.getString("food_name"));
                    foodqun.add(resultSet.getString("stock_quanitity"));
                }
            }
                out.writeUTF(username);
                out.writeUTF(phone);
                out.writeUTF(location);
                out.writeUTF(finalprice+"");
                out2.writeObject((LinkedList<String>)foodname);
                out2.writeObject((LinkedList<String>)foodqun);
                
        }
        else if(action.contains("changePass")){
                    String checkAccount=in.readUTF().trim();
                    preparedStatement = connection.prepareStatement(checkAccount);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    DataOutputStream out=new DataOutputStream(s.getOutputStream());
                    out.writeUTF(resultSet.getString("password"));
                    out.flush();
                    String changepassword=in.readUTF().trim();
                    preparedStatement = connection.prepareStatement(changepassword);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    
        }
        else if(action.contains("deliver")){
            String foodlist=in.readUTF().trim();
             preparedStatement = connection.prepareStatement(foodlist);
             resultSet = preparedStatement.executeQuery();
             
            //update check for 0 quanitity
            String delete = "DELETE FROM food WHERE stock_quanitity=0";
              preparedStatement = connection.prepareStatement(delete);
              resultSet = preparedStatement.executeQuery();       
        }
    }
     public boolean isExist( String Query) {
        try {
            ResultSet resultSet = null;
            PreparedStatement preparedStatement = null;
            
            Connection connection = javaconnect.setconnection();
            preparedStatement = connection.prepareStatement(Query);
            resultSet= preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e2) {
            System.err.println(e2.getMessage());
        }
        return false;
    }
    
}
