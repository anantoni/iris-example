Iris-Reasoner examples
---

This example project introduces you to Iris-Reasoner. The file src/iris/Main.java provides the basic guidelines on using the Java API of IRIS in order to read and parse IRIS Datalog programs containing facts, rules and queries and execute the rules and queries on the knowledge base generated by the facts of each project. Instead of having all the facts, rules and queries in a file we decided to demonstrate a more modular approach. 

In particular, for each IRIS Datalog project (family, graph, factorial), you will find a subdirectory containing the fact files for the project -- one for each relation (Again you could have all the relation facts in one file but we decided to provide a cleaner use-case) --, a file with the Datalog rules for this particular project and
a file with the Datalog queries for this particular project.

You should be able to perform your analysis for project 4 by doing only minor modifications to this example. As a first step your Spiglet visitors should generate the fact files for each Spiglet file you provide.

The next step will be to create and fill the knowledge base for the analyzed program as our example shows and then perform your analyses by executing the rules for each analysis (again, you could have one rule file for each analysis and add more parameters to your program in order to perform a specific analysis on the datalog project you want).

A good structure you could use is the following, which requires minor modifications to the code we provide you:
```
IrisExample/
        generated-facts/Factorial/instruction.iris
                                 /next.iris
                                 /varDef.iris
                                 ...
        generated-facts/TreeVisitor/instruction.iris
                                  /next.iris
                                  /varDef.iris
                                 ...
        analysis-logic/DeadCodeComputation.iris
                      /CopyPropagation.iris
                      /BasicBlocksComputation.iris
                      ...
                      
        queries/queries.iris (one query file with all the queries which will demonstrate your analyses results)
        src/iris/Main.java
                /Main.class
  ```              
  In such a scenario you would need to be able to handle three command-line arguments. The first, would be the path
  to the facts directory, the second the path to the rules directory and the third the path the queries directory.
  
  Then your Java program should fill the knowledge base with all the facts found in the fact files of the first      path (as we demonstrated in our example) and do the same for the rules directory (parse the rules and execute 
  them on the filled knowledge base) and the queries directory (parse one or more query files and execute the
  queries on the resulting knowledge base.
  
  In this case the invocation of your Java program would be:

        java -cp $(CLASSPATH) iris.Main generated-facts/Factorial analysis-logic queries

                                 
                                 
