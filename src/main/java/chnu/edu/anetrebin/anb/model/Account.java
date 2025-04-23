package chnu.edu.anetrebin.anb.model;

import chnu.edu.anetrebin.anb.enums.AccountStatus;
import chnu.edu.anetrebin.anb.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ACCOUNT_NUMBER_FORMAT = "%s%010d";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, name = "account_name")
    private String accountName;

    @Column(unique = true, nullable = false, length = 30, name = "account_number")
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    @DecimalMin("0.00")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "currency")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "account_status")
    private AccountStatus accountStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiverAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> receivedTransactions;

    @PrePersist
    protected void onCreate() {
        this.balance = BigDecimal.valueOf(100);
        this.accountStatus = AccountStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static String generateAccountNumber(Currency currency) {
        long accountNum = 1_000_000_000L + SECURE_RANDOM.nextLong(9_000_000_000L);
        return String.format(ACCOUNT_NUMBER_FORMAT, currency.getCode(), accountNum);
    }
}
