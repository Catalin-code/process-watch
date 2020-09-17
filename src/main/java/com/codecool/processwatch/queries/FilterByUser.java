package com.codecool.processwatch.queries;

import com.codecool.processwatch.domain.Query;
import com.codecool.processwatch.domain.Process;

import java.util.stream.Stream;

/**
 * This is the identity query.  It selects everything from its source.
 */
public class FilterByUser implements Query {
    private String name;

    public FilterByUser(String name){
        this.name = name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Process> run(Stream<Process> input) {
        return input.filter(p ->p.getUserName().contains(name));
    }
}
