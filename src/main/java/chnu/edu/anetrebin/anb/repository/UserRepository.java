package chnu.edu.anetrebin.anb.repository;

import chnu.edu.anetrebin.anb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<User> findAllByOrderByIdAsc();
}