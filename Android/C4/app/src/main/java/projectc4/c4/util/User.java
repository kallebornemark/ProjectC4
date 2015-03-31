package projectc4.c4.util;

/**
 * @author Kalle Bornemark
 */
public class User {
    private final String username;
    private boolean isPlaying = false;

    public User(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
