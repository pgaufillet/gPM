package org.topcased.gpm.business.util.log;

import org.apache.log4j.Level;

/**
 * Defines the minimum set of levels recognized by GPM
 * 
 * @author cmarchiv
 */
public class GPMLevel extends Level
{

    /** The HIGH_INFO code level */
    private static final int HIGH_INFO_INT = Level.INFO_INT + 10;

    /** The MEDIUM_INFO code level */
    private static final int MEDIUM_INFO_INT = Level.INFO_INT;

    /** The LOW_INFO code level */
    private static final int LOW_INFO_INT = Level.DEBUG_INT + 10;

    /** The HIGH_INFO level */
    public static final Level HIGH_INFO = new GPMLevel(HIGH_INFO_INT, "HIGH_INFO", 3);

    /** The MEDIUM_INFO code level */
    public static final Level MEDIUM_INFO = new GPMLevel(MEDIUM_INFO_INT, "MEDIUM_INFO", 2);

    /** The LOW_INFO code level */
    public static final Level LOW_INFO = new GPMLevel(LOW_INFO_INT, "LOW_INFO", 1);

    /**
     * Instantiate a Level object.
     * 
     * @param pLevel The Level.
     * @param pLevelStr the levelStr
     * @param pSyslogEquivalent THe syslogEquivalent
     */
    protected GPMLevel(int pLevel, String pLevelStr, int pSyslogEquivalent)
    {
        super(pLevel, pLevelStr, pSyslogEquivalent);
    }

    /**
     * Convert the string passed as argument to a level. If the conversion
     * fails, then this method returns {@link #DEBUG}.
     */
    public static Level toLevel(String arg)
    {
        return (Level) toLevel(arg, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a level. If the conversion
     * fails, then this method returns {@link #DEBUG}.
     */
    public static Level toLevel(int pVal)
    {
        return (Level) toLevel(pVal, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a level. If the conversion
     * fails, then this method returns the specified default.
     */
    public static Level toLevel(int pVal, Level pDefaultLevel)
    {
        switch (pVal)
        {

            case HIGH_INFO_INT:
                return GPMLevel.HIGH_INFO;
            case MEDIUM_INFO_INT:
                return GPMLevel.MEDIUM_INFO;
            case LOW_INFO_INT:
                return GPMLevel.LOW_INFO;

            default:
                return Level.toLevel(pVal);
        }
    }

    /**
     * Convert the string passed as argument to a level. If the conversion
     * fails, then this method returns the value of <code>defaultLevel</code>.
     */
    public static Level toLevel(String pArg, Level pDefaultLevel)
    {

        if (pArg == null)
            return pDefaultLevel;

        String lStr = pArg.toUpperCase();

        if (lStr.equals("HIGH_INFO"))
        {
            return GPMLevel.HIGH_INFO;
        }
        else if (lStr.equals("MEDIUM_INFO"))
        {
            return GPMLevel.MEDIUM_INFO;
        }
        else if (lStr.equals("LOW_INFO"))
        {
            return GPMLevel.LOW_INFO;
        }
        else
        {
            return Level.toLevel(pArg, pDefaultLevel);
        }
    }

}
