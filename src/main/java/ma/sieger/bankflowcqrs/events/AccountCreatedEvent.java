package ma.sieger.bankflowcqrs.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.sieger.bankflowcqrs.enums.AccountStatus;

@Getter @AllArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    private double initialBalance;
    private AccountStatus accountStatus;
    private String currency;
}