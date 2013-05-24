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
package edu.udo.cs.wvtool.generic.wordfilter;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * Abstract call implementing basic functionality for several word filters.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public abstract class AbstractStopWordFilter implements WVTWordFilter, TokenEnumeration {

    /** the token stream from which the characters are read */
    private TokenEnumeration input;

    /**
     * The token, which is currently provided. This buffer is neccessary, to implement the semantic of TokenEnumeration
     */
    private String currentToken;

    private int minNumChars;

    private final static int DEFAULT_MIN_CHARS = 3;

    /**
     * Constructor for StopWordsWrapper.
     */
    public AbstractStopWordFilter(int minNumChars) {
        this.minNumChars = minNumChars;
    }

    /**
     * Constructor for StopWordsWrapper.
     */
    public AbstractStopWordFilter() {
        this(DEFAULT_MIN_CHARS);
    }

    /**
     * @see edu.udo.cs.wvtool.generic.wordfilter.WVTWordFilter#filter(TokenEnumeration, WVTDocumentInfo)
     */
    public TokenEnumeration filter(TokenEnumeration source, WVTDocumentInfo d) throws WVToolException {

        if (source != null) {

            input = source;
            readNextValidToken();
            return this;
        } else
            return null;

    }

    /**
     * @see edu.udo.cs.wvtool.util.TokenEnumeration#hasMoreTokens()
     */
    public boolean hasMoreTokens() {

        if (input != null)
            return (currentToken != null);
        else
            return false;
    }

    /**
     * @see edu.udo.cs.wvtool.util.TokenEnumeration#nextToken()
     */
    public String nextToken() {

        try {

            String result = null;

            // If unequal null, return the current token and read another one
            // from the stream

            if (currentToken != null) {
                result = currentToken;
                readNextValidToken();
            } else
                result = null;

            return result;

        } catch (Exception e) {
            WVToolLogger.getGlobalLogger().logException("Could not read next token", e);
            return null;
        }

    }

    /**
     * Read tokens from the stream until a token is found that is not filtered or the end of the stream is reached.
     */
    private void readNextValidToken() {

        try {

            if (!input.hasMoreTokens()) {
                currentToken = null;
                return;
            }

            // Read tokens until one passes the filter or the end of the stream
            // is reached
            String t = input.nextToken();
            while (input.hasMoreTokens() && (isStopword(t) || (t.length() < minNumChars))) {
                t = input.nextToken();
            }

            // Find out whether the last token was a stop word (both conditions
            // of the loop could have failed at once, so it is not enough to
            // check, whether the end of the stream is reached.
            if (!isStopword(t) && (t.length() >= minNumChars))
                currentToken = t;
            else
                currentToken = null;

        } catch (Exception e) {
            currentToken = null;
            WVToolLogger.getGlobalLogger().logException("Could not read from token stream.", e);
        }

    }

    /**
     * Determines whether the specified word is stopword.
     * 
     * @param t a word
     * @return true, if t is a stopword, false otherwise
     */
    protected abstract boolean isStopword(String t);

    /**
     * Get the minimal number of characters a word must contain to be processed.
     * 
     * @return the minimal number of characters
     */
    public int getMinNumChars() {
        return minNumChars;
    }

    /**
     * Set the minimal number of characters a word must contain to be processed.
     * 
     * @param minNumChars the minimal number of characters
     */
    public void setMinNumChars(int minNumChars) {
        this.minNumChars = minNumChars;
    }

}
