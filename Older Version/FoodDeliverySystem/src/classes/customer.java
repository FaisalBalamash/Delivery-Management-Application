package classes;

import gui.Login;
import gui.javaconnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;

public class customer extends JFrame {

    private String username;
    private String password;
    private String phone;
    private String Address;
    private LinkedList<Food> food;

    Connection connection = javaconnect.setconnection();
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    Statement statement;

    public customer(String username, String password) {
        this.username = username;
        this.password = password;
        customerPanel();
    }

    public customer(String username, String password, String phone, String Address) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.Address = Address;
        customerPanel();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void customerPanel() {
        JFrame mainFrame = new JFrame();
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setSize(900, 563);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton logoutButton, makeOrderButton, changePasswordButton;
        JLabel MenuTitleLabel, typeOfWindowLabel;
        Font title = new Font("Arial", Font.PLAIN, 34);

        MenuTitleLabel = new JLabel("Choose a service");
        MenuTitleLabel.setBounds(300, 50, 400, 50);
        MenuTitleLabel.setFont(title);
        mainFrame.add(MenuTitleLabel);

        typeOfWindowLabel = new JLabel("Customer");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        mainFrame.add(typeOfWindowLabel);

        int ySpacer = 75;

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(25, 25, 75, 50);
        mainFrame.add(logoutButton);

        makeOrderButton = new JButton("Make a new order");
        makeOrderButton.setBounds(175, 75 + (ySpacer * 1), 500, 50);
        mainFrame.add(makeOrderButton);

        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(175, 75 + (ySpacer * 2), 500, 50);
        mainFrame.add(changePasswordButton);

        // =============== Button action ===============
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                int reply = JOptionPane.showConfirmDialog(null, "Do you want to log out ?", "Logout Prompt", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    new Login();
                    mainFrame.setVisible(false);
                } else {
                }
            }
        });

        makeOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                makeOrderPanel();
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

    public void makeOrderPanel() {
        food = new LinkedList<>();
        JFrame makeOrderScreenFrame = new JFrame();
        makeOrderScreenFrame.setLayout(null);
        makeOrderScreenFrame.setVisible(true);
        makeOrderScreenFrame.setSize(900, 563);
        makeOrderScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton backButton, orderButton;
        JLabel typeOfWindowLabel;
        JTextField orderInputTextField;

        typeOfWindowLabel = new JLabel("Customer");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        makeOrderScreenFrame.add(typeOfWindowLabel);

        orderButton = new JButton("Order");
        orderButton.setBounds(175, 450, 500, 50);
        makeOrderScreenFrame.add(orderButton);

        backButton = new JButton("<");
        backButton.setBounds(25, 25, 75, 50);
        makeOrderScreenFrame.add(backButton);

        orderInputTextField = new JTextField();
        orderInputTextField.setBounds(175, 385, 500, 50);
        makeOrderScreenFrame.add(orderInputTextField);

        // Data to be displayed in the JTable
        String[] columnNames = {"Food ID", "Food Name", "Quanitity", "Price"};

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

        try {
            String foodlist = "select * from food where stock_quanitity!=0 order by id";
            preparedStatement = connection.prepareStatement(foodlist);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String Food_id = resultSet.getString("id");
                String Food_name = resultSet.getString("food_name");
                String quantity = resultSet.getString("stock_quanitity");
                String price = resultSet.getString("price");
                model.addRow(new Object[]{Food_id, Food_name, quantity, price});
                food.add(new Food(Integer.parseInt(resultSet.getString("id")), resultSet.getString("food_name"), Integer.parseInt(resultSet.getString("stock_quanitity")),
                        Double.parseDouble(resultSet.getString("price"))));

            }

            JScrollBar sb = scroll.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());

            scroll.setBounds(100, 100, 650, 275);
            makeOrderScreenFrame.add(scroll);

        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
        }

        // =============== Button action ===============
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                customerPanel();
                makeOrderScreenFrame.setVisible(false);
            }
        });

        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                String order = orderInputTextField.getText();
                LinkedList<Food> foodc = new LinkedList<>();
                Food temp = new Food();
                String[] order2list = order.split("\\,");

                for (int i = 0; i < order2list.length; i++) {
                    if (i == 0) {
                        for (int j = 0; j < food.size(); j++) {
                            if (food.get(j).getId() == Integer.parseInt(order2list[i])) {
                                temp.setId(food.get(j).getId());
                                temp.setFoodName(food.get(j).getFoodName());
                                temp.setFoodQuantity(1);
                                temp.setFoodPrice(food.get(j).getFoodPrice());
                                food.get(j).setFoodQuantity(food.get(j).getFoodQuantity() - 1);
                                break;
                            }
                        }
                    } else if (Integer.parseInt(order2list[i]) != temp.getId()) {
                        foodc.add(new Food(temp));
                        for (int j = 0; j < food.size(); j++) {
                            if (food.get(j).getId() == Integer.parseInt(order2list[i])) {
                                if (food.get(j).getFoodQuantity() == 0) {
                                    break;
                                }
                                temp.setId(food.get(j).getId());
                                temp.setFoodName(food.get(j).getFoodName());
                                temp.setFoodQuantity(1);
                                temp.setFoodPrice(food.get(j).getFoodPrice());
                                food.get(j).setFoodQuantity(food.get(j).getFoodQuantity() - 1);
                                break;
                            }
                        }
                    } else if (Integer.parseInt(order2list[i]) == temp.getId()) {
                        for (int j = 0; j < food.size(); j++) {
                            if (food.get(j).getId() == Integer.parseInt(order2list[i])) {
                                if (food.get(j).getFoodQuantity() == 0) {
                                    break;
                                }
                                food.get(j).setFoodQuantity(food.get(j).getFoodQuantity() - 1);
                                temp.setFoodQuantity(temp.getFoodQuantity() + 1);
                                break;
                            }
                        }
                    }

                }

                JOptionPane.showMessageDialog(null, "Thank you For your order!");
                customerPanel();
                makeOrderScreenFrame.setVisible(false);

                foodc.add(new Food(temp));
                Order finalorder = new Order();

                try {
                    String foodlist = "select * from customer where username = '" + username + "'";
                    preparedStatement = connection.prepareStatement(foodlist);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    //////////////////////////////////////////////////////////////////////////

                    finalorder.setOrderOwnerID(Integer.parseInt(resultSet.getString("id")));

                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
                }
                for (int i = 0; i < foodc.size(); i++) {
                    finalorder.addToFoodList(foodc.get(i));
                }
                //id order
                finalorder.setId(finalorder.getOrderOwnerID() + 3410);
                int id = finalorder.getId();
                try {
                    for (int i = 0; i < finalorder.getFoodList().size(); i++) {
                        String foodlist = "Insert into food_order values(" + id + ",'" + finalorder.getFoodList().get(i).getFoodQuantity()
                                + "','" + finalorder.genreteFinalPrice() + "','" + finalorder.getFoodList().get(i).getId()
                                + "','" + finalorder.getOrderOwnerID() + "',null)";
                        preparedStatement = connection.prepareStatement(foodlist);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                    }

                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
                }
                LinkedList<Food> foodd = food;

                for (int i = 0; i < foodd.size(); i++) {
                    try {
                        String foodlist = "UPDATE food SET stock_quanitity=" + foodd.get(i).getFoodQuantity() + " WHERE id=" + foodd.get(i).getId();
                        preparedStatement = connection.prepareStatement(foodlist);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                    } catch (SQLException e2) {
                        JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
                    }
                }

            }
        }
        );

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
                customerPanel();
                frame.setVisible(false);
            }
        });

        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {

                    String checkAccount = "select * from customer where username='" + username + "'";

                    preparedStatement = connection.prepareStatement(checkAccount);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    if (oldPasswordTextField.getText().trim().equals(resultSet.getString("password"))) {
                        String changepassword = "update customer set password='" + newPasswordTextField.getText().trim()
                                + "' where username ='" + username + "'";
                        preparedStatement = connection.prepareStatement(changepassword);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        JOptionPane.showMessageDialog(null, "Your password is changed!");
                        customerPanel();
                        frame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and old password and try again.");
                    }

                } catch (SQLException e2) {
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
