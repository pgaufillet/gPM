/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.topcased.gpm.business.serialization.converter.DateTimeConverter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * TransitionHistoryData class
 * 
 * @author Laurent Latil
 */
@XStreamAlias("transitionData")
public class TransitionHistoryData implements Serializable {

    /**
     * Default serial ID 
     */
    private static final long serialVersionUID = 1L;

    @XStreamAsAttribute
    private String login;

    @XStreamAsAttribute
    private String originState;

    @XStreamAsAttribute
    private String destinationState;

    @XStreamAlias("date")
    @XStreamAsAttribute
    @XStreamConverter(DateTimeConverter.class)
    private Date transitionDate;

    @XStreamAlias("transitionName")
    @XStreamAsAttribute
    private String transitionName;

    private String sheetId;

    public final String getLogin() {
        return login;
    }

    public final void setLogin(String pLogin) {
        login = pLogin;
    }

    public final String getOriginState() {
        return originState;
    }

    public final void setOriginState(String pOriginState) {
        originState = pOriginState;
    }

    public final String getDestinationState() {
        return destinationState;
    }

    public final void setDestinationState(String pDestinationState) {
        destinationState = pDestinationState;
    }

    public final Date getTransitionDate() {
        return transitionDate;
    }

    public final void setTransitionDate(Date pTransitionDate) {
        transitionDate = pTransitionDate;
    }

    public final String getTransitionName() {
        return transitionName;
    }

    public final void setTransitionName(String pTransitionName) {
        this.transitionName = pTransitionName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof TransitionHistoryData) {
            TransitionHistoryData lOther = (TransitionHistoryData) pOther;

            if (!StringUtils.equals(login, lOther.login)) {
                return false;
            }
            if (!StringUtils.equals(originState, lOther.originState)) {
                return false;
            }
            if (!StringUtils.equals(destinationState, lOther.destinationState)) {
                return false;
            }
            if (!StringUtils.equals(sheetId, lOther.sheetId)) {
                return false;
            }
            if (!DateUtils.isSameInstant(transitionDate, lOther.transitionDate)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(transitionDate).toHashCode();
    }

    public String getSheetId() {
        return sheetId;
    }

    public void setSheetId(String pSheetId) {
        this.sheetId = pSheetId;
    }

}
