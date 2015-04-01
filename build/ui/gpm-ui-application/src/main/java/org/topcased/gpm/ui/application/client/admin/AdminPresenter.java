/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.admin.dictionary.DictionaryAdminPresenter;
import org.topcased.gpm.ui.application.client.admin.product.ProductAdminPresenter;
import org.topcased.gpm.ui.application.client.admin.user.UserAdminPresenter;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.inject.Inject;

/**
 * The presenter for the AdminView.
 * 
 * @author tpanuel
 */
public class AdminPresenter extends AbstractPresenter<AdminDisplay> {
    private final ProductAdminPresenter product;

    private final UserAdminPresenter user;

    private final DictionaryAdminPresenter dico;

    /**
     * Create a presenter for the AdminView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pProduct
     *            The product management presenter
     * @param pUser
     *            The user management presenter
     * @param pDico
     *            The dictionary management presenter
     */
    @Inject
    public AdminPresenter(final AdminDisplay pDisplay,
            final EventBus pEventBus, final ProductAdminPresenter pProduct,
            final UserAdminPresenter pUser, final DictionaryAdminPresenter pDico) {
        super(pDisplay, pEventBus);
        product = pProduct;
        user = pUser;
        dico = pDico;
    }

    /**
     * Initialize the administration space.
     * 
     * @param pResult
     *            The administration info.
     */
    public void initAdminSpace(final OpenAdminModuleResult pResult) {
        getDisplay().addTabChangeHandler(new SelectionHandler<Integer>() {
            private boolean lProductInit = false;

            private boolean lUserInit = false;

            private boolean lDicoInit = false;

            @Override
            public void onSelection(SelectionEvent<Integer> pEvent) {
                if (!lProductInit && pEvent.getSelectedItem() == 0) {
                    product.init(pResult);
                    product.bind();
                    lProductInit = true;
                }
                else if (!lUserInit && pEvent.getSelectedItem() == 1) {
                    user.init(pResult);
                    user.bind();
                    lUserInit = true;
                }
                else if (!lDicoInit && pEvent.getSelectedItem() == 2) {
                    dico.init(pResult);
                    dico.bind();
                    lDicoInit = true;
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        getDisplay().setTabs(product.getDisplay().asWidget(),
                user.getDisplay().asWidget(), dico.getDisplay().asWidget());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Nothing to do
    }

    /**
     * Get the product admin presenter.
     * 
     * @return The product admin presenter.
     */
    public ProductAdminPresenter getProductAdmin() {
        return product;
    }

    /**
     * Get the user admin presenter.
     * 
     * @return The user admin presenter.
     */
    public UserAdminPresenter getUserAdmin() {
        return user;
    }

    /**
     * Get the dico admin presenter.
     * 
     * @return The dico admin presenter.
     */
    public DictionaryAdminPresenter getDicoAdmin() {
        return dico;
    }
}