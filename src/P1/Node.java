package P1;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class Node {
    private int id;
    private double energy;
    private String transmissionModel;
    private String receptionModel;
    private int currentRound;
    private double x; // Coordonnée X du nœud
    private double y; // Coordonnée Y du nœud
    private List<String> messages = new ArrayList<>();
    private Color color;

    public Node(int id, double energy, String transmissionModel, String receptionModel) {
        this.id = id;
        this.energy = energy;
        this.transmissionModel = transmissionModel;
        this.receptionModel = receptionModel;
        this.x = Math.random() * 100; // Génération aléatoire de la coordonnée X (ajustez selon vos besoins)
        this.y = Math.random() * 100; // Génération aléatoire de la coordonnée Y (ajustez selon vos besoins)
        this.color = Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }


    @Override
    public String toString() {
        return "Node{id=" + id + ", energy=" + energy + ", transmissionModel='" + transmissionModel + '\'' +
                ", receptionModel='" + receptionModel + '\'' + '}';
    }


    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public boolean isPreviousClusterHead() {
        // Implement your logic here
        return false;
    }

    public int getX() {
        return (int) x; // Convertir en entier si nécessaire
    }

    public int getY() {
        return (int) y; // Convertir en entier si nécessaire
    }
    public int getId() {
        return (int) id; // Convertir en entier si nécessaire
    }

    public Color getColor() {
        return color;
    }
    
    public void sendMessage(String message) {
        messages.add(message);
    }

    public void sendAcceptanceMessage(Node clusterHead) {
        String message = "I am Node " + getId() + ". I accept Node " + clusterHead.getId() + " as my Cluster Head.";
        clusterHead.sendMessage(message);
        System.out.println(message);
    }

    public List<String> getMessages() {
        return messages;
    }
}
