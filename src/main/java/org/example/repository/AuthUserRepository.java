package org.example.repository;

import org.example.domain.AuthUser;
import org.example.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Integer> {

    @Query("select a from AuthUser a where upper(a.username) = upper(?1) and  a.active = true")
    Optional<AuthUser> findByUsername(String username);

    @Query("select a.id from AuthUser a where a.username = ?1")
    Integer findAuthIdByUsername(String username);

    @Modifying
    @Transactional
    @Query("update AuthUser a set a.role = :inRole, a.active = :inActive  where a.username = :inUsername")
    void updateAuthUser(@Param("inUsername") String username,
                        @Param("inRole") Role role,
                        @Param("inActive") boolean active);
}
