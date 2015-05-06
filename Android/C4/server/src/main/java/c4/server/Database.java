package c4.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import c4.utils.C4Constants;
import c4.utils.Elo;
import c4.utils.Highscore;
import c4.utils.User;


/**
 *
 * @author Kalle Bornemark, Jimmy Maksymiw, Erik Sandgren, Emil Sandgren.
 */
public class Database {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

/*    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:app/src/main/java/projectc4/c4/server/db/c4_database.db");
            statement = connection.createStatement();
            System.out.println("Connection to database established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:server/src/main/java/c4/server/db/c4_database.db");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeAll() {
        try { if (resultSet != null) resultSet.close(); } catch (SQLException e) {}
        try { if (statement != null) statement.close(); } catch (SQLException e) {}
        try { if (connection != null) connection.close(); } catch (SQLException e) {}
    }

    /**
     * Checks if username + password matches the database,
     * and returns a String-array with the user's info.
     *
     * @param username Username to check existence
     * @param password Password to check together with username
     * @return String-array with following info in slot:
     * 0: Potential error message
     * 1: Username
     * 2: Firstname
     * 3: Lastname
     * 4: ELO
     * 5: Wins
     * 6: Losses
     * 7: Draws
     */
    public synchronized String[] attemptLogin(String username, String password) {
        String[] res = new String[8];
        try {
            connect();
            resultSet = statement.executeQuery("select * from User where username = '" + username + "'");
            if (resultSet.next()) {
                resultSet = statement.executeQuery("select * from User where (username = '" + username + "' and password = '" + password + "')");
                if (resultSet.next()) {
                    System.out.println("User and password matches database, login accepted.\nSending back string-array to server");
                    res[1] = username;                                      // Username
                    res[2] = resultSet.getString("firstname");              // Firstname
                    res[3] = resultSet.getString("lastname");               // Lastname
                    res[4] = Double.toString(resultSet.getDouble("elo"));   // ELO
                    int wins = resultSet.getInt("wins");                    // Number of wins
                    int losses = resultSet.getInt("losses");                // Number of losses
                    int draws = resultSet.getInt("draws");                  // Number of draws
                    res[5] = Integer.toString(wins);
                    res[6] = Integer.toString(losses);
                    res[7] = Integer.toString(draws);
                } else {
                    res[0] = "Wrong password for user: " + username;
                }
            } else {
                res[0] = "No such user: " + username;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }

        return res;
    }

    public synchronized void newGameResult(String username, String opponentUsername, int result) {
        double elo , opponentElo, newElo, newOpponentElo;
        String res, opponentRes = "";

        try {
            connect();
            // Get initial ELO
            System.out.println("Attempting to get initial ELO and opponent ElO...");
            resultSet = statement.executeQuery("select elo from User where username = '" + username + "';");
            elo = resultSet.getDouble(1);
            resultSet = statement.executeQuery("select elo from User where username = '" + opponentUsername + "';");
            opponentElo = resultSet.getDouble(1);
            System.out.println("ELO: " + elo + ", OpponentELO: " + opponentElo);
            newElo = elo;
            newOpponentElo = opponentElo;

            // Calculate new ELO and get result type
            System.out.println("Attempting to calculate new ELO and get result type...");
            if (result == C4Constants.WIN) {
                res = "wins";
                opponentRes = "losses";
                newElo += Elo.calculateElo(elo, opponentElo);
                newOpponentElo -= Elo.calculateElo(opponentElo, elo);
            } else if (result == C4Constants.LOSS) {
                res = "losses";
                opponentRes = "wins";
                newElo -= Elo.calculateElo(elo, opponentElo);
                newOpponentElo += Elo.calculateElo(opponentElo, elo);
            } else {
                res = "draws";
            }

            // Update both ELO's
            statement.executeUpdate("update User set elo = '" + newElo + "' where username = '" + username + "'");
            statement.executeUpdate("update User set elo = '" + newOpponentElo + "' where username = '" + opponentUsername + "'");
            System.out.println("New ELO: " + newElo + ", Result: " + res + "\n" +
                               "New Opponent ELO: " + newOpponentElo + ", Result: " + opponentRes);
            System.out.println("ELO Updated in database.");

            // Update database with result
            statement.executeUpdate("update User set " + res + " = " + res  + " + 1 where username = '" + username + "'");
            statement.executeUpdate("update User set " + opponentRes + " = " + opponentRes  + " + 1 where username = '" + opponentUsername + "'");
            System.out.println("User: " + username + " updated with result: " + res);
            System.out.println("Opponent: " + opponentUsername + " updated with result: " + opponentRes);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public synchronized void updateUser(User user) {
        setFirstname(user.getUsername(), user.getFirstName());
        setLastname(user.getUsername(), user.getLastName());
    }

    public synchronized void setFirstname(String username, String firstname) {
        try {
            connect();
            statement.executeUpdate("update User set firstname = '" + firstname + "' where username = '" + username + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    //För test. Att skriva ut highscore
    public void print (String[][] tmpArray) {
        System.out.println("-------------------------");
        for (int i = 0; i < tmpArray.length; i++) {
            for (int j = 0; j < tmpArray[i].length; j++) {
                System.out.print(tmpArray[i][j] + " - ");
            }
            System.out.println();
        }
    }

    public synchronized Highscore getHighscore() {
        Highscore highscore = new Highscore();
        try {
            connect();

            // Highscore, top 10 based on elo.
            resultSet = statement.executeQuery("select username, elo, wins, losses, draws from User order by elo desc limit 10");
            String[][] tmpArray = new String[10][5];
            int tmprow = 0;
            while(resultSet.next()) {
                tmpArray[tmprow][0] = resultSet.getString(1);
                tmpArray[tmprow][1] = String.format("%.2f", resultSet.getDouble(2));
                tmpArray[tmprow][2] = Integer.toString(resultSet.getInt(3));
                tmpArray[tmprow][3] = Integer.toString(resultSet.getInt(4));
                tmpArray[tmprow][4] = Integer.toString(resultSet.getInt(5));
                tmprow++;
            }
//            print(tmpArray);
            highscore.setTop10Elo(tmpArray);

            // Highscore, top 10 based on wins.
            resultSet = statement.executeQuery("select username, elo, wins, losses, draws from User order by wins desc limit 10");
            tmpArray = new String[10][5];
            tmprow = 0;
            while(resultSet.next()) {
                tmpArray[tmprow][0] = resultSet.getString(1);
                tmpArray[tmprow][1] = String.format("%.2f", resultSet.getDouble(2));
                tmpArray[tmprow][2] = Integer.toString(resultSet.getInt(3));
                tmpArray[tmprow][3] = Integer.toString(resultSet.getInt(4));
                tmpArray[tmprow][4] = Integer.toString(resultSet.getInt(5));
                tmprow++;
            }
//            print(tmpArray);
            highscore.setTop10Wins(tmpArray);

            // Highscore, top 10 based on losses.
            resultSet = statement.executeQuery("select username, elo, wins, losses, draws from User order by losses desc limit 10");
            tmpArray = new String[10][5];
            tmprow = 0;
            while(resultSet.next()) {
                tmpArray[tmprow][0] = resultSet.getString(1);
                tmpArray[tmprow][1] = String.format("%.2f", resultSet.getDouble(2));
                tmpArray[tmprow][2] = Integer.toString(resultSet.getInt(3));
                tmpArray[tmprow][3] = Integer.toString(resultSet.getInt(4));
                tmpArray[tmprow][4] = Integer.toString(resultSet.getInt(5));
                tmprow++;
            }
//            print(tmpArray);
            highscore.setTop10Losses(tmpArray);

            // Highscore, top 10 based on draws.
            resultSet = statement.executeQuery("select username, elo, wins, losses, draws from User order by draws desc limit 10");
            tmpArray = new String[10][5];
            tmprow = 0;
            while(resultSet.next()) {
                tmpArray[tmprow][0] = resultSet.getString(1);
                tmpArray[tmprow][1] = String.format("%.2f", resultSet.getDouble(2));
                tmpArray[tmprow][2] = Integer.toString(resultSet.getInt(3));
                tmpArray[tmprow][3] = Integer.toString(resultSet.getInt(4));
                tmpArray[tmprow][4] = Integer.toString(resultSet.getInt(5));
                tmprow++;
            }
//            print(tmpArray);
            highscore.setTop10draws(tmpArray);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return highscore;
    }

    public synchronized void setLastname(String username, String lastname) {
        try {
            connect();
            statement.executeUpdate("update User set lastname = '" + lastname + "' where username = '" + username + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public synchronized void setElo(double elo, String username) {
        try {
            connect();
            statement.executeUpdate("update User set elo = '" + elo + "' where username = '" + username + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public synchronized double getElo(String username) {
        double elo = 0;
        try {
            connect();
            resultSet = statement.executeQuery("select elo from User where username = '" + username + "'");
            elo = resultSet.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return elo;
    }

    public synchronized int[] getGameResults(String username) {
        int[] results = new int[3];
        try {
            connect();
            resultSet = statement.executeQuery("select wins, losses, draws from User where username = '" + username + "'");
            for (int i = 1; i <= 3; i++) {
                results[i-1] = resultSet.getInt(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return results;
    }

    /**
     * Gets requested user info from client and checks if username and email is available.
     * If available, inserts new user into table and returns the user object.
     * If not, returns an error message.
     * @param user Requested user info
     * @return Error message or created user object
     */
    public synchronized Object newUser(User user) {
        String username = user.getUsername();
        String firstname = user.getFirstName();
        String lastname = user.getLastName();
        String email = user.getEmail();
        String password = user.getPassword();

        try {
            connect();

            // Create user if username available
            resultSet = statement.executeQuery("select * from User where username = '" + username + "'");
            if (!resultSet.next()) {

                // Create email if username available
                resultSet = statement.executeQuery("select * from User where email = '" + email + "'");
                if (!resultSet.next()) {

                    statement.executeUpdate("insert into User (username, firstname, lastname, email, password) values (" +
                                            "'" + username + "', " +
                                            "'" + firstname + "', " +
                                            "'" + lastname + "', " +
                                            "'" + email + "', " +
                                            "'" + password + "');");


                    System.out.println("Database: new user created (" + username + ")");

                } else {
                    return "Email address already taken!";
                }

            } else {
                System.out.println("newUser called - USERNAME ALREADY TAKEN");
                return "Username already taken!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return user;
    }
}