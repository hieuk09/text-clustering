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
package edu.udo.cs.wvtool.generic.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;

import edu.udo.cs.wvtool.main.WVTWordVector;

/**
 * This class represents a mechanism, by which word vectors are stored to a character stream. The format of the file is the following:
 * 
 * <name_of_source1>; <value11> <value12> ... <name_of_source2>; <value21> <value22> ... ...
 * 
 * or, if you chose to store them in a sparse format:
 * 
 * <name_of_source1>; <index>:<value> <index>:<value> ... ...
 * 
 * @author Michael Wurst
 * @version $Id: WordVectorWriter.java,v 1.5 2007/05/20 18:06:05 mjwurst Exp $
 * 
 */
public class WordVectorWriter implements WVTOutputFilter {

    private final PrintWriter out;

    private final boolean sparse;

    private final boolean idAsAttribute;

    private final boolean useLabel;

    private int offset = 0;

    /**
     * Create a new instance of WordVectorFile
     * 
     * @param out the stream to which to write the vectors
     * @param sparse should the vectors be written in sparse format
     * 
     */
    public WordVectorWriter(Writer out, boolean sparse) {

        this(out, sparse, false, false, 0);

    }

    /**
     * Create a new instance of WordVectorFile
     * 
     * @param out the stream to which to write the vectors
     * @param sparse should the vectors be written in sparse format
     * @param idAsAttribute write the id as attribute at the end of each line or in front of the word vector, separated by a semicolon
     * @param useLabel should the label information be used as additional attribute
     */
    public WordVectorWriter(Writer out, boolean sparse, boolean idAsAttribute, boolean useLabel, int offset) {
        super();

        this.idAsAttribute = idAsAttribute;
        this.out = new PrintWriter(new BufferedWriter(out));
        this.sparse = sparse;
        this.useLabel = useLabel;
        this.offset = offset;
    }

    /**
     * @see edu.udo.cs.wvtool.generic.output.WVTOutputFilter#write(WVTWordVector)
     */
    public void write(WVTWordVector wv) {

        String id;
        int cutIndex = wv.getDocumentInfo().getSourceName().lastIndexOf(File.separator);

        if (cutIndex > 0)
            id = wv.getDocumentInfo().getSourceName().substring(cutIndex + 1);
        else
            id = wv.getDocumentInfo().getSourceName();

        int label = wv.getDocumentInfo().getClassValue();

        if (this.out != null) {

            double[] v = wv.getValues();

            if (!idAsAttribute)
                this.out.print(id + ";");
            else if (sparse)
                this.out.print("id:" + id);
            else
                this.out.print(id);

            for (int i = 0; i < v.length; i++)
                if (sparse) {
                    if (v[i] > 0.0) {
                        this.out.print(" " + (i + offset) + ":" + v[i]);
                    }
                } else
                    this.out.print(" " + v[i]);

            if (useLabel)
                this.out.print(" " + label);

            this.out.println();

        }

    }

    public void close() {

        this.out.close();
    }

}
