package project;

import java.sql.*;
/*
Database connection initialization class
 */
public class InitDb {
    private String url;
    private String userName;
    private String password;
    private int count;
    private String type;
    private Connection connection;

    public void setType(String type) {
        this.type = type;
    }

    public InitDb() {
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() {
        try {
            if (type.equals("Oracle")){
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection("jdbc:oracle:thin:@" + url, userName, password);
            }else {
                 Class.forName("com.mysql.cj.jdbc.Driver");
                 connection = DriverManager.getConnection("jdbc:mysql://" + url + "?serverTimezone=Europe/Moscow", userName, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
