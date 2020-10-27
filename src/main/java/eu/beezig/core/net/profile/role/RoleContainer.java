package eu.beezig.core.net.profile.role;

import eu.beezig.core.net.profile.override.UserOverride;

public class RoleContainer {
    private final UserRole role;
    private UserOverride override;

    public RoleContainer(UserRole role) {
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }

    public UserOverride getOverride() {
        return override;
    }

    public void setOverride(UserOverride override) {
        this.override = override;
    }
}
