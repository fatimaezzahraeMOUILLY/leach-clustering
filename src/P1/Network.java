package P1;
public class Network {
    private int numberOfNodes;
    private String position;
    private int simulationDuration;
    private double communicationRadius;

    public Network(int numberOfNodes, String position, int simulationDuration, double communicationRadius) {
        this.numberOfNodes = numberOfNodes;
        this.position = position;
        this.simulationDuration = simulationDuration;
        this.communicationRadius = communicationRadius;
    }
}