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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * Input filter for PDF documents, based on the PDFBox library.
 * 
 * @author Michael Wurst
 * @version $Id$
 *
 */
public class PDFInputFilter implements WVTInputFilter {

    public Reader convertToPlainText(InputStream source, WVTDocumentInfo d) {

        String plainText = null;
        try {
            PDDocument document = PDDocument.load(source);
            PDFTextStripper stripper = new PDFTextStripper();
            plainText = stripper.getText(document);
            document.close();
        } catch (IOException e) {
            WVToolLogger.getGlobalLogger().logException("Could not read or convert PDF Document", e);
            plainText = new String();
        }

        return new StringReader(plainText);
    }

}
