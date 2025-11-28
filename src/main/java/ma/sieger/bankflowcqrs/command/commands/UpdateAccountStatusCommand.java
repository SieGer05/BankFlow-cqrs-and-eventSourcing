package ma.sieger.bankflowcqrs.command.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.sieger.bankflowcqrs.enums.AccountStatus;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter @AllArgsConstructor
public class UpdateAccountStatusCommand {
    @TargetAggregateIdentifier
    private String accountId;
    private AccountStatus accountStatus;
}