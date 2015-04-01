/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container.sheet;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.link.AbstractLoadLinkCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.common.container.ContainerPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.client.menu.user.AbstractSheetDetailMenuBuilder;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetCreationResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationResult;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentTotalSizeException;
import org.topcased.gpm.ui.application.shared.exception.UIEmptyAttachmentException;
import org.topcased.gpm.ui.application.shared.exception.UIInvalidCharacterException;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The presenter for display a sheet.
 * 
 * @author tpanuel
 */
public abstract class SheetPresenter extends ContainerPresenter<SheetDisplay> {
    private final AbstractSheetDetailMenuBuilder menuBuilder;

    private String productName;

    private int version;

    /**
     * Create a presenter for the AbstractSheetView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pMenuBuilder
     *            The menu builder.
     * @param pVisuCommand
     *            The visualization command.
     * @param pEditCommand
     *            The edition command.
     * @param pLoadLinkCommand
     *            The load link command.
     */
    public SheetPresenter(final SheetDisplay pDisplay,
            final EventBus pEventBus,
            final AbstractSheetDetailMenuBuilder pMenuBuilder,
            final OpenSheetOnVisualizationCommand pVisuCommand,
            final OpenSheetOnEditionCommand pEditCommand,
            final AbstractLoadLinkCommand pLoadLinkCommand) {
        super(pDisplay, pEventBus, pVisuCommand, pEditCommand, pLoadLinkCommand);
        menuBuilder = pMenuBuilder;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerPresenter#getOpenCloseWorkspacePanelActionType()
     */
    @Override
    protected Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspacePanelActionType() {
        return LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(productName);
    }

    /**
     * Get the product name.
     * 
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * get version
     * 
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerPresenter#initContainer(org.topcased.gpm.ui.application.shared.command.container.GetContainerResult)
     */
    @Override
    public void initContainer(final GetContainerResult<?> pResult) {
        final GetSheetResult lSheetResult = (GetSheetResult) pResult;
        final List<UIAttachmentException> lAttachedFieldsStatus = lSheetResult.getAttachedFieldsInError();
        final UiSheet lSheet = lSheetResult.getContainer();
        final List<Translation> lLinkGroups;

        version = lSheet.getVersion();

        productName = lSheet.getProductName();
        // The state is not displayed on creation mode
        if (!(pResult instanceof GetSheetCreationResult)) {
            getDisplay().setSheetState(lSheet.getState());
            getDisplay().setTransitionHistory(lSheet.getTransitionHistoric());
        }
        // Get links
        if (pResult instanceof GetSheetEditionResult) {
            lLinkGroups = ((GetSheetEditionResult) pResult).getLinkGroups();
        }
        else if (pResult instanceof GetSheetVisualizationResult) {
            lLinkGroups = ((GetSheetVisualizationResult) pResult).getLinkGroups();
        }
        else {
        	lLinkGroups = null;
        }
        // Initialize
        init(lSheet.getId(), lSheet.getFunctionalReference(), lSheet,
        		lSheet.getFieldGroups(), lLinkGroups, lAttachedFieldsStatus);
        
        // Fail if an error had been detected
        if (lAttachedFieldsStatus != null && !lAttachedFieldsStatus.isEmpty()) {
        	StringBuilder lSBuilder = new StringBuilder();
        	lSBuilder.append(Ui18n.MESSAGES.fieldErrorAttachmentIntroduction());
        	for (UIAttachmentException lError : lAttachedFieldsStatus) {
        		lSBuilder.append("</br> - ");
        		if (lError instanceof UIEmptyAttachmentException) {
        			lSBuilder.append(Ui18n.MESSAGES.fieldErrorAttachmentEmptyFile(lError.getItem()));
        		}
        		if (lError instanceof UIAttachmentTotalSizeException) {
        			lSBuilder.append(Ui18n.MESSAGES.fieldErrorAttachmentSizeExceeded(lError.getItem()));
        		}
        		if (lError instanceof UIInvalidCharacterException) {
        			lSBuilder.append(Ui18n.MESSAGES.fieldErrorAttachmentInvalidCharacter(lError.getItem(),
        					lError.getInvalidCharactersAsString()));
        		}
        	}
        	Application.INJECTOR.getErrorMessagePresenter().displayMessage(lSBuilder.toString());
        }

		// Display tool bar
		getDisplay().setToolBar(menuBuilder.buildMenu(pResult.getActions(), lSheetResult.getExtendedActions()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.AbstractPresenter#onUnbind()
     */
    @Override
    protected void onUnbind() {
        // Nothing to unbind
    }
}
