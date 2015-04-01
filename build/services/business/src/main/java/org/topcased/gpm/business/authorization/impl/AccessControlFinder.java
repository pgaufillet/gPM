/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.topcased.gpm.domain.accesscontrol.AccessControl;
import org.topcased.gpm.domain.accesscontrol.Role;
import org.topcased.gpm.domain.constant.AccessControlConstant;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.process.Node;
import org.topcased.gpm.domain.product.Product;

/**
 * Search for an access control object according to criteria.
 * 
 * @author llatil
 * @param <AC>
 *            Actual access control class to return.
 */
class AccessControlFinder<AC extends AccessControl> {

    /** Hibernate session */
    private final Session session;

    /**
     * Constructs a new AccessControl finder
     * 
     * @param pSession
     *            Hibernate session
     */
    public AccessControlFinder(Session pSession) {
        session = pSession;
    }

    /**
     * Search an access control instance in the database.
     * 
     * @param pClazz
     *            Actual class of the access control
     * @param pProduct
     *            Product
     * @param pState
     *            State of the sheet
     * @param pRole
     *            Role
     * @param pAdditionalCriterion
     *            Additional criterion for the query
     * @return The access control found according to the search parameters, or
     *         null if not exist in DB.
     */
    public AC getAccessControl(Class<AC> pClazz, Product pProduct, Node pState,
            Role pRole, Criterion pAdditionalCriterion,
            boolean pUseContextHierarchy) {
        List<Criterion> lCriteria;

        if (null == pAdditionalCriterion) {
            final List<Criterion> lEmptyCriterion = new ArrayList<Criterion>(0);
            lCriteria = lEmptyCriterion;
        }
        else {
            lCriteria = new ArrayList<Criterion>(1);
            lCriteria.add(pAdditionalCriterion);
        }
        return getAccessControl(pClazz, pProduct, pState, pRole, lCriteria,
                pUseContextHierarchy);
    }

    /**
     * Search an access control instance in the database.
     * 
     * @param pClazz
     *            Actual class of the access control
     * @param pProduct
     *            Product
     * @param pState
     *            State of the sheet
     * @param pRole
     *            Role
     * @param pAdditionalCriterion
     *            Additional criterion for the query
     * @return The access control found according to the search parameters, or
     *         null if not exist in DB.
     */
    public AC getAccessControl(Class<AC> pClazz, Product pProduct, Node pState,
            Role pRole, FieldsContainer pType, Criterion pAdditionalCriterion,
            boolean pUseContextHierarchy) {
        List<Criterion> lCriteria;

        if (null == pAdditionalCriterion) {
            final List<Criterion> lEmptyCriterion = new ArrayList<Criterion>(0);
            lCriteria = lEmptyCriterion;
        }
        else {
            lCriteria = new ArrayList<Criterion>(1);
            lCriteria.add(pAdditionalCriterion);
        }
        return getAccessControl(pClazz, pProduct, pState, pRole, pType,
                lCriteria, pUseContextHierarchy);
    }

    /**
     * Get an access control object from the DB corresponding to the given
     * search criteria.
     * 
     * @param pClazz
     *            Actual access control class to search.
     * @param pProduct
     *            Product of the access control (or null)
     * @param pState
     *            State of the sheet (or null)
     * @param pRole
     *            Role (or null)
     * @param pAdditionalCriteria
     *            List of additional HQL query criterion
     * @return The access control found according to the search parameters, or
     *         null if not exist in DB.
     */
    public AC getAccessControl(Class<AC> pClazz, Product pProduct, Node pState,
            Role pRole, List<Criterion> pAdditionalCriteria,
            boolean pUseContextHierarchy) {

        if (pUseContextHierarchy) {
            return getAccessControl(pClazz, createContextHierarchy(pProduct,
                    pState, pRole), pAdditionalCriteria);
        }
        // else
        return getAccessControl(pClazz, createContext(pProduct, pState, pRole),
                pAdditionalCriteria);
    }

    /**
     * Get an access control object from the DB corresponding to the given
     * search criteria.
     * 
     * @param pClazz
     *            Actual access control class to search.
     * @param pProduct
     *            Product of the access control (or null)
     * @param pState
     *            State of the sheet (or null)
     * @param pRole
     *            Role (or null)
     * @param pAdditionalCriteria
     *            List of additional HQL query criterion
     * @return The access control found according to the search parameters, or
     *         null if not exist in DB.
     */
    public AC getAccessControl(Class<AC> pClazz, Product pProduct, Node pState,
            Role pRole, FieldsContainer pType,
            List<Criterion> pAdditionalCriteria, boolean pUseContextHierarchy) {

        if (pUseContextHierarchy) {
            return getAccessControl(pClazz, createContextHierarchy(pProduct,
                    pState, pRole, pType), pAdditionalCriteria);
        }
        // else
        return getAccessControl(pClazz, createContext(pProduct, pState, pRole,
                pType), pAdditionalCriteria);
    }

    public Context createContext(Product pProduct, Node pState, Role pRole) {
        return new Context(pProduct, pState, pRole);
    }

    public Context createContext(Product pProduct, Node pState, Role pRole,
            FieldsContainer pType) {
        return new Context(pProduct, pState, pRole, pType);
    }

    public List<Context> createContextHierarchy(Product pProduct, Node pState,
            Role pRole) {
        final int lMaxContextsInHierarchy = 8;

        List<AccessControlFinder.Context> lContexts;
        lContexts =
                new ArrayList<AccessControlFinder.Context>(
                        lMaxContextsInHierarchy);

        if (null != pProduct) {
            if (null != pState) {
                lContexts.add(new Context(pProduct, pState, pRole));
                lContexts.add(new Context(pProduct, pState, null));
            }
            lContexts.add(new Context(pProduct, null, pRole));
            lContexts.add(new Context(pProduct, null, null));
        }
        if (null != pState) {
            lContexts.add(new Context(null, pState, pRole));
            lContexts.add(new Context(null, pState, null));
        }
        lContexts.add(new Context(null, null, pRole));
        lContexts.add(new Context(null, null, null));
        return lContexts;
    }

    public List<Context> createContextHierarchy(Product pProduct, Node pState,
            Role pRole, FieldsContainer pType) {
        final int lMaxContextsInHierarchy = 12;

        List<AccessControlFinder.Context> lContexts;
        lContexts =
                new ArrayList<AccessControlFinder.Context>(
                        lMaxContextsInHierarchy);

        if (null != pProduct) {
            if (null != pState) {
                if (null != pType) {
                    lContexts.add(new Context(pProduct, pState, pRole, pType));
                    lContexts.add(new Context(pProduct, pState, null, pType));
                }
            }
            else {
                if (null != pType) {
                    lContexts.add(new Context(pProduct, null, pRole, pType));
                }
            }
            lContexts.add(new Context(pProduct, null, pRole, null));
            lContexts.add(new Context(pProduct, null, null, pType));
            lContexts.add(new Context(pProduct, null, null, null));
        }
        if (null != pState) {
            if (null != pType) {
                lContexts.add(new Context(null, pState, pRole, pType));
                lContexts.add(new Context(null, pState, null, pType));
            }
        }
        if (null != pRole) {
            lContexts.add(new Context(null, null, pRole, pType));
            lContexts.add(new Context(null, null, pRole, null));
        }
        lContexts.add(new Context(null, null, null, pType));
        lContexts.add(new Context(null, null, null, null));
        return lContexts;
    }

    /**
     * Get an access control object from the DB corresponding to the given list
     * of search contexts.
     * 
     * @param pClazz
     *            Actual access control class to search.
     * @param pContexts
     *            List of contexts (state, role, product) for the search.
     * @param pAdditionalCriteria
     *            List of additional HQL query criterion
     * @return The access control found according to the search parameters, or
     *         null if not exist in DB.
     */
    public AC getAccessControl(Class<AC> pClazz,
            List<AccessControlFinder.Context> pContexts,
            List<Criterion> pAdditionalCriteria) {
        AC lAccessControl = null;

        // Iterate over all context (ordered) to find an AccessControl instance.
        for (Context lCtx : pContexts) {
            lAccessControl =
                    getAccessControl(pClazz, lCtx, pAdditionalCriteria);
            if (null != lAccessControl) {
                return lAccessControl;
            }
        }
        return lAccessControl;
    }

    /**
     * Get an access control object from the DB corresponding to the given list
     * of search contexts.
     * 
     * @param pClazz
     *            Actual access control class to search.
     * @param pCtx
     *            Context (state, role, product) for the search.
     * @param pAdditionalCriteria
     *            List of additional HQL query criterion
     * @return The access control found according to the search parameters, or
     *         null if not exist in DB.
     */
    @SuppressWarnings("unchecked")
    public AC getAccessControl(Class<AC> pClazz,
            AccessControlFinder.Context pCtx,
            List<Criterion> pAdditionalCriteria) {
        Criteria lCriteria = session.createCriteria(pClazz, "ac");

        // First use restrictions on the authorizations
        if (null != pCtx.getRole()) {
            lCriteria.add(Restrictions.eq(
                    AccessControlConstant.DB_ATTRIBUTE_ROLECONTROL.getAsString(),
                    pCtx.getRole()));
        }
        else {
            lCriteria.add(Restrictions.isNull(
                    AccessControlConstant.DB_ATTRIBUTE_ROLECONTROL.getAsString()));
        }
        if (null != pCtx.getProduct()) {
            lCriteria.add(Restrictions.eq(
                    AccessControlConstant.DB_ATTRIBUTE_PRODUCTCONTROL.getAsString(),
                    pCtx.getProduct()));
        }
        else {
            lCriteria.add(Restrictions.isNull(
                    AccessControlConstant.DB_ATTRIBUTE_PRODUCTCONTROL.getAsString()));
        }
        if (null != pCtx.getState()) {
            lCriteria.add(Restrictions.eq(
                    AccessControlConstant.DB_ATTRIBUTE_STATECONTROL.getAsString(),
                    pCtx.getState()));
        }
        else {
            lCriteria.add(Restrictions.isNull(
                    AccessControlConstant.DB_ATTRIBUTE_STATECONTROL.getAsString()));
        }
        if (null != pCtx.getType()) {
            lCriteria.add(Restrictions.eq(
                    AccessControlConstant.DB_ATTRIBUTE_TYPECONTROL.getAsString(),
                    pCtx.getType()));
        }
        else {
            lCriteria.add(Restrictions.isNull(
                    AccessControlConstant.DB_ATTRIBUTE_TYPECONTROL.getAsString()));
        }

        // Add optional criteria
        for (Criterion lAddedCriterion : pAdditionalCriteria) {
            lCriteria.add(lAddedCriterion);
        }
        return (AC) lCriteria.uniqueResult();
    }

    /**
     * Helper class used to store a context search. It contains the three common
     * criteria used to look for an access control: - The role of user - The
     * state of the sheet - The product Each attribute may be null, but at least
     * one needs to be valued.
     * 
     * @author llatil
     */
    public final static class Context {
        /**
         * Constructs a new finder context
         * 
         * @param pProduct
         *            Product
         * @param pState
         *            Sheet state
         * @param pRole
         *            Role of user
         */
        public Context(Product pProduct, Node pState, Role pRole,
                FieldsContainer pType) {
            product = pProduct;
            state = pState;
            role = pRole;
            type = pType;
        }

        public Context(Product pProduct, Node pState, Role pRole) {
            product = pProduct;
            state = pState;
            role = pRole;
            type = null;
        }

        /**
         * Get the product
         * 
         * @return Returns the product.
         */
        public Product getProduct() {
            return product;
        }

        /**
         * Get the role
         * 
         * @return Returns the role.
         */
        public Role getRole() {
            return role;
        }

        /**
         * Get the state node
         * 
         * @return Returns the state.
         */
        public Node getState() {
            return state;
        }

        public FieldsContainer getType() {
            return type;
        }

        /** Product */
        private final Product product;

        /** Sheet state */
        private final Node state;

        /** Role */
        private final Role role;

        private final FieldsContainer type;
    }
}
