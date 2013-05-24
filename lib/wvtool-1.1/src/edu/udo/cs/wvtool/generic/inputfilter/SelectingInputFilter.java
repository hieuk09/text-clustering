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
package edu.udo.cs.wvtool.generic.inputfilter;

import java.io.InputStream;
import java.io.Reader;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * <p>InputFilter that automatically selects an apropriate filter according to the
 * the file ending. The following rules are used.</p>
 * <ul>
 * <li> HTM, HTML, htm, html -> SimpleTagIgnoringReader</li>
 * <li> XML, xml -> XMLInputFilter</li>
 * <li> PDF, pdf -> PDFInputFilter</li>
 * <li> all other -> TextInputFilter</li>
 * </ul>
 * @author Michael Wurst
 * @version $Id$
 *
 */
public class SelectingInputFilter implements WVTInputFilter {

    private WVTInputFilter pdfFilter = null;

    private WVTInputFilter textFilter = null;

    private WVTInputFilter xmlFilter = null;

    private WVTInputFilter htmlFilter = null;

    public Reader convertToPlainText(InputStream source, WVTDocumentInfo d) throws WVToolException {

        // Determine the type
        int type = WVTConfiguration.determineType(d);

        switch (type) {

        case WVTConfiguration.TYPE_PDF:
            
            if(pdfFilter == null)
                pdfFilter = new PDFInputFilter();
            
            return pdfFilter.convertToPlainText(source, d);

        case WVTConfiguration.TYPE_XML:
            
            if(xmlFilter == null)
                xmlFilter = new XMLInputFilter();
            
            return xmlFilter.convertToPlainText(source, d);

        case WVTConfiguration.TYPE_HTML:
            
            if(htmlFilter == null)
                htmlFilter = new SimpleTagIgnoringReader();
            
            return htmlFilter.convertToPlainText(source, d);

        default:
            
            if(textFilter == null)
                textFilter = new TextInputFilter();
            
            return textFilter.convertToPlainText(source, d);

        }

    }

}
