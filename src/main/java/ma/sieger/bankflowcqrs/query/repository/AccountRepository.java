package ma.sieger.bankflowcqrs.query.repository;

import ma.sieger.bankflowcqrs.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}