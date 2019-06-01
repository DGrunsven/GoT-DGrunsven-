package io.gameoftrades.student14.Stedentour;

import java.util.*;

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
/*Source: 
https://docs.oracle.com/javase/tutorial/java/generics/why.html
https://dzone.com/articles/hack-1-understanding-the-use-cases-of-generics
https://www.baeldung.com/java-generics
*/
