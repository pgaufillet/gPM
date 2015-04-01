/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation;

import org.apache.commons.lang.StringUtils;

/**
 * Various utilities methods
 * 
 * @author llatil
 */
public class InstantiateUtils {

    /**
     * Remove all excess blanks in a string. (keeps only one blank char. between
     * words)
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

        for (String lWord : StringUtils.split(pStr, "\n ")) {
            if (!lFirst) {
                lStrBuf.append(' ');
            }
            lStrBuf.append(lWord);
            lFirst = false;
        }
        return lStrBuf.toString();
    }
}
