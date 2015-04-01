/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.common.accesscontrol;

/**
 * Integer constants (flags) for roles management.
 * 
 * @author ahaugomm
 */
public class Roles {

    private final static int ROLE_ON_ALL_PRODUCTS_SHIFT = 3;

    /** Role on product (value is 1) */
    public final static int PRODUCT_ROLE = 1 << 0; //  1

    /** Role on each product of the instance (value is 2) */
    public final static int INSTANCE_ROLE = 1 << 1; //  2

    /** Role on one of the specified products (value is 4) */
    public final static int ROLE_ON_ONE_PRODUCT = 1 << 2; //  4

    /** Role on each one of the specified products (value is 8) */
    public final static int ROLE_ON_ALL_PRODUCTS =
            1 << ROLE_ON_ALL_PRODUCTS_SHIFT; //  8

}
