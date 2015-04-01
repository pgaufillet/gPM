/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.user;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;

/**
 * Get user affectation popup result.
 * 
 * @author jlouisy
 */
public class GetAffectationResult implements Result {

    private static final long serialVersionUID = -2996829566735222575L;

    private UiUserAffectation affectation;

    private List<String> products;

    /**
     * Empty constructor for serialization.
     */
    public GetAffectationResult() {
    }

    /**
     * Create GetAffectationResult with values.
     * 
     * @param pAffectation
     *            the affectation.
     * @param pProducts
     *            list of all the products.
     */
    public GetAffectationResult(UiUserAffectation pAffectation, List<String> pProducts) {
        super();
        affectation = pAffectation;
        products = pProducts;
    }

    public UiUserAffectation getAffectation() {
        return affectation;
    }

    public List<String> getProducts() {
        return products;
    }

}
