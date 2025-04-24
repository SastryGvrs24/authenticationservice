package com.mtechproject.gsastry.authenticationservice.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<AppUser> appUsers;


	// Default constructor
    public Role() {}

    // Constructor
    public Role(String roleName) {
        this.roleName = roleName;
    }

    // Getter and setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}

