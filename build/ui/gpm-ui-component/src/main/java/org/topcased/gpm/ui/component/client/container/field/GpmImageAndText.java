/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.ui.component.client.exception.NotImplementedException;
import org.topcased.gpm.ui.component.client.layout.GpmHorizontalPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * GpmImageAndText
 * 
 * @author jeballar
 */
public class GpmImageAndText extends AbstractGpmField<GpmHorizontalPanel> {

    private HTML label = new HTML();

    private List<Translation> possibleImageValues;

    private List<Translation> possibleTextValues;

    /**
     * Show Image and associated text if indicated in this constructor.
     * 
     * @param pPossibleImageValues
     *            The possible images values
     */
    public GpmImageAndText(List<Translation> pPossibleImageValues) {
        super(new GpmHorizontalPanel());
        ((GpmHorizontalPanel) getWidget()).add(label);
        possibleImageValues = pPossibleImageValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        if (pOther instanceof BusinessChoiceField) {
            set(((BusinessChoiceField) pOther).getAsString());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return label.getHTML();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public AbstractGpmField<GpmHorizontalPanel> getEmptyClone() {
        final GpmImageAndText lClone = new GpmImageAndText(possibleImageValues);

        initEmptyClone(lClone);

        lClone.setPossibleTextValues(possibleTextValues);

        return lClone;
    }

    /**
     * get possible image values
     * 
     * @return the possible image values
     */
    public List<Translation> getPossibleImageValues() {
        return possibleImageValues;
    }

    /**
     * get possible text values
     * 
     * @return the possible text values
     */
    public List<Translation> getPossibleTextValues() {
        return possibleTextValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        throw new NotImplementedException(
                "The component is a visualization component, this method should never be executed");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return false;
    }

    /**
     * Set the value
     * 
     * @param pValue
     *            The new value
     */
    public void set(final String pValue) {
        // Get image translation
        String lImageFileName = pValue;
        for (Translation lPossibleImageValue : possibleImageValues) {
            if (lPossibleImageValue.equals(lImageFileName)) {
                lImageFileName = lPossibleImageValue.getTranslatedValue();
            }
        }

        final String lImageUrl = GWT.getModuleBaseURL() + lImageFileName;

        performedImageAndText(pValue, lImageFileName, lImageUrl);
    }

    
    /**
     * Reduce cyclomatic complexity
     * @param pValue
     * @param pImageFileName
     * @param pImageUrl
     */
    private void performedImageAndText(final String pValue,
            final String pImageFileName, final String pImageUrl) {
        if (possibleTextValues != null) {
            // Get text translation
            String lText = pValue;
            for (Translation lPossibleTextValue : possibleTextValues) {
                if (lPossibleTextValue.equals(lText)) {
                    lText = lPossibleTextValue.getTranslatedValue();
                }
            }
            if (null != lText && !lText.trim().isEmpty()) {
                    label.setHTML(new Image(pImageUrl).toString() + "&nbsp;" + lText);
            }
        }
        else if(null != pImageFileName && !pImageFileName.trim().isEmpty()) {
            // Do not display empty image (image file name is null) or empty
            new Image(pImageUrl);
            label.setHTML(new Image(pImageUrl).toString());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        // Visualization field, cannot be enabled/disabled
    }

    /**
     * set possible image values
     * 
     * @param pPossibleImageValues
     *            the possible image values to set
     */
    public void setPossibleImageValues(List<Translation> pPossibleImageValues) {
        possibleImageValues = pPossibleImageValues;
    }

    /**
     * set possible text values
     * 
     * @param pPossibleTextValues
     *            the possible text values to set
     */
    public void setPossibleTextValues(List<Translation> pPossibleTextValues) {
        possibleTextValues = pPossibleTextValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
