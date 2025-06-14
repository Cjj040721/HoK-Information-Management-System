package com.example.hokims.core;

import com.example.hokims.model.User;

/**
 * UserContext is a utility class that manages the currently logged-in user.
 * It provides static methods for login/logout and role-based checks.
 */
public class UserContext {
    // Holds the currently logged-in user
    private static User currentUser;

    /**
     * Logs in a user by setting the currentUser.
     * @param user the user to be logged in
     */
    public static void login(User user) {
        currentUser = user;
    }

    /**
     * Logs out the current user by clearing the currentUser.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Retrieves the currently logged-in user.
     * @return the current user, or null if not logged in
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks whether a user is currently logged in.
     * @return true if a user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Checks whether the logged-in user has the "Admin" role.
     * Case-insensitive comparison is used.
     * @return true if the user is an admin, false if not logged in or not an admin
     */
    public static boolean isAdmin() {
        return isLoggedIn() && "Admin".equalsIgnoreCase(currentUser.getRole());
    }

    /**
     * Checks whether the logged-in user has the "Player" role.
     * Case-insensitive comparison is used.
     * @return true if the user is a player, false if not logged in or not a player
     */
    public static boolean isPlayer() {
        return isLoggedIn() && "Player".equalsIgnoreCase(currentUser.getRole());
    }
}
