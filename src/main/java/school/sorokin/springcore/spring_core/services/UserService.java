package school.sorokin.springcore.spring_core.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import school.sorokin.springcore.spring_core.exceptions.IsAlreadyExistException;
import school.sorokin.springcore.spring_core.exceptions.NoEntityWithThisIdException;
import school.sorokin.springcore.spring_core.models.User;
import school.sorokin.springcore.spring_core.services.helpers.TransactionHelper;

import java.util.Optional;


@Service
public class UserService {

    private final SessionFactory sessionFactory;
    private final TransactionHelper transactionHelper;
    private final AccountService accountService;

    public UserService(SessionFactory sessionFactory, TransactionHelper transactionHelper, AccountService accountService) {
        this.sessionFactory = sessionFactory;
        this.transactionHelper = transactionHelper;
        this.accountService = accountService;
    }

    public String createUser(String login) throws IsAlreadyExistException, NoEntityWithThisIdException {
        if (login != null){
            User createdUser = transactionHelper.executeInTransaction(() -> {
                var session = sessionFactory.getCurrentSession();
                User foundedUser = session.createQuery("FROM User WHERE login = :login", User.class)
                        .setParameter("login", login)
                        .getSingleResultOrNull();

                if (foundedUser != null) try {
                    throw new IsAlreadyExistException("User with login " + login + " is already exist");
                } catch (IsAlreadyExistException e) {
                    System.out.println(e.getMessage());
                }
                User user = new User(login);
                session.persist(user);

                accountService.createAccount(user);
                return user;
            });

            return "User created: " + createdUser;
        }
        throw new NoEntityWithThisIdException("Login cannot be null");
    }

    public String findAllUsers(){
        try (Session session = sessionFactory.openSession()){
            return "List of users: \n" + session.createQuery("SELECT u from User u", User.class).list();
        }
    }

    public Optional<User> findUserById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            var user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }


}
