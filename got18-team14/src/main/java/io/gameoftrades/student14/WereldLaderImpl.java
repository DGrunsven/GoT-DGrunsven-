package io.gameoftrades.student14;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.kaart.Terrein;
import io.gameoftrades.model.kaart.TerreinType;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.HandelType;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.model.markt.Markt;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WereldLaderImpl implements WereldLader {
   
    @Override
    public Wereld laad(String resource) {
        try {
            InputStream inputStream = WereldLaderImpl.class.getResourceAsStream(resource);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputReader);

            Kaart kaart      = laadKaart(reader);
            List<Stad>steden = laadSteden(kaart, reader);
            Markt markten    = laadMarkten(steden, reader);

         return new Wereld(kaart, steden, markten);
        } catch (IOException e) {
            System.err.println("Error: Bestand onleesbaar");
        }
           return null;
    }

    private Kaart laadKaart(BufferedReader reader) throws IOException {
        String kaartLine = reader.readLine();
        kaartLine = kaartLine.replaceAll("\\s", "");
        String[] line2 = kaartLine.split(",");
        int breedte = Integer.parseInt(line2[0]);
        int hoogte = Integer.parseInt(line2[1]);
        Kaart kaart = new Kaart(breedte, hoogte);
        for (int row = 0; row < kaart.getHoogte(); row++) {
            String terreinLine = (String) reader.readLine();
            terreinLine = terreinLine.replaceAll("\\s", "");
            if (terreinLine.length() != kaart.getBreedte()) {
                throw new IllegalArgumentException("Onjuiste afmetingen");
            }
            for (int col = 0; col < kaart.getBreedte(); col++) {
                char ch = terreinLine.charAt(col);
                TerreinType t = TerreinType.fromLetter(ch);
                Coordinaat c = Coordinaat.op(col, row);
                new Terrein(kaart, c, t);
            }
        }
        return kaart;
    }

    private ArrayList<Stad> laadSteden(Kaart kaart, BufferedReader reader) throws IOException {
        ArrayList<Stad> steden = new ArrayList<>();
        String stedenLine = reader.readLine();
        stedenLine = stedenLine.replaceAll("\\s", "");
        int stedenLengte = Integer.parseInt(stedenLine);

        for (int row = 0; row < stedenLengte; row++) {
            String stadLine = (String) reader.readLine();
            stadLine = stadLine.replaceAll("\\s", "");
            String[] stadLine2 = stadLine.split(",");
            int stadB = Integer.parseInt(stadLine2[0]);
            int stadH = Integer.parseInt(stadLine2[1]);

            if ((stadB < 1 || stadB >= kaart.getBreedte()) || (stadH < 1 || stadH >= kaart.getHoogte())) {
                throw new IllegalArgumentException("Stad heeft onjuiste coordinaten");
            } else {
                Stad stad = new Stad(Coordinaat.op(stadB, stadH), stadLine2[2]);
                steden.add(stad);
            }
        }
        return steden;
    }

    private Markt laadMarkten(List<Stad> steden, BufferedReader reader) throws IOException {
        ArrayList<Handel> handels = new ArrayList<>();
        String marktLine = reader.readLine();
        marktLine = marktLine.replaceAll("\\s", "");
        int marktLengte = Integer.parseInt(marktLine);
        for (int row = 0; row < marktLengte; row++) {
            String marktetLine = (String) reader.readLine();
            marktetLine = marktetLine.replaceAll("\\s", "");
            String[] marktetLine2 = marktetLine.split(",");
            int prijs = Integer.parseInt(marktetLine2[3]);
            Handelswaar h = new Handelswaar(marktetLine2[2]);
            HandelType ht = HandelType.valueOf(marktetLine2[1]);
            Stad s = zoekStad(steden, marktetLine2[0]);
            Handel HH = new Handel(s, ht, h, prijs);
            handels.add(HH);
        }
        return new Markt(handels);
    }

   public Stad zoekStad(List<Stad > steden, String name) {
        for(Stad stad : steden)
            if(stad.getNaam().equals(name))
                return stad;
        return null;
     }
}

/*
Bronnen
http://underpop.free.fr/j/java/developing-games-in-java/1592730051_ch05lev1sec1.html
http://stackoverflow.com
*/
