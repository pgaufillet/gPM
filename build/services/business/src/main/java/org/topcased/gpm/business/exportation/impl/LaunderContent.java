/***************************************************************
 * Copyright (c) 2012 ATOS. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Romain Ranzato (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

/**
 * Launder data with algorithm
 * 
 * @author rranzato
 */
public class LaunderContent {

    /**
     * Algorithm used to launder data [a,b,c,d,...] -> a [e,f,g,h,...] -> e
     * 
     * @param pFieldValueData
     *            FieldValueData parameter
     * @return lFinal
     */
    public static String launderData(String pValue) {
        char[] lTab1 = { 'a', 'b', 'c', 'd', 'A', 'B', 'C', 'D', '0', '1' };
        char[] lTab2 = { 'e', 'f', 'g', 'h', 'E', 'F', 'G', 'H', '2', '3' };
        char[] lTab3 = { 'i', 'j', 'k', 'l', 'I', 'J', 'K', 'L', '4', '5' };
        char[] lTab4 = { 'm', 'n', 'o', 'p', 'M', 'N', 'O', 'P', '6', '7' };
        char[] lTab5 = { 'q', 'r', 's', 't', 'Q', 'R', 'S', 'T', '8', '9' };
        char[] lTab6 = { 'u', 'v', 'w', 'x', 'U', 'V', 'W', 'X', '$', '$' };
        char[] lTab7 = { 'y', 'z', '.', '/', '*', ':', ';', '?', '!', '*' };
        char[] lTab8 = { 'à', 'é', 'è', 'ê', 'ù', '#', '[', ']', '(', ')' };
        char[] lTab9 = { '/', '\\', '-', '+', '{', '}', '`', '£', ',', '%' };
        char[] lTab10 = { ' ' , '=', '\'', '"', '&', '~', '|', 'ç', '°', '^' };

        StringBuffer lFinal = new StringBuffer();

        for (int i = 0; i < pValue.length(); i++) {
            {
                char lTemp = pValue.charAt(i);

                for (int j = 0; j < lTab1.length; j++) {
                    if (lTab1[j] == lTemp) {
                        lFinal.append("a");
                    }
                    else if (lTab2[j] == lTemp) {
                        lFinal.append("e");
                    }
                    else if (lTab3[j] == lTemp) {
                        lFinal.append("i");
                    }
                    else if (lTab4[j] == lTemp) {
                        lFinal.append("m");
                    }
                    else if (lTab5[j] == lTemp) {
                        lFinal.append("q");
                    }
                    else if (lTab6[j] == lTemp) {
                        lFinal.append("u");
                    }
                    else if (lTab7[j] == lTemp) {
                        lFinal.append("y");
                    }
                    else if (lTab8[j] == lTemp) {
                        lFinal.append("à");
                    }
                    else if (lTab9[j] == lTemp) {
                        lFinal.append("/");
                    }
                    else if (lTab10[j] == lTemp) {
                        lFinal.append(" ");
                    }
                }
            }
        }
        return lFinal.toString();
    }
}
