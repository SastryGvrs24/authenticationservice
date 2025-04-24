package com.mtechproject.gsastry.authenticationservice.domain;

import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class JobSeeker extends AppUser{

    private List<String> skills;
    private String userLocation;
    private List<String> preferredLocations;
    private float experience;

    public JobSeeker() {
    }

    public JobSeeker(String username, String password) {
        super(username, password);
    }

    public JobSeeker(String username, String password, String fullName) {
        super(username, password, fullName);
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public List<String> getPreferredLocations() {
        return preferredLocations;
    }

    public void setPreferredLocations(List<String> preferredLocations) {
        this.preferredLocations = preferredLocations;
    }

    public float getExperience() {
        return experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }
}
