package com.example.hokims.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The User class represents an account within the system, either an admin or a player.
 * It contains login credentials and a role designation.
 *
 * Lombok Annotations:
 * - @Data: Generates getters, setters, equals(), hashCode(), and toString()
 * - @NoArgsConstructor: Generates a no-argument constructor
 * - @AllArgsConstructor: Generates a constructor with all fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // Username used for login and identification
    private String username;

    // Password used for authentication (should be encrypted/stored securely in real applications)
    private String password;

    // Role of the user in the system (e.g., "Admin" or "Player")
    private String role;

    /**
     * Checks if the user has an administrative role.
     *
     * @return true if the user's role is "Admin" (case-insensitive), false otherwise
     */
    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(role);
    }
}
