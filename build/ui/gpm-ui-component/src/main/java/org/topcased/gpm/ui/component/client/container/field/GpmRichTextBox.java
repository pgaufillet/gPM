/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.exception.NotImplementedException;
import org.topcased.gpm.ui.component.client.field.GpmRichTextBoxWidget;
import org.topcased.gpm.ui.component.client.popup.GpmPopupPanel;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * GpmRichTextBox refers a RichTextArea with its tool bar in the meaning of GWT
 * but adapted for the gPM core.
 * <p>
 * This component can be zoomed and is sizeable.
 * <p/>
 * 
 * @TODO Implement the validation error message.
 * @author frosier
 */
public class GpmRichTextBox extends AbstractGpmField<GpmRichTextBoxWidget>
        implements BusinessStringField {

    private static final String LABEL_EDIT_FIELD =
            "Editing the field description";

    private static final String LABEL_VALIDATE_FIELD = "Validate";

    private static final double ZOOM_RATIO_WIDTH = 0.9;

    private static final double ZOOM_RATIO_HEIGHT = 0.75;

    /**
     * Creates an empty rich text box with a GpmRichTextToolBar.
     */
    public GpmRichTextBox() {
        super(new GpmRichTextBoxWidget());
        initZoomButton();
    }

    /**
     * Initialize zoom button.
     */
    public void initZoomButton() {
        getWidget().getZoomButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                final GpmPopupPanel lZoom = new GpmPopupPanel(true);
                final GpmRichTextBoxWidget lRichTextBox =
                        new GpmRichTextBoxWidget();

                lZoom.setHeader(new Label(LABEL_EDIT_FIELD));
                lRichTextBox.setHTML(getWidget().getHTML());
                lZoom.setContent(lRichTextBox);
                lRichTextBox.expandWidth();

                lZoom.addButton(LABEL_VALIDATE_FIELD).addClickHandler(
                        new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent pEvent) {
                                lZoom.hide();
                            }
                        });
                lZoom.setRatioSize(ZOOM_RATIO_WIDTH, ZOOM_RATIO_HEIGHT);
                lZoom.show();

                lZoom.addCloseHandler(new CloseHandler<PopupPanel>() {

                    @Override
                    public void onClose(CloseEvent<PopupPanel> pEvent) {
                        setAsString(lRichTextBox.getHTML());
                    }
                });
            }
        });
    }

    /*** GpmField implementation ****/

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(final BusinessField pOther) {
        getWidget().setHTML(((BusinessStringField) pOther).getAsString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getWidget().getHTML();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        return getWidget().getHTML().equals(
                GpmStringUtils.getEmptyIfNull(pOther.getAsString()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public String get() {
        return getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(String pValue) {
        setAsString(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
        getWidget().setHTML(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        getWidget().setEnabled(false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmRichTextBox getEmptyClone() {
        final GpmRichTextBox lClone = new GpmRichTextBox();

        initEmptyClone(lClone);
        lClone.setEnabled(getWidget().getRichTextArea().isEnabled());

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getInternalUrlSheetReference()
     */
    @Override
    public String getInternalUrlSheetReference() {
        throw new NotImplementedException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getSize()
     */
    @Override
    public int getSize() {
        throw new NotImplementedException("Not implemented method");
    }

    /**
     * TODO Implement "intelligent" height definition Set the height of the rich
     * text area
     * 
     * @param pHeight
     *            new height
     */
    public void setHeight(String pHeight) {
        // TODO Implement size setting for RichTextBox
        //        richTextBox.setHeight(pHeight);
    }

    /**
     * TODO Implement "intelligent" width definition Set the width of the rich
     * text area
     * 
     * @param pWidth
     *            new width
     */
    public void setWidth(String pWidth) {
        // TODO Implement size setting for RichTextBox
        //        richTextBox.setWidth(pWidth);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }
}