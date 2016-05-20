package iris;

import org.deri.iris.Configuration;
import org.deri.iris.KnowledgeBase;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.compiler.Parser;
import org.deri.iris.optimisations.magicsets.MagicSets;
import org.deri.iris.storage.IRelation;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An example program created for graph path evaluation.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Please give directory path.");
            System.exit(-1);
        }

        Parser parser = new Parser();

        final String projectDirectory = args[0];
        Map<IPredicate, IRelation> factMap = new HashMap<>();

        /** The following loop -- given a project directory -- will list and read parse all fact files in its "/facts"
         *  subdirectory. This allows you to have multiple .iris files with your program facts. For instance you can
         *  have one file for each relation's facts as our examples show.
         */
        final File factsDirectory = new File(projectDirectory + "/facts");
        if (factsDirectory.isDirectory()) {
            for (final File fileEntry : factsDirectory.listFiles()) {

                if (fileEntry.isDirectory())
                    System.out.println("Omitting directory " + fileEntry.getPath());

                else {
                    Reader factsReader = new FileReader(fileEntry);
                    parser.parse(factsReader);

                    // Retrieve the facts and put all of them in factMap
                    factMap.putAll(parser.getFacts());
                }
            }
        }
        else {
            System.err.println("Invalid facts directory path");
            System.exit(-1);
        }

        File rulesFile = new File(projectDirectory + "/rules.iris");
        Reader rulesReader = new FileReader(rulesFile);

        File queriesFile = new File(projectDirectory + "/queries.iris");
        Reader queriesReader = new FileReader(queriesFile);

        // Parse rules file.
        parser.parse(rulesReader);
        // Retrieve the rules from the parsed file.
        List<IRule> rules = parser.getRules();

        // Parse queries file.
        parser.parse(queriesReader);
        // Retrieve the queries from the parsed file.
        List<IQuery> queries = parser.getQueries();

        // Create a default configuration.
        Configuration configuration = new Configuration();

        // Enable Magic Sets together with rule filtering.
        configuration.programOptmimisers.add(new MagicSets());

        // Create the knowledge base.
        IKnowledgeBase knowledgeBase = new KnowledgeBase(factMap, rules, configuration);

        // Evaluate all queries over the knowledge base.
        for (IQuery query : queries) {
            List<IVariable> variableBindings = new ArrayList<>();
            IRelation relation = knowledgeBase.execute(query, variableBindings);

            // Output the variables.
            System.out.println("\n" + query.toString() + "\n" + variableBindings);

            // Output each tuple in the relation, where the term at position i
            // corresponds to the variable at position i in the variable
            // bindings list.
            for (int i = 0; i < relation.size(); i++) {
                System.out.println(relation.get(i));
            }
        }
    }

}
