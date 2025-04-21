package school.sorokin.springcore.spring_core.services.operations;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.exceptions.IsAlreadyExistException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.OperationType;
import school.sorokin.springcore.spring_core.services.AccountService;
import school.sorokin.springcore.spring_core.services.UserService;

import java.util.Scanner;

@Component
public class CreateUserOperation implements OperationCommand{

    private final UserService userService;
    private final Scanner scanner;

    public CreateUserOperation(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter login for new user:");
        String login = scanner.nextLine();

        try {

            String result = userService.createUser(login);
            System.out.println(result);

        } catch (IsAlreadyExistException | NoEntityWithThisIdException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.USER_CREATE;
    }
}
