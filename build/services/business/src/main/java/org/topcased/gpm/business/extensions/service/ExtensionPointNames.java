/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

/**
 * Name of the different extension points used by gPM
 * 
 * @author tpanuel
 */
public class ExtensionPointNames {
    /** Extension called before the creation of a values container */
    public final static String PRE_CREATE = "preCreate";

    /** Extension called after the creation of a values container */
    public final static String POST_CREATE = "postCreate";

    /** Extension called before the update of a values container */
    public final static String PRE_UPDATE = "preUpdate";

    /** Extension called after the update of a values container */
    public final static String POST_UPDATE = "postUpdate";

    /** Extension called after user role update */
    public final static String POST_UPDATE_ROLES = "postUpdateRoles";
    
    /** Extension called before the deletion of a values container */
    public final static String PRE_DELETE = "preDelete";

    /** Extension called after the deletion of a values container */
    public final static String POST_DELETE = "postDelete";

    /** Extension called when a sheet or a product is about to be created */
    public final static String POST_GET_MODEL = "postGetModel";

    /** Extension called when a sheet is initialized using another */
    public final static String POST_GET_INITIALIZATION_MODEL =
            "postGetInitializationModel";

    /** Extension called when a sheet is duplicated using another */
    public final static String POST_GET_DUPLICATION_MODEL =
            "postGetDuplicationModel";

    /** Extension called when a sheet is about to change its state */
    public final static String PRE_CHANGE_STATE = "preChangeState";

    /** Extension called when a sheet has changed its state */
    public final static String POST_CHANGE_STATE = "postChangeState";

    /** Extension called when a sheet enter a state node in a transition */
    public final static String ENTER_STATE = "enterState";

    /** Extension called when a sheet leave a state node in a transition */
    public final static String LEAVE_STATE = "leaveState";

    /** Extension called before the lock on a sheet */
    public final static String PRE_LOCK = "preLock";

    /** Extension called after the lock on a sheet */
    public final static String POST_LOCK = "postLock";

    /** Extension called before the creation of a revision */
    public final static String PRE_CREATE_REVISION = "preCreateRevision";

    /** Extension called after the creation of a revision */
    public final static String POST_CREATE_REVISION = "postCreateRevision";

    /** Extension called before the deletion of a revision */
    public final static String PRE_DELETE_REVISION = "preDeleteRevision";

    /** Extension called after the deletion of a revision */
    public final static String POST_DELETE_REVISION = "postDeleteRevision";

    /** The extension called before an export */
    public static final String PRE_EXPORT = "preExport";

}
