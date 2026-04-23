package ro.tacklestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.tacklestore.model.AdminUser;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsername(String username);
}

