/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.impl.report;

/**
 * Type of the element that can be import.
 * 
 * @author mkargbo
 */
public enum ElementType {

    SHEET(), PRODUCT(), LINK(), SHEET_LINK(), PRODUCT_LINK(), FILTER(), USER(), USER_ROLE(), CATEGORY(), ENVIRONMENT();
}
