package school.sorokin.springcore.spring_core.services;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import school.sorokin.springcore.spring_core.exceptions.NegativeBalanceException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.Account;
import school.sorokin.springcore.spring_core.models.User;
import school.sorokin.springcore.spring_core.services.helpers.TransactionHelper;

import java.util.Optional;


@Service
@PropertySource("classpath:application.properties")
public class AccountService {

    private final TransactionHelper transactionHelper;

    private final SessionFactory sessionFactory;

    @Value("${account.default-amount}")
    private double moneyAmount;

    @Value("${account.transfer-commission}")
    private int commission;

    public AccountService(TransactionHelper transactionHelper, SessionFactory sessionFactory) {
        this.transactionHelper = transactionHelper;
        this.sessionFactory = sessionFactory;
    }

    private Optional<Account> findAccountById(Long id) {
        var account = sessionFactory.getCurrentSession()
                .get(Account.class, id);
        return Optional.of(account);
    }

    public String createAccount(User user) {
        Account createdAccount = transactionHelper.executeInTransaction(() -> {

            Account account = new Account(user, moneyAmount);

            sessionFactory.getCurrentSession().persist(account);
            return account;
        });

        return "New account created with ID: " + createdAccount.getId() + " for user: " + createdAccount.getUser().getId();
    }

    public String addMoney(long accountId, double depositAmount) {
        Account account = transactionHelper.executeInTransaction(() -> {
            var session = sessionFactory.getCurrentSession();
            Account foundedAccount = findAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find account with id: " + accountId));

            if (depositAmount <= 0)
                throw new IllegalArgumentException("Deposit amount couldn't be negative of zero");

            foundedAccount.setMoneyAmount(foundedAccount.getMoneyAmount() + depositAmount);
            session.persist(foundedAccount);

            return foundedAccount;
        });

        return "Amount " + depositAmount + " deposited to account ID: " + account.getUser().getId();
    }

    public String reduceMoney(long accountId, double withdrawAmount) {

        Account updatedAccount = transactionHelper.executeInTransaction(() -> {
            var session = sessionFactory.getCurrentSession();
            Account foundedAccount = findAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find account with id: " + accountId));

            if (withdrawAmount <= 0)
                throw new IllegalArgumentException("Deposit amount couldn't be negative of zero");

            if (foundedAccount.getMoneyAmount() - withdrawAmount < 0)
                throw new IllegalArgumentException(
                        String.format("Balance couldn't be negative. You balance: %.2f, withdraw: %.2f\n", foundedAccount.getMoneyAmount(), withdrawAmount));

            foundedAccount.setMoneyAmount(foundedAccount.getMoneyAmount() - withdrawAmount);
            session.persist(foundedAccount);

            return foundedAccount;
        });
        return "Amount " + withdrawAmount + " withdraw from account ID: " + updatedAccount.getId();
    }

    public String transBetweenAccounts(long sourceId, long targetId, double transferAmount) {
        return transactionHelper.executeInTransaction(() -> {
            Account account1 = findAccountById(sourceId)
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find account with id: " + sourceId));
            Account account2 = findAccountById(targetId)
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find account with id: " + targetId));

            double totalAmount = transferAmount;
            if (account1.getUser().getId() != account2.getUser().getId()){
                totalAmount += ((transferAmount / 100)*commission);
            }

            reduceMoney(sourceId, totalAmount);
            addMoney(targetId, transferAmount);

            return String.format("Amount %.2f transferred from account ID %d to account ID %d.\n",
                    transferAmount, sourceId, targetId);
        });
    }

    public String closeAccount(long accountId) throws NoEntityWithThisIdException {
        return transactionHelper.executeInTransaction(() -> {
            var session = sessionFactory.getCurrentSession();
            Account foundedAccount = findAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find account with id: " + accountId));

            User foundedUser = session.get(User.class, foundedAccount.getUser().getId());
            if (foundedUser == null)
                throw new IllegalArgumentException("Cannot find user with id: " + foundedAccount.getUser().getId());

            if (foundedUser.getAccounts().size() == 1) throw new IllegalArgumentException("You couldn't close your account if you have only one");

            double money = foundedAccount.getMoneyAmount();

            foundedUser.getAccounts().remove(foundedAccount);
            int accountTransId = foundedUser.getAccounts().get(0).getId();
            addMoney(accountTransId, money);

            session.persist(foundedAccount);
            session.remove(foundedAccount);

            return "Account with id %d is closed\n".formatted(accountId);
        });
    }

}
