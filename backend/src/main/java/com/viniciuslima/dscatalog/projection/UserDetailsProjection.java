package com.viniciuslima.dscatalog.projection;

public interface UserDetailsProjection {
    String getUsername();

    String getPassword();

    Long getRoleId();

    String getAuthority();
}
