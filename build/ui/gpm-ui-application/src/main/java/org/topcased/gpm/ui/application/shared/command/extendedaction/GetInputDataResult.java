/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.extendedaction;

import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.facade.shared.container.inputdata.UiInputData;

/**
 * GetInputDataResult
 * 
 * @author nveillet
 */
public class GetInputDataResult extends AbstractExecuteExtendedActionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -5865077889194125259L;

    private String extendedActionName;

    private ExtendedActionType extendedActionType;

    private String extensionContainerId;

    private UiInputData inputData;

    /**
     * Empty constructor for serialization.
     */
    public GetInputDataResult() {
    }

    /**
     * Create GetInputDataResult with values
     * 
     * @param pExtendedActionName
     *            the extended action name
     * @param pExtensionContainerId
     *            the extension container identifier
     * @param pInputData
     *            the input data
     * @param pExtendedActionType
     *            the extended action type
     */
    public GetInputDataResult(String pExtendedActionName,
            String pExtensionContainerId, UiInputData pInputData,
            ExtendedActionType pExtendedActionType) {
        super();
        extendedActionName = pExtendedActionName;
        extensionContainerId = pExtensionContainerId;
        inputData = pInputData;
        extendedActionType = pExtendedActionType;
    }

    /**
     * get extended action name
     * 
     * @return the extended action name
     */
    public String getExtendedActionName() {
        return extendedActionName;
    }

    /**
     * get extended action type
     * 
     * @return the extended action type
     */
    public ExtendedActionType getExtendedActionType() {
        return extendedActionType;
    }

    /**
     * get extension container identifier
     * 
     * @return the extension container identifier
     */
    public String getExtensionContainerId() {
        return extensionContainerId;
    }

    /**
     * get input data
     * 
     * @return the input data
     */
    public UiInputData getInputData() {
        return inputData;
    }
}
