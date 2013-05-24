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

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import websphinx.Crawler;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTInputList;

/**
 * Input list obtained by crawling from a set of initial URLs.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class CrawledInputList implements WVTInputList {

    // private final int numClasses;

    private final List docInfos = new Vector();

    private final WVToolCrawler[] crawlers;

    public CrawledInputList(WVToolCrawler[] crawlers) {

        this.crawlers = crawlers;

        for (int i = 0; i < crawlers.length; i++) {
            configure(crawlers[i]);
            crawlers[i].run();
        }

        for (int i = 0; i < crawlers.length; i++)
            for (Iterator it = crawlers[i].getURLS().keySet().iterator(); it.hasNext();) {
                URL url = (URL) it.next();
                if (crawlers.length > 1)
                    docInfos.add(new WVTDocumentInfo(url.toExternalForm(), "", "", "", i));
                else
                    docInfos.add(new WVTDocumentInfo(url.toExternalForm(), "", "", ""));
            }
    }

    public CrawledInputList(WVToolCrawler crawler) {

        this(new WVToolCrawler[] { crawler });
    }

    public Iterator getEntries() {

        return docInfos.iterator();
    }

    public int getNumClasses() {
        return crawlers.length;
    }

    private void configure(Crawler c) {

        c.setDownloadParameters(c.getDownloadParameters().changeObeyRobotExclusion(true));

    }

}
