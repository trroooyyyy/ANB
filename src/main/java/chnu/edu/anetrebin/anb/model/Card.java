package chnu.edu.anetrebin.anb.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16, name = "card_number")
    private String cardNumber;

    @Column(nullable = false, name = "expiry_date")
    private YearMonth expiryDate;

    @Column(nullable = false, columnDefinition = "SMALLINT")
    private short cvv;

    @Column(nullable = false)
    private String cardHolderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        this.cardHolderName = user.getName() + " " + user.getSurname();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
