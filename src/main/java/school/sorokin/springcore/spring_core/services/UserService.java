package school.sorokin.springcore.spring_core.services;

import org.springframework.stereotype.Service;
import school.sorokin.springcore.spring_core.exceptions.IsAlreadyExistException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.Account;
import school.sorokin.springcore.spring_core.models.User;
import school.sorokin.springcore.spring_core.repositories.FakeAccountTable;
import school.sorokin.springcore.spring_core.repositories.FakeUserTable;


@Service
public class UserService {

    private final FakeUserTable fakeBD;
    private final FakeAccountTable fakeAccountTable;

    public UserService(FakeUserTable fakeBD, FakeAccountTable fakeAccountTable) {
        this.fakeBD = fakeBD;
        this.fakeAccountTable = fakeAccountTable;
    }

    public String createUser(String login) throws IsAlreadyExistException, NoEntityWithThisIdException {
        if (login != null){

            if (fakeBD.findByLogin(login).isPresent()) throw new IsAlreadyExistException("User with login " + login + " is already exist");

            User user = new User(login);

            fakeBD.getUsers().add(user);

            Account account = new Account(user.getId(), 500.0);
            user.getAccounts().add(account);
            fakeAccountTable.getAccounts().add(account);

            return "User created: " + user;
        }
        throw new NoEntityWithThisIdException("Login cannot be null");
    }

    public String findAllUsers(){
        return "List of all users:\n" + fakeBD.getUsers();
    }


}
