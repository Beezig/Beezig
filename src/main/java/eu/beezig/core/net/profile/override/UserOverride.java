package eu.beezig.core.net.profile.override;

import java.util.List;

public class UserOverride {
    List<UserOverrideBase> overrides;

    public UserOverride(List<UserOverrideBase> overrides) {
        this.overrides = overrides;
    }

    public List<UserOverrideBase> getOverrides() {
        return this.overrides;
    }
}
