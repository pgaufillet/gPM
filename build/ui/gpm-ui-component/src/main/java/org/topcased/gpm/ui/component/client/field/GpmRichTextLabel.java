/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.field.formater.GpmFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;

/**
 * GpmRichTextVisualisation extends GpmLabel for all its method, except
 * setAsString to directly set the HTML content of the field
 * 
 * @author jeballar
 */
public class GpmRichTextLabel extends GpmLabel<String> {

    /**
     * Constructor
     */
    public GpmRichTextLabel() {
        super(GpmStringFormatter.getInstance());
    }

    /**
     * Creates an empty gPM label.
     * 
     * @param pFormatter
     *            The formatter.
     */
    public GpmRichTextLabel(GpmFormatter<String> pFormatter) {
        super(pFormatter);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.GpmLabel#set(java.lang.Object)
     */
    public void setAsString(String pHTMLContent) {
        getWidget().getElement().setInnerHTML(pHTMLContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.GpmLabel#getEmptyClone()
     */
    @Override
    public GpmLabel<String> getEmptyClone() {
        GpmRichTextLabel lWidget = new GpmRichTextLabel(formatter);
        lWidget.setFieldName(getFieldName());
        lWidget.setFieldDescription(getFieldDescription());
        lWidget.setMandatory(isMandatory());
        // TODO
        // setWidth(pWidth);
        // setHeight(pHeight);
        return lWidget;
    }
}
