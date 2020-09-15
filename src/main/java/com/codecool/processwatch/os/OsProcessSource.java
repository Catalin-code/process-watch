package com.codecool.processwatch.os;

import java.util.stream.Stream;

import com.codecool.processwatch.domain.Process;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.User;

/**
 * A process source using the Java {@code ProcessHandle} API to retrieve information
 * about the current processes.
 */
public class OsProcessSource implements ProcessSource {
    /**
     * {@inheritDoc}
     */

    private Process convertProcessHandleToProcess(ProcessHandle processHandle){
        User user = new User(processHandle.info().user().orElse("User not found"));
        Process process = new Process(processHandle.pid(),
                                      processHandle.parent().map(handle ->handle.pid()).orElse(-1l),
                                      user,
                                      processHandle.info().command().orElse("Name not found"),
                                      processHandle.info().arguments().orElse(new String[0]));
        return process;
    }

    @Override
    public Stream<Process> getProcesses() {
        return ProcessHandle.allProcesses().map(processHandle ->convertProcessHandleToProcess(processHandle));
    }
}
