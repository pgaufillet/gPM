/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.dbutils;

import java.util.List;

/**
 * Interface to be implemented by all DB backend support
 * 
 * @author llatil
 */
public interface DbOperations {

    /**
     * Connect to the DB
     */
    public void connect();

    /**
     * Get the list of table names from the DB
     * 
     * @return Table names list
     */
    public List<String> getTableNames();

    /**
     * Get the list of sequence names from the DB
     * 
     * @return Sequence names list
     */
    public List<String> getSequenceNames();

    /**
     * Drop the specified tables
     * 
     * @param pTableNames
     *            List of table names to drop
     * @return Number of tables successfully dropped.
     */
    public int dropTables(List<String> pTableNames);

    /**
     * Drop the specified sequences
     * 
     * @param pSequenceNames
     *            List of sequence names to drop
     */
    public void dropSequences(List<String> pSequenceNames);

    /**
     * Drop all the tables
     * 
     * @return Count of dropped tables
     */
    public int dropAllTables();

    /**
     * Drop all the sequences
     * 
     * @return Count of dropped sequences
     */
    public int dropAllSequences();
}
