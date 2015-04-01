/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util;

import java.io.Serializable;

/**
 * FieldType
 * 
 * @author nveillet
 * @author phtsaan
 */
public enum FieldType implements Serializable {

    STRING, INTEGER, REAL, BOOLEAN, DATE, CHOICE, ATTACHED, POINTER, MULTIPLE, MULTIVALUED, VIRTUAL, APPLET,
}
