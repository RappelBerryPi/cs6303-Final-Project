package edu.utdallas.cs6303.finalproject.model.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.UserTOTP;


@Repository
public interface UserTOTPRepository extends JpaRepository<UserTOTP, Long> {
    UserTOTP findFirstByUserName(String userName);
    void deleteByUserName(String userName);
    
}