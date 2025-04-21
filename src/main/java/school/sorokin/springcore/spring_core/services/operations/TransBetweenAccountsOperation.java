package school.sorokin.springcore.spring_core.services.operations;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.exceptions.NegativeBalanceException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.OperationType;
import school.sorokin.springcore.spring_core.services.AccountService;

import java.util.Scanner;

@Component
public class TransBetweenAccountsOperation implements OperationCommand{

    private final Scanner scanner;
    private final AccountService accountService;

    public TransBetweenAccountsOperation(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter source account ID:");
            int sourceId = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter target account ID:");
            int targetId = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter amount to transfer:");
            double transferAmount = Integer.parseInt(scanner.nextLine());

            String result = accountService.transBetweenAccounts(sourceId, targetId, transferAmount);
            System.out.println(result);

        }catch (NoEntityWithThisIdException | NumberFormatException | NegativeBalanceException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_TRANSFER;
    }
}
