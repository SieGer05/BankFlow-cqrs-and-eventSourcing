package ma.sieger.bankflowcqrs.command.dto;

public record DebitAccountRequestDTO(String accountId, double amount, String currency) {}