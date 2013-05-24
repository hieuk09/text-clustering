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
package edu.udo.cs.wvtool.generic.stemmer;

import org.tartarus.snowball.ext.LovinsStemmer;

/**
 * Wrapper for the lovins stemmer.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class SnowballLovinsStemmer extends AbstractStemmer {

    /** the stemmer itself */
    private final LovinsStemmer stemmer = new LovinsStemmer();

    public String getBase(String s) {
        // System.out.print(s+":");
        stemmer.setCurrent(s);
        stemmer.stem();
        // System.out.println(stemmer.getCurrent());
        return stemmer.getCurrent();
    }

}
