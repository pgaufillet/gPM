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
 * ProcessDefinition entity
 * 
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "PROCESS_DEFINITION")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ProcessDefinition implements java.io.Serializable,
        org.topcased.gpm.domain.PersistentObject {
    private static final long serialVersionUID = -1487840056828386179L;

    /**
     * <p>
     * State type enumeration.
     * <p>
     * It could be of type :
     * <ul>
     * <li>STARTSTATE</li>
     * <li>ENDSTATE</li>
     * <li>STATE</li>
     * </ul>
     */
    public static enum stateType {
        STARTSTATE, ENDSTATE, STATE
    }

    /**
     * process definition name
     */
    protected java.lang.String name;

    @javax.persistence.Column(name = "NAME", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "process_definition_name_idx")
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    protected java.lang.String id;

    /**
     * 
     */
    @javax.persistence.Id
    @org.hibernate.annotations.GenericGenerator(name = "UUID_GEN", strategy = "org.topcased.gpm.domain.util.UuidGenerator")
    @javax.persistence.GeneratedValue(generator = "UUID_GEN")
    @javax.persistence.Column(name = "ID", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
    }

    /**
     * A process definition has a node list
     */
    protected Set<org.topcased.gpm.domain.process.Node> nodes =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.process.Node>();

    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, mappedBy = "processDefinition", fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.process.Node.class)
    @org.hibernate.annotations.OrderBy(clause = "NODE_FK")
    @org.hibernate.annotations.ForeignKey(name = "NODE_FKC")
    public Set<org.topcased.gpm.domain.process.Node> getNodes() {
        return nodes;
    }

    public void setNodes(
            java.util.Set<org.topcased.gpm.domain.process.Node> pNodes) {
        this.nodes = pNodes;
    }

    /**
     * Returns <code>true</code> if the argument is an ProcessDefinition
     * instance and all identifiers for this entity equal the identifiers of the
     * argument entity. Returns <code>false</code> otherwise.
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof ProcessDefinition)) {
            return false;
        }
        final ProcessDefinition lProcessDefinition =
                (ProcessDefinition) pObject;
        if (this.getId() == null || lProcessDefinition.getId() == null
                || !lProcessDefinition.getId().equals(getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers.
     * 
     * @return a hash code value for this object.
     * @see java.lang.Object#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        int lHashCode = 0;
        if (id != null) {
            lHashCode = id.hashCode();
        }
        return lHashCode;
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.process.ProcessDefinition}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.process.ProcessDefinition}
     */
    public static org.topcased.gpm.domain.process.ProcessDefinition newInstance() {
        return new org.topcased.gpm.domain.process.ProcessDefinition();
    }

}