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

/**
 * List of attached file related error codes.
 * 
 * @author Olivier Juin
 */
public enum AttachmentStatus {
    ZERO_SIZE("Cannot attach empty file %1"),
    EXCEEDED_SIZE("Attached files total size exceeds size limit (%1 MB)"),
    INVALID_NAME("Attached file %1 contains an invalid character ( %2 )");
    
    private String message;
    
    private AttachmentStatus(String pMessage) {
    	message = pMessage;
    }
    
    String getMessage(Object[] items) {
    	String replacedMessage = message;
    	int index = 1;
    	for (Object item : items) {
    		replacedMessage.replaceAll("%" + index, item.toString());
    		index++;
    	}
    	return replacedMessage;
    }
}
