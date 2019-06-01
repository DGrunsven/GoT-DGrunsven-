package io.gameoftrades.student14.Stedentour;

import io.gameoftrades.student14.PadImpl;

public class EdgeImpl<V> implements Edge<V> {

    private PadImpl pad;
    private V start;
    private V end;
    private int weight;
    
    public EdgeImpl(V start, V end, int weight){
        this.start = start; 
        this.end = end; 
        this.weight = weight; 
    }

    @Override
    public V start() {
        return start; 
    }

    @Override
    public V end() {
        return end;
    }

    @Override
    public int weight() {
        return weight;
    }
    



}
