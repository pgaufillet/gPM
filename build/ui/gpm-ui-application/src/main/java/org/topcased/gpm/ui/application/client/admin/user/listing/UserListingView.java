/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user.listing;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnEditionCommand;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;
import org.topcased.gpm.ui.component.client.layout.stack.GpmStackLayoutPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for listing of the user management space.
 * 
 * @author nveillet
 */
public class UserListingView extends GpmStackLayoutPanel implements
        UserListingDisplay {

    private FlowPanel usersByLogin;

    private boolean usersByLoginOddEven;

    private ScrollPanel usersByLoginPanel;

    private FlowPanel usersByName;

    private boolean usersByNameOddEven;

    private ScrollPanel usersByNamePanel;

    /**
     * Constructor
     */
    public UserListingView() {
        usersByLoginOddEven = true;
        usersByNameOddEven = true;

        // User sorted by login
        usersByLoginPanel = new ScrollPanel();
        usersByLogin = new FlowPanel();
        usersByLogin.setStylePrimaryName(INSTANCE.css().gpmFilterTable());
        usersByLoginPanel.setWidget(usersByLogin);
        addPanel(CONSTANTS.navigationUserTabLoginTitle(), usersByLoginPanel,
                true);

        // User sorted by name
        usersByNamePanel = new ScrollPanel();
        usersByName = new FlowPanel();
        usersByName.setStylePrimaryName(INSTANCE.css().gpmFilterTable());
        usersByNamePanel.setWidget(usersByName);
        addPanel(CONSTANTS.navigationUserTabNameTitle(), usersByNamePanel,
                false);
    }

    private void addUser(final FlowPanel pTable, final String pLogin,
            final String pValue, final OpenUserOnEditionCommand pOpenHandler,
            final boolean pOddEven) {
        // The filter name
        final GpmTextButton lUser = new GpmTextButton(pLogin, pValue);

        lUser.addClickHandler(pOpenHandler);
        pTable.add(lUser);

        if (pOddEven) {
            lUser.addStyleName(INSTANCE.css().evenRow());
        }
        else {
            lUser.addStyleName(INSTANCE.css().oddRow());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.listing.UserListingDisplay#addUserByLogin(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnEditionCommand)
     */
    @Override
    public void addUserByLogin(String pUserLogin, String pUserName,
            String pUserFirstname, OpenUserOnEditionCommand pOpenHandler) {

        String lValue = pUserLogin + " (" + pUserName;
        if (pUserFirstname != null) {
            lValue += " " + pUserFirstname;
        }
        lValue += ")";

        addUser(usersByLogin, pUserLogin, lValue, pOpenHandler,
                usersByLoginOddEven);
        usersByLoginOddEven = !usersByLoginOddEven;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.listing.UserListingDisplay#addUserByName(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.ui.application.client.command.admin.user.OpenUserOnEditionCommand)
     */
    public void addUserByName(final String pUserLogin, final String pUserName,
            final String pUserFirstname,
            final OpenUserOnEditionCommand pOpenHandler) {

        String lValue = pUserName;
        if (pUserFirstname != null) {
            lValue += " " + pUserFirstname;
        }
        lValue += " (" + pUserLogin + ")";

        addUser(usersByName, pUserLogin, lValue, pOpenHandler,
                usersByNameOddEven);
        usersByNameOddEven = !usersByNameOddEven;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.StackLayoutPanel#clear()
     */
    @Override
    public void clear() {
        usersByLogin.clear();
        usersByLoginOddEven = true;

        usersByName.clear();
        usersByNameOddEven = true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMaximize()
     */
    @Override
    public void doMaximize() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMinimize()
     */
    @Override
    public void doMinimize() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doRestore()
     */
    @Override
    public void doRestore() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getHeight()
     */
    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinHeight()
     */
    @Override
    public int getMinHeight() {
        return Math.max(usersByLogin.getOffsetHeight(),
                usersByName.getOffsetHeight());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinWidth()
     */
    @Override
    public int getMinWidth() {
        return Math.max(usersByLogin.getOffsetWidth(),
                usersByName.getOffsetWidth());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getWidth()
     */
    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // Nothing to do
    }
}