package edu.utdallas.cs6303.finalproject.model.database.repositories;

import edu.utdallas.cs6303.finalproject.model.database.LoginAttemptLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptLogRepository extends JpaRepository<LoginAttemptLog, Long> {
}