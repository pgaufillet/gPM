/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is use to store all elements of the XML File.
 * 
 * @author llatil
 */
@XStreamAlias("gpm")
public class Gpm {

    /** The version of gPM Application. */
    private String version;

    /** The dictionary. */
    private Dictionary dictionary;

    /** The sheet links. */
    private List<Link> sheetLinks;

    /** The product links. */
    private List<Link> productLinks;

    /** The translations. */
    private List<Translation> translations;

    /** The product types. */
    private List<ProductType> productTypes;

    /** The product link types. */
    private List<LinkType> productLinkTypes;

    /** The sheet types. */
    private List<SheetType> sheetTypes;

    /** The sheet link types. */
    private List<LinkType> sheetLinkTypes;

    /** The products. */
    private List<Product> products;

    /** The sheets. */
    private List<SheetData> sheets;

    /** The filters. */
    private List<Filter> filters;

    /** The reports. */
    private List<ReportModel> reports;

    /** The users. */
    private List<User> users;

    /** The roles. */
    private List<Role> roles;

    /** The user roles. */
    private List<UserRole> userRoles;

    /** The access controls. */
    private List<AccessCtl> accessControls;

    /** The commands. */
    private List<Command> commands;

    /** The attributes. */
    private List<Attribute> attributes;

    /** The extended actions. */
    private List<ExtendedAction> extendedActions;

    /** The options. */
    private Option options;

    /** The extension points. */
    private List<ExtensionPoint> extensionPoints;

    private List<FilterAccessConstraint> filterAccessConstraints;

    /** The filter access controls. */
    private List<FilterAccessCtl> filterAccessControls;

    /** The type mapping */
    private List<TypeMapping> mapping;

    /**
     * Gets the extension points.
     * 
     * @return the extension points
     */
    public List<ExtensionPoint> getExtensionPoints() {
        return extensionPoints;
    }

    /**
     * Sets the extension points.
     * 
     * @param pExtensionPoints
     *            the extension points
     */
    public void setExtensionPoints(List<ExtensionPoint> pExtensionPoints) {
        extensionPoints = pExtensionPoints;
    }

    /**
     * Gets the attributes.
     * 
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Gets the users.
     * 
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Gets the roles.
     * 
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Gets the filters.
     * 
     * @return the filters
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     * Gets the translations.
     * 
     * @return the translations
     */
    public List<Translation> getTranslations() {
        return translations;
    }

    /**
     * Gets the access controls.
     * 
     * @return the access controls
     */
    public List<AccessCtl> getAccessControls() {
        return accessControls;
    }

    /**
     * Gets the products.
     * 
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Gets the sheets.
     * 
     * @return the sheets
     */
    public List<SheetData> getSheets() {
        return sheets;
    }

    /**
     * Gets the product types.
     * 
     * @return the product types
     */
    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    /**
     * Gets the sheet types.
     * 
     * @return the sheet types
     */
    public List<SheetType> getSheetTypes() {
        return sheetTypes;
    }

    /**
     * Gets the dictionary.
     * 
     * @return the dictionary
     */
    public Dictionary getDictionary() {
        return dictionary;
    }

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the sheet links.
     * 
     * @return the sheet links
     */
    public List<Link> getSheetLinks() {
        return sheetLinks;
    }

    /**
     * Gets the sheet link types.
     * 
     * @return the sheet link types
     */
    public List<LinkType> getSheetLinkTypes() {
        return sheetLinkTypes;
    }

    /**
     * Gets the commands.
     * 
     * @return Returns the commands.
     */
    public List<Command> getCommands() {
        return commands;
    }

    /**
     * Gets the user roles.
     * 
     * @return the user roles
     */
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    /**
     * Gets the reports.
     * 
     * @return the reports
     */
    public List<ReportModel> getReports() {
        return reports;
    }

    /**
     * Gets the options.
     * 
     * @return the options
     */
    public Option getOptions() {
        return options;
    }

    /**
     * get extendedActions.
     * 
     * @return the extendedActions
     */
    public List<ExtendedAction> getExtendedActions() {
        return extendedActions;
    }

    /**
     * Gets the product links.
     * 
     * @return the product links
     */
    public final List<Link> getProductLinks() {
        return productLinks;
    }

    /**
     * Sets the product links.
     * 
     * @param pProductLinks
     *            the new product links
     */
    public final void setProductLinks(List<Link> pProductLinks) {
        productLinks = pProductLinks;
    }

    /**
     * Gets the product link types.
     * 
     * @return the product link types
     */
    public final List<LinkType> getProductLinkTypes() {
        return productLinkTypes;
    }

    /**
     * Sets the product link types.
     * 
     * @param pProductLinkTypes
     *            the new product link types
     */
    public final void setProductLinkTypes(List<LinkType> pProductLinkTypes) {
        productLinkTypes = pProductLinkTypes;
    }

    public final void setVersion(String pVersion) {
        version = pVersion;
    }

    public List<FilterAccessConstraint> getFilterAccessConstraints() {
        return filterAccessConstraints;
    }

    public List<FilterAccessCtl> getFilterAccessControls() {
        return filterAccessControls;
    }

    /**
     * Get the mapping.
     * 
     * @return The mapping.
     */
    public List<TypeMapping> getMapping() {
        return mapping;
    }

    /**
     * Set the mapping.
     * 
     * @param pMapping
     *            The new mapping.
     */
    public void setMapping(final List<TypeMapping> pMapping) {
        mapping = pMapping;
    }
}