package projectc4.c4.util;

import java.io.Serializable;

/**
 * @author Kalle Bornemark
 */
public class User implements Serializable {
    private final String username;
    private boolean isPlaying = false;

    public User(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
