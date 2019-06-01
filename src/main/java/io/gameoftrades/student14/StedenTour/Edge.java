package io.gameoftrades.student14.Stedentour;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;
import java.util.ArrayList;

public interface Edge<V> {
    public abstract V start();
    public abstract V end();
    public abstract int weight();

}
