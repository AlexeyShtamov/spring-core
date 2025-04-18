package school.sorokin.springcore.spring_core.services;

import org.springframework.stereotype.Service;
import school.sorokin.springcore.spring_core.exceptions.IsAlreadyExistException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.User;
import school.sorokin.springcore.spring_core.repositories.FakeUserTable;

import java.util.Optional;

@Service
public class UserService {

    private final FakeUserTable fakeBD;

    public UserService(FakeUserTable fakeBD) {
        this.fakeBD = fakeBD;
    }

    public String createUser(String login) throws IsAlreadyExistException, NoEntityWithThisIdException {
        if (login != null){
            Optional<User> optionalUser =fakeBD.findByLogin(login);

            if (optionalUser.isPresent())
                throw new IsAlreadyExistException("User with login " + login + " is already exist");

            User user = new User(login);
            fakeBD.getUsers().add(user);

            return "User created: " + user;
        }
        throw new NoEntityWithThisIdException("Login cannot be null");
    }

    public User findUser(){
        return null;
    }

    public String findAllUsers(){
        return "List of all users:\n" + fakeBD.getUsers();
    }


}
