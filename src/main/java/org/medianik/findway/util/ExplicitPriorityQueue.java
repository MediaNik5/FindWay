package org.medianik.findway.util;

import java.util.Comparator;
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

    public ExplicitPriorityQueue(){
        Comparator<IntIntObjTriple<T>> comparator = Comparator.comparingInt(IntIntObjTriple::getKey);
        comparator = comparator.thenComparingInt(IntIntObjTriple::getAddon);

        queue = new PriorityQueue<>(comparator);
    }

    public boolean add(int key, int then, T value){
        return queue.add(new IntIntObjTriple<>(key, then, value));
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public T poll(){
        return queue.poll().getValue();
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
        for(var triple : queue){
            if(triple.getValue().equals(value)){
                triple.setKey(key);
                triple.setAddon(then);
                return true;
            }
        }
        add(key, then, value);
        return false;
    }
}
