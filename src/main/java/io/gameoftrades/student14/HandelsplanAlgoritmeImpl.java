/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gameoftrades.student14;

import io.gameoftrades.debug.Debugger;
import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.algoritme.HandelsplanAlgoritme;
import io.gameoftrades.model.markt.Handelsplan;
import io.gameoftrades.model.markt.actie.Actie;
import io.gameoftrades.model.markt.actie.HandelsPositie;
import java.util.ArrayList;

public class HandelsplanAlgoritmeImpl implements HandelsplanAlgoritme {

    private Debugger debugger;

    public void setDebugger(Debugger debugger) {
        this.debugger = debugger;
    }

    ArrayList<Actie> a = new ArrayList<>();

    @Override
    public Handelsplan bereken(Wereld wereld, HandelsPositie hp) {
        Handelsplan plan = new Handelsplan(a);
        if (debugger != null) {
            debugger.debugActies(wereld.getKaart(), hp, a);
        }
        return plan;
    }

    @Override
    public String toString() {
        return "Empty!";
    }
}

