package org.example.repository;

import org.example.domain.User;
import org.example.dto.user.GetUserDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u join AuthUser a on u.authUserId = a.id where a.username = ?1")
    Optional<User> findByUsername(String username);

    @Query("select u from User u join AuthUser a on u.authUserId = a.id")
    List<GetUserDetailsDTO> findAllUsers();
}
