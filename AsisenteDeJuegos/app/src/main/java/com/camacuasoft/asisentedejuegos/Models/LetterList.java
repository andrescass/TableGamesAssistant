package com.camacuasoft.asisentedejuegos.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nosotros on 01/05/2017.
 */

public class LetterList {
    private List<String> letterList;

    public LetterList()
    {
        letterList = new ArrayList<>();

        letterList.add("A");
        letterList.add("B");
        letterList.add("C");
        letterList.add("D");
        letterList.add("E");
        letterList.add("F");
        letterList.add("G");
        letterList.add("H");
        letterList.add("I");
        letterList.add("J");
        letterList.add("K");
        letterList.add("L");
        letterList.add("M");
        letterList.add("N");
        letterList.add("Ñ");
        letterList.add("O");
        letterList.add("P");
        letterList.add("Q");
        letterList.add("R");
        letterList.add("S");
        letterList.add("T");
        letterList.add("U");
        letterList.add("V");
        letterList.add("W");
        letterList.add("X");
        letterList.add("Y");
        letterList.add("Z");
    }

    public String getLetter(int i)
    {
        return letterList.get(i);
    }

    public void removeLetter(int i)
    {
        letterList.remove(i);
    }

    public String getAndRemove(int i)
    {
        String aux = letterList.get(i);
        letterList.remove(i);
        return aux;
    }

    public int getSize()
    {
        return letterList.size();
    }


}
