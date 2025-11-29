package ma.sieger.bankflowcqrs.query.dto;

import ma.sieger.bankflowcqrs.query.entities.Account;
import ma.sieger.bankflowcqrs.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(
        Account account,
        List<AccountOperation> operations) {}