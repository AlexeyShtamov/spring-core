package school.sorokin.springcore.spring_core.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private static int count;

    private final int id;
    private String login;
    private List<Account> accounts;

    public User(String login) {
        this.id = ++count;
        this.login = login;
        this.accounts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accounts=" + accounts +
                '}';
    }

}
