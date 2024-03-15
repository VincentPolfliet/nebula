package dev.vinpol.nebula.javaGOAP;

import dev.vinpol.nebula.javaGOAP.graph.DirectedWeightedGraph;
import dev.vinpol.nebula.javaGOAP.graph.IWeightedGraph;
import dev.vinpol.nebula.javaGOAP.graph.WeightedEdge;

/**
 * DefaultGoapPlanner.java --- The default implementation of the GoapPlanner.
 *
 * @author P H - 15.03.2017
 *
 */
public class DefaultGoapPlanner extends GoapPlanner {

	@Override
	protected <EdgeType extends WeightedEdge> IWeightedGraph<GraphNode, EdgeType> generateGraphObject() {
		return new DirectedWeightedGraph<GraphNode, EdgeType>();
	}
}
