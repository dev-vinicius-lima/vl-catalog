package com.viniciuslima.dscatalog.repositories;

import com.viniciuslima.dscatalog.entities.Role;
import com.viniciuslima.dscatalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
