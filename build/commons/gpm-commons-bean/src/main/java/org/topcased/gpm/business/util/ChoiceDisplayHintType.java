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
 * ChoiceDisplayHintType
 * 
 * @author nveillet
 */
public enum ChoiceDisplayHintType implements Serializable {

    /** Drop-down list */
    LIST,

    /** List to check (radio, checkbox) */
    NOT_LIST,

    /** List to check (radio, checkbox) with image */
    NOT_LIST_IMAGE,

    /** List to check (radio, checkbox) with image and text */
    NOT_LIST_IMAGE_TEXT,

    /** Choice values in a tree */
    TREE;
}
