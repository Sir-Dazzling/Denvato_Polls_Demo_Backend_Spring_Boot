package com.example.denvato_polls_demo.denvato_polls_demo.repository;

import com.example.denvato_polls_demo.denvato_polls_demo.models.Role;
import com.example.denvato_polls_demo.denvato_polls_demo.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByName(RoleName roleName);
}