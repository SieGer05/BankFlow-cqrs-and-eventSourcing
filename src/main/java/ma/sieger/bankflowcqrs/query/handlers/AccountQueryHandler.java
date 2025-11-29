package ma.sieger.bankflowcqrs.query.handlers;

import ma.sieger.bankflowcqrs.query.dto.AccountStatementResponseDTO;
import ma.sieger.bankflowcqrs.query.entities.Account;
import ma.sieger.bankflowcqrs.query.entities.AccountOperation;
import ma.sieger.bankflowcqrs.query.queries.GetAccountStatementQuery;
import ma.sieger.bankflowcqrs.query.queries.GetAllAccountsQuery;
import ma.sieger.bankflowcqrs.query.queries.WatchEventQuery;
import ma.sieger.bankflowcqrs.query.repository.AccountRepository;
import ma.sieger.bankflowcqrs.query.repository.OperationRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountQueryHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query) {
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<AccountOperation> operations = operationRepository.findByAccountId(query.getAccountId());

        return new AccountStatementResponseDTO(account, operations);
    }

    @QueryHandler
    public AccountOperation on(WatchEventQuery query) {
        return AccountOperation.builder().build();
    }
}