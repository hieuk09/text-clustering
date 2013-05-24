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
package edu.udo.cs.wvtool.config;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import edu.udo.cs.wvtool.generic.charmapper.DummyCharConverter;
import edu.udo.cs.wvtool.generic.inputfilter.SelectingInputFilter;
import edu.udo.cs.wvtool.generic.loader.UniversalLoader;
import edu.udo.cs.wvtool.generic.stemmer.LovinsStemmerWrapper;
import edu.udo.cs.wvtool.generic.tokenizer.SimpleTokenizer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.generic.wordfilter.StopWordsWrapper;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.util.WVToolLogger;

/**
 * WVTool Configuration. The configuration consists of a set of rules that determine which components to use for each step, dependend on given information about the document. This allows to dynamically choose, e.g. a tokenizer, depending whether the document was read from PDF or from plain text. Rules have no special structure, despite that they take document information as input and return an Object representing the component to be used.
 * 
 * A shortcut for cases where always the same component is used, independently of the document, is the WVTConfigurationFact.
 * 
 * Configuration rules can be provided in three ways:
 * 
 * 1. A simple standard configuration can be used 2. The configuration can be read from a stream (not yet implemented) 3. Setter Methods can be used to create the configuration manually from Java code
 * 
 * @author Michael Wurst
 * @version $Id: WVTConfiguration.java,v 1.6 2007/05/22 16:39:58 mjwurst Exp $
 * 
 */
public class WVTConfiguration {

    /* constants to label the individual steps */
    public static final String STEP_INPUT_FILTER = "inputfilter";

    public static final String STEP_CHAR_MAPPER = "charmapper";

    public static final String STEP_LOADER = "loader";

    public static final String STEP_TOKENIZER = "tokenizer";

    public static final String STEP_WORDFILTER = "wordfilter";

    public static final String STEP_STEMMER = "stemmer";

    public static final String STEP_VECTOR_CREATION = "vectorcreation";

    public static final String STEP_OUTPUT = "output";

    /* constants that define the file type */

    public static final int TYPE_TEXT = 0;

    public static final int TYPE_XML = 1;

    public static final int TYPE_HTML = 2;

    public static final int TYPE_PDF = 3;

    /** data structure to store the rules for the individual steps */
    private Map ruleSet = null;

    /**
     * Creates a new instance of WVTConfiguration by reading a configuration from a stream.
     * 
     * @param in the stream, from which to read the configuration
     */
    public WVTConfiguration(Reader in) {

        // TODO
    }

    /**
     * Creates a new instance of WVTConfiguration, setting up a standard configuration
     */
    public WVTConfiguration() {

        ruleSet = new HashMap();

        setConfigurationRule(STEP_INPUT_FILTER, new WVTConfigurationFact(new SelectingInputFilter()));

        setConfigurationRule(STEP_LOADER, new WVTConfigurationFact(new UniversalLoader()));
        // setConfigurationRule(
        // STEP_INPUT_FILTER,
        // new WVTConfigurationFact(new TextInputFilter()));
        setConfigurationRule(STEP_CHAR_MAPPER, new WVTConfigurationFact(new DummyCharConverter()));
        setConfigurationRule(STEP_TOKENIZER, new WVTConfigurationFact(new SimpleTokenizer()));
        setConfigurationRule(STEP_WORDFILTER, new WVTConfigurationFact(new StopWordsWrapper()));
        setConfigurationRule(STEP_STEMMER, new WVTConfigurationFact(new LovinsStemmerWrapper()));
        setConfigurationRule(STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));
        setConfigurationRule(STEP_OUTPUT, new WVTConfigurationFact(null));

    }

    /**
     * Set a rule for a given step.
     * 
     * @param step the name of the step
     * @param rule the rule for this step
     */
    public final void setConfigurationRule(String step, WVTConfigurationRule rule) {

        ruleSet.put(step, rule);

    }

    /**
     * Get the object to use in a given step according to given document informations.
     * 
     * @param step the name of the step
     * @param info the document information
     * @return the object to use
     */
    public final Object getComponentForStep(String step, WVTDocumentInfo info) {

        try {

            WVTConfigurationRule rule = (WVTConfigurationRule) ruleSet.get(step);

            if (rule == null)
                return null;

            return rule.getMatchingComponent(info);

        } catch (Exception e) {
            WVToolLogger.getGlobalLogger().logException("Could not initialize step.", e);
            return null;
        }
    }

    /**
     * Determines the type of file using some heuristics.
     * 
     * @param info the document info
     * @return an int representing a file type
     */
    public static int determineType(WVTDocumentInfo info) {

        String typeStr = info.getContentType();
        if (typeStr.length() == 0) {

            String sourceName = info.getSourceName();
            int index = sourceName.lastIndexOf('.');
            if (index >= 0)
                typeStr = sourceName.substring(index + 1);
        }

        if (typeStr.equalsIgnoreCase("htm"))
            typeStr = "html";

        if (typeStr.equalsIgnoreCase("pdf"))
            return TYPE_PDF;

        if (typeStr.equalsIgnoreCase("html"))
            return TYPE_HTML;

        if (typeStr.equalsIgnoreCase("xml"))
            return TYPE_XML;

        return TYPE_TEXT;

    }

}
