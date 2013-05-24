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

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * This class represents a list of information items describing the documents, from which a word list or word vectors should be created.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class WVTFileInputList implements WVTInputList {

    /** the list of input files */
    private List inputList = null;

    /** the number of classes */
    private int numClasses;

    /**
     * Creates a new empty instance of WVTInputList
     * 
     * @param numClasses the number of class values, 0 if no classes are provided
     */
    public WVTFileInputList(int numClasses) {

        inputList = new ArrayList();
        this.numClasses = numClasses;
    }

    /**
     * Creates a new instance of WVTInputList by reading a XML file
     * 
     * @param numClasses the number of class values
     * @param xmlSource the name
     */
    public WVTFileInputList(int numClasses, Reader xmlSource) {

        // TODO
    }

    /**
     * Return the list of entries
     * 
     * @param expanded should the list be expanded? (directoriy entries are converted to a list of files)
     * @return enumeration of the lists entries
     */
    public Iterator getEntries(boolean expanded) {

        if (!expanded)
            return inputList.iterator();

        else {
            Iterator listElements = inputList.iterator();
            List expandedElements = new ArrayList();

            // Go through all entries and check, whether they point to
            // directories
            while (listElements.hasNext()) {
                WVTDocumentInfo docInfo = (WVTDocumentInfo) listElements.next();
                File inputFile = new File(docInfo.getSourceName());

                if (inputFile.exists()) {
                    if (inputFile.isDirectory()) {
                        // If a directory is found get all files contained in it
                        // and add them to the expanded list
                        File fileList[] = inputFile.listFiles();
                        for (int i = 0; i < fileList.length; i++) {
                            // When creating a new entry, preserve all
                            // information but the
                            // location of the file
                            if (fileList[i].isFile()) {
                                if (docInfo.hasClassValue()) {
                                    expandedElements.add(new WVTDocumentInfo(fileList[i].getAbsolutePath(), docInfo.getContentType(), docInfo.getContentEncoding(), docInfo.getContentLanguage(), docInfo.getClassValue()));
                                } else {
                                    expandedElements.add(new WVTDocumentInfo(fileList[i].getAbsolutePath(), docInfo.getContentType(), docInfo.getContentEncoding(), docInfo.getContentLanguage()));
                                }
                            }
                        }
                    } else {
                        expandedElements.add(docInfo);
                    }
                } else {
                    WVToolLogger.getGlobalLogger().logMessage("File " + inputFile.getAbsolutePath() + " not found. Assuming the text is directly encoded as document source...", WVToolLogger.WARNING);
                    expandedElements.add(docInfo);
                }
            }

            return expandedElements.iterator();
        }

    }

    /**
     * Add an entry to the list.
     * 
     * @param d the entry
     */
    public void addEntry(WVTDocumentInfo d) {

        inputList.add(d);

    }

    /**
     * Remove an entry from the list.
     * 
     * @param d the entry
     */
    public void removeEntry(WVTDocumentInfo d) {

        inputList.remove(d);

    }

    /**
     * Returns the numClasses.
     * 
     * @return int
     */
    public int getNumClasses() {
        return numClasses;
    }

    public Iterator getEntries() {

        return getEntries(true);
    }

}
