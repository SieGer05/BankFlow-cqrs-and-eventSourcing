package ma.sieger.bankflowcqrs.command.dto;

public record AddNewAccountRequestDTO(double initialBalance, String currency) {}