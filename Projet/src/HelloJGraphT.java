import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.io.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

//@example:uriCreate:end
//@example:render:begin
//@example:render:end
//@example:uriCreate:begin
//@example:uriCreate:end

/**
 * A simple introduction to using JGraphT.
 *
 * @author Barak Naveh
 */
public final class HelloJGraphT
{
    private HelloJGraphT()
    {
    } // ensure non-instantiability.

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     *
     * @throws URISyntaxException if invalid URI is constructed.
     * @throws ExportException if graph cannot be exported.
     */
    public static void main(String[] args)
            throws URISyntaxException,
            ExportException
    {
        Graph<String, DefaultEdge> stringGraph = createStringGraph();

        // note undirected edges are printed as: {<v1>,<v2>}
        System.out.println("-- toString output");
        // @example:toString:begin
        System.out.println(stringGraph.toString());
        // @example:toString:end
        System.out.println();

        // Prints the shortest path from vertex i to vertex c. This certainly
        // exists for our particular directed graph.
        System.out.println("Shortest path from i to c:");

        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg =
                new DijkstraShortestPath<>(stringGraph);
        ShortestPathAlgorithm.SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths("v1");
        System.out.println(iPaths.getPath("v4") + "\n");
        System.out.println(dijkstraAlg.getPathWeight("v1","v4"));
        // @example:traverse:begin

        // create a graph based on URI objects
        Graph<URI, DefaultEdge> hrefGraph = createHrefGraph();

        // find the vertex corresponding to www.jgrapht.org
        // @example:findVertex:begin
        URI start = hrefGraph
                .vertexSet().stream().filter(uri -> uri.getHost().equals("www.jgrapht.org")).findAny()
                .get();
        // @example:findVertex:end

        // @example:traverse:end

        // perform a graph traversal starting from that vertex
        System.out.println("-- traverseHrefGraph output");
        traverseHrefGraph(hrefGraph, start);
        System.out.println();

        System.out.println("-- renderHrefGraph output");
        renderHrefGraph(hrefGraph);
        System.out.println();
    }

    /**
     * Creates a toy directed graph based on URI objects that represents link structure.
     *
     * @return a graph based on URI objects.
     */
    private static Graph<URI, DefaultEdge> createHrefGraph()
            throws URISyntaxException
    {
        // @example:uriCreate:begin

        Graph<URI, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

        URI google = new URI("http://www.google.com");
        URI wikipedia = new URI("http://www.wikipedia.org");
        URI jgrapht = new URI("http://www.jgrapht.org");

        // add the vertices
        g.addVertex(google);
        g.addVertex(wikipedia);
        g.addVertex(jgrapht);

        // add edges to create linking structure
        g.addEdge(jgrapht, wikipedia);
        g.addEdge(google, jgrapht);
        g.addEdge(google, wikipedia);
        g.addEdge(wikipedia, google);

        // @example:uriCreate:end

        return g;
    }

    /**
     * Traverse a graph in depth-first order and print the vertices.
     *
     * @param hrefGraph a graph based on URI objects
     *
     * @param start the vertex where the traversal should start
     */
    private static void traverseHrefGraph(Graph<URI, DefaultEdge> hrefGraph, URI start)
    {
        // @example:traverse:begin
        Iterator<URI> iterator = new DepthFirstIterator<>(hrefGraph, start);
        while (iterator.hasNext()) {
            URI uri = iterator.next();
            System.out.println(uri);
        }
        // @example:traverse:end
    }

    /**
     * Render a graph in DOT format.
     *
     * @param hrefGraph a graph based on URI objects
     */
    private static void renderHrefGraph(Graph<URI, DefaultEdge> hrefGraph)
            throws ExportException
    {
        // @example:render:begin

        // use helper classes to define how vertices should be rendered,
        // adhering to the DOT language restrictions
        ComponentNameProvider<URI> vertexIdProvider = new ComponentNameProvider<URI>()
        {
            public String getName(URI uri)
            {
                return uri.getHost().replace('.', '_');
            }
        };
        ComponentNameProvider<URI> vertexLabelProvider = new ComponentNameProvider<URI>()
        {
            public String getName(URI uri)
            {
                return uri.toString();
            }
        };
        GraphExporter<URI, DefaultEdge> exporter =
                new DOTExporter<>(vertexIdProvider, vertexLabelProvider, null);
        Writer writer = new StringWriter();
        exporter.exportGraph(hrefGraph, writer);
        System.out.println(writer.toString());
        // @example:render:end
    }

    /**
     * Create a toy graph based on String objects.
     *
     * @return a graph based on String objects.
     */
    private static Graph<String, DefaultEdge> createStringGraph()
    {
        Graph<String, DefaultEdge> g = new SimpleDirectedWeightedGraph<>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);
        g.addEdge(v4, v1);
        g.addEdge(v1, v3);

        g.setEdgeWeight(v1,v2,5);
        g.setEdgeWeight(v2,v3,3);
        g.setEdgeWeight(v3,v4,1);
        g.setEdgeWeight(v4,v1,9);
        g.setEdgeWeight(v1,v3,6);

        return g;
    }
}