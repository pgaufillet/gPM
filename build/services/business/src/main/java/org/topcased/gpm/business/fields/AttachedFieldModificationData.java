/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.fields;

/**
 * @author Atos
 */
public class AttachedFieldModificationData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public AttachedFieldModificationData() {
    }

    /**
     * Constructor without default values.
     */
    public AttachedFieldModificationData(final java.lang.String pName,
            final java.lang.String pId) {
        this.name = pName;
        this.id = pId;
    }

    /**
     * Constructor taking all properties.
     */
    public AttachedFieldModificationData(final java.lang.String pName,
            final java.lang.String pMimeType, final byte[] pContent,
            final java.lang.String pId) {
        this.name = pName;
        this.mimeType = pMimeType;
        this.content = pContent;
        this.id = pId;
    }

    /**
     * Copies constructor from other AttachedFieldModificationData
     */
    public AttachedFieldModificationData(
            AttachedFieldModificationData pOtherBean) {
        if (pOtherBean != null) {
            this.name = pOtherBean.getName();
            this.mimeType = pOtherBean.getMimeType();
            this.content = pOtherBean.getContent();
            this.id = pOtherBean.getId();
        }
    }

    private java.lang.String name;

    /**
     * <p>
     * The name of the attached file.
     * </p>
     */
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    private java.lang.String mimeType = null;

    /**
     * <p>
     * The mime type of the attached file. Null only if content is null.
     * </p>
     */
    public java.lang.String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(java.lang.String pMimeType) {
        this.mimeType = pMimeType;
    }

    private byte[] content = null;

    /**
     * <p>
     * The content of the attached file. Always null except in case of save or
     * update.
     * </p>
     */
    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] pContent) {
        this.content = pContent;
    }

    private java.lang.String id;

    /**
     * 
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
    }

}