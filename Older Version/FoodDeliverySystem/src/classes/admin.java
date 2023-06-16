package classes;

import gui.Login;
import gui.javaconnect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class admin extends JFrame {

    private String username;
    private String password;
    Connection connection = javaconnect.setconnection();
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    Statement statement;

    public admin(String username, String password) {
        this.username = username;
        this.password = password;
        adminPanel();
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

    public void adminPanel() {

        JFrame mainFrame = new JFrame();
        mainFrame.setLayout(null);
        mainFrame.setVisible(true);
        mainFrame.setSize(900, 563);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton logoutButton, viewDeliveryButton, viewFoodButton, addFoodButton, addDeliveryButton, viewCustomersButton, makeOrderButton;
        JLabel MenuTitleLabel, typeOfWindowLabel;
        Font title = new Font("Arial", Font.PLAIN, 34);

        MenuTitleLabel = new JLabel("Choose a service");
        MenuTitleLabel.setBounds(300, 50, 400, 50);
        MenuTitleLabel.setFont(title);
        mainFrame.add(MenuTitleLabel);

        typeOfWindowLabel = new JLabel("Admin");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        mainFrame.add(typeOfWindowLabel);

        int ySpacer = 75;

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(25, 25, 75, 50);
        mainFrame.add(logoutButton);

        viewDeliveryButton = new JButton("View Delivery");
        viewDeliveryButton.setBounds(175, 75 + (ySpacer * 1), 500, 50);
        mainFrame.add(viewDeliveryButton);

        viewFoodButton = new JButton("View Food");
        viewFoodButton.setBounds(175, 75 + (ySpacer * 2), 500, 50);
        mainFrame.add(viewFoodButton);

        viewCustomersButton = new JButton("View Customers");
        viewCustomersButton.setBounds(175, 75 + (ySpacer * 3), 500, 50);
        mainFrame.add(viewCustomersButton);

        addFoodButton = new JButton("Add Food");
        addFoodButton.setBounds(175, 75 + (ySpacer * 4), 500, 50);
        mainFrame.add(addFoodButton);

        addDeliveryButton = new JButton("Add Delivery");
        addDeliveryButton.setBounds(175, 75 + (ySpacer * 5), 500, 50);
        mainFrame.add(addDeliveryButton);

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

        viewDeliveryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                viewDeliveryPanel();
                mainFrame.setVisible(false);
            }
        });
        viewFoodButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                viewFoodPanel();
                mainFrame.setVisible(false);
            }
        });
        addFoodButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                addFoodPanel();
                mainFrame.setVisible(false);
            }
        });
        viewCustomersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                viewCustomerPanel();
                mainFrame.setVisible(false);
            }
        });

        addDeliveryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                addDeliveryPanel();
                mainFrame.setVisible(false);
            }
        });

    }

    public void addFoodPanel() {
        JFrame addFoodFrame = new JFrame();
        addFoodFrame.setLayout(null);
        addFoodFrame.setVisible(true);
        addFoodFrame.setSize(500, 313);
        addFoodFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton addProductButton, backButton;
        JLabel typeOfWindowLabel, foodNameLabel, stockQuantityLabel, priceLabel;
        JTextField FoodNameTextField, stockQuantityTextField, priceTextField;

        typeOfWindowLabel = new JLabel("Admin");
        typeOfWindowLabel.setBounds(400, 10, 100, 20);
        addFoodFrame.add(typeOfWindowLabel);

        int ySpacer = 35;

        foodNameLabel = new JLabel("Food Name");
        foodNameLabel.setBounds(100, 50, 120, 30);
        addFoodFrame.add(foodNameLabel);
        FoodNameTextField = new JTextField();
        FoodNameTextField.setBounds(200, 50, 200, 30);
        addFoodFrame.add(FoodNameTextField);

        stockQuantityLabel = new JLabel("Stock_Quantity");
        stockQuantityLabel.setBounds(100, 50 + ySpacer * 1, 120, 30);
        addFoodFrame.add(stockQuantityLabel);
        stockQuantityTextField = new JTextField();
        stockQuantityTextField.setBounds(200, 50 + ySpacer * 1, 200, 30);
        addFoodFrame.add(stockQuantityTextField);

        priceLabel = new JLabel("Price");
        priceLabel.setBounds(100, 50 + ySpacer * 2, 120, 30);
        addFoodFrame.add(priceLabel);
        priceTextField = new JTextField();
        priceTextField.setBounds(200, 50 + ySpacer * 2, 200, 30);
        addFoodFrame.add(priceTextField);

        backButton = new JButton("<");
        backButton.setBounds(15, 15, 75, 50);
        addFoodFrame.add(backButton);

        addProductButton = new JButton("Add Product");
        addProductButton.setBounds(100, 200, 300, 50);
        addFoodFrame.add(addProductButton);

        // =============== Button action ===============
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String insert = "insert into food values(auto_increment.nextval, '" + FoodNameTextField.getText().trim() + "', " + stockQuantityTextField.getText().trim()
                            + ", " + priceTextField.getText().trim() + ")";
                    statement = connection.createStatement();
                    statement.executeUpdate(insert);
                    JOptionPane.showMessageDialog(addFoodFrame, "You successfully added product!");
                    adminPanel();
                    addFoodFrame.setVisible(false);

                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(addFoodFrame, e2.getMessage(),
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        );

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                adminPanel();
                addFoodFrame.setVisible(false);
            }
        });

    }

    public void addDeliveryPanel() {
        JFrame addDeliveryFrame = new JFrame();
        addDeliveryFrame.setLayout(null);
        addDeliveryFrame.setVisible(true);
        addDeliveryFrame.setSize(500, 313);
        addDeliveryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton addDeliveryButton, backButton;
        JLabel typeOfWindowLabel, DeliveryNameLabel, DeliveryPasswordLabel, phoneLabel, vehicleLabel;
        JTextField DeliveryNameTextField, DeliveryPasswordTextField, phoneTextField, vehicleTextField;

        typeOfWindowLabel = new JLabel("Admin");
        typeOfWindowLabel.setBounds(400, 10, 100, 20);
        addDeliveryFrame.add(typeOfWindowLabel);

        int ySpacer = 35;

        DeliveryNameLabel = new JLabel("Delivery Username");
        DeliveryNameLabel.setBounds(100, 50, 120, 30);
        addDeliveryFrame.add(DeliveryNameLabel);
        DeliveryNameTextField = new JTextField();
        DeliveryNameTextField.setBounds(200, 50, 200, 30);
        addDeliveryFrame.add(DeliveryNameTextField);

        DeliveryPasswordLabel = new JLabel("Delivery Password");
        DeliveryPasswordLabel.setBounds(100, 50 + ySpacer * 1, 120, 30);
        addDeliveryFrame.add(DeliveryPasswordLabel);
        DeliveryPasswordTextField = new JTextField();
        DeliveryPasswordTextField.setBounds(200, 50 + ySpacer * 1, 200, 30);
        addDeliveryFrame.add(DeliveryPasswordTextField);

        phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(100, 50 + ySpacer * 2, 120, 30);
        addDeliveryFrame.add(phoneLabel);
        phoneTextField = new JTextField();
        phoneTextField.setBounds(200, 50 + ySpacer * 2, 200, 30);
        addDeliveryFrame.add(phoneTextField);

        vehicleLabel = new JLabel("vehicle");
        vehicleLabel.setBounds(100, 50 + ySpacer * 3, 120, 30);
        addDeliveryFrame.add(vehicleLabel);
        vehicleTextField = new JTextField();
        vehicleTextField.setBounds(200, 50 + ySpacer * 3, 200, 30);
        addDeliveryFrame.add(vehicleTextField);

        backButton = new JButton("<");
        backButton.setBounds(15, 15, 75, 50);
        addDeliveryFrame.add(backButton);

        addDeliveryButton = new JButton("Add Delivery");
        addDeliveryButton.setBounds(100, 200, 300, 50);
        addDeliveryFrame.add(addDeliveryButton);

        // =============== Button action ===============
        addDeliveryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String queryCustomer = "select * from customer where username='" + DeliveryNameTextField.getText().trim() + "'";
                    String QueryDelivery = "select * from delivery where username='" + DeliveryNameTextField.getText().trim() + "'";
                    String QueryAdmin = "select * from admin where username='" + DeliveryNameTextField.getText().trim() + "'";
                    boolean existcustomer = isExist(addDeliveryFrame, queryCustomer);
                    boolean existdelivery = isExist(addDeliveryFrame, QueryDelivery);
                    boolean existadmin = isExist(addDeliveryFrame, QueryAdmin);
                    if ((existcustomer || existdelivery || existadmin) == false) {
                        if (phoneTextField.getText().length() == 10) {
                            String insert = "insert into delivery values(auto_increment.nextval, '" + DeliveryNameTextField.getText().trim() + "', '" + DeliveryPasswordTextField.getText().trim()
                                    + "', " + phoneTextField.getText().trim() + ", '" + vehicleTextField.getText().trim() + "')";
                            statement = connection.createStatement();
                            statement.executeUpdate(insert);
                            JOptionPane.showMessageDialog(addDeliveryFrame, "You successfully registered a new delivery!");
                            adminPanel();
                            addDeliveryFrame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(addDeliveryFrame, "Phone number must be a 10 digit number!",
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(addDeliveryFrame, "Username exist!");
                    }

                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(addDeliveryFrame, "Phone number must be a 10 digit number!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        );

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                adminPanel();
                addDeliveryFrame.setVisible(false);
            }
        });

    }

    public void viewFoodPanel() {
        JFrame makeOrderScreenFrame = new JFrame();
        makeOrderScreenFrame.setLayout(null);
        makeOrderScreenFrame.setVisible(true);
        makeOrderScreenFrame.setSize(900, 563);
        makeOrderScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton backButton;
        JLabel typeOfWindowLabel;

        typeOfWindowLabel = new JLabel("Admin");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        makeOrderScreenFrame.add(typeOfWindowLabel);

        backButton = new JButton("<");
        backButton.setBounds(25, 25, 75, 50);
        makeOrderScreenFrame.add(backButton);

        // Data to be displayed in the JTable
        String[] columnNames = {"Food ID", "Food Name", "Quanitity", "Price"};

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
            }

            JScrollBar sb = scroll.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());

            scroll.setBounds(100, 100, 650, 350);
            makeOrderScreenFrame.add(scroll);

        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
        }

        // =============== Button action ===============
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                adminPanel();
                makeOrderScreenFrame.setVisible(false);
            }
        });

    }

    public void viewCustomerPanel() {
        JFrame makeOrderScreenFrame = new JFrame();
        makeOrderScreenFrame.setLayout(null);
        makeOrderScreenFrame.setVisible(true);
        makeOrderScreenFrame.setSize(900, 563);
        makeOrderScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton backButton;
        JLabel typeOfWindowLabel;

        typeOfWindowLabel = new JLabel("Admin");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        makeOrderScreenFrame.add(typeOfWindowLabel);

        backButton = new JButton("<");
        backButton.setBounds(25, 25, 75, 50);
        makeOrderScreenFrame.add(backButton);

        // Data to be displayed in the JTable
        String[] columnNames = {"Customer ID", "Username", "Password", "Phone Number", "Address"};

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

        try {
            String foodlist = "select * from customer order by id";
            preparedStatement = connection.prepareStatement(foodlist);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String customer_id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                String location = resultSet.getString("location");
                model.addRow(new Object[]{customer_id, username, password, phone, location});
            }

            JScrollBar sb = scroll.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());

            scroll.setBounds(100, 100, 650, 350);
            makeOrderScreenFrame.add(scroll);

        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
        }

        // =============== Button action ===============
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                adminPanel();
                makeOrderScreenFrame.setVisible(false);
            }
        });

    }

    public void viewDeliveryPanel() {
        JFrame makeOrderScreenFrame = new JFrame();
        makeOrderScreenFrame.setLayout(null);
        makeOrderScreenFrame.setVisible(true);
        makeOrderScreenFrame.setSize(900, 563);
        makeOrderScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton backButton;
        JLabel typeOfWindowLabel;

        typeOfWindowLabel = new JLabel("Admin");
        typeOfWindowLabel.setBounds(800, 10, 100, 20);
        makeOrderScreenFrame.add(typeOfWindowLabel);

        backButton = new JButton("<");
        backButton.setBounds(25, 25, 75, 50);
        makeOrderScreenFrame.add(backButton);

        // Data to be displayed in the JTable
        String[] columnNames = {"Delivery ID", "Username", "Password", "Phone Number", "Vehicle"};

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

        try {
            String foodlist = "select * from delivery order by id";
            preparedStatement = connection.prepareStatement(foodlist);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String customer_id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                String vehicle = resultSet.getString("vehicle");
                model.addRow(new Object[]{customer_id, username, password, phone, vehicle});
            }

            JScrollBar sb = scroll.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());

            scroll.setBounds(100, 100, 650, 350);
            makeOrderScreenFrame.add(scroll);

        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(makeOrderScreenFrame, e2.getMessage());
        }

        // =============== Button action ===============
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                adminPanel();
                makeOrderScreenFrame.setVisible(false);
            }
        });

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

}
