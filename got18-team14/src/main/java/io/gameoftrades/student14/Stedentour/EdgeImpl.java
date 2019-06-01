/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gameoftrades.student14.Stedentour;

import io.gameoftrades.student14.PadImpl;

/**
 *
 * @author donna
 */
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
