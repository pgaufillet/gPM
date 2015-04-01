/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.command.link.AbstractLoadLinkCommand;
import org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.component.client.container.GpmDisplayGroupPanel;
import org.topcased.gpm.ui.component.client.container.GpmLinkGroupPanel;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.UiFieldGroup;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The presenter for the AbstractSheetView.
 * 
 * @author tpanuel
 * @param <D>
 *            The type of container display.
 */
public abstract class ContainerPresenter<D extends ContainerDisplay> extends
        TabElementPresenter<D> {

    protected final UiFieldManager containerFieldManager;

    private final Map<String, UiFieldManager> linkFieldsManager;

    private final ClickHandler visu;

    private final ClickHandler edit;

    private final AbstractLoadLinkCommand loadLink;

    private String id;

    /**
     * Create a presenter for the AbstractSheetView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pVisuCommand
     *            The visualization command.
     * @param pEditCommand
     *            The edition command.
     * @param pLoadLinkCommand
     *            The load link command.
     */
    public ContainerPresenter(final D pDisplay, final EventBus pEventBus,
            final ClickHandler pVisuCommand, final ClickHandler pEditCommand,
            final AbstractLoadLinkCommand pLoadLinkCommand) {
        super(pDisplay, pEventBus);
        containerFieldManager = new UiFieldManager();
        linkFieldsManager = new HashMap<String, UiFieldManager>();
        visu = pVisuCommand;
        edit = pEditCommand;
        loadLink = pLoadLinkCommand;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabElementPresenter#getTabId()
     */
    @Override
    public String getTabId() {
        return id;
    }

    /**
     * Initialize the container.
     * 
     * @param pContainerId
     *            The container id.
     * @param pContainerRef
     *            The container reference.
     * @param pContainer
     *            The container.
     * @param pFieldGroups
     *            The field groups.
     * @param pLinkGroups
     *            The link groups.
     * @param pAttachedFieldsInError
     *            The Attached fields in error
     */
    protected void init(final String pContainerId, final String pContainerRef,
            final UiContainer pContainer,
            final List<UiFieldGroup> pFieldGroups,
            final List<Translation> pLinkGroups,
            final List<UIAttachmentException> pAttachedFieldsInError) {
        id = pContainerId;
        getDisplay().setContainerReference(pContainerRef);
        // Display fields
        containerFieldManager.init(getDisplay().getContainerFieldSet());
        for (final UiFieldGroup lGroup : pFieldGroups) {
            getDisplay().addFieldGroup(
                    lGroup.getGroupName(),
                    containerFieldManager.buildGroup(pContainer,
                            lGroup.getFieldNames(), pAttachedFieldsInError),
                    lGroup.isOpen());
        }
        // Display links
        if (pLinkGroups != null) {
            // Links are grouped by type
            for (final Translation lLinkGroup : pLinkGroups) {
                final GpmLinkGroupPanel lLinkGroupPanel =
                        getDisplay().addLinkGroup(lLinkGroup.getValue(),
                                lLinkGroup.getTranslatedValue(), loadLink);
                lLinkGroupPanel.close();
            }
        }
    }

    /**
     * Initialize a link group
     * 
     * @param pValuesContainerId
     *            the values container identifier
     * @param pLinkTypeName
     *            the link type name
     * @param pLinks
     *            the links
     */
    public void initLinks(String pValuesContainerId, String pLinkTypeName,
            List<UiLink> pLinks) {
        final GpmLinkGroupPanel lLinkGroupPanel =
                getDisplay().getLinkGroup(pLinkTypeName);

        // Create a sub-group for each Link
        for (final UiLink lLink : pLinks) {
            GpmDisplayGroupPanel lLinkPanel;

            if (pValuesContainerId.equals(lLink.getOriginId())) {
                lLinkPanel =
                        lLinkGroupPanel.addLink(lLink.getDestinationId(),
                                lLink.getDestinationReference(), visu, edit);
            }
            else {
                lLinkPanel =
                        lLinkGroupPanel.addLink(lLink.getOriginId(),
                                lLink.getOriginReference(), visu, edit);
            }
            // Build the field manager for the link
            UiFieldManager lLinkFieldManager = new UiFieldManager();
            lLinkFieldManager.init(lLinkPanel);
            linkFieldsManager.put(lLink.getId(), lLinkFieldManager);

            // Add field creation handler for the link panel
            lLinkPanel.setFieldCreationHandler(lLinkFieldManager.buildLink(lLink));

            lLinkGroupPanel.open();
        }
    }

    /**
     * Initialize the container.
     * 
     * @param pResult
     *            The result to display the container.
     */
    abstract public void initContainer(final GetContainerResult<?> pResult);

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        // Nothing to do
    }

    /**
     * Validate all the fields.
     * 
     * @return The list of error.
     */
    public List<String> validate() {
        final List<String> lResult = new ArrayList<String>();

        lResult.addAll(containerFieldManager.validate());
        for (final UiFieldManager lLinkManager : linkFieldsManager.values()) {
            lResult.addAll(lLinkManager.validate());
        }

        return lResult;
    }

    /**
     * Get the upload registers.
     * 
     * @return The upload registers.
     */
    public List<GpmUploadFileRegister> getUploadRegisters() {
        final List<GpmUploadFileRegister> lResult =
                new ArrayList<GpmUploadFileRegister>();

        lResult.addAll(containerFieldManager.getUploadRegisters());
        for (final UiFieldManager lLinkManager : linkFieldsManager.values()) {
            lResult.addAll(lLinkManager.getUploadRegisters());
        }

        return lResult;
    }

    /**
     * Get the updated fields.
     * 
     * @return The updated fields.
     */
    public List<UiField> getUpdatedFields() {
        return containerFieldManager.getUpdatedFields();
    }

    /**
     * Get the updated link fields by link ID
     * 
     * @return The updated link fields by link ID
     */
    public Map<String, List<UiField>> getUpdatedLinkFields() {
        Map<String, List<UiField>> lUpdatedLinkFields =
                new HashMap<String, List<UiField>>();

        for (String lLinkId : linkFieldsManager.keySet()) {
            List<UiField> lUiFields =
                    linkFieldsManager.get(lLinkId).getUpdatedFields();
            if (!lUiFields.isEmpty()) {
                lUpdatedLinkFields.put(lLinkId, lUiFields);
            }
        }

        return lUpdatedLinkFields;
    }

    /**
     * Get the open / close workspace action type.
     * 
     * @return The open / close workspace action type.
     */
    protected abstract Type<ActionEventHandler<OpenCloseWorkspacePanelAction>> getOpenCloseWorkspacePanelActionType();
}
