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
package edu.udo.cs.wvtool.main;

import java.util.Iterator;

/**
 * Represents a set of resources that should be processed by the word vector tool.
 * 
 * @author Michael Wurst
 * @version $Id: WVTInputList.java,v 1.4 2007/05/20 18:06:02 mjwurst Exp $
 * 
 */
public interface WVTInputList {

    /**
     * Return the list of entries
     * 
     * @return enumeration of the lists entries
     */
    public abstract Iterator getEntries();

    /**
     * Returns the number of classes
     * 
     * @return int
     */
    public abstract int getNumClasses();

}
