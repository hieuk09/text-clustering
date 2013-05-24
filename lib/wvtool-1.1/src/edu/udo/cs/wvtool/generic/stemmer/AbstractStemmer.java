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
package edu.udo.cs.wvtool.generic.stemmer;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * An abstract stemmer.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public abstract class AbstractStemmer implements WVTStemmer, SimpleStemmer, TokenEnumeration {

    private TokenEnumeration source;

    public TokenEnumeration stem(TokenEnumeration source, WVTDocumentInfo d) throws WVToolException {

        this.source = source;

        return this;
    }

    public String nextToken() throws WVToolException {

        if (source.hasMoreTokens())
            return getBase(source.nextToken());
        else
            return null;
    }

    public abstract String getBase(String s) throws WVToolException;

    public boolean hasMoreTokens() {

        return source.hasMoreTokens();
    }

}
