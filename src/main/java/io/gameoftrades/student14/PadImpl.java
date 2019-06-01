package io.gameoftrades.student14;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;
import java.util.ArrayList;
import java.util.List;

public class PadImpl implements Pad {

    private List<Richting> bewegingen;
    private Coordinaat begin;
    private Kaart kaart;
    private int totaleTijd = -1;

    @Override
    public int getTotaleTijd() {
        if (totaleTijd >= 0) {
            return totaleTijd;
        }
        Coordinaat temp = begin;
        int sum = 0;
        for (int i = 0; i < bewegingen.size(); i++) {
            temp = temp.naar(bewegingen.get(i));
            int bp = kaart.getTerreinOp(temp).getTerreinType().getBewegingspunten();
            sum += bp;
        }
        return totaleTijd = sum;
    }

    @Override
    public Richting[] getBewegingen() {
        Richting[] bewegingen2 = bewegingen.toArray(new Richting[bewegingen.size()]);
        return bewegingen2;
    }

    public PadImpl(List<Richting> bewegingen, Coordinaat begin, Kaart kaart) {
        this.bewegingen = bewegingen;
        this.begin = begin;
        this.kaart = kaart;
    }

    @Override
    public Pad omgekeerd() {
        ArrayList<Richting> omkeren = new ArrayList<>();
        for (int i = 0; i < bewegingen.size(); i++) {
            Richting temp = bewegingen.get(bewegingen.size() - i - 1);
            Richting temp2 = temp.omgekeerd();
            omkeren.add(temp2);
        }
        PadImpl p = new PadImpl(omkeren, volg(begin), kaart);
        return p;
    }

    @Override
    public Coordinaat volg(Coordinaat start) {
        Coordinaat temp = start;
        for (int x = 0; x < bewegingen.size(); x++) {
            temp = temp.naar(bewegingen.get(x));
        }
        return temp;
    }
}
