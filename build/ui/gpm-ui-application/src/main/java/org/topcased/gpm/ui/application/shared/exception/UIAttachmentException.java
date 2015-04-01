/***************************************************************
 * Copyright (c) 2014 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.exception;

/**
 * Attachment related exception
 * 
 * @author Olivier Juin
 */
public abstract class UIAttachmentException extends Exception {

	private static final long serialVersionUID = -8424602676578147867L;

	protected String item;
	protected String invalidCharactersAsString;
	
	public String getItem() {
		return item;
	}
	
	public String getInvalidCharactersAsString() {
		return invalidCharactersAsString;
	}
}
