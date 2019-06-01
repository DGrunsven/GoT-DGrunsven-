/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package io.gameoftrades.student14;

import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Pad;
import io.gameoftrades.model.kaart.Richting;
import java.util.ArrayList;
import java.util.List;

/**
 * Wat stelt de Pad klasse voor? Het gekke lijntje dat op de kaart wordt
 * getrokken. Je moet berekenen: stel je wilt dit pad aflopen; hoeveel
 * bewegingspunten kost dit? (getTotaletijd). Je moet de richtingen waaruit dit
 * pad is opgebouwd geven met getBewegingen. Het omgekeerde pad (dus van A naar
 * B en B naar A). Tenslotte als je het pad uitvoert vanuit punt A -> op welk
 * punt kom ik dan uit? (volg). We beginnen bij Pad (onder naar boven).
 *
 * @author donna
 */

//Update: padImpl krijgt nu een Edge. PadImpl is een speciaal geval van EdgeChristo. Elk pad is een edge, maar niet elke edge is een Pad. Dat is wat implements of extends betekent. De andere manier om hetzelfde voor elkaar
//te krijgen is composition. Dan zeg je: als je een edge wil maken, een manier om een edge te maken door een Pad te geven. Maar een van de manieren is om een Pad te geven. Dus Edge is een concrete klasse met een constructor die een Pad inneemt. 
//Update2: dus weg abstract methodes etc. Composition nu: terug naar origneel. dus een losse Edge en vertice klassen maken
public class PadImpl implements Pad{

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

    @Override //Zou hem niet gebruiken. Levert overhead op; als je iets gaat berekenen (cachen als vb). Je kunt in je hoofd twee algoritmes hebben; eentje cached wel en de andere niet. Ze doen hetzelfde. Stel je vraagt
    //nooit 2x dezelfde vraag. Welke zal sneller zijn? Intiuatie; indien cache nooit gebruikt wordt; het maakt niet uit. MAAR een cache toevoegen aan een methode zal een cost zijn, dat indien je nooit 2x dezelfde vraag
    //stelt zul je onnzinige dingen gaan berekenen (wegschrijven, bijhouden etc). Indien het rekenwerk duur is = weinig verschil. Bij makkelijk rekenwerk. Dit extra rekenen etc is overhead. Deze methode geeft ook cache. 
    //De overhead zal 1% van de tijd zijn met cachen. Indien bij omgekeerd gebruikt = wss langzamer. Relatief tot de kosten van het uitrekenen zal de overhead toenemen. De kosten van dingen in caches opslaan is bij een goedkoop probleem te groot. 
    public Pad omgekeerd() {
        ArrayList<Richting> omkeren = new ArrayList<>();
        for (int i = 0; i < bewegingen.size(); i++) {
            Richting temp = bewegingen.get(bewegingen.size() - i - 1); //anders out of bounds
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
    
    public Coordinaat getBegin() {
        return begin; 
    }

}
