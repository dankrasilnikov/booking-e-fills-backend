package com.zephyra.station.models;

import com.sun.istack.NotNull;
import jakarta.persistence.*;


import java.util.Objects;


@Entity
@Table(name = "app_user",schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "supabase_id", unique = true)
    private String supabaseId;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;  ///!!!!!!!!!!!!!!!!!!!!!!!

    public User( String email, String username) {

        this.email = email;
        this.username = username;
    }

    public User() {}
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSupabaseId() {
        return supabaseId;
    }

    public void setSupabaseId(String supabaseId) {
        this.supabaseId = supabaseId;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
