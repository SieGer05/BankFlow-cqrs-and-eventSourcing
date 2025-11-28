package ma.sieger.bankflowcqrs.command.Aggregates;

import lombok.extern.slf4j.Slf4j;
import ma.sieger.bankflowcqrs.command.commands.AddAccountCommand;
import ma.sieger.bankflowcqrs.command.commands.CreditAccountCommand;
import ma.sieger.bankflowcqrs.command.commands.DebitAccountCommand;
import ma.sieger.bankflowcqrs.command.commands.UpdateAccountStatusCommand;
import ma.sieger.bankflowcqrs.enums.AccountStatus;
import ma.sieger.bankflowcqrs.events.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private AccountStatus status;

    public AccountAggregate() {}

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        log.info("---> Account command received <---");

        if (command.getInitialBalance() <= 0)
            throw new IllegalArgumentException("Initial balance must be greater than 0.");

        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                AccountStatus.CREATED,
                command.getCurrency()
        ));

        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.info("---> Account created <---");
        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.status = event.getAccountStatus();
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        log.info("---> Account activated <---");
        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        log.info("---> Credit account command received <---");

        if (!status.equals(AccountStatus.ACTIVATED))
            throw new RuntimeException("The account: " + command.getAccountId() + " is not activated.");

        if (command.getAmount() <= 0)
            throw new IllegalArgumentException("Amount must be greater than 0.");

        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getAccountId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("---> Account credited <---");
        this.accountId = event.getAccountId();
        this.balance = this.balance + event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        log.info("---> Debit account command received <---");

        if (!status.equals(AccountStatus.ACTIVATED))
            throw new RuntimeException("The account: " + command.getAccountId() + " is not activated.");

        if (balance < command.getAmount())
            throw new RuntimeException("Insufficient funds.");

        if (command.getAmount() <= 0)
            throw new IllegalArgumentException("Amount must be greater than 0.");

        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getAccountId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        log.info("---> Account debited <---");
        this.accountId = event.getAccountId();
        this.balance = this.balance - event.getAmount();
    }

    @CommandHandler
    public void handle(UpdateAccountStatusCommand command) {
        log.info("---> Account status command received <---");

        if (command.getAccountStatus() == status)
            throw new RuntimeException("This account: " + command.getAccountId() + " is already " + command.getAccountStatus().name());

        AggregateLifecycle.apply(new AccountStatusUpdatedEvent(
                command.getAccountId(),
                command.getAccountStatus()
        ));
    }

    @EventSourcingHandler
    public void on(AccountStatusUpdatedEvent event) {
        log.info("---> Account status updated <---");
        this.accountId = event.getAccountId();
        this.status = event.getAccountStatus();
    }
}
