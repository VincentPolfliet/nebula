package dev.vinpol.nebula.javaGOAP;



import dev.vinpol.nebula.javaGOAP.graph.DirectedWeightedGraph;
import dev.vinpol.nebula.javaGOAP.graph.WeightedEdge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * WeightedGraphTest.java --- WeightedGraph TestFile.
 *
 * @author P H - 14.03.2017
 *
 */
public class DirectedWeightedGraphTest {

	@Test
	public void weights() {
		int vertexCount = 5;
		int edgeCount = 2;
		DirectedWeightedGraph<Integer, WeightedEdge> g = createBasicConnectedTestWeightedGraph(vertexCount, edgeCount);

		assertTrue(g.getEdgeWeight(g.getEdge(0, 1)) == 0);
	}

	public static DirectedWeightedGraph<Integer, WeightedEdge> createBasicTestWeightedGraph(int vertexCount) {
		DirectedWeightedGraph<Integer, WeightedEdge> g = new DirectedWeightedGraph<Integer, WeightedEdge>();

		for (int i = 0; i < vertexCount; i++) {
			g.addVertex(i);
		}

		return g;
	}

	public static DirectedWeightedGraph<Integer, WeightedEdge> createBasicConnectedTestWeightedGraph(int vertexCount,
			int edgeCount) {
		DirectedWeightedGraph<Integer, WeightedEdge> g = createBasicTestWeightedGraph(vertexCount);

		for (int i = 0; i < edgeCount; i++) {
			g.addEdge(i, i + 1, new WeightedEdge());
		}
		return g;
	}
}
