package Innova.AdaSchool.Biblioteca.repository;

import Innova.AdaSchool.Biblioteca.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public interface UserPostgreRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.myBooks = :myBooks WHERE u.email = :email")
    void updateMyBooks(String email, HashMap<String, String> myBooks);
}
