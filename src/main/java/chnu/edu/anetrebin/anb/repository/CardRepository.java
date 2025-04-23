package chnu.edu.anetrebin.anb.repository;

import chnu.edu.anetrebin.anb.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByCardNumber(String cardNumber);

    List<Card> findAllByOrderByIdAsc();
}