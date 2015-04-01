/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/

package org.topcased.gpm.domain.process;

import java.util.Set;

/**
 * Node Entity
 * 
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "NODE")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Node extends
        org.topcased.gpm.domain.extensions.ExtensionsContainer {
    private static final long serialVersionUID = -5925005161152252319L;

    /**
     * node name
     */
    protected java.lang.String name;

    @javax.persistence.Column(name = "NAME", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "node_name_idx")
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    /**
     * the node type help to determine if the node is start state, state or end
     * state
     */
    protected java.lang.String type;

    @javax.persistence.Column(name = "TYPE", length = 10, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    /**
     * Retrieves the node Type
     * 
     * @return ProcessDefinition.stateType or null
     */
    public String getType() {
        return type;
    }

    public void setType(java.lang.String pType) {
        this.type = pType;
    }

    /**
     * specify the Transition that belong to the node
     */
    protected Set<org.topcased.gpm.domain.process.Transition> transitions;

    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, mappedBy = "fromNode", fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.process.Transition.class)
    @org.hibernate.annotations.OrderBy(clause = "TRANSITION_FK")
    @org.hibernate.annotations.ForeignKey(name = "TRANSITION_FKC")
    public Set<org.topcased.gpm.domain.process.Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(
            java.util.Set<org.topcased.gpm.domain.process.Transition> pTransitions) {
        this.transitions = pTransitions;
    }

    /**
     * specify the process definitin to which the node belong to
     */
    protected org.topcased.gpm.domain.process.ProcessDefinition processDefinition;

    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.process.ProcessDefinition.class)
    @javax.persistence.JoinColumn(nullable = true, name = "PROCESS_DEFINITION_FK")
    @org.hibernate.annotations.ForeignKey(name = "SHEET_TYPE_PROCESS_DEFINITION")
    @org.hibernate.annotations.Index(name = "node_pd_type_idx", columnNames = {
                                                                               "PROCESS_DEFINITION_FK",
                                                                               "TYPE" })
    public org.topcased.gpm.domain.process.ProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }

    public void setProcessDefinition(
            org.topcased.gpm.domain.process.ProcessDefinition pProcessDefinition) {
        this.processDefinition = pProcessDefinition;
    }

    /**
     * 
     */
    @javax.persistence.Transient
    public void dummy() {
        // Dummy method. No actual implementation.
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.extensions.ExtensionsContainer</code> class
     * it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainer#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.extensions.ExtensionsContainerImpl</code>
     * class it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainer#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of {@link org.topcased.gpm.domain.process.Node}.
     * 
     * @return a new instance of {@link org.topcased.gpm.domain.process.Node}
     */
    public static org.topcased.gpm.domain.process.Node newInstance() {
        return new org.topcased.gpm.domain.process.Node();
    }

}