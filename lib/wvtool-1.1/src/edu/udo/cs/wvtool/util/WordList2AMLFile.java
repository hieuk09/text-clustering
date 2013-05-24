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

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Writer;

import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * A class that writes a word list as an AML file.
 * Note that you probably need to add a mapping for the id Attribute when
 * using the SparseFormatExampleSource.
 * 
 * @author Michael Wurst
 * @version $Id$
 *
 */
public class WordList2AMLFile {

    public static void storeWordList(WVTWordList wl, Writer outStream, boolean useClass, String dataFileName) {

        PrintWriter out = new PrintWriter(new BufferedWriter(outStream));

        out.println("<attributeset default_source=\"" + dataFileName + "\">");

        for (int i = 0; i < wl.getNumWords(); i++) {
            String word = wl.getWord(i);
            out.println("<attribute");
            out.println("name      = \"" + word + "\"");
            out.println("sourcecol = \"" + (i + 1) + "\"");
            out.println("valuetype = \"real\"");
            out.println("/>");
            out.println();
        }

        out.println("<id");
        out.println("name      = \"id\"");
        out.println("sourcecol = \"" + (wl.getNumWords() + 1) + "\"");
        out.println("valuetype = \"nominal\"");
        out.println("/>");
        out.println();

        if (useClass) {
            out.println("<label");
            out.println("name      = \"label\"");
            out.println("sourcecol = \"" + (wl.getNumWords() + 2) + "\"");
            out.println("valuetype = \"integer\"");
            out.println("/>");
            out.println();
        }

        out.println("</attributeset>");

        out.close();
    }

}
