package school.sorokin.springcore.spring_core.services.operations;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.OperationType;
import school.sorokin.springcore.spring_core.services.AccountService;

import java.util.Scanner;

@Component
public class CloseAccountOperation implements OperationCommand{

    private final Scanner scanner;
    private final AccountService accountService;

    public CloseAccountOperation(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }


    @Override
    public void execute() {
        try {
            System.out.println("Enter account ID to close:");
            int accountId = Integer.parseInt(scanner.nextLine());

            String result = accountService.closeAccount(accountId);
            System.out.println(result);

        } catch (NoEntityWithThisIdException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CLOSE;
    }
}
