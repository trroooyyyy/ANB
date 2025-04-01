package chnu.edu.anetrebin.anb.model;

import chnu.edu.anetrebin.anb.enums.AccountStatus;
import chnu.edu.anetrebin.anb.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Column(nullable = false, name = "account_status")
    private AccountStatus accountStatus;

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

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
