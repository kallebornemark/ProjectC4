package projectc4.c4.server;

import projectc4.c4.util.User;

import java.sql.*;

/**
 * @author Kalle Bornemark
 */
public class SQLiteJDBC {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public SQLiteJDBC() {
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

    public User attemptLogin(String username, String password) {
        try {
            resultSet = statement.executeQuery("select * from User where (username = '" + username + "' and password = '" + password + "');");
            if (resultSet.next()) {
                System.out.println("User and password exists in database, creating user and sending back to client");

                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                double elo = resultSet.getDouble("elo");

                return new User(username, firstname, lastname, elo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
