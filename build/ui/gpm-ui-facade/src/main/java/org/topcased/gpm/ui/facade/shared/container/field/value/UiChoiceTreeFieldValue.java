/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.value;

import java.util.List;

import org.topcased.gpm.business.util.Translation;

/**
 * UiChoiceTreeFieldValue
 * 
 * @author jlouisy
 */
public class UiChoiceTreeFieldValue extends Translation {

    /** serialVersionUID */
    private static final long serialVersionUID = 5734149403642450578L;

    private boolean selectable;

    private List<UiChoiceTreeFieldValue> children;

    /**
     * Empty Constructor
     */
    public UiChoiceTreeFieldValue() {
        super();
    }

    /**
     * Constructor
     * 
     * @param pValue
     *            value
     * @param pTranslatedValue
     *            translated value
     * @param pIsSelectable
     *            is value selectable ?
     * @param pChildren
     *            list of children
     */
    public UiChoiceTreeFieldValue(String pValue, String pTranslatedValue,
            boolean pIsSelectable, List<UiChoiceTreeFieldValue> pChildren) {
        super(pValue, pTranslatedValue);
        selectable = pIsSelectable;
        children = pChildren;
    }

    /**
     * get children.
     * 
     * @return children list.
     */
    public List<UiChoiceTreeFieldValue> getChildren() {
        return children;
    }

    /**
     * get selectable field.
     * 
     * @return selectable
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * set cildren list.
     * 
     * @param pChildren
     *            list of children.
     */
    public void setChildren(List<UiChoiceTreeFieldValue> pChildren) {
        children = pChildren;
    }

    /**
     * set selectable.
     * 
     * @param pSelectable
     *            selactable
     */
    public void setSelectable(boolean pSelectable) {
        selectable = pSelectable;
    }

}
