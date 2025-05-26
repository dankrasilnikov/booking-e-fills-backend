package com.zephyra.station.dto;

public class SupabaseUserDTO {
    private String username;
    private String supabaseId;
    private String email;
    private String password;
    private UserMetadata user_metadata;

    public static class UserMetadata {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSupabaseId() {
        return supabaseId;
    }

    public void setSupabaseId(String supabaseId) {
        this.supabaseId = supabaseId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserMetadata getUser_metadata() {
        return user_metadata;
    }

    public void setUser_metadata(UserMetadata user_metadata) {
        this.user_metadata = user_metadata;
    }
}
