package org.medianik.findway.exception;

import javafx.util.Pair;
import org.medianik.findway.util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class EnvironmentHolder{
    HashSet<Object> environment = new HashSet<>();

    public EnvironmentHolder(Object... environment){
        this.environment.addAll(Arrays.asList(environment));
    }

    public EnvironmentHolder(Collection<Object> environment){
        this.environment.addAll(environment);
    }

    public EnvironmentHolder(Map<String, Object> environment){
        for(var e : environment.entrySet())
            this.environment.add(new Pair<>(e.getKey(), e.getValue()));
    }

    @Override
    public String toString(){
        String startingString = "EnvironmentHolder{" +
                environment.toString() +
                '}';

        return Util.beautify(startingString);
    }

}
