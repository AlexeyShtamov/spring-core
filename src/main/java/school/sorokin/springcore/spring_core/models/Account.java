package school.sorokin.springcore.spring_core.models;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double moneyAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Account(User user, double moneyAmount) {
        this.user = user;
        this.moneyAmount = moneyAmount;
    }

    public Account() {

    }


    public int getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "moneyAmount=" + moneyAmount +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
