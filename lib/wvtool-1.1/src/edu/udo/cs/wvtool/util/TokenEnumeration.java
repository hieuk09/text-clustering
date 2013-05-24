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
 * Interface for an enumeration of tokens. If an error occurs during the execution of nextToken, an Exceptions is thrown.
 * 
 * @author Michael Wurst
 * @version $Id: TokenEnumeration.java,v 1.3 2007/05/20 18:06:04 mjwurst Exp $
 * 
 */
public interface TokenEnumeration {

    /**
     * Return the next token from the stream.
     * 
     * @return a String value, or null if there are no more tokens
     */
    public String nextToken() throws WVToolException;

    /**
     * Determine whether there are tokens left in the Enumeration. If an error occurs, false is returned.
     * 
     * @return a <code>boolean</code> value
     */
    boolean hasMoreTokens();

}
