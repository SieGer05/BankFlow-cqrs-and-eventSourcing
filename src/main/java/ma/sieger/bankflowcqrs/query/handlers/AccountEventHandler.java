package ma.sieger.bankflowcqrs.query.handlers;

import lombok.extern.slf4j.Slf4j;
import ma.sieger.bankflowcqrs.enums.OperationType;
import ma.sieger.bankflowcqrs.events.*;
import ma.sieger.bankflowcqrs.query.entities.Account;
import ma.sieger.bankflowcqrs.query.entities.AccountOperation;
import ma.sieger.bankflowcqrs.query.repository.AccountRepository;
import ma.sieger.bankflowcqrs.query.repository.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventHandler {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public AccountEventHandler(AccountRepository accountRepository,
                               OperationRepository operationRepository,
                               QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage eventMessage) {
        log.info("---> Query Side: AccountCreatedEvent received <---");

        Account account = Account.builder()
                .id(event.getAccountId())
                .balance(event.getInitialBalance())
                .status(event.getAccountStatus())
                .currency(event.getCurrency())
                .createdAt(eventMessage.getTimestamp())
                .build();

        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("---> Query Side: AccountActivatedEvent received <---");

        Account account = accountRepository.findById(event.getAccountId())
                .orElseThrow(() -> new RuntimeException(
                        "Account not found when activating: " + event.getAccountId()
                ));

        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountStatusUpdatedEvent event) {
        log.info("---> Query Side: AccountStatusUpdatedEvent received <---");

        Account account = accountRepository.findById(event.getAccountId())
                .orElseThrow(() -> new RuntimeException(
                        "Account not found when updating status: " + event.getAccountId()
                ));

        account.setStatus(event.getAccountStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent event, EventMessage eventMessage) {
        log.info("---> Query Side: AccountDebitedEvent received <---");

        Account account = accountRepository.findById(event.getAccountId())
                .orElseThrow(() -> new RuntimeException(
                        "Account not found when debiting: " + event.getAccountId()
                ));

        AccountOperation accountOperation = AccountOperation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.DEBIT)
                .currency(event.getCurrency())
                .account(account)
                .build();

        operationRepository.save(accountOperation);

        account.setBalance(account.getBalance() - event.getAmount());
        accountRepository.save(account);

        queryUpdateEmitter.emit(e -> true, accountOperation);
    }

    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage eventMessage) {
        log.info("---> Query Side: AccountCreditedEvent received <---");

        Account account = accountRepository.findById(event.getAccountId())
                .orElseThrow(() -> new RuntimeException(
                        "Account not found when crediting: " + event.getAccountId()
                ));

        AccountOperation accountOperation = AccountOperation.builder()
                .amount(event.getAmount())
                .date(eventMessage.getTimestamp())
                .type(OperationType.CREDIT)
                .currency(event.getCurrency())
                .account(account)
                .build();

        operationRepository.save(accountOperation);

        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);

        queryUpdateEmitter.emit(e -> true, accountOperation);
    }
}
