/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gameoftrades.student14.Stedentour;

import io.gameoftrades.model.kaart.Coordinaat;

/**
 *
 * @author donna
 */
public class VertexImpl implements Vertex {

    private Coordinaat coordinaat;

    public VertexImpl(Coordinaat coordinaat) {
        this.coordinaat = coordinaat;
    }

    public Coordinaat getCoordinaat() {
        return coordinaat;
    }
    
}
