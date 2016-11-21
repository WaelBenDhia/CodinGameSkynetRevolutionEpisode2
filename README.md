# CodinGameSkynetRevolutionEpisode2
My solution for the CodinGame puzzle Skynet Revolution Episode 2

The solution consists of the following algorithm:
First step if the Agent is on a node that links directly to a gateway then sever that link.
Else it calculates the "distances" of nodes to the node the Agent occupies. If the path to the node contains illogical hops meaning hops that move that take you to a node that does not link to a gateway then a penalty is incurred. Then we sever the either the closest node that links to two gateways or the closest node that links to a gateway if no node links to two gateways.
