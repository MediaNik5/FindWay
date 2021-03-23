package org.medianik.findway.util;

public class IntIntObjTriple<V>{

    private int key;
    private int addon;
    private V value;

    public IntIntObjTriple(int key, int addon, V value){
        this.key = key;
        this.addon = addon;
        this.value = value;
    }

    public int getKey(){
        return key;
    }

    public int getAddon(){
        return addon;
    }

    public V getValue(){
        return value;
    }

    public void setKey(int key){
        this.key = key;
    }

    public void setAddon(int addon){
        this.addon = addon;
    }

    public void setValue(V value){
        this.value = value;
    }
}
