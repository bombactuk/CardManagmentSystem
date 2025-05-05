package com.bank.cardManagementSystem.repository;

import com.bank.cardManagementSystem.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByCardNumberEncrypted(String cardNumberEncrypted);

    Optional<Card> findByCardNumberEncrypted(String cardNumberEncrypted);

    List<Card> findAllByUserUsername(String username);

}
