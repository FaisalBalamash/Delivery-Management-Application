package gui;

import classes.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login {

    Connection connection = javaconnect.setconnection();
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    Statement statement;

    public Login() {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setSize(500, 313);

        JButton loginButton, registerButton;
        JLabel programNameLabel, usernameLabel, passwordLabel;
        JTextField usernameTextField, passwordTextField;
        Font title = new Font("Arial", Font.PLAIN, 34);

        programNameLabel = new JLabel("Food Delivery System");
        programNameLabel.setBounds(75, 25, 400, 50);
        programNameLabel.setFont(title);
        frame.add(programNameLabel);

        loginButton = new JButton("login");
        loginButton.setBounds(100, 200, 130, 50);
        frame.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(250, 200, 130, 50);
        frame.add(registerButton);

        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(100, 100, 120, 30);
        frame.add(usernameLabel);
        usernameTextField = new JTextField();
        usernameTextField.setBounds(180, 100, 200, 30);
        frame.add(usernameTextField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100, 135, 120, 30);
        frame.add(passwordLabel);
        passwordTextField = new JTextField();
        passwordTextField.setBounds(180, 135, 200, 30);
        frame.add(passwordTextField);

        // =============== Button action ===============
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String username;
                    String queryCustomer = "select * from customer where username='" + usernameTextField.getText().trim() + "'";
                    String QueryDelivery = "select * from delivery where username='" + usernameTextField.getText().trim() + "'";
                    String QueryAdmin = "select * from admin where username='" + usernameTextField.getText().trim() + "'";
                    boolean existcustomer = isExist(frame, queryCustomer);
                    boolean existdelivery = isExist(frame, QueryDelivery);
                    boolean existadmin = isExist(frame, QueryAdmin);

                    if (existadmin) {
                        preparedStatement = connection.prepareStatement(QueryAdmin);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        username = resultSet.getString("username");
                        if (passwordTextField.getText().trim().equals(resultSet.getString("password"))) {
                            JOptionPane.showMessageDialog(null, "You successfully logged in!");
                            new admin(usernameTextField.getText().trim(), passwordTextField.getText().trim());
                            frame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and password and try again.");
                        }
                    } else if (existdelivery) {
                        preparedStatement = connection.prepareStatement(QueryDelivery);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        username = resultSet.getString("username");
                        if (passwordTextField.getText().trim().equals(resultSet.getString("password"))) {
                            JOptionPane.showMessageDialog(null, "You successfully logged in!");
                            new delivery(usernameTextField.getText().trim(), passwordTextField.getText().trim());
                            frame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and password and try again.");
                        }
                    } else if (existcustomer) {
                        preparedStatement = connection.prepareStatement(queryCustomer);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        username = resultSet.getString("username");
                        if (passwordTextField.getText().trim().equals(resultSet.getString("password"))) {
                            JOptionPane.showMessageDialog(null, "You successfully logged in!");
                            new customer(usernameTextField.getText().trim(), passwordTextField.getText().trim());
                            frame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and password and try again.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and password and try again.");
                    }

                } catch (SQLException e2) {
                    System.out.println(e2.getMessage());
                    JOptionPane.showMessageDialog(frame, "Incorrect Credentials, Verify your username and password and try again.");
                }
            }
        }
        );

        registerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                registerPanel();
                frame.setVisible(false);
            }
        }
        );

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void registerPanel() {
        JFrame registerFrame = new JFrame();
        registerFrame.setLayout(null);
        registerFrame.setVisible(true);
        registerFrame.setSize(500, 313);
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton registerButton, backButton;
        JLabel typeOfWindowLabel, usernameLabel, passwordLabel, phoneLabel, addressLabel;
        JTextField usernameTextField, passwordTextField, phoneTextField, addressTextField;
        typeOfWindowLabel = new JLabel("Registeration");
        typeOfWindowLabel.setBounds(400, 10, 100, 20);
        registerFrame.add(typeOfWindowLabel);

        int ySpacer = 35;

        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(100, 50, 120, 30);
        registerFrame.add(usernameLabel);
        usernameTextField = new JTextField();
        usernameTextField.setBounds(200, 50, 200, 30);
        registerFrame.add(usernameTextField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(100, 50 + ySpacer * 1, 120, 30);
        registerFrame.add(passwordLabel);
        passwordTextField = new JTextField();
        passwordTextField.setBounds(200, 50 + ySpacer * 1, 200, 30);
        registerFrame.add(passwordTextField);

        phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(100, 50 + ySpacer * 2, 120, 30);
        registerFrame.add(phoneLabel);
        phoneTextField = new JTextField();
        phoneTextField.setBounds(200, 50 + ySpacer * 2, 200, 30);
        registerFrame.add(phoneTextField);

        addressLabel = new JLabel("Address");
        addressLabel.setBounds(100, 50 + ySpacer * 3, 120, 30);
        registerFrame.add(addressLabel);
        addressTextField = new JTextField();
        addressTextField.setBounds(200, 50 + ySpacer * 3, 200, 30);
        registerFrame.add(addressTextField);

        registerButton = new JButton("Register");
        registerButton.setBounds(100, 200, 300, 50);
        registerFrame.add(registerButton);

        backButton = new JButton("<");
        backButton.setBounds(15, 15, 75, 50);
        registerFrame.add(backButton);

        // =============== Button action ===============
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new Login();
                registerFrame.setVisible(false);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String queryCustomer = "select * from customer where username='" + usernameTextField.getText().trim() + "'";
                    String QueryDelivery = "select * from delivery where username='" + usernameTextField.getText().trim() + "'";
                    String QueryAdmin = "select * from admin where username='" + usernameTextField.getText().trim() + "'";
                    boolean existcustomer = isExist(registerFrame, queryCustomer);
                    boolean existdelivery = isExist(registerFrame, QueryDelivery);
                    boolean existadmin = isExist(registerFrame, QueryAdmin);
                    if (phoneTextField.getText().length() == 10) {
                        if ((existcustomer || existdelivery || existadmin) == false) {
                            String insert = "insert into customer values(auto_increment.nextval, '" + usernameTextField.getText().trim() + "', '" + passwordTextField.getText().trim()
                                    + "', " + phoneTextField.getText().trim() + ", '" + addressTextField.getText().trim() + "')";
                            statement = connection.createStatement();
                            statement.executeUpdate(insert);
                            JOptionPane.showMessageDialog(registerFrame, "You successfully Registered in!");
                            new customer(usernameTextField.getText().trim(), passwordTextField.getText().trim(), phoneTextField.getText().trim(), addressTextField.getText().trim());
                            registerFrame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(registerFrame, "Username exist!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(registerFrame, "Phone Number must be 10 digit number!");
                    }
                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(registerFrame, "Phone Number must be 10 digit number!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
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
}
