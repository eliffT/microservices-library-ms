package com.turkcell.borrowservice.infrastructure.persistence.entity.readmodel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user_read_models")
public class UserReadModelEntity {

    @Id
    @Column(nullable = false, columnDefinition = "uuid")
    private UUID userId;

    @Column(nullable = false)
    private String membershipLevel; // STANDARD, GOLD, BANNED

    public UserReadModelEntity() {}

    public UserReadModelEntity(UUID userId, String membershipLevel) {
        this.userId = userId;
        this.membershipLevel = membershipLevel;
    }

    // Getters ve Setters

    public UUID userId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String membershipLevel() {
        return membershipLevel;
    }
    public void setMemberLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    // İş kuralını kontrol eden yardımcı metot
    public boolean isBanned() {
        return this.membershipLevel.equals("BANNED");
    }

}