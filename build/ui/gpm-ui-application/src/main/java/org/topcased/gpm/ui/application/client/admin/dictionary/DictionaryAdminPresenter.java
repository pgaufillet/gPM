/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.admin.dictionary.detail.DictionaryDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentCreationDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.detail.EnvironmentDetailPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.DictionaryListingPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.listing.EnvironmentListingPresenter;
import org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationPresenter;
import org.topcased.gpm.ui.application.client.common.AbstractPresenter;
import org.topcased.gpm.ui.application.client.common.workspace.detail.EmptyDetailPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.menu.admin.dictionary.DictionaryAdminMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.CreateEnvironmentResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetDictionaryCategoriesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetDictionaryCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoriesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.GetEnvironmentCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.UpdateDictionaryCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.dictionary.UpdateEnvironmentCategoryValuesResult;

import com.google.inject.Inject;

/**
 * The presenter for the DictionaryAdminView.
 * 
 * @author tpanuel
 */
public class DictionaryAdminPresenter extends
        AbstractPresenter<DictionaryAdminDisplay> {

    private final DictionaryDetailPresenter dictionaryDetail;

    private final DictionaryListingPresenter dictionaryListing;

    private final EmptyDetailPresenter emptyDetail;

    private final EnvironmentCreationDetailPresenter environmentCreationDetail;

    private final EnvironmentDetailPresenter environmentDetail;

    private final EnvironmentListingPresenter environmentListing;

    private final DictionaryAdminMenuBuilder menuBuilder;

    private final DictionaryNavigationPresenter navigation;

    /**
     * Create a presenter for the DicoAdminView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public DictionaryAdminPresenter(
            final DictionaryAdminDisplay pDisplay,
            final EventBus pEventBus,
            final DictionaryNavigationPresenter pNavigation,
            final DictionaryListingPresenter pDictionaryListing,
            final EnvironmentListingPresenter pEnvironmentListing,
            final DictionaryDetailPresenter pDictionaryDetail,
            final EnvironmentDetailPresenter pEnvironmentDetail,
            final EnvironmentCreationDetailPresenter pEnvironmentCreationDetail,
            final EmptyDetailPresenter pEmptyDetail,
            final DictionaryAdminMenuBuilder pMenuBuilder) {
        super(pDisplay, pEventBus);
        navigation = pNavigation;
        dictionaryListing = pDictionaryListing;
        environmentListing = pEnvironmentListing;
        dictionaryDetail = pDictionaryDetail;
        environmentDetail = pEnvironmentDetail;
        environmentCreationDetail = pEnvironmentCreationDetail;
        emptyDetail = pEmptyDetail;
        menuBuilder = pMenuBuilder;
    }

    /**
     * get dictionary detail presenter
     * 
     * @return the dictionary detail presenter
     */
    public DictionaryDetailPresenter getDictionaryDetail() {
        return dictionaryDetail;
    }

    /**
     * get dictionary listing presenter
     * 
     * @return the dictionary listing presenter
     */
    public DictionaryListingPresenter getDictionaryListing() {
        return dictionaryListing;
    }

    /**
     * get environment creation detail presenter
     * 
     * @return the environment creation detail presenter
     */
    public EnvironmentCreationDetailPresenter getEnvironmentCreationDetail() {
        return environmentCreationDetail;
    }

    /**
     * get environment detail presenter
     * 
     * @return the environment detail presenter
     */
    public EnvironmentDetailPresenter getEnvironmentDetail() {
        return environmentDetail;
    }

    /**
     * get environment listing presenter
     * 
     * @return the environment listing presenter
     */
    public EnvironmentListingPresenter getEnvironmentListing() {
        return environmentListing;
    }

    /**
     * Initialize the workspace.
     * 
     * @param pResult
     *            The result.
     */
    public void init(final OpenAdminModuleResult pResult) {
        //Add the menu tool bar
        getDisplay().setToolBar(
                menuBuilder.buildMenu(pResult.getDictionaryActions(), null));

        // Set the filters
        navigation.init(pResult);
        navigation.setEnvironments(pResult.getAvailableEnvironments());

        // Initialize environment creation view
        environmentCreationDetail.init(pResult);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @SuppressWarnings("rawtypes")
	@Override
    protected void onBind() {
        // Display sub panels
        getDisplay().setContent(navigation.getDisplay(),
                emptyDetail.getDisplay(), emptyDetail.getDisplay());

        addEventHandler(GlobalEvent.LOAD_DICTIONARY.getType(),
                new ActionEventHandler<GetDictionaryCategoriesResult>() {
                    @Override
                    public void execute(
                            final GetDictionaryCategoriesResult pResult) {
                        getDisplay().setContent(navigation.getDisplay(),
                                dictionaryListing.getDisplay(),
                                emptyDetail.getDisplay());
                        dictionaryListing.setCategories(pResult.getCategories());

                        // clear category detail
                        dictionaryDetail.clear();
                        environmentDetail.clear();
                    }
                });
        addEventHandler(GlobalEvent.LOAD_ENVIRONMENT.getType(),
                new ActionEventHandler<GetEnvironmentCategoriesResult>() {
                    @Override
                    public void execute(
                            final GetEnvironmentCategoriesResult pResult) {
                        getDisplay().setContent(navigation.getDisplay(),
                                environmentListing.getDisplay(),
                                emptyDetail.getDisplay());
                        environmentListing.setEnvironment(pResult.getEnvironment());
                        environmentListing.setCategories(pResult.getCategories());

                        // clear category detail
                        dictionaryDetail.clear();
                        environmentDetail.clear();
                    }
                });
        addEventHandler(GlobalEvent.HIDE_CATEGORY.getType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(final EmptyAction pResult) {
                        getDisplay().setDetailContent(emptyDetail.getDisplay());
                        // clear category detail
                        dictionaryDetail.clear();
                        environmentDetail.clear();
                    }
                });
        addEventHandler(GlobalEvent.LOAD_DICTIONARY_CATEGORY.getType(),
                new ActionEventHandler<GetDictionaryCategoryValuesResult>() {
                    @Override
                    public void execute(
                            final GetDictionaryCategoryValuesResult pResult) {
                        getDisplay().setDetailContent(
                                dictionaryDetail.getDisplay());
                        dictionaryDetail.setCategoryName(dictionaryListing.getSelectedCategory());
                        dictionaryDetail.setCategoryValues(pResult.getValues());
                    }
                });
        addEventHandler(GlobalEvent.LOAD_ENVIRONMENT_CATEGORY.getType(),
                new ActionEventHandler<GetEnvironmentCategoryValuesResult>() {
                    @Override
                    public void execute(
                            final GetEnvironmentCategoryValuesResult pResult) {
                        getDisplay().setDetailContent(
                                environmentDetail.getDisplay());
                        environmentDetail.setCategoryName(environmentListing.getSelectedCategory());
                        environmentDetail.setAvailableCategoryValues(pResult.getDictionaryValues());
                        environmentDetail.setCategoryValues(pResult.getEnvironmentValues());
                    }
                });
        addEventHandler(GlobalEvent.SAVE_DICTIONARY_CATEGORY.getType(),
                new ActionEventHandler<UpdateDictionaryCategoryValuesResult>() {
                    @Override
                    public void execute(
                            final UpdateDictionaryCategoryValuesResult pResult) {
                        dictionaryDetail.setCategoryValues(dictionaryDetail.getCategoryValues());
                    }
                });
        addEventHandler(
                GlobalEvent.SAVE_ENVIRONMENT_CATEGORY.getType(),
                new ActionEventHandler<UpdateEnvironmentCategoryValuesResult>() {
                    @Override
                    public void execute(
                            final UpdateEnvironmentCategoryValuesResult pResult) {
                        environmentDetail.setCategoryValues(environmentDetail.getCategoryValues());
                    }
                });
        addEventHandler(GlobalEvent.LOAD_NEW_ENVIRONMENT.getType(),
                new ActionEventHandler<EmptyAction>() {
                    @Override
                    public void execute(final EmptyAction pResult) {
                        getDisplay().setContent(navigation.getDisplay(),
                                environmentCreationDetail.getDisplay(),
                                emptyDetail.getDisplay());
                        environmentCreationDetail.clear();

                        // clear category detail
                        dictionaryDetail.clear();
                        environmentDetail.clear();
                    }
                });
        addEventHandler(GlobalEvent.CREATE_ENVIRONMENT.getType(),
                new ActionEventHandler<CreateEnvironmentResult>() {
                    @Override
                    public void execute(final CreateEnvironmentResult pResult) {
                        getDisplay().setContent(navigation.getDisplay(),
                                environmentListing.getDisplay(),
                                emptyDetail.getDisplay());

                        // Refresh environment list
                        navigation.clear();
                        navigation.setEnvironments(pResult.getEnvironmentList());

                        // Display new environment
                        environmentListing.setEnvironment(pResult.getEnvironment());
                        environmentListing.setCategories(pResult.getCategories());
                    }
                });

        // Bind sub panels
        navigation.bind();
        dictionaryListing.bind();
        environmentListing.bind();
        dictionaryDetail.bind();
        environmentDetail.bind();
        environmentCreationDetail.bind();
        emptyDetail.bind();
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
}