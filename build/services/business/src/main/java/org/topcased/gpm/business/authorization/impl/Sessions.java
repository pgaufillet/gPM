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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.topcased.gpm.business.exception.InvalidTokenException;

/**
 * Class session
 * 
 * @author llatil
 * @param <S>
 *            Context class of the session
 */
public class Sessions<S extends AbstractContext> {
    /** Map between tokens & contexts. */
    private final Map<String, S> sessionsMap =
            new ConcurrentHashMap<String, S>();

    private ThreadLocal<S> lastUsedSession = new ThreadLocal<S>();

    /** Length (in chars) required for the tokens */
    private static final int TOKEN_LENGTH = 24;

    /** Range of characters usable in a token */
    private static final int TOKEN_CHARS_RANGE = 92;

    /**
     * First character usable in a token (used to avoid any control chars in the
     * token string)
     */
    private static final char TOKEN_FIRST_CHAR = '!';

    /** Random generator singleton */
    private static final Random RANDOM_GENERATOR;
    static {
        try {
            RANDOM_GENERATOR = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Cannot get an instance of the SecureRandom class");
        }
    }

    /**
     * Create a new session
     * 
     * @param pSession
     *            Session context object to store
     * @return The session token.
     */
    protected String create(S pSession) {
        String lToken = pSession.getToken();

        sessionsMap.put(lToken, pSession);
        return lToken;
    }

    /**
     * Create a random token
     * 
     * @return A random token
     */
    protected static String getRandomToken() {
        StringBuilder lGeneratedToken = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int lRandomCharValue =
                    TOKEN_FIRST_CHAR
                            + RANDOM_GENERATOR.nextInt(TOKEN_CHARS_RANGE);
            lGeneratedToken.append((char) lRandomCharValue);
        }

        return lGeneratedToken.toString();
    }

    /**
     * Get the context from a token
     * 
     * @param pToken
     *            Token value
     * @return Context.
     * @throws InvalidTokenException
     *             The token is invalid (blank or not existing session).
     */
    public final S getContext(String pToken) throws InvalidTokenException {
        if (null == pToken) {
            throw new InvalidTokenException("Token is null");
        }

        S lSession = getContextWithoutControl(pToken);

        if (null == lSession) {
            throw new InvalidTokenException("Invalid session token");
        }
        if (!lSession.refresh()) {
            throw new InvalidTokenException("Session expired");
        }
        return lSession;
    }

    private S getContextWithoutControl(String pToken) {
        S lSession = lastUsedSession.get();

        if (null == lSession || !lSession.getToken().equals(pToken)) {
            lSession = sessionsMap.get(pToken);
            lastUsedSession.set(lSession);
        }
        return lSession;
    }

    /**
     * Remove a session
     * 
     * @param pToken
     *            Token value.
     * @return The removed token
     * @throws InvalidTokenException
     *             The token is invalid.
     */
    public S remove(String pToken) throws InvalidTokenException {
        if (null == pToken) {
            throw new InvalidTokenException("Session token is null");
        }
        S lSession = sessionsMap.remove(pToken);
        if (lSession != null) {
            lSession.invalid();
        }
        else {
            throw new InvalidTokenException(pToken,
                    "Invalid token ''{0}'' (or session expired)");
        }
        return lSession;
    }

    /**
     * Check if a given token is valid
     * 
     * @param pAuthToken
     *            Token value
     * @return true if token is valid
     */
    public final boolean isTokenValid(String pAuthToken) {
        if (null == pAuthToken) {
            return false;
        }
        S lSession = getContextWithoutControl(pAuthToken);
        return lSession != null && lSession.isValid();
    }

    /**
     * Return all the session context
     * 
     * @return All the session context
     */
    public Collection<S> getAll() {
        return sessionsMap.values();
    }
}
