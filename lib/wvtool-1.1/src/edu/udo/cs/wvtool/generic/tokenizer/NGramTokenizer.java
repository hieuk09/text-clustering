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
import java.util.ArrayList;
import java.util.List;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * Creates tokens by creating ngrams of the tokens received from an inner tokenizer.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class NGramTokenizer implements WVTTokenizer, TokenEnumeration {

    /**
     * The token, which is currently provided. This buffer is neccessary, to implement the semantic of TokenEnumeration
     */
    private final List currentTokens;

    private final int n;

    private TokenEnumeration input;

    private final WVTTokenizer tokenizer;

    public NGramTokenizer(int n, WVTTokenizer tokenizer) {

        this.n = n;
        this.tokenizer = tokenizer;
        input = null;
        currentTokens = new ArrayList();
    }

    /**
     * @see edu.udo.cs.wvtool.generic.tokenizer.WVTTokenizer#tokenize(Reader, WVTDocumentInfo)
     */
    public TokenEnumeration tokenize(Reader source, WVTDocumentInfo d) throws WVToolException {

        if (source != null) {

            input = tokenizer.tokenize(source, d);
            readNextToken();
            return this;

        } else
            return null;

    }

    /**
     * Read a token from the character stream and store it into currentToken. If there are no more tokens left store a null value.
     * 
     */
    private void readNextToken() throws WVToolException {

        if (input.hasMoreTokens()) {

            String token = input.nextToken();
            if (token.length() > n) {

                for (int i = 0; i < token.length() - n + 1; i++) {

                    String ngram = token.substring(i, i + n);
                    currentTokens.add(ngram);
                }

            } else
                currentTokens.add(token);
        }

    }

    /**
     * @see edu.udo.cs.wvtool.util.TokenEnumeration#hasMoreTokens()
     */
    public boolean hasMoreTokens() {

        // If the current token does not equal the null value, then there is at
        // least this token left
        if (input != null)
            return (currentTokens.size() > 0);
        else
            return false;
    }

    /**
     * @see edu.udo.cs.wvtool.util.TokenEnumeration#nextToken()
     */
    public String nextToken() throws WVToolException {

        String result = null;

        // If unequal null, return the current token and read another one from
        // the stream

        if (currentTokens.size() > 0) {
            result = (String) currentTokens.get(0);
            currentTokens.remove(0);
            if (currentTokens.size() == 0)
                readNextToken();
        } else
            result = null;

        return result;
    }

}
