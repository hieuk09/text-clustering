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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.udo.cs.wvtool.util.WVToolException;

/**
 * A stemmer that is based on an explicit dictionary containing pairs of terms and base forms. Terms can be described by regular expressions as well. All terms in a text that fullfill a given regular expression are then assigned the user given base form. It is also possible to provide a fallback stemmer that is called if a term is not found in the dictionary.
 * 
 * @author Michael Wurst
 * @version $Id$
 * 
 */
public class DictionaryStemmer extends AbstractStemmer {

    private final Map termMap = new HashMap();

    private final Map regExprList = new LinkedHashMap();

    private final Map additionalMap = new HashMap();

    private final boolean addMappings;

    private final SimpleStemmer fallBackStemmer;

    public DictionaryStemmer() {

        super();

        fallBackStemmer = null;
        addMappings = false;
    }

    public DictionaryStemmer(Reader in_) throws IOException {
        this(in_, null, false);
    }

    /**
     * 
     * @param in_ a stream to the dictionary file
     * @param stemmer a fallback stemmer that is applied, if a word is not found in the dictionary
     * @param addMappings if true, mappings creates implicitely by the fallback stemmer are added and can later be stored.
     * @throws IOException
     */
    public DictionaryStemmer(Reader in_, SimpleStemmer stemmer, boolean addMappings) throws IOException {

        super();

        this.addMappings = addMappings;
        fallBackStemmer = stemmer;
        BufferedReader in = new BufferedReader(in_);
        String buf = null;
        while ((buf = in.readLine()) != null) {
            StringTokenizer tokens = new StringTokenizer(buf, ": ");
            if (tokens.hasMoreTokens()) {
                String target = tokens.nextToken();
                while (tokens.hasMoreTokens()) {
                    String term = tokens.nextToken();
                    if (!containsLettersOnly(term)) {
                        addRegularExpression(term, target);
                    } else
                        termMap.put(term, target);
                }
            }
        }

        in.close();
    }

    private boolean containsLettersOnly(String s) {

        boolean result = true;

        for (int i = 0; (i < s.length()) && result; i++)
            if (!Character.isLetter(s.charAt(i)))
                result = false;

        return result;
    }

    public String getBase(String s) throws WVToolException {

        String sLower = s.toLowerCase();
        String base = (String) termMap.get(sLower);
        if (base != null) {
            return base;
        }

        Iterator it = regExprList.keySet().iterator();
        while (it.hasNext() && (base == null)) {

            Pattern pattern = (Pattern) it.next();
            Matcher matcher = pattern.matcher(sLower);
            if (matcher.matches())
                base = (String) regExprList.get(pattern);
        }

        if ((base == null) && (fallBackStemmer != null)) {
            base = fallBackStemmer.getBase(sLower);
            if ((base != null) && (addMappings))
                additionalMap.put(sLower, base);
        }

        if (base == null)
            base = sLower;

        return base;
    }

    public void addTermMapping(String term, String base) {

        if (termMap.get(term) == null)
            termMap.put(term.toLowerCase(), base.toLowerCase());
    }

    public void addRegularExpression(String regExprStr, String base) {

        Pattern pattern = Pattern.compile(regExprStr);
        regExprList.put(pattern, base.toLowerCase());

    }

    public void writeAddedMappings(Writer out_) {

        PrintWriter out = new PrintWriter(out_);
        Iterator it = additionalMap.keySet().iterator();

        Map collector = new HashMap();
        while (it.hasNext()) {

            String term = (String) it.next();
            String base = (String) additionalMap.get(term);
            List list = (List) collector.get(base);
            if (list == null) {
                list = new LinkedList();
                collector.put(base, list);
            }
            list.add(term);
        }

        Iterator it2 = collector.keySet().iterator();
        while (it2.hasNext()) {

            String base = (String) it2.next();
            List list = (List) collector.get(base);
            out.print(base + ":");
            Iterator it3 = list.iterator();
            while (it3.hasNext()) {
                out.print(it3.next() + " ");
            }

            out.println();

        }

        out.close();
    }
}
