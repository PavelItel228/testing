package kpi.prject.testing.testing.repository;

import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<List<User>> findAllByRole(Role role);
}
