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
 * Exception generated when an attached file with invalid characters is processed
 * 
 * @author Olivier Juin
 */
public class UIInvalidCharacterException extends UIAttachmentException {

	private static final long serialVersionUID = 5618300804465530898L;

	@SuppressWarnings("unused")
	private UIInvalidCharacterException() {}
	
	public UIInvalidCharacterException(String pFilename, String pInvalidCharactersAsString) {
		item = pFilename;
		invalidCharactersAsString = pInvalidCharactersAsString;
	}
}
