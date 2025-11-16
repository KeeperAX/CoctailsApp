package models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private Map<Integer, Integer> ratings; // cocktailId -> rating (1-5)

    public User(int id, String username, String email, String passwordHash) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ratings = new HashMap<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Map<Integer, Integer> getRatings() {
        return ratings;
    }

    public void rateCocktail(int cocktailId, int rating) {
        if (rating >= 1 && rating <= 5) {
            this.ratings.put(cocktailId, rating);
        }
    }

    public Integer getRatingForCocktail(int cocktailId) {
        return this.ratings.get(cocktailId);
    }

    public void removeCocktailRating(int cocktailId) {
        this.ratings.remove(cocktailId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
