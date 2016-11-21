import java.util.*;


class Network{
    private int[][] graph;
    private boolean[] gateways;
    private int[] nodeDanger;
    private int gatewayCounter;
    private int G;
    private int SI;

    public Network(int N, int G){
        graph = new int[N][N];
        gateways = new boolean[N];
        nodeDanger = new int[N];
        this.G=G;
    }

    public void addLink(int N1, int N2){
        graph[N1][N2] = 1;
        graph[N2][N1] = 1;
    }

    public void addGateWay(int EI){
        gateways[EI] = true;
        for(int i = 0; i<graph.length; i++){
            if(graph[i][EI] != 0)
                graph[i][EI] = 2;
            if(graph[EI][i] != 0)
                graph[EI][i] = 2;
        }
        gatewayCounter++;
        if(gatewayCounter == G)
            calculateDnagerForNodes();
    }

    private void calculateDnagerForNodes(){
        for(int i = 0; i < nodeDanger.length; i++){
            if(!gateways[i])
                for(int j = 0; j < graph[i].length; j++)
                    if(graph[i][j] == 2)
                        nodeDanger[i]++;
        }
    }

    public void setSI(int SI) {
        this.SI = SI;
    }

    //Calculates distances from agent, if the path always contains nodes that link to gateways then that distance is 0, otherwise it incurs a penalty if the path contains illogical hops
    private int[] distanceFromSI(){
        int[] distances = new int[graph.length];
        Arrays.fill(distances, Integer.MAX_VALUE);
        int emptyCells = graph.length;
        int checkAgainst = -1;
        List<Integer> queue = new LinkedList<Integer>();
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[SI] = 0;
        emptyCells--;
        queue.add(SI);
        while (emptyCells>0 && !queue.isEmpty()){
            checkAgainst = queue.remove(0);
            if(distances[checkAgainst] != Integer.MAX_VALUE) {
                for (int i = 0; i < graph.length; i++)
                    if (graph[i][checkAgainst] != 0 && distances[i] > distances[checkAgainst] + 1 + (nodeDanger[checkAgainst] == 0 ? (nodeDanger[i] == 0 ? 2 : 1) : 0)) {
                        queue.add(i);
                        distances[i] = distances[checkAgainst] + (nodeDanger[checkAgainst] == 0 ? (nodeDanger[i] == 0 ? 2 : 1) : 0);
                        emptyCells--;
                    }
            }
        }
        return distances;
    }

    public String severBestLink(){
        //First we see if SI can jump to an exit node this turn, if so then we sever that link.
        for(int i = 0; i<graph.length; i++)
            if(graph[i][SI] == 2){
                graph[i][SI] = 0;
                graph[SI][i] = 0;
                nodeDanger[SI]--;
                return i+" "+SI;
            }
        //We find the "closest" dangerous node, meaning node which links to two or more gateways
        int[] distances = distanceFromSI();
        int smallestDoubleDist = Integer.MAX_VALUE;
        int closestDoubleNode = -1;
        int smallestSingleDist = Integer.MAX_VALUE;
        int closestSingleNode = -1;
        for(int i = 0; i<distances.length; i++){
            if(nodeDanger[i] > 1 && distances[i]<smallestDoubleDist) {
                closestDoubleNode = i;
                smallestDoubleDist = distances[i];
            }
            if(nodeDanger[i] > 0 && distances[i]<smallestSingleDist) {
                closestSingleNode = i;
                smallestSingleDist = distances[i];
            }
        }
        int closestNode = closestDoubleNode == -1 ? closestSingleNode : closestDoubleNode;
        for(int i = 0; i<graph[closestNode].length; i++){
            if(graph[i][closestNode] == 2){
                graph[i][closestNode] = 0;
                graph[closestNode][i] = 0;
                nodeDanger[closestNode]--;
                return i+" "+closestNode;
            }
        }
        return "Zolda";
    }
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();
        int L = in.nextInt();
        int E = in.nextInt();
        Network network = new Network(N, E);
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt();
            int N2 = in.nextInt();
            network.addLink(N1, N2);
        }
        for (int i = 0; i < E; i++) {
            network.addGateWay(in.nextInt());
        }
        while (true) {
            int SI = in.nextInt();
            network.setSI(SI);
            System.out.println(network.severBestLink());
        }
    }
}