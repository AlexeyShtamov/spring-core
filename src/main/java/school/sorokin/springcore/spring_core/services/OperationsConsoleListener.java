package school.sorokin.springcore.spring_core.services;

import org.springframework.stereotype.Service;
import school.sorokin.springcore.spring_core.models.OperationType;
import school.sorokin.springcore.spring_core.services.operations.OperationCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class OperationsConsoleListener {

    private final String MAIN_TEXT = """
            \nPlease enter one of operation type:
            -USER_CREATE
            -ACCOUNT_CREATE
            -SHOW_ALL_USERS
            -ACCOUNT_DEPOSIT
            -ACCOUNT_WITHDRAW
            -ACCOUNT_TRANSFER
            -ACCOUNT_CLOSE
            """;

    private final Scanner scanner;

    private final Map<OperationType, OperationCommand> commandMap;


    public OperationsConsoleListener(Scanner scanner, List<OperationCommand> commands) {
        this.scanner = scanner;

        this.commandMap = new HashMap<>();
        commands.forEach(command -> commandMap.put(command.getOperationType(), command));
    }

    public void listen(){

        while (true){

            System.out.println(MAIN_TEXT);

            OperationType operation;
            try {
                operation = OperationType.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException e){
                System.out.println("No such operation exists");
                continue;
            }
            commandMap.get(operation).execute();
        }
    }
}
