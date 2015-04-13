package projectc4.c4.server;

import projectc4.c4.util.User;

import java.sql.*;

/**
 * @author Kalle Bornemark
 */
public class Database {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:app/src/main/java/projectc4/c4/server/db/c4_database.db");
            statement = connection.createStatement();
            System.out.println("Connection to database established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean newUser(String username, String password) {
        try {
            statement.executeUpdate("insert into User ('username', 'password')" +
                                    "values ('" + username + "', '" + password + "')");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public String[] attemptLogin(String username, String password) {
        String[] res = new String[5];
        try {
            resultSet = statement.executeQuery("select * from User where username = '" + username + "'");
            if (resultSet.next()) {
                resultSet = statement.executeQuery("select * from User where (username = '" + username + "' and password = '" + password + "')");
                if (resultSet.next()) {
                    System.out.println("User and password matches database, login accepted.\nSending back string-array to server");
                    res[0] = username;
                    res[1] = resultSet.getString("firstname");
                    res[2] = resultSet.getString("lastname");
                    res[3] = Double.toString(resultSet.getDouble("elo"));
                } else {
                    res[4] = "Wrong password for user: " + username;
                }
            } else {
                res[4] = "No such user: " + username;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void setFirstname(String username, String firstname) {
        try {
            statement.executeUpdate("update User set firstname = " + firstname + " where username = " + username + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLastname(String username, String lastname) {
        try {
            statement.executeUpdate("update User set lastname = " + lastname + " where username = " + username + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setElo(double elo, String username) {
        try {
            statement.executeUpdate("update User set elo = " + elo + " where username = " + username + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
