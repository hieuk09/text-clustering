/*
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 *  USA.
 */
package edu.udo.cs.wvtool.external;

import java.util.HashSet;

import edu.udo.cs.wvtool.generic.wordfilter.AbstractStopWordFilter;

/**
 * Stopword Filter for french text documents.
 * 
 * @author jean-charles douet
 * @version $Id$
 *
 */
public class StopwordsFrench extends AbstractStopWordFilter {

    /** The hashtable containing the list of stopwords */
    private static HashSet m_Stopwords = null;

    private static String[] stopWords = new String[] { "à","afin","aucun","auquel","autre",
        "autres","aux","auxquelles","auxquels","avec",
        "beaucoup","ca","car","ce","ceci","cela","celle","celles","celui","ces",
        "c","cette","ceux","chaque","chez","ci","combien","comme","comment","d",
        "dans","de","depuis","des","desquelles","desquels","donc","dont","du","duquel",
        "elle","elles","en","encore","entre","et","etc","hors","il","ils",
        "jamais","je","jusqu","jusque","la","là","meme","même","laquelle","le",
        "leur","mien","nôtre","sien","tien","vôtre","lequel","lequels",
        "les","mêmes","lesquelles","leurs","mais","ne","ni","nous",
        "on","or","ou","par","parce","pas","peu","plus","plusieurs","point",
        "pour","pourquoi","puisque","quand","quant","que","quel","qui","quoi","sa",
        "sans","ses","si","soit","son","sous","souvent","sur","toujours","tous",
        "tres","tu","une","vers","vous","y","aspect","chercher","comparaison","correspondance",
        "correspondre","difference","different","equivalence","être","exister","expliquer","faire","fait","general",
        "idee","identique","lien","lier","particulier","probleme","rapport","recherche","relation","representer",
        "sembler","signifier","specifique","structure","voir","a","ai","aie","aient","ait",
        "aura","auraient","aurait","auront","avaient","avait","avoir","ayant","correspond","est",
        "etaient","etait","ete","eu","eue","eurent","eut","existe","existent","explique",
        "faite","faites","faits","furent","fussent","fut","ont","peut",
        "peuvent","recherchent","represente","representent","semble","semblent","sera","serai",
        "seraient","serait", "seras","seront","signifie","signifient","soient","sont" };

    static {
        if (m_Stopwords == null) {
            m_Stopwords = new HashSet();
            
            for (int i = 0; i < stopWords.length; i++) {
                m_Stopwords.add(stopWords[i]);
            }
        }

    }

    public boolean isStopword(String str) {

        return m_Stopwords.contains(str.toLowerCase());
    }

}
