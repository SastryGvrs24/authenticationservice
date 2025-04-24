package com.mtechproject.gsastry.authenticationservice.domain;


import jakarta.persistence.Entity;

@Entity
public class Recruiter extends AppUser {


    public Recruiter() {
    }

    public Recruiter(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    public Recruiter(String username, String password) {
        super(username, password);
    }
}
