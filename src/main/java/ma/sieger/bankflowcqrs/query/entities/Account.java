package ma.sieger.bankflowcqrs.query.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import ma.sieger.bankflowcqrs.enums.AccountStatus;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    private String id;
    private double balance;
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
}