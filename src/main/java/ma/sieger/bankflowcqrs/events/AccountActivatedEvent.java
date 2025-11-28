package ma.sieger.bankflowcqrs.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.sieger.bankflowcqrs.enums.AccountStatus;

@Getter @AllArgsConstructor
public class AccountActivatedEvent {
    private String accountId;
    private AccountStatus status;
}