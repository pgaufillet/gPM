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
 * Exception generated when an empty attached file is processed
 * 
 * @author Olivier Juin
 */
public class FacadeEmptyAttachmentException extends FacadeAttachmentException {

	private static final long serialVersionUID = -1831876929450265599L;

	@SuppressWarnings("unused")
	private FacadeEmptyAttachmentException() {}
	
	public FacadeEmptyAttachmentException(String pFilename) {
		item = pFilename;
	}
}
