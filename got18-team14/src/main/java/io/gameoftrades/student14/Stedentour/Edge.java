/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gameoftrades.student14.Stedentour;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;
import java.util.ArrayList;

/**
 *
 * @author donna
 */
public interface Edge<V> {
    public abstract V start();
    public abstract V end();
    public abstract int weight();

}
