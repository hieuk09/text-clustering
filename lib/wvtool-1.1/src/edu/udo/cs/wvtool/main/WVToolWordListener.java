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
 * An interface for an algorithm that listens to the documents and words that are processed by the word vector tool. Such an listener can e.g. be passed to the iterateWords method of WVTool.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public interface WVToolWordListener {

    /**
     * Invoked as a new document is opened for processing.
     * 
     * @param docInfo info about the currently processed document
     */
    public void openNewDocument(WVTDocumentInfo docInfo);

    /**
     * Invoked as a word is processed in the current document.
     * 
     * @param word a word to process
     */
    public void processWord(String word);

}
