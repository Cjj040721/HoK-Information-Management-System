package com.example.hokims.util;

import com.example.hokims.model.User;
import com.example.hokims.repository.DataStore;

import java.util.Optional;

/**
 * AuthManager handles user authentication logic.
 * It does not manage session state—only verifies credentials.
 */
public class AuthManager {

    /**
     * Validates login credentials against stored users.
     *
     * @param username the input username
     * @param password the input password
     * @return the matched User object if credentials are valid; null otherwise
     */
    public static User login(String username, String password) {
        return DataStore.users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}
