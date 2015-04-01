/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.domain.process;

/**
 * Entity Transition
 * 
 * @author phtsaan
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "TRANSITION")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Transition extends
        org.topcased.gpm.domain.extensions.ExtensionsContainer {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3815894882507838357L;

    protected java.lang.String name;

    /**
     * Get the name of the transition
     * 
     * @return the name
     */
    @javax.persistence.Column(name = "NAME", length = 4000, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "transition_name_idx")
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    /**
     * 
     */
    protected String toNodeName;

    @javax.persistence.Column(name = "TO_NAME", length = 4000, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getToName() {
        return this.toNodeName;
    }

    public void setToName(java.lang.String pToNodeName) {
        this.toNodeName = pToNodeName;
    }

    /**
     * 
     */
    protected org.topcased.gpm.domain.process.Node fromNode;

    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.process.Node.class)
    @javax.persistence.JoinColumn(nullable = true, name = "FROM_NODE_FK")
    @org.hibernate.annotations.ForeignKey(name = "FROM_NODE_FKC")
    @org.hibernate.annotations.Index(name = "transition_from_fk_idx")
    public org.topcased.gpm.domain.process.Node getFromNode() {
        return this.fromNode;
    }

    public void setFromNode(org.topcased.gpm.domain.process.Node pFromNode) {
        this.fromNode = pFromNode;
    }

    protected boolean isOutTransiton;

    /**
     * 
     */
    @javax.persistence.Column(name = "OUT_GOING", nullable = true, unique = false)
    @org.hibernate.annotations.Type(type = "boolean")
    public boolean isOutgoingTransition() {
        return this.isOutTransiton;
    }

    public void setOutgoingTransition(boolean pIsOutTransiton) {
        this.isOutTransiton = pIsOutTransiton;
    }

    /**
     * The confirmation message
     */
    protected String confirmationMessage;

    @javax.persistence.Column(name = "CONFIRMATION_MESSAGE", nullable = true, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public String getConfirmationMessage() {
        return this.confirmationMessage;
    }

    public void setConfirmationMessage(String pConfirmationMessage) {
        this.confirmationMessage = pConfirmationMessage;
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.process.Transition}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.process.Transition}
     */
    public static org.topcased.gpm.domain.process.Transition newInstance() {
        return new org.topcased.gpm.domain.process.Transition();
    }

}
