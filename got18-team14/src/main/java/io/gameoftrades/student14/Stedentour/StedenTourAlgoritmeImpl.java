/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gameoftrades.student14.Stedentour;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.algoritme.StedenTourAlgoritme;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.student14.SnelstePadAlgoritmeImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author donna
 */
public class StedenTourAlgoritmeImpl implements StedenTourAlgoritme, Debuggable {

    private SnelstePadAlgoritme snelstePadAlgoritme = new SnelstePadAlgoritmeImpl();
    private Debugger debugger;

    @Override
    public List<Stad> bereken(Kaart kaart, List<Stad> steden) {
        Graph<Stad> graph = buildGraph(kaart, steden);
        List<Stad> results = minBP(graph);
        if (debugger != null) {
            debugger.debugSteden(kaart, results);
        }
        return results;
    }

    private List<Stad> minBP(Graph<Stad> graph) {
        Map<Index, Integer> minCostBP = new HashMap<>();
        Map<Index, Stad> parent = new HashMap<>();
        List<Stad> intermediateCities = new ArrayList<>(graph.getVertices());
        Stad startVertex = intermediateCities.get(0);
        intermediateCities.remove(0);
        List<List<Stad>> allSets = generatePowerSet(intermediateCities);
        for (List<Stad> set : allSets) {
            for (Stad currentVertex : graph.getVertices()) {
                if (set.contains(currentVertex) || set.contains(startVertex)) {
                    continue;
                }
                Index index = new Index(currentVertex, set);
                if (set.isEmpty()) {
                    parent.put(index, startVertex);
                    minCostBP.put(index, graph.getEdge(startVertex, currentVertex).weight());
                } else {
                    computeMinEntry(currentVertex, set, index, graph, minCostBP, parent);
                }
            }
        }
        Index index = new Index(startVertex, intermediateCities);
        computeMinEntry(startVertex, intermediateCities, index, graph, minCostBP, parent);
        List<Edge<Stad>> edges = new ArrayList<>();
        int currentMax = 0;
        int currentMaxIndex = -1;
        Stad currentVertex = startVertex;
        List<Stad> set = new ArrayList<>(intermediateCities);
        boolean guard = true;
        while (guard) {
            guard = !set.isEmpty();
            Index index2 = new Index(currentVertex, set);
            Stad prevVertex = parent.get(index2);
            Edge<Stad> e = graph.getEdge(prevVertex, currentVertex);
            edges.add(e);
            set.remove(prevVertex);
            currentVertex = prevVertex;
            if (currentMax < e.weight()) {
                currentMax = e.weight();
                currentMaxIndex = edges.size() - 1;
            }

        }
        List<Edge<Stad>> list2 = new ArrayList<>(edges.subList(currentMaxIndex + 1, edges.size()));
        List<Edge<Stad>> list = edges.subList(0, currentMaxIndex + 1);
        list2.addAll(list);
        List<Stad> result = list2.stream()
                .map(edge -> edge.end())
                .collect(toList());
        return result;
    }

    private static class Index {

        Stad currentVertex;
        List<Stad> vertexSet;

        private Index(Stad vertex, List<Stad> set) {
            currentVertex = vertex; //links altijd this.
            vertexSet = set;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + Objects.hashCode(this.currentVertex);
            hash = 79 * hash + Objects.hashCode(this.vertexSet);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Index other = (Index) obj;
            if (!Objects.equals(this.currentVertex, other.currentVertex)) {
                return false;
            }
            if (!Objects.equals(this.vertexSet, other.vertexSet)) {
                return false;
            }
            return true;
        }
    }

    private void computeMinEntry(Stad currentVertex, List<Stad> set, Index index, Graph graph, Map<Index, Integer> minCostBP, Map<Index, Stad> parent) {
        int minWeight = Integer.MAX_VALUE;
        Stad minPrevVertex = null;
        List<Stad> copySet = new ArrayList<>(set);
        for (int i = 0; i < set.size(); i++) {
            int weight = graph.getEdge(set.get(i), currentVertex).weight() + getCost(copySet, i, minCostBP);
            if (weight < minWeight) {
                minWeight = weight;
                minPrevVertex = set.get(i);
            }
            minCostBP.put(index, minWeight);
            parent.put(index, minPrevVertex);
        }
    }

    public Graph<Stad> buildGraph(Kaart kaart, List<Stad> steden) {
        HashMap<Stad, List<Edge<Stad>>> edgeMap = new HashMap<>();
        for (int i = 0; i < steden.size(); i++) {
            Stad key = steden.get(i);
            List<Edge<Stad>> value = neighbourEdges(key, steden, kaart);
            edgeMap.put(key, value);
        }
        Graph<Stad> graph = new Graph<>(steden, edgeMap);
        return graph;
    }

    public List<Edge<Stad>> neighbourEdges(Stad start, List<Stad> endpoints, Kaart kaart) {
        List<Edge<Stad>> edges = new ArrayList<>();
        for (Stad endpoint : endpoints) {
            Pad p = snelstePadAlgoritme.bereken(kaart, start.getCoordinaat(), endpoint.getCoordinaat());
            int edgeWeight = p.getTotaleTijd();
            Edge<Stad> edge = new EdgeImpl<>(start, endpoint, edgeWeight);
            edges.add(edge);
        }
        return edges;
    }

    private int getCost(List<Stad> set, int indexPrevVertex, Map<Index, Integer> minCostBP) {
        Stad prevVertex = set.remove(indexPrevVertex);
        Index index = new Index(prevVertex, set);
        int cost = minCostBP.get(index);
        set.add(indexPrevVertex, prevVertex);
        return cost;
    }

    private List<List<Stad>> generatePowerSet(List<Stad> input) {
        List<List<Stad>> result = new ArrayList<>();
        for (int i = 0; i <= input.size(); i++) {
            result.addAll(createListsOfFixedSize(input, 0, i));
        }
        return result;
    }

    private List<List<Stad>> createListsOfFixedSize(List<Stad> input, int start, int number) {
        List<List<Stad>> result = new ArrayList<>();
        if (number == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        for (int i = start; i <= input.size() - number; i++) {
            Stad elm = input.get(i);
            List<List<Stad>> subResults = createListsOfFixedSize(input, i + 1, number - 1);
            for (List<Stad> subResult : subResults) {
                subResult.add(0, elm);
            }
            result.addAll(subResults);
        }
        return result;
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public String toString() {
        return "Held-Karp!";
    }

}
