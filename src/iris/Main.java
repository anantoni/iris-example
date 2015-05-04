

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
import java.util.List;
import java.util.Map;

/**
 * An example program created for graph path evaluation.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // Create a Reader on the Datalog program file.
        File program = new File("graph.iris");
        Reader reader = new FileReader(program);

        // Parse the Datalog program.
        Parser parser = new Parser();
        parser.parse(reader);

        // Retrieve the facts, rules and queries from the parsed program.
        Map<IPredicate, IRelation> factMap = parser.getFacts();
        List<IRule> rules = parser.getRules();
        List<IQuery> queries = parser.getQueries();

        // Create a default configuration.
        Configuration configuration = new Configuration();

        // Enable Magic Sets together with rule filtering.
        //configuration.programOptmimisers.add(new RuleFilter());
        configuration.programOptmimisers.add(new MagicSets());

        // Convert the map from predicate to relation to a IFacts object.
        //IFacts facts = new Facts(factMap, configuration.relationFactory);

        // Create the knowledge base.
        IKnowledgeBase knowledgeBase = new KnowledgeBase(factMap, rules, configuration);
        //IKnowledgeBase knowledgeBase = new KnowledgeBase(factMap, rules, configuration);
        //IKnowledgeBase knowledgeBase = new RdbKnowledgeBase(facts, rules, configuration);

        // Evaluate all queries over the knowledge base.
        for (IQuery query : queries) {
            List<IVariable> variableBindings = new ArrayList<>();
            IRelation relation = knowledgeBase.execute(query, variableBindings);
            //IRelation relation = knowledgeBase.execute(query);
            // Output the variables.
            System.out.println(variableBindings);

            // For performance reasons compute the relation size only once.
            int relationSize = relation.size();

            // Output each tuple in the relation, where the term at position i
            // corresponds to the variable at position i in the variable
            // bindings list.
            for (int i = 0; i < relationSize; i++) {
                System.out.println(relation.get(i));
            }
        }
    }

}
