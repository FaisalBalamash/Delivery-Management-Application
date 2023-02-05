package delivery;


import login.Login;
import fooddeliverysystem.server.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.sql.*;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class delivery extends JFrame {
    private String username;
    private String password;
    Connection connection;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    Statement statement;

    public delivery(String username, String password, Connection connection) {
        this.connection = connection;
        this.username = username;
        this.password = password;
        deliveryPanel();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void deliveryPanel() {
        JFrame mainFrame = new JFrame();
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setSize(900, 563);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton logoutButton, viewOrdersButton, changePasswordButton;
        JLabel MenuTitleLabel, typeOfWindowLabel;
        Font title = new Font("Arial", Font.PLAIN, 34);

        MenuTitleLabel = new JLabel("Choose a service");
        MenuTitleLabel.setBounds(300, 50, 400, 50);
        MenuTitleLabel.setFont(title);
        mainFrame.add(MenuTitleLabel);

        typeOfWindowLabel = new JLabel("Delivery");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        mainFrame.add(typeOfWindowLabel);

        int ySpacer = 75;

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(25, 25, 75, 50);
        mainFrame.add(logoutButton);

        viewOrdersButton = new JButton("View Orders");
        viewOrdersButton.setBounds(175, 75 + (ySpacer * 1), 500, 50);
        mainFrame.add(viewOrdersButton);

        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(175, 75 + (ySpacer * 2), 500, 50);
        mainFrame.add(changePasswordButton);

        // =============== Button action ===============
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                int reply = JOptionPane.showConfirmDialog(null, "Do you want to log out ?", "Logout Prompt", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    new Login(connection);
                    mainFrame.setVisible(false);
                } else {
                }
            }
        });

        viewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                viewOrderPanel();
                mainFrame.setVisible(false);
            }
        });
        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                changePasswordPanel();
                mainFrame.setVisible(false);
            }
        });
    }

    public void viewOrderPanel() {
        JFrame makeOrderScreenFrame = new JFrame();
        makeOrderScreenFrame.setLayout(null);
        makeOrderScreenFrame.setVisible(true);
        makeOrderScreenFrame.setSize(900, 563);
        makeOrderScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton backButton, viewOrderButton;
        JLabel typeOfWindowLabel;
        JTextField orderInputTextField;

        typeOfWindowLabel = new JLabel("Delivery");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        makeOrderScreenFrame.add(typeOfWindowLabel);

        viewOrderButton = new JButton("View order");
        viewOrderButton.setBounds(175, 450, 500, 50);
        makeOrderScreenFrame.add(viewOrderButton);

        backButton = new JButton("<");
        backButton.setBounds(25, 25, 75, 50);
        makeOrderScreenFrame.add(backButton);

        orderInputTextField = new JTextField();
        orderInputTextField.setBounds(175, 385, 500, 50);
        makeOrderScreenFrame.add(orderInputTextField);

        String[] columnNames = {"Order ID", "Username", "Phone", "Location"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        // Initializing the JTable
        JTable table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


            try{
             String foodlist = "select DISTINCT food_order.id,"
                    + "customer.username,customer.phone,customer.location from food_order,customer where food_order.customer_id=customer.id";
            Socket client=new Socket("127.0.0.1",8899);
            DataOutputStream out=new DataOutputStream(client.getOutputStream());

            out.writeUTF("delivery");
            out.writeUTF("seeOrder");
            out.writeUTF(foodlist);
            
            ObjectInputStream in=new ObjectInputStream(client.getInputStream());

            LinkedList<String>ids=(LinkedList<String>)in.readObject();
            LinkedList<String>usernames=(LinkedList<String>)in.readObject();
            LinkedList<String>phones=(LinkedList<String>)in.readObject();
            LinkedList<String>locations=(LinkedList<String>)in.readObject();
            out.close();
            in.close();
            for (int i=0;i<ids.size();i++) {
                model.addRow(new Object[]{ids.get(i), usernames.get(i), password, phones.get(i), locations.get(i)});
            }
            
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            JScrollBar sb = scroll.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());

            scroll.setBounds(100, 100, 650, 275);
            makeOrderScreenFrame.add(scroll);



        // =============== Button action ===============
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                deliveryPanel();
                makeOrderScreenFrame.setVisible(false);
            }
        });

        viewOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                acceptOrderPanel(orderInputTextField.getText().trim());
                makeOrderScreenFrame.setVisible(false);
            }
        });

    }

    public void acceptOrderPanel(String orderID) {
        JFrame makeOrderScreenFrame = new JFrame();
        makeOrderScreenFrame.setLayout(null);
        makeOrderScreenFrame.setVisible(true);
        makeOrderScreenFrame.setSize(900, 563);
        makeOrderScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton backButton, deliverButton;
        JLabel typeOfWindowLabel, nameLabel, LocationLabel, phoneLabel, finalpriceLabel;
        Font font = new Font("Arial", Font.PLAIN, 20);

        backButton = new JButton("<");
        backButton.setBounds(25, 25, 75, 50);
        makeOrderScreenFrame.add(backButton);

        deliverButton = new JButton("Deliver");
        deliverButton.setBounds(500, 375, 350, 50);
        makeOrderScreenFrame.add(deliverButton);

        typeOfWindowLabel = new JLabel("Delivery");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        makeOrderScreenFrame.add(typeOfWindowLabel);
        ; // Data to be displayed in the JTable
        String[] columnNames = {"Food Name", "Quantity"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        // Initializing the JTable
        JTable table = new JTable();
        table.setEnabled(false);
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

       
        int finalprice = 0;
        try {
            Socket client=new Socket("127.0.0.1", 8899);
            DataOutputStream out=new DataOutputStream(client.getOutputStream());
           
           
            out.writeUTF("delivery");
            out.writeUTF("acceptOrder");
            String foodlist = "select food.food_name, customer.username,customer.phone,customer.location,food_order.final_price ,food_order.stock_quanitity from food_order,customer,food where food_order.customer_id=customer.id and food_order.id = "
                    + orderID + " and food.id=food_order.food_id";
            out.writeUTF(foodlist);
             DataInputStream in2=new DataInputStream(client.getInputStream());
             ObjectInputStream in=new ObjectInputStream(client.getInputStream());
                nameLabel = new JLabel("Name: " + in2.readUTF()); // Customer Name
                nameLabel.setBounds(100, 150, 300, 100);
                nameLabel.setFont(font);
                makeOrderScreenFrame.add(nameLabel);

                LocationLabel = new JLabel("Location: " + in2.readUTF()); // Customer Name
                LocationLabel.setBounds(100, 200, 300, 100);
                LocationLabel.setFont(font);
                makeOrderScreenFrame.add(LocationLabel);

                phoneLabel = new JLabel("Phone: " + in2.readUTF()); // Customer Name
                phoneLabel.setBounds(100, 250, 300, 100);
                phoneLabel.setFont(font);
                makeOrderScreenFrame.add(phoneLabel);

                finalprice=Integer.parseInt(in2.readUTF());
                LinkedList<String> name=(LinkedList<String>)in.readObject();
                LinkedList<String> quntity=(LinkedList<String>)in.readObject();
                in.close();
                for(int i=0;i<name.size();i++) {
                    String Food_name=name.get(i);
                     String quantity=quntity.get(i);
                    model.addRow(new Object[]{Food_name, quantity});
            }
            
            finalpriceLabel = new JLabel("Final Price: " + finalprice+ " SAR"); //finalprice Name
                finalpriceLabel.setBounds(100, 300, 300, 100);
                finalpriceLabel.setFont(font);
                makeOrderScreenFrame.add(finalpriceLabel);

            JScrollBar sb = scroll.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());

            scroll.setBounds(500, 100, 350, 250);
            makeOrderScreenFrame.add(scroll);
        } 
        catch (ClassNotFoundException e2) {
            JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
        }
        catch (IOException e2) {
            e2.printStackTrace();
            JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
        }

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                deliveryPanel();
                makeOrderScreenFrame.setVisible(false);
            }
        });

        deliverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Socket client=new Socket("127.0.0.1",8899);
                    DataOutputStream out=new DataOutputStream(client.getOutputStream());
                    
                    out.writeUTF("delivery");
                    out.writeUTF("deliver");
                   
                    
                    String foodlist = "DELETE FROM food_order WHERE id=" + orderID;
                    out.writeUTF(foodlist);
                     
                    JOptionPane.showMessageDialog(makeOrderScreenFrame, "You successfully accepted the order!");
                    deliveryPanel();
                    makeOrderScreenFrame.setVisible(false);

                } catch (IOException e2) {
                    JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
                }
 
            }
        });
    }

    public void changePasswordPanel() {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setSize(500, 313);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton changePasswordButton, backButton;
        JLabel changePasswordLabel, oldPasswordLabel, newPasswordLabel;
        JTextField oldPasswordTextField, newPasswordTextField;
        Font title = new Font("Arial", Font.PLAIN, 34);

        backButton = new JButton("<");
        backButton.setBounds(15, 15, 75, 50);
        frame.add(backButton);

        changePasswordLabel = new JLabel("Change Password");
        changePasswordLabel.setBounds(110, 25, 400, 50);
        changePasswordLabel.setFont(title);
        frame.add(changePasswordLabel);

        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(100, 200, 290, 50);
        frame.add(changePasswordButton);

        oldPasswordLabel = new JLabel("Old Password");
        oldPasswordLabel.setBounds(100, 100, 120, 30);
        frame.add(oldPasswordLabel);
        oldPasswordTextField = new JTextField();
        oldPasswordTextField.setBounds(190, 100, 200, 30);
        frame.add(oldPasswordTextField);

        newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setBounds(100, 135, 120, 30);
        frame.add(newPasswordLabel);
        newPasswordTextField = new JTextField();
        newPasswordTextField.setBounds(190, 135, 200, 30);
        frame.add(newPasswordTextField);

        // =============== Button action ===============
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                deliveryPanel();
                frame.setVisible(false);
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Socket s=new Socket("127.0.0.1",8899);
                    DataOutputStream out=new DataOutputStream(s.getOutputStream());
                    DataInputStream in=new DataInputStream(s.getInputStream());
                    out.writeUTF("delivery");
                    out.writeUTF("changePass");
                    
                    String checkAccount = "select * from delivery where username='" + username + "'";
                    out.writeUTF(checkAccount);

                    if (oldPasswordTextField.getText().trim().equals(in.readUTF().trim())) {
                        String changepassword = "update delivery set password='" + newPasswordTextField.getText().trim()
                                + "' where username ='" + username + "'";
                        out.writeUTF(changepassword);
                        
                        JOptionPane.showMessageDialog(null, "Your password is changed!");
                        deliveryPanel();
                        frame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and old password and try again.");
                    }

                } catch (IOException e2) {
                    JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and old password and try again.");
                }
            }
        }
        );
    }

    public boolean isExist(JFrame frame, String Query) {
        try {
            preparedStatement = connection.prepareStatement(Query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(frame, "Username exist!");
        }
        return false;
    }

};
