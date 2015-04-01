/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.applet.IAppletDisplayHint;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.component.client.util.GpmAppletBuilder;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Applet field.
 * <p>
 * Use {@link FlowPanel} widget to display the applet.
 * </p>
 * 
 * @author phtsaan
 */
public class GpmApplet extends AbstractGpmField<FlowPanel> implements
        BusinessSimpleField<String> {

    private IAppletDisplayHint iAppletDisplayHint;

    private GpmAppletBuilder gpmAppletBuilder;

    private String sheetId;

    private PopupPanel popupPanel;

    /* sheet mode */
    private boolean isEdited;

    private String appletName;

    private List<String> paramList;

    private Button button;

    private HTMLPanel htmlCode = null;

    /**
     * Constructor
     * 
     * @param pGpmAppletBuilder
     * @param pSheetId
     * @param pIAppletDisplayHint
     * @param pParametersNames
     */
    public GpmApplet(final GpmAppletBuilder pGpmAppletBuilder,
            final String pSheetId, IAppletDisplayHint pIAppletDisplayHint,
            List<String> pParametersNames) {
        super(new FlowPanel());
        this.appletName = pGpmAppletBuilder.getAppletName();
        this.sheetId = pSheetId;
        this.paramList = pParametersNames;
        iAppletDisplayHint = pIAppletDisplayHint;
        popupPanel = new PopupPanel();

        gpmAppletBuilder = pGpmAppletBuilder;

        button = new Button(appletName);
        getWidget().add(button);

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                if (isEdited) {
                    Map<String, String> lParams =
                            iAppletDisplayHint.getParametersCouple(sheetId,
                                    paramList);
                    gpmAppletBuilder.setListParamMap(lParams);
                    if (htmlCode == null) {
                        htmlCode =
                                new HTMLPanel(
                                        gpmAppletBuilder.buildAppletHTMLString().toString());
                    }

                    htmlCode.setVisible(true);
                    popupPanel.clear();
                    popupPanel.add(htmlCode);
                    popupPanel.show();
                    popupPanel.setVisible(false);
                }
            }
        });
    }

    @Override
    public AbstractGpmField<FlowPanel> getEmptyClone() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean pEnabled) {

    }

    @Override
    public void copy(BusinessField pOther) {
        // not thing to do
    }

    @Override
    public String getAsString() {
        return null;
    }

    @Override
    public boolean hasSameValues(BusinessField pOther) {
        return false;
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
        set(pValue);
    }

    @Override
    public void set(String pValue) {
        //not thing to do
    }

    @Override
    public String get() {
        return null;
    }

    public String getSheetID() {
        return this.sheetId;
    }

    public void setEdited(boolean pIsEdited) {
        this.isEdited = pIsEdited;

    }

    public void setGpmAppletBuilder(GpmAppletBuilder pGpmAppletBuilder) {
        gpmAppletBuilder = pGpmAppletBuilder;
    }

    /**
     * retrieves applet displayHint
     * 
     * @return iAppletDisplayHint
     */
    public IAppletDisplayHint getlIAppletDisplayHint() {
        return iAppletDisplayHint;
    }

    /**
     * setApplet Display hint
     * 
     * @param pIAppletDisplayHint
     */
    public void setlIAppletDisplayHint(IAppletDisplayHint pIAppletDisplayHint) {
        this.iAppletDisplayHint = pIAppletDisplayHint;
    }
}
