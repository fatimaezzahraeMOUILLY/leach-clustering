package P1;

import javafx.util.Pair; 

import java.util.ArrayList;
import java.util.List;

public class LeachAlgorithm {

    private static final double P = 0.1; // Pourcentage des cluster heads voulu

    public Pair<List<List<Node>>, List<Node>> performClustering(Network network, List<Node> nodes, int currentRound) {
        List<List<Node>> clusters = new ArrayList<>();
        List<Node> clusterHeads = new ArrayList<>();

        // Calcul du seuil T(n) pour chaque nœud
        for (Node node : nodes) {
            double threshold = calculateThreshold(node, currentRound);

            // Choix aléatoire du nœud en tant que cluster head en fonction du seuil
            if (Math.random() < threshold) {
                clusterHeads.add(node);
                clusters.add(new ArrayList<>()); // Création d'un nouveau cluster vide
            }
        }
        // Attribution des nœuds aux clusters (communication directe avec le cluster head le plus proche)
        for (Node node : nodes) {
            double minDistance = Double.MAX_VALUE;
            Node closestClusterHead = null;

            for (Node clusterHead : clusterHeads) {
                double distance = calculateDistance(node, clusterHead);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestClusterHead = clusterHead;
                }
            }

            // Ajout du nœud au cluster du cluster head le plus proche
            clusters.get(clusterHeads.indexOf(closestClusterHead)).add(node);
        }

        

        // Mettre à jour le tour actuel pour les nœuds
        for (Node node : nodes) {
            node.setCurrentRound(currentRound);
        }

        return new Pair<>(clusters, clusterHeads);
    }

    private double calculateThreshold(Node node, int currentRound) {
        // Calcul du seuil T(n) en fonction de la formule spécifiée
        double rMod = currentRound % (1 / P);
        double threshold = P / (1 - P * rMod);

        // Si le nœud a été cluster head au cours des 1/P tours précédents, le seuil est fixé à 0
        if (node.isPreviousClusterHead()) {
            threshold = 0;
        }

        return threshold;
    }

    private double calculateDistance(Node node1, Node node2) {
        // Par exemple, distance euclidienne dans cet exemple
        return Math.sqrt(Math.pow(node1.getX() - node2.getX(), 2) + Math.pow(node1.getY() - node2.getY(), 2));
    }
}
