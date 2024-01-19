package com.mindhub.homebanking.dto;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;

public class AdminDTO {
    private Long id;
    private String firstName, lastName, email;
    private RoleType roleType;
    public AdminDTO (Client admin){
        id = admin.getId();
        firstName = admin.getFirstName();
        lastName = admin.getLastName();
        email = admin.getEmail();
        roleType = admin.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", roleType=" + roleType +
                '}';
    }
}
