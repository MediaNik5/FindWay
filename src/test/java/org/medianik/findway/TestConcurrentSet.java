package org.medianik.findway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestConcurrentSet{

    @Test
    public void test(){
        Set<Integer> set = ConcurrentHashMap.newKeySet(5);
        set.addAll(Set.of(1, 2, 3, 4, 5));
        for(var a : set){
            set.remove(5);
            set.remove(1);
            set.add(6);
            Assertions.assertEquals(set.size(), 4);
        }
        Assertions.assertEquals(set.size(), 4);
    }
}
