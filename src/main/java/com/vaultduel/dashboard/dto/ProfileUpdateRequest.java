package com.vaultduel.dashboard.dto;

import jakarta.validation.constraints.Size;

/** Self-service profile update: all fields optional (null/blank = leave unchanged). */
public class ProfileUpdateRequest {

    private String fullName;

    @Size(min = 8, message = "New password must be at least 8 characters")
    private String newPassword;

    /** Required (and checked) only when newPassword is set. */
    private String currentPassword;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
}
