package school.sorokin.springcore.spring_core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import school.sorokin.springcore.spring_core.services.OperationsConsoleListener;
import school.sorokin.springcore.spring_core.services.operations.AddMoneyOperation;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class BeanConfig {

    @Bean
    public Scanner scanner(){
        return new Scanner(System.in);
    }

}
