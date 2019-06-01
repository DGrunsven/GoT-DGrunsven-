/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gameoftrades.student14.Stedentour;

import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.student14.PadImpl;
import io.gameoftrades.student14.SnelstePadAlgoritmeImpl;
import java.util.*;

/**
 *
 * @author donna
 */
public class Graph <V>{ 

    private List<V> vertices;
    private HashMap<V, List<Edge<V>>> edges;

    public Graph(List<V> vertices, HashMap<V, List<Edge<V>>> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    public List<V> getVertices() {
        return vertices;
    }
 
    public Edge<V> getEdge(V a, V b) {
        return edges.get(a).stream()
                .filter(x -> x.end().equals(b)) 
                .findFirst()
                .orElse(null);
    }
}
