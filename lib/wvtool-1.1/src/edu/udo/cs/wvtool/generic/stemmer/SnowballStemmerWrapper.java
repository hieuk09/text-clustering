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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.tartarus.snowball.SnowballProgram;

import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;

/**
 * Wrapper for the snowball stemmer package.
 * 
 * @author Michael Wurst
 * @version $Id: SnowballStemmerWrapper.java,v 1.7 2007/05/20 18:06:02 mjwurst Exp $
 * 
 */
public class SnowballStemmerWrapper extends AbstractStemmer {

    /** the stemmer itself */
    private SnowballProgram stemmer;

    private Method stemMethod;

    private static final String DEFAULT_LANGUAGE = "english";

    /**
     * Constructor for SnowballStemmerWrapper.
     */
    public SnowballStemmerWrapper() throws WVToolException {
        super();
        setLanguage(DEFAULT_LANGUAGE);
    }

    public TokenEnumeration stem(TokenEnumeration source, WVTDocumentInfo d) throws WVToolException {

        super.stem(source, d);
        setLanguage(d.getContentLanguage());

        return this;

    }

    private void setLanguage(String language) throws WVToolException {

        try {

            Class stemClass = Class.forName("org.tartarus.snowball.ext." + language + "Stemmer");
            stemmer = (SnowballProgram) stemClass.newInstance();

            stemMethod = stemClass.getMethod("stem", new Class[0]);

        } catch (Exception e) {
            throw new WVToolException("Could not initialize the snowball stemmer for the specified language.", e);
        }

    }

    public String getBase(String s) throws WVToolException {
        if (s != null) {
            String s2 = s.toLowerCase();
            stemmer.setCurrent(s2);

            try {
                stemMethod.invoke(stemmer, new Object[0]);
            } catch (IllegalAccessException e) {

                throw new WVToolException("Could not stem word " + s, e);

            } catch (InvocationTargetException e) {
                throw new WVToolException("Could not stem word " + s, e);

            }

            return stemmer.getCurrent();
        } else
            return null;

    }
}
