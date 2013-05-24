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
package edu.udo.cs.wvtool.crawler;

import java.util.HashMap;
import java.util.Map;

import websphinx.Crawler;
import websphinx.Page;

/**
 * An abstract class that must be overridden by all specialized crawlers that are used to construct a crawled input list.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public abstract class WVToolCrawler extends Crawler {

    private final Map urlsToVectorize = new HashMap();

    /** the MIME content type of the crawled documents */
    public String contentType = "";

    /** the encoding of the crawled document */
    public String contentEncoding = "";

    /** the language the documents are written in (english, german, ...) */
    public String contentLanguage = "";

    public WVToolCrawler(String encoding, String language, String type) {
        super();
        contentEncoding = encoding;
        contentLanguage = language;
        contentType = type;
    }

    public void visit(Page page) {

        if (vectorizePage(page)) {
            urlsToVectorize.put(page.getURL(), page);
        } else
            page.discardContent();
    }

    protected abstract boolean vectorizePage(Page page);

    public final Map getURLS() {

        return urlsToVectorize;
    }

}
