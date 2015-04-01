/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.event;

import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterResult;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksResult;
import org.topcased.gpm.ui.application.shared.command.sheet.DeleteSheetResult;

import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The local action event types.
 * 
 * @author tpanuel
 * @param <R>
 *            The type of result.
 */
public final class LocalEvent<R extends Result> {

    /**
     * Close a sheet.
     */
    public final static LocalEvent<CloseTabAction> CLOSE_SHEET =
            new LocalEvent<CloseTabAction>();

    /**
     * Delete a sheet.
     */
    public final static LocalEvent<DeleteSheetResult> DELETE_SHEET =
            new LocalEvent<DeleteSheetResult>();

    /**
     * Execute a sheet table filter.
     */
    public final static LocalEvent<AbstractCommandFilterResult> EXECUTE_SHEET_TABLE_FILTER =
            new LocalEvent<AbstractCommandFilterResult>();

    /**
     * Execute a sheet tree filter.
     */
    public final static LocalEvent<AbstractCommandFilterResult> EXECUTE_SHEET_TREE_FILTER =
            new LocalEvent<AbstractCommandFilterResult>();

    /**
     * Remove the executed sheet tree filter.
     */
    @SuppressWarnings("rawtypes")
	public final static LocalEvent<EmptyAction> REMOVE_SHEET_TREE_FILTER =
            new LocalEvent<EmptyAction>();

    /**
     * Open or close a workspace panel (navigation ,listing or detail) for the
     * user space.
     */
    public final static LocalEvent<OpenCloseWorkspacePanelAction> OPEN_CLOSE_SHEET_WORKSPACE =
            new LocalEvent<OpenCloseWorkspacePanelAction>();

    /**
     * Delete a sheet filter.
     */
    public final static LocalEvent<DeleteFilterResult> DELETE_SHEET_FILTER =
            new LocalEvent<DeleteFilterResult>();

    /**
     * Load sheet links
     */
    public final static LocalEvent<GetLinksResult> LOAD_SHEET_LINKS =
            new LocalEvent<GetLinksResult>();

    private final Map<String, Type<ActionEventHandler<R>>> types;

    private LocalEvent() {
        types = new HashMap<String, Type<ActionEventHandler<R>>>();
    }

    /**
     * Get the local type.
     * 
     * @param pLocalId
     *            The local id.
     * @return The type.
     */
    public Type<ActionEventHandler<R>> getType(final String pLocalId) {
        Type<ActionEventHandler<R>> lType = types.get(pLocalId);

        if (lType == null) {
            lType = new Type<ActionEventHandler<R>>();
            types.put(pLocalId, lType);
        }

        return lType;
    }
}