package org.medianik.findway.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * <p>{@code PriorityQueue}, that is sorted with first priority value, else with second.</p>
 * <br>For example:</br>
 * <br>[12, 34, V]</br>
 * <br>[12, 45, V]</br>
 * <br>[13, 1, V]</br>
 * <br>[23, 1, V]</br>
 * @param <T> Type of stored values.
 */
public class ExplicitPriorityQueue<T>{

    private final PriorityQueue<IntIntObjTriple<T>> queue;
    private final DummyKey dummyKey = new DummyKey();
    private final HashMap<T, IntIntObjTriple<T>> map;

    public ExplicitPriorityQueue(){
        Comparator<IntIntObjTriple<T>> comparator = Comparator.comparingInt(IntIntObjTriple::getKey);
        comparator = comparator.thenComparingInt(IntIntObjTriple::getAddon);

        queue = new PriorityQueue<>(comparator);
        map = new HashMap<>();
    }

    public boolean add(int key, int then, T value){
        var newItem = new IntIntObjTriple<>(key, then, value);
        map.put(value, newItem);
        return queue.add(newItem);
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public T poll(){
        T out = queue.poll().getValue();

        prepareDummy(out);
        IntIntObjTriple<T> t;
        map.remove(dummyKey);

        return out;
    }

    private void prepareDummy(T out){
        dummyKey.setEqualTo(out);
        dummyKey.setHash(out.hashCode());
    }

    public void clear(){
        queue.clear();
    }

    @Override
    public String toString(){
        return queue.toString();
    }

    /**
     * Replaces first occurrence of {@code value} in this {@code queue}, else adds new triple.
     * @return true if replaced value, false if added new one.
     */
    public boolean replaceValue(int key, int then, T value){
        prepareDummy(value);
        IntIntObjTriple<T> t;
        if((t = map.get(dummyKey)) != null){
            t.setKey(key);
            t.setAddon(then);
            return true;
        }
        add(key, then, value);
        return false;
    }

    private static class DummyKey{
        private int hash;
        private Object equalTo;

        public void setEqualTo(Object equalTo){
            this.equalTo = equalTo;
        }
        public void setHash(int hash){
            this.hash = hash;
        }
        @Override
        public boolean equals(Object o){
            return o == equalTo;
        }
        @Override
        public int hashCode(){
            return hash;
        }
    }

}
