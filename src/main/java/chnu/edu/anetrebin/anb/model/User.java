package chnu.edu.anetrebin.anb.model;

import chnu.edu.anetrebin.anb.enums.UserStatus;
import chnu.edu.anetrebin.anb.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String login;

    @Column(length = 50, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_status")
    private UserStatus userStatus;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String surname;

    @Column(unique = true, length = 100)
    private String email;

    @Column(unique = true, length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.role = Role.CUSTOMER;
        this.userStatus = UserStatus.ACTIVE;
        this.accounts = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addCard(Card card) {
        cards.add(card);
    }
}
