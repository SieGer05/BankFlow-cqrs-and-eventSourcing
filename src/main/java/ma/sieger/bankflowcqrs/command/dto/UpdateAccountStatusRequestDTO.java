package ma.sieger.bankflowcqrs.command.dto;

import ma.sieger.bankflowcqrs.enums.AccountStatus;

public record UpdateAccountStatusRequestDTO(String accountId, AccountStatus accountStatus) {}