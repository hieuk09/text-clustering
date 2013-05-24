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
package edu.udo.cs.wvtool.generic.wordfilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * Filters all words specified in a file. The file must contain one stopword per line.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class StopWordFilterFile extends AbstractStopWordFilter {

    private final Set stopWords = new HashSet();

    public StopWordFilterFile(int minNumChars, Reader stopWordStream) throws IOException {
        super(minNumChars);
        readStopWords(stopWordStream);
    }

    public StopWordFilterFile(int minNumChars) throws IOException {
        super(minNumChars);

        readStopWords(new FileReader(System.getProperty("wvtool.stopwords")));
    }

    public StopWordFilterFile() throws IOException {
        super();
        readStopWords(new FileReader(System.getProperty("wvtool.stopwords")));
    }

    private void readStopWords(Reader stopWordStream) throws IOException {

        BufferedReader in = new BufferedReader(stopWordStream);
        String token = null;
        while ((token = in.readLine()) != null) {
            stopWords.add(token);
        }

        in.close();
    }

    protected boolean isStopword(String t) {

        return stopWords.contains(t);
    }

}
