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

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

import com.google.gwt.user.client.ui.Frame;

/**
 * GpmExternalContent
 * 
 * @author jeballar
 */
public class GpmExternalContent extends AbstractGpmField<Frame> {

    private final Frame frame;

    private String url;

    /**
     * Constructor
     */
    public GpmExternalContent() {
        super(new Frame());
        frame = (Frame) getWidget();
    }

    /**
     * Set the URL of the iframe
     * 
     * @param pUrl
     *            The URL of the iframe
     */
    public void setExternalContent(String pUrl) {
       // frame.setUrl(getFormattedUrl(pUrl));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public AbstractGpmField<Frame> getEmptyClone() {

        final GpmExternalContent lClone = new GpmExternalContent();
        initEmptyClone(lClone);
        lClone.frame.setWidth(frame.getElement().getStyle().getWidth());
        lClone.frame.setHeight(frame.getElement().getStyle().getHeight());
        if (null != url) {
            lClone.frame.setUrl(getFormattedUrl(url));
        }

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        // Not implemented because only use in Visualisation
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        // Not implemented because only use in Visualisation 
        // Realy? NO !!
        getWidget().setUrl(getFormattedUrl(pOther.getAsString()));
        url = pOther.getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        // Not implemented because only use in Visualisation
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        // Not implemented because only use in Visualisation
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        // Not implemented because only use in Visualisation
        return false;
    }

    /**
     * Set pixel width of the iframe
     * 
     * @param pWidth
     *            New width of the iframe
     */
    public void setWidth(int pWidth) {
        frame.setWidth(pWidth + "px");
    }

    /**
     * Set pixel height of the iframe
     * 
     * @param pHeight
     *            New height of the iframe
     */
    public void setHeight(int pHeight) {
        frame.setHeight(pHeight + "px");
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

    /**
     * Retrieves formatted Url build with the one given by the user
     * 
     * @param pUrl
     *            The given Url
     * @return URL String Url including the protocol : HTTP
     */
    private String getFormattedUrl(final String pUrl) {
        final StringBuilder lUrl = new StringBuilder(pUrl);
        final int lWwwPrefixLength = 4;
        if (lUrl.substring(0, lWwwPrefixLength).equalsIgnoreCase("www.")) {
            lUrl.insert(0, "http://");
        }
        return lUrl.toString();
    }
}
