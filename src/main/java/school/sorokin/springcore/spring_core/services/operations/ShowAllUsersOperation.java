package school.sorokin.springcore.spring_core.services.operations;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.models.OperationType;
import school.sorokin.springcore.spring_core.services.UserService;

@Component
public class ShowAllUsersOperation implements OperationCommand{

    private final UserService userService;

    public ShowAllUsersOperation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println(userService.findAllUsers());
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.SHOW_ALL_USERS;
    }
}
