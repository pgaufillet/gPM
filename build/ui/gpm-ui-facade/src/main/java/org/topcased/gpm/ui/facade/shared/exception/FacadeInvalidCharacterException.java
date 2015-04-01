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
 * Exception generated when an attached file with invalid characters is processed
 * 
 * @author Olivier Juin
 */
public class FacadeInvalidCharacterException extends FacadeAttachmentException {

	private static final long serialVersionUID = 7585863167271860119L;

	@SuppressWarnings("unused")
	private FacadeInvalidCharacterException() {}
	
	public FacadeInvalidCharacterException(String pFilename) {
		item = pFilename;
	}
}
