package com.codecool.processwatch.queries;

import com.codecool.processwatch.domain.Query;
import com.codecool.processwatch.domain.Process;

import java.util.stream.Stream;

/**
 * This is the identity query.  It selects everything from its source.
 */
public class FilterByPPID implements Query {
    private String ppid;

    public FilterByPPID(String ppid){
        this.ppid = ppid;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Process> run(Stream<Process> input) {
        return input.filter(p ->String.valueOf(p.getParentPid()).contains(ppid));
    }
}
