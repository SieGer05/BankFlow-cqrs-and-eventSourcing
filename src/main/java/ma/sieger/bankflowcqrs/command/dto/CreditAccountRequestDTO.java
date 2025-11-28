package ma.sieger.bankflowcqrs.command.dto;

public record CreditAccountRequestDTO(String accountId, double amount, String currency) {}