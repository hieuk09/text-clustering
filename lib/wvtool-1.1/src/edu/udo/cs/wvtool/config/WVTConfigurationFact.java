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
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * Class used to simplify the process of creating rules, in cases, in which simply a constant value is returned.
 * 
 * @author Michael Wurst
 * @version $Id: WVTConfigurationFact.java,v 1.3 2007/05/20 18:06:03 mjwurst Exp $
 * 
 */
public class WVTConfigurationFact implements WVTConfigurationRule {

    /** the name of the class from which to create an object as component */
    private String className;

    /** the object that represents the component */
    private final Object component;

    /**
     * Constructor for a configuration fact.
     * 
     * @param className the name of the class, which represents the component
     */
    public WVTConfigurationFact(String className) {

        this.className = className;
        this.component = null;
    }

    /**
     * Constructor for a configuration fact.
     * 
     * @param component the component to return
     */
    public WVTConfigurationFact(Object component) {

        this.component = component;
    }

    /**
     * @see edu.udo.cs.wvtool.config.WVTConfigurationRule#getMatchingComponent(WVTDocumentInfo)
     */
    public Object getMatchingComponent(WVTDocumentInfo d) {

        Object result = null;

        // If directly the component is stored, return it

        if (component != null)
            return component;

        else {

            // Otherwise create it according to the class name provided.

            try {
                result = Class.forName(className).newInstance();
            } catch (Exception e) {
                WVToolLogger.getGlobalLogger().logException("Could not initialize step.", e);
                result = null;
            }

            return result;

        }
    }

}
