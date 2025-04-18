package school.sorokin.springcore.spring_core.services;

import org.springframework.stereotype.Service;
import school.sorokin.springcore.spring_core.exceptions.IsAlreadyExistException;
import school.sorokin.springcore.spring_core.exceptions.NegativeBalanceException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.OperationType;

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

    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;

    public OperationsConsoleListener(UserService userService, AccountService accountService, Scanner scanner) {
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = scanner;
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

            switch (operation){
                case USER_CREATE -> callCreateUser();
                case ACCOUNT_CREATE -> callCreateAccount();
                case ACCOUNT_DEPOSIT -> callAddMoney();
                case ACCOUNT_TRANSFER -> callTransBetweenAccounts();
                case SHOW_ALL_USERS -> callShowAllUsers();
                case ACCOUNT_WITHDRAW -> callWithdrawMoney();
                case ACCOUNT_CLOSE -> callCloseAccount();
            }
        }


    }

    private void callCreateUser(){
        System.out.println("Enter login for new user:");
        String login = scanner.nextLine();

        try {

            String result = userService.createUser(login);
            System.out.println(result);

        } catch (IsAlreadyExistException | NoEntityWithThisIdException e) {
            System.out.println(e.getMessage());
        }
    }

    private void callCreateAccount(){
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

    private void callShowAllUsers(){
        System.out.println(userService.findAllUsers());
    }

    private void callAddMoney(){
        try {
            System.out.println("Enter account ID:");
            int accountId = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter amount to deposit:");
            double depositAmount = Integer.parseInt(scanner.nextLine());

            String result = accountService.addMoney(accountId, depositAmount);
            System.out.println(result);

        }catch (NoEntityWithThisIdException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private void callWithdrawMoney(){
        try {
            System.out.println("Enter account ID:");
            int accountId = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter amount of withdraw:");
            double withdrawAmount = Integer.parseInt(scanner.nextLine());

            String result = accountService.reduceMoney(accountId, withdrawAmount);
            System.out.println(result);

        }catch (NoEntityWithThisIdException | NumberFormatException | NegativeBalanceException e) {
            System.out.println(e.getMessage());
        }
    }

    private void callTransBetweenAccounts(){
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

    private void callCloseAccount(){
        try {
            System.out.println("Enter account ID to close:");
            int accountId = Integer.parseInt(scanner.nextLine());

            String result = accountService.closeAccount(accountId);
            System.out.println(result);

        } catch (NoEntityWithThisIdException e) {
            System.out.println(e.getMessage());
        }
    }
}
