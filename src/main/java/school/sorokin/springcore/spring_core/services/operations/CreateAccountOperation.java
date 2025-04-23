package school.sorokin.springcore.spring_core.services.operations;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.OperationType;
import school.sorokin.springcore.spring_core.models.User;
import school.sorokin.springcore.spring_core.services.AccountService;
import school.sorokin.springcore.spring_core.services.UserService;

import java.util.NoSuchElementException;
import java.util.Scanner;

@Component
public class CreateAccountOperation implements OperationCommand{

    private final Scanner scanner;
    private final AccountService accountService;
    private final UserService userService;

    public CreateAccountOperation(Scanner scanner, AccountService accountService, UserService userService) {
        this.scanner = scanner;
        this.accountService = accountService;
        this.userService = userService;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter the user id for which to create an account:");
            int userId = Integer.parseInt(scanner.nextLine());

            User user = userService.findUserById((long) userId)
                    .orElseThrow(() -> new NoSuchElementException("No user with id " + userId));

            String result = accountService.createAccount(user);
            System.out.println(result);

        } catch (NumberFormatException e){
            System.out.println("Unsupported format of number");
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.ACCOUNT_CREATE;
    }
}
