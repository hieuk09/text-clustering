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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * Class that dynamically selects the word filter according to the selected language. Per default, german and english are supported.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class SelectingWordFilter extends AbstractStopWordFilter {

    // All classes added to the map must be subclasses of AbstractWordFilter!!!!
    private final Map stopWordWrapperMap;

    public SelectingWordFilter() {
        super();

        stopWordWrapperMap = new HashMap();

        StopWordsWrapper englishWrapper = new StopWordsWrapper();
        StopWordsWrapperGerman germanWrapper = new StopWordsWrapperGerman();

        stopWordWrapperMap.put("english", englishWrapper);
        stopWordWrapperMap.put("german", germanWrapper);
        stopWordWrapperMap.put("English", englishWrapper);
        stopWordWrapperMap.put("German", germanWrapper);

    }

    public SelectingWordFilter(Map stopWordWrapperMap) {
        super();

        this.stopWordWrapperMap = stopWordWrapperMap;
    }

    public void setMinNumChars(int minNumChars) {

        Iterator it = stopWordWrapperMap.keySet().iterator();
        while (it.hasNext()) {
            ((AbstractStopWordFilter) it.next()).setMinNumChars(minNumChars);
        }
    }

    public TokenEnumeration filter(TokenEnumeration source, WVTDocumentInfo d) throws WVToolException {

        WVTWordFilter filter = (WVTWordFilter) stopWordWrapperMap.get(d.getContentLanguage());
        if (filter == null)
            return source;
        else
            return filter.filter(source, d);
    }

    protected boolean isStopword(String t) {

        return true;
    }

}
