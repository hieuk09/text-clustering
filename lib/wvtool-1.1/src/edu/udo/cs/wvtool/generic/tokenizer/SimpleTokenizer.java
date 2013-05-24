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
package edu.udo.cs.wvtool.generic.tokenizer;

import java.io.Reader;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;

/**
 * This class implements a simple tokenizer. All characters for which Character.isLetter() returns false, are considered to be seperators and are removed.
 * 
 * @author Michael Wurst
 * @version $Id: SimpleTokenizer.java,v 1.3 2007/05/20 18:06:02 mjwurst Exp $
 * 
 */

public class SimpleTokenizer implements WVTTokenizer, TokenEnumeration {

    /** The underlying character stream of the currently tokenized document */
    private Reader input;

    /**
     * The token, which is currently provided. This buffer is neccessary, to implement the semantic of TokenEnumeration
     */
    private String currentToken;

    public SimpleTokenizer() {

        input = null;
        currentToken = null;
    }

    /**
     * @see edu.udo.cs.wvtool.generic.tokenizer.WVTTokenizer#tokenize(Reader, WVTDocumentInfo)
     */
    public TokenEnumeration tokenize(Reader source, WVTDocumentInfo d) {

        if (source != null) {

            input = source;
            readNextToken();
            return this;

        } else
            return null;

    }

    /**
     * Read a token from the character stream and store it into currentToken. If there are no more tokens left store a null value.
     * 
     */
    private void readNextToken() {

        StringBuffer buf = new StringBuffer();
        boolean endReached = false;
        int in = 0;

        try {

            // Read from the stream, until a letter occurs

            in = input.read();
            char ch = (char) in;

            while ((in != -1) && !Character.isLetter(ch)) {
                in = input.read();
                ch = (char) in;
            }

            if (in != -1)
                buf.append(ch);

            // Read from the stream, util a non-letter occurs

            while ((in != -1) && Character.isLetter(ch)) {

                in = input.read();
                ch = (char) in;

                if (Character.isLetter(ch))
                    buf.append(ch);

            }
        } catch (Exception e) {
            endReached = true;

        }

        if (in == -1)
            endReached = true;

        if (endReached) {

            // If the stream ended with a non-empty token, this is the last
            // token, otherwise there is no more token.

            if (buf.length() > 0)
                currentToken = buf.toString();
            else
                currentToken = null;

            return;
        } else {

            // if the end of the stream has not been reached yet, simply store
            // the extracted token.
            currentToken = buf.toString();
            return;
        }

    }

    /**
     * @see edu.udo.cs.wvtool.util.TokenEnumeration#hasMoreTokens()
     */
    public boolean hasMoreTokens() {

        // If the current token does not equal the null value, then there is at
        // least this token left
        if (input != null)
            return (currentToken != null);
        else
            return false;
    }

    /**
     * @see edu.udo.cs.wvtool.util.TokenEnumeration#nextToken()
     */
    public String nextToken() {

        String result = null;

        // If unequal null, return the current token and read another one from
        // the stream

        if (currentToken != null) {
            result = currentToken;
            readNextToken();
        } else
            result = null;

        return result;
    }

}
