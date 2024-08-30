package com.modernfamily.ukids.domain.user.model.repository;

import com.modernfamily.ukids.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsById(String id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findById(String id);
    Optional<User> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "update User u " +
            "set u.password = :#{#user.password}, " +
            "u.name = :#{#user.name}, " +
            "u.email = :#{#user.email}, " +
            "u.phone = :#{#user.phone}, " +
            "u.profileImage = :#{#user.profileImage} " +
            "where u.id = :#{#user.id}")
    void userUpdate(User user);

}
