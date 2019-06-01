package io.gameoftrades.student14;

import io.gameoftrades.debug.Debuggable;
import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.algoritme.SnelstePadAlgoritme;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SnelstePadAlgoritmeImpl implements SnelstePadAlgoritme, Debuggable {

    private Debugger debugger;
    private HashMap<Coordinaat, Set<Pad>> padCache = new HashMap<>();

    @Override
    public Pad bereken(Kaart kaart, Coordinaat start, Coordinaat eind) { 
        Coordinaat start2 = Coordinaat.op(start.getX() - 1, start.getY() - 1);
        Coordinaat eind2 = Coordinaat.op(eind.getX() - 1, eind.getY() - 1);
        if (padCache.containsKey(start2)) { 
            Set<Pad> restPaden = padCache.get(start2); 
            return vindPad(restPaden, start2, eind2);
        }
        Set<Pad> onbekendeVakjes = new HashSet<>();
        Set<Pad> bekendeVakjes = new HashSet<>();
        ArrayList<Richting> leegPad = new ArrayList<>();
        Pad padZonderBP = new PadImpl(leegPad, start2, kaart);
        onbekendeVakjes.add(padZonderBP);
        while (!onbekendeVakjes.isEmpty()) {
            Pad kortstePad = onbekendeVakjes.stream()
                    .sorted((pad1, pad2) -> pad1.getTotaleTijd() - pad2.getTotaleTijd())
                    .findFirst()
                    .get();
            onbekendeVakjes.remove(kortstePad);
            bekendeVakjes.add(kortstePad);

            Coordinaat korstePadEindC = kortstePad.volg(start2);
            Richting[] richtingen = kaart.getTerreinOp(korstePadEindC).getMogelijkeRichtingen();
            for (int i = 0; i < richtingen.length; i++) {
                Coordinaat buurC = korstePadEindC.naar(richtingen[i]);
                int bpHuidig = kaart.getTerreinOp(buurC).getTerreinType().getBewegingspunten();
                boolean alVerkend = bekendeVakjes.stream()
                        .map(x -> x.volg(start2))
                        .anyMatch(x -> x.equals(buurC));

                if (!alVerkend) {
                    int kortstePadnaarXBP = kortstePad.getTotaleTijd() + bpHuidig;
                    Pad kortstePadNaarX = onbekendeVakjes.stream()
                            .filter(x -> x.volg(start2).equals(buurC))
                            .findFirst()
                            .orElse(null);

                    if (kortstePadNaarX == null || kortstePadnaarXBP < kortstePadNaarX.getTotaleTijd()) {
                        ArrayList<Richting> richtingenNieuw = new ArrayList<Richting>(Arrays.asList(kortstePad.getBewegingen()));
                        richtingenNieuw.add(richtingen[i]);
                        Pad beterPadNaarX = new PadImpl(richtingenNieuw, start2, kaart);
                        if (kortstePadNaarX != null) {
                            onbekendeVakjes.remove(kortstePadNaarX);
                        }
                        onbekendeVakjes.add(beterPadNaarX);
                    }
                }
            }
        }
        Pad kortstePadNaarEind = vindPad(bekendeVakjes, start2, eind2);
        if (debugger!= null) {
          debugger.debugPad(kaart, start2, kortstePadNaarEind);  
        }
        return kortstePadNaarEind;
    }
 
    public Pad vindPad(Set<Pad> bekendeVakjes, Coordinaat start2, Coordinaat eind2) {
        Pad kortstePadNaarEind = bekendeVakjes.stream()
                .filter(x -> x.volg(start2).equals(eind2))
                .findFirst()
                .orElse(null);
        return kortstePadNaarEind;
    }

    @Override
    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    @Override
    public String toString() {
        return "Dijkstra!";
    }
}
