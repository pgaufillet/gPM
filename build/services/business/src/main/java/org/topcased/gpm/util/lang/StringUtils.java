/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.lang;

import org.topcased.gpm.business.exception.GDMException;

/**
 * String utilities
 * 
 * @author Laurent Latil
 */
public class StringUtils {

    /** String use to indicate a field value has been truncated */
    public static final String PARTIAL_INFO = "#PARTIAL INFO# ";

    /** Large String value length */
    public static final int LARGE_STRING_LENGTH = 4000;

    /** uni-code token use to introduce query parameter */
    public static final String UNI_CODE_TOKEN = "&@#@";

    /**
     * Remove all excess blanks in a string. This method keeps only one blank
     * character between two words
     * 
     * @param pStr
     *            Input string
     * @return The normalized string
     */
    public static String normalizeString(String pStr) {
        if (null == pStr) {
            return null;
        }
        StringBuilder lStrBuf = new StringBuilder();
        boolean lFirst = true;

        for (String lWord : pStr.split("\n ")) {
            if (!lFirst) {
                lStrBuf.append(' ');
            }
            lStrBuf.append(lWord);
            lFirst = false;
        }
        return lStrBuf.toString();
    }

    /**
     * Retrieves the parameter tag to inject in the query, parameter position
     * given
     * 
     * @param pPosition
     *            The parameter position in the query
     * @return String The parameter tag
     */
    public static String getParameterTag(final int pPosition) {
        // add white space to insure that the parameter will be set as string token
        return " " + UNI_CODE_TOKEN + pPosition + " ";
    }

    /**
     * Retrieves an array of String, the content is the parameter tag to be
     * injected in the query where clause, this method is used by
     * <code> DateQueryGenerator </code>, to generate a where clause.<br>
     * The first index of the array contains the started date tag and the second
     * contains the ended one.<br>
     * In the case the where clause contains only one date
     * <code> example :date <= aDate</code>, the tag array in initialized whit
     * the same value, so that we can use index <code>0</code> or <code>1</code>
     * in the where clause.
     * 
     * @param pPositions
     *            The parameter position in the query
     * @return String [] The array of parameter tag.
     * @throws GDMException
     *             GDMException is raised when both parameter position are set
     *             to -1
     */
    public static String[] getParameterTag(final Integer[] pPositions) {
        // add white space to insure that the parameter will be set as string token
        String[] lParameters = new String[2];
        if (pPositions[0] != -1 && pPositions[1] != -1) {
            lParameters[0] = " " + UNI_CODE_TOKEN + pPositions[0] + " ";
            lParameters[1] = " " + UNI_CODE_TOKEN + pPositions[1] + " ";
        }
        else if (pPositions[0] == -1 && pPositions[1] != -1) {
            lParameters[0] = " " + UNI_CODE_TOKEN + pPositions[1] + " ";
            lParameters[1] = " " + UNI_CODE_TOKEN + pPositions[1] + " ";
        }
        else if (pPositions[0] != -1 && pPositions[1] == -1) {
            lParameters[0] = " " + UNI_CODE_TOKEN + pPositions[0] + " ";
            lParameters[1] = " " + UNI_CODE_TOKEN + pPositions[0] + " ";
        }
        else {
            throw new GDMException("Incorrect parameter position !");
        }
        return lParameters;
    }

    /**
     * Retrieves the parameter position form parameter tag The parameter tag
     * should be like UNI_CODE_TOKEN + position.
     * 
     * @param pParameterTag
     *            The parameter tag get from the query
     * @return Integer the parameter position
     * @throws GDMException
     *             Throws a GDMException if a number format exception is raised
     */
    public static int getPositinFromTag(String pParameterTag) {
        String lValue =
                pParameterTag.substring(UNI_CODE_TOKEN.length(),
                        pParameterTag.length());
        int lIntValue = -1;
        try {
            lIntValue = Integer.parseInt(lValue);
        }
        catch (Exception ex) {
            throw new GDMException(ex);
        }
        return lIntValue;
    }

    /**
     * Retrieves a String with the correct new line encoding This make values
     * registered with "\r\n" as new line to be encoded as well as those which
     * using "\n"
     * 
     * @param pValue
     * @return new String
     */
    public static String unEscapeNewLine(String pValue) {
        if (null != pValue) {
            return pValue.replaceAll("\r\n", "\n");
        }
        else {
            return null;
        }

    }

    /**
     * Retrieves a String length including new line characters.
     * String.length(gpm\n) = 3 String.length(gpm\r\n) = 4
     * getStringLength(gpm\n)= 4 getStringLength(gpm\r\n)= 4
     * 
     * @param pValue
     * @return integer the String length not null or -1 if the String is nullµ.
     */
    public static int getStringLength(String pValue) {
        if (null != pValue) {
            return pValue.replaceAll("\n", "\r\n").length();
        }
        else {
            return -1;
        }
    }

    /**
     * Retrieves a String with the correct new line encoding
     * This make  values registered with "\n" as new line to be encoded as well as those 
     * which using "\r\n"
     * @param pValue
     * @return new String
     */
    public static String escapeNewLine(String pValue){
        /**
         * magic value used to avoid confusion between "\r\n" and "\n" during
         * string replace.
         */
        String tag = "&@#@";
        if(null != pValue){
            return pValue.replaceAll("\r\n", tag).replaceAll("\n", "\r\n")
            .replaceAll(tag, "\r\n") ;
        }
        else {
            return null;
        }
    }
}
