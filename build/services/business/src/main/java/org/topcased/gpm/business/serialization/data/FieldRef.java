/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class implement a concrete fieldRef.
 * 
 * @author sidjelli
 */
@XStreamAlias("fieldRef")
public class FieldRef extends NamedElement {

    /** Generated UID */
    private static final long serialVersionUID = 1955813630728837900L;

    /**
     * Constructs a new NamedElement (default ctor).
     */
    public FieldRef() {
    }

    /**
     * Constructs a new FieldRef.
     * 
     * @param pFieldName
     *            Name of the referenced field
     */
    public FieldRef(String pFieldName) {
        setName(pFieldName);
    }
}
