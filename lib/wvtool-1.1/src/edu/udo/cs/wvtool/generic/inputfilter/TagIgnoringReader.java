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
import java.io.Reader;

import edu.udo.cs.wvtool.external.XmlReader;

/**
 * A reader that ignores alll tags.
 * 
 * @author Stefan Haustein
 * @version $Id: TagIgnoringReader.java,v 1.3 2007/05/20 18:06:04 mjwurst Exp $
 * 
 */
public class TagIgnoringReader extends Reader {

    private XmlReader xr;

    private Reader reader;

    private char[] current;

    private int pos;

    public TagIgnoringReader(Reader reader) throws IOException {
        super();
        this.reader = reader;
        xr = new XmlReader(reader);
        xr.relaxed = true;
        xr.defineCharacterEntity("auml", "\u00E4");
        xr.defineCharacterEntity("ouml", "\u00F6");
        xr.defineCharacterEntity("uuml", "\u00FC");
        xr.defineCharacterEntity("Auml", "\u00C4");
        xr.defineCharacterEntity("Ouml", "\u00D6");
        xr.defineCharacterEntity("Uuml", "\u00DC");
        xr.defineCharacterEntity("nbsp", " ");
        xr.defineCharacterEntity("szlig", "\u00DF");
        current = readText();
    }

    char[] readText() throws IOException {
        while (true) {
            if (xr.getType() == XmlReader.END_DOCUMENT)
                return null;

            if (xr.next() == XmlReader.TEXT)
                return xr.getText().toCharArray();
        }
    }

    public int read() throws IOException {
        if (current == null)
            return -1;

        if (pos >= current.length) {
            current = readText();
            pos = 0;
            if (current == null)
                return -1;
        }

        return current[pos++];
    }

    /**
     * @see java.io.Reader#read(char[], int, int)
     */

    public int read(char[] cbuf, int off, int len) throws IOException {

        if (current == null)
            return -1;

        if (pos >= current.length) {
            current = readText();
            pos = 0;
            if (current == null)
                return -1;
        }

        int count = Math.min(len, current.length - pos);
        System.arraycopy(current, pos, cbuf, off, count);
        pos += count;
        return count;
    }

    /**
     * @see java.io.Reader#close()
     */
    public void close() throws IOException {
        reader.close();
    }

}
