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
 * Represents relevant information about a document.
 * 
 * @author Michael Wurst
 * @version $Id: WVTDocumentInfo.java,v 1.3 2007/05/20 18:06:02 mjwurst Exp $
 * 
 */
public class WVTDocumentInfo {

    /**
     * the source of the document as String. This can be either a path to a directory/file or an URL.
     */
    public String sourceName = "";

    /** the MIME content type of the document */
    public String contentType = "";

    /** the encoding of the document */
    public String contentEncoding = "";

    /** the language the document is written in (english, german, ...) */
    public String contentLanguage = "";

    /** the class value, which is assigned to the document. */
    public int classValue = -1;

    /** has the document a class value assigened to it? */
    private final boolean hasClassValue;

    /** Creates a new instance of WVTDocumentInfo */
    public WVTDocumentInfo(String sourceName, String contentType, String contentEncoding, String contentLanguage, int classValue) {

        if (sourceName != null)
            this.sourceName = sourceName;

        if (contentEncoding != null)
            this.contentEncoding = contentEncoding;

        if (contentLanguage != null)
            this.contentLanguage = contentLanguage;

        if (contentType != null)
            this.contentType = contentType;

        this.classValue = classValue;
        hasClassValue = true;
    }

    /** Creates a new instance of WVTDocumentInfo */
    public WVTDocumentInfo(String sourceName, String contentType, String contentEncoding, String contentLanguage) {

        if (sourceName != null)
            this.sourceName = sourceName;

        if (contentEncoding != null)
            this.contentEncoding = contentEncoding;

        if (contentLanguage != null)
            this.contentLanguage = contentLanguage;

        if (contentType != null)
            this.contentType = contentType;

        hasClassValue = false;

    }

    /**
     * Getter for property contentLanguage.
     * 
     * @return Value of property contentLanguage.
     */
    public java.lang.String getContentLanguage() {
        return contentLanguage;
    }

    /**
     * Getter for property contentEncoding.
     * 
     * @return Value of property contentEncoding.
     */
    public java.lang.String getContentEncoding() {
        return contentEncoding;
    }

    /**
     * Getter for property contentType.
     * 
     * @return Value of property contentType.
     */
    public java.lang.String getContentType() {
        return contentType;
    }

    /**
     * Getter for property sourceName.
     * 
     * @return value of sourceName.
     */
    public java.lang.String getSourceName() {
        return sourceName;
    }

    /**
     * Getter for property classValue.
     * 
     * @return class value (-1, if not set).
     */
    public int getClassValue() {
        return classValue;
    }

    /**
     * has the document a class value assigned to it. a boolean
     */
    public boolean hasClassValue() {

        return hasClassValue;

    }

    public int hashCode() {
        return (new String(contentType + contentEncoding + contentEncoding + sourceName + classValue)).hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof WVTDocumentInfo) {

            WVTDocumentInfo obj2 = (WVTDocumentInfo) obj;
            return (obj2.getSourceName().equals(this.getSourceName()) && obj2.getContentEncoding().equals(this.getContentEncoding()) && obj2.getContentLanguage().equals(this.getContentLanguage()) && obj2.getContentType().equals(this.getContentType()) && (obj2.getClassValue() == this.getClassValue()));

        } else
            return false;
    }
}
