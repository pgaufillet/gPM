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
 * Exception generated when the sum of all attached files size reaches a predefined ceiling.
 * 
 * @author Olivier Juin
 */
public class FacadeAttachmentTotalSizeException extends FacadeAttachmentException {

	private static final long serialVersionUID = 2898444726585623121L;

	@SuppressWarnings("unused")
	private FacadeAttachmentTotalSizeException() {}
	
	public FacadeAttachmentTotalSizeException(int pCeiling) {
		item = "" + pCeiling;
	}
}
