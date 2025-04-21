package school.sorokin.springcore.spring_core.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import school.sorokin.springcore.spring_core.exceptions.NegativeBalanceException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.Account;
import school.sorokin.springcore.spring_core.models.User;
import school.sorokin.springcore.spring_core.repositories.FakeAccountTable;
import school.sorokin.springcore.spring_core.repositories.FakeUserTable;

import java.util.Optional;

@Service
@PropertySource("classpath:application.properties")
public class AccountService {

    private final FakeUserTable fakeUserTable;
    private final FakeAccountTable fakeAccountTable;

    @Value("${account.default-amount}")
    private double moneyAmount;

    @Value("${account.transfer-commission}")
    private int commission;

    public AccountService(FakeUserTable fakeUserTable, FakeAccountTable fakeAccountTable) {
        this.fakeUserTable = fakeUserTable;
        this.fakeAccountTable = fakeAccountTable;
    }

    public String createAccount(int userId) throws NoEntityWithThisIdException {
        User user = fakeUserTable.findById(userId).orElseThrow(() -> new NoEntityWithThisIdException("Cannot find user with id: " + userId));

        Account account = new Account(userId, moneyAmount);
        fakeAccountTable.getAccounts().add(account);
        user.getAccounts().add(account);

        return "New account created with ID: " + account.getId() + " for user: " + user.getLogin();

    }

    public String addMoney(int accountId, double depositAmount) throws NoEntityWithThisIdException, NegativeBalanceException {
        Account account = fakeAccountTable.findById(accountId).orElseThrow(() -> new NoEntityWithThisIdException("Cannot find account with id: " + accountId));

        if (depositAmount <= 0) throw new NegativeBalanceException("Deposit amount couldn't be negative of zero");

        account.setMoneyAmount(account.getMoneyAmount() + depositAmount);
        return "Amount " + depositAmount + " deposited to account ID: " + accountId;
    }

    public String reduceMoney(int accountId, double withdrawAmount) throws NegativeBalanceException, NoEntityWithThisIdException {
        Account account = fakeAccountTable.findById(accountId).orElseThrow(() -> new NoEntityWithThisIdException("Cannot find account with id: " + accountId));

        if (withdrawAmount <= 0) throw new NegativeBalanceException("Withdraw amount couldn't be negative of zero");

        if (account.getMoneyAmount() - withdrawAmount < 0)
            throw new NegativeBalanceException(
                    String.format("Balance couldn't be negative. You balance: %.2f, withdraw: %.2f\n", account.getMoneyAmount(), withdrawAmount));

        account.setMoneyAmount(account.getMoneyAmount() - withdrawAmount);
        return "Amount " + withdrawAmount + " withdraw from account ID: " + accountId;
    }

    public String transBetweenAccounts(int sourceId, int targetId, double transferAmount) throws NoEntityWithThisIdException, NegativeBalanceException {

        Account account1 = fakeAccountTable.findById(sourceId)
                .orElseThrow(() -> new NoEntityWithThisIdException("No source target with id: " + sourceId));
        Account account2 = fakeAccountTable.findById(targetId)
                .orElseThrow(() -> new NoEntityWithThisIdException("No source target with id: " + targetId));

        double totalAmount = transferAmount;
        if (account1.getUserId() != account2.getUserId()){
            totalAmount += ((transferAmount / 100)*commission);
            System.out.println(totalAmount);
        }


        reduceMoney(sourceId, totalAmount);
        addMoney(targetId, transferAmount);

        return String.format("Amount %.2f transferred from account ID %d to account ID %d.\n",
                transferAmount, sourceId, targetId);
    }

    public String closeAccount(int accountId) throws NoEntityWithThisIdException {
        Account account = fakeAccountTable.findById(accountId)
                .orElseThrow(() -> new NoEntityWithThisIdException("Cannot find account with id: " + accountId));

        User user = fakeUserTable.findById(account.getUserId())
                .orElseThrow(() -> new NoEntityWithThisIdException("Cannot find user with id: " + accountId));

        if (user.getAccounts().size() == 1) return "You couldn't close your account if you have only one";

        double money = account.getMoneyAmount();

        user.getAccounts().remove(account);
        int accountTransId = user.getAccounts().get(0).getId();
        try {
            addMoney(accountTransId, money);
        } catch (NegativeBalanceException e) {
            System.out.println(e.getMessage());
        }

        return "Account with ID "+ accountId +" has been closed.";

    }

}
