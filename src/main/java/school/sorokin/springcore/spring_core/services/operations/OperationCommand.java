package school.sorokin.springcore.spring_core.services.operations;

import school.sorokin.springcore.spring_core.models.OperationType;

public interface OperationCommand {

    void execute();
    OperationType getOperationType();

}
