1. To run the wordnet example you need a working installation of Wordnet 2.1, which can be obtained from

2. The word vector tool communicates with Wordnet using the Java Wordnet Library. This library uses a xml configuration file to specify where the database is located. You need to provide a path to this file to the word vector using the environment variable wordnet. You can specify this value by passing it to java using the -D option:

java -Dwvtool.wnconfig=<properties_file> WVToolWordNetExample 

An example configuration file can be found in the sample/wordnet directory. For more information about configuring the Java Wordnet Library please refer to http://jwordnet.sourceforge.net. 