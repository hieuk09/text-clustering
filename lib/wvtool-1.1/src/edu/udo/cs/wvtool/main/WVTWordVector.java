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

/**
 * Represents an individual word vector in non-sparse form. Along with the vector intself, the corresponding document information is referenced.
 * 
 * @author Michael Wurst
 * @version $Id: WVTWordVector.java,v 1.3 2007/05/20 18:06:02 mjwurst Exp $
 * 
 */
public class WVTWordVector {

    /** the values */
    private double[] values = null;

    /** reference to the document information */
    private WVTDocumentInfo documentInfo = null;

    /**
     * Returns the documentInfo.
     * 
     * @return WVTDocumentInfo
     */
    public WVTDocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    /**
     * Returns the values.
     * 
     * @return double[]
     */
    public double[] getValues() {
        return values;
    }

    /**
     * Sets the documentInfo.
     * 
     * @param documentInfo The documentInfo to set
     */
    public void setDocumentInfo(WVTDocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
    }

    /**
     * Sets the values.
     * 
     * @param values The values to set
     */
    public void setValues(double[] values) {
        this.values = values;
    }

}
