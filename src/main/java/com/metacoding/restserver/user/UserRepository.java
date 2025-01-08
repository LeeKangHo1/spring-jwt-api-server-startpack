package com.metacoding.restserver.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // jpql
    @Query("select u from User u where u.username=:username")
    Optional<User> findByUsername(@Param("username") String username); // @Param 써야 함
}