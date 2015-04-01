/***************************************************************
 * Copyright (c) 2014 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos)
 ******************************************************************/
package org.topcased.gpm.business.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;

/**
 * Attached file related checks
 * 
 * @author Olivier Juin
 */
public class AttachmentUtils {
	
	// Invalid chars: square, arobace, quote, backquote, dollar 
	private static final char[] invalidChars = new char[] { 178, '@', '\'', '`', '$' };

	public static char[] getInvalidChars() {
		return invalidChars;
	}
	
    @SuppressWarnings("unchecked")
    public static void correctAttachmentNames(CacheableSheet pSheetData) {
    	List<AttachedFieldValueData> lAttachedFieldValues =
    			(List<AttachedFieldValueData>) pSheetData.getAllAttachedFileValues();
        for (AttachedFieldValueData lFieldData : lAttachedFieldValues) {
            if (!StringUtils.isBlank(lFieldData.getValue())
                    && lFieldData.getNewContent() != null
                    && !StringUtils.isBlank(lFieldData.getFilename())) {
            	
            	String lName = lFieldData.getFilename();
				for (char c : invalidChars) {
					if (lName.indexOf(c) != -1) {
						lName = lName.replace(c, '_');
					}
				}
                lFieldData.setFilename(lName);
            }
        }
	}
    
    public static String getInvalidCharactersAsString() {
    	StringBuilder lBuilder = new StringBuilder();
    	for (int i=0; i<invalidChars.length; i++) {
    		if (i > 0) {
    			lBuilder.append(' ');
    		}
    		lBuilder.append(invalidChars[i]);
    	}
    	return lBuilder.toString();
    }
}
