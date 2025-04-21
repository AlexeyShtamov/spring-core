package school.sorokin.springcore.spring_core.repositories;

import org.springframework.stereotype.Component;
import school.sorokin.springcore.spring_core.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FakeUserTable {

    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> findByLogin(String login){
        return users.stream().filter(e -> e.getLogin().equals(login)).findFirst();
    }

    public Optional<User> findById(int id){
        return users.stream().filter(e -> e.getId() == id).findFirst();
    }
}
