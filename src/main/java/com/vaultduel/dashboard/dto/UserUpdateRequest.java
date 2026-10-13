package com.vaultduel.dashboard.dto;

/** Admin-only: update another user's role/enabled state. All fields optional (null = leave unchanged). */
public class UserUpdateRequest {
    private String role;      // "USER" or "ADMIN"
    private Boolean enabled;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
