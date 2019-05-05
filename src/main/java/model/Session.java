package model;

public class Session {

    private String username;
    private boolean valid;
    private long lastLoginMillis;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isValid  () {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getLastLoginMillis() {
        return lastLoginMillis;
    }

    public void setLastLoginMillis(long lastLoginMillis) {
        this.lastLoginMillis = lastLoginMillis;
    }
}
