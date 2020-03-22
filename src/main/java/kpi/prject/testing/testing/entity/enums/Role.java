package kpi.prject.testing.testing.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum  Role implements GrantedAuthority {
    ROLE_USER, ROLE_INSPECTOR;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
