package school.sorokin.springcore.spring_core.services.operations;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.OperationType;
import school.sorokin.springcore.spring_core.services.AccountService;

import java.util.Scanner;

@Component
public class CreateAccountOperation implements OperationCommand{

    private final Scanner scanner;
    private final AccountService accountService;

    public CreateAccountOperation(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
        this.accountService = accountService;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter the user id for which to create an account:");
            int userId = Integer.parseInt(scanner.nextLine());

            String result = accountService.createAccount(userId);
            System.out.println(result);

        } catch (NoEntityWithThisIdException e){
            System.out.println(e.getMessage());
        } catch (NumberFormatException e){
            System.out.println("Unsupported format of number");
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CREATE;
    }
}
