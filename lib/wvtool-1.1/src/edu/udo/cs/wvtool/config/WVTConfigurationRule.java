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
package edu.udo.cs.wvtool.config;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;

/**
 * This interface abstracts from rules, used to select an appropriate component for a given document. The input to a rule is always information about a document being processed, the output is the component to use for this document. For each step, an individual rule has to be specified, though on an abstract level, they all have the same interface.
 * 
 * @author Michael Wurst
 * @version $Id: WVTConfigurationRule.java,v 1.3 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public interface WVTConfigurationRule {

    /**
     * Get a component object for a given document info
     * 
     * @param d information about a document
     * @return the object to use for the document
     */
    public Object getMatchingComponent(WVTDocumentInfo d) throws WVTConfigException;

}
