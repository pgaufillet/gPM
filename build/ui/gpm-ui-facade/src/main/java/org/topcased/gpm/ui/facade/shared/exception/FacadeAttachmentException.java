/***************************************************************
 * Copyright (c) 2014 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.exception;

/**
 * Attachment related exception
 * 
 * @author Olivier Juin
 */
public abstract class FacadeAttachmentException extends Exception {

	private static final long serialVersionUID = 2965301691574650332L;

	protected String item;
	
	public String getItem() {
		return item;
	}
}
