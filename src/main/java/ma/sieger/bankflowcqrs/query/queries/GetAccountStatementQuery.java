package ma.sieger.bankflowcqrs.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class GetAccountStatementQuery {
    private String accountId;
}
