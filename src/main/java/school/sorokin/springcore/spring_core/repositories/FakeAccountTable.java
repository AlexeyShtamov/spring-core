package school.sorokin.springcore.spring_core.repositories;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.models.Account;
import school.sorokin.springcore.spring_core.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FakeAccountTable {

    private final List<Account> accounts = new ArrayList<>();


    public List<Account> getAccounts() {
        return accounts;
    }

    public Optional<Account> findById(int accountId) {
        return accounts.stream().filter(e -> e.getId() == accountId).findFirst();
    }
}
