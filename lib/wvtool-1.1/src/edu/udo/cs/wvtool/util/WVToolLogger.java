  /*
  WVTool - Word Vector Tool
  Copyright (C) 2001-2007

	    Michael Wurst       

  web:   http://wvtool.sourceforge.net

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License as 
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version. 

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA.
*/
package edu.udo.cs.wvtool.util;

/**
 * Singelton logging class. The log should be invoked by first calling WVToolLogger.getGlobalLogger() and then to invoke the logging operations on this object.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public abstract class WVToolLogger {

    public static final int STATUS = 0;

    public static final int WARNING = 1;

    public static final int EXCEPTION = 2;

    private static WVToolLogger logger = new StdOutLogger();

    private static int logLevel = EXCEPTION;

    /**
     * Log a message, if the current log level is equal or higher than the one of the message.
     * 
     * @param s the message
     * @param level the loglevel of the message
     */
    public abstract void logMessage(String s, int level);

    /**
     * Log an exception.
     * 
     * @param s some additional message
     * @param e the exception to log
     */
    public void logException(String s, Exception e) {

        logger.logMessage(s + ":" + e, EXCEPTION);
    }

    /**
     * Set the log level.
     * 
     * @param minLogLevel the minimal log level
     */
    public synchronized void setLogLevel(int minLogLevel) {

        logLevel = minLogLevel;
    }

    /**
     * Get the current log level.
     * 
     * @return the log level
     */
    public synchronized int getLogLevel() {

        return logLevel;
    }

    /**
     * Get the global logging instance.
     * 
     * @return the current logger
     */
    public static synchronized WVToolLogger getGlobalLogger() {

        return logger;
    }

    /**
     * Set the global logging instance.
     * 
     * @param logger the logger
     */
    public static synchronized void setGlobalLogger(WVToolLogger logger) {
        WVToolLogger.logger = logger;
    }
}
