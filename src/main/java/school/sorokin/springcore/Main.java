package school.sorokin.springcore;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school.sorokin.springcore.spring_core.services.OperationsConsoleListener;

import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("school.sorokin.springcore");

        OperationsConsoleListener ocl = applicationContext.getBean(OperationsConsoleListener.class);

        Executors.newSingleThreadExecutor().submit(ocl::listen);
    }
}