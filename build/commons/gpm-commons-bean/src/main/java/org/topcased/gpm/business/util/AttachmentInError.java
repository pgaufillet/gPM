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

import org.topcased.gpm.business.exception.GDMException;

/**
 * An attached file related error
 * 
 * @author Olivier Juin
 */
public class AttachmentInError {

	private AttachmentStatus errorStatus;
    private Object[] items;
 
    // For GWT serialization
    @SuppressWarnings("unused")
	private AttachmentInError() {}
    
    /**
     * Constructor for any number of items
     * 
     * @param pStatus
     * @param pItems
     */
    public AttachmentInError(AttachmentStatus pStatus, Object... pItems) {
    	errorStatus = pStatus;
    	items = pItems;
    }

	public AttachmentStatus getErrorStatus() {
		return errorStatus;
	}

	public Object getItem(int i) {
		return items[i];
	}

	public Object[] getItems() {
		return items;
	}

	public String getMessage() {
		return errorStatus.getMessage(items);
	}
	
	public GDMException toException() {
		return new GDMException(errorStatus.getMessage(items));
	}
}
