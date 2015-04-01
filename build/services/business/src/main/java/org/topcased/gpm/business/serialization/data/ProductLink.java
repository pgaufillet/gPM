/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ProductLink
 *<p>
 * A product's link can be defined in two ways:
 * <ol>
 * <li>With linked products identifier: using Link object attribute 'originId'
 * and 'destinationId'.</li>
 * <li>Without the identifier but with the linked products reference using
 * attributes 'originProductName' and 'destinationProductName'.</li>
 * </ol>
 * </p>
 * 
 * @author mkargbo
 */
@XStreamAlias("productLink")
public class ProductLink extends Link {

    /** serialVersionUID */
    private static final long serialVersionUID = -1605570433399875472L;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.data.Link#getTagName()
     */
    @Override
    public String getTagName() {
        return "productLink";
    }

}
