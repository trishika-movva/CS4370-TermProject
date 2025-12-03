/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.csx370.models;

import java.sql.Timestamp;

/**
 * Represents an authenticated user account.
 */
public class User {

    /**
     * Unique identifier for the user.
     */
    private final String userId;

    /**
     * Account-level identifiers.
     */
    private final String username;
    private final String email;
    private final String passwordHash;
    private final Timestamp createdAt;

    /**
     * Optional profile details (kept for compatibility with the P2 views).
     */
    private final String firstName;
    private final String lastName;

    /**
     * Path of the profile image file for the user.
     */
    private final String profileImagePath;

    private User(String userId, String username, String email, String passwordHash, Timestamp createdAt,
                 String firstName, String lastName, String profileImagePath) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImagePath = profileImagePath != null ? profileImagePath : getAvatarPath(userId);
    }

    /**
     * Constructs a User from the ApplicationTracker user schema.
     */
    public User(String userId, String username, String email, String passwordHash, Timestamp createdAt) {
        this(userId, username, email, passwordHash, createdAt, null, null, getAvatarPath(userId));
    }

    /**
     * Constructs a User with legacy profile fields.
     */
    public User(String userId, String firstName, String lastName) {
        this(userId, firstName, null, null, null, firstName, lastName, getAvatarPath(userId));
    }

    /**
     * Constructs a User with legacy profile fields.
     */
    public User(String userId, String firstName, String lastName, String profileImagePath) {
        this(userId, firstName, null, null, null, firstName, lastName, profileImagePath);
    }

    /**
     * Given a userId generate a valid avatar path.
     */
    private static String getAvatarPath(String userId) {
        int fileNo = (userId.hashCode() % 20) + 1;
        String avatarFileName = String.format("avatar_%d.png", fileNo);
        return "/avatars/" + avatarFileName;
    }

    /**
     * Returns the user ID.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the username, falling back to first name if not set.
     */
    public String getUsername() {
        return username != null ? username : firstName;
    }

    /**
     * Returns the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the stored password hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Returns the account creation timestamp.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the first name, falling back to username for legacy compatibility.
     */
    public String getFirstName() {
        return firstName != null ? firstName : getUsername();
    }

    /**
     * Returns the last name of the user.
     */
    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    /**
     * Returns the path of the profile image file for the user.
     */
    public String getProfileImagePath() {
        return profileImagePath;
    }
}
