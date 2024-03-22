package P1;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.util.ArrayList;
import java.util.List;
import javafx.util.Duration;


public class LeachUI {
    private TextField numberOfNodesField;
    private TextField simulationDurationField;
    private TextField communicationRadiusField;
    private Button clusterButton;
    private Button showNetworkButton; // Nouveau bouton pour afficher le réseau avant le clustering
    private ListView<Node> nodesListView;
    private ListView<List<Node>> clustersListView;
    private GridPane grid;

    public LeachUI(Stage primaryStage) {
        // Créer un nouveau GridPane pour cette instance
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Creating UI components
        Label numberOfNodesLabel = new Label("Number of Nodes:");
        numberOfNodesField = new TextField();

        Label simulationDurationLabel = new Label("Simulation Duration:");
        simulationDurationField = new TextField();

        Label communicationRadiusLabel = new Label("Communication Radius:");
        communicationRadiusField = new TextField();

        clusterButton = new Button("Run Clustering");
        showNetworkButton = new Button("Show Network"); // Nouveau bouton

        nodesListView = new ListView<>();
        clustersListView = new ListView<>();

        // Adding components to the grid
        grid.add(numberOfNodesLabel, 0, 0);
        grid.add(numberOfNodesField, 1, 0);
        grid.add(simulationDurationLabel, 0, 1);
        grid.add(simulationDurationField, 1, 1);
        grid.add(communicationRadiusLabel, 0, 2);
        grid.add(communicationRadiusField, 1, 2);
        grid.add(clusterButton, 0, 3, 2, 1);
        grid.add(showNetworkButton, 0, 4, 2, 1); // Ajouter le nouveau bouton
        grid.add(new Label("Nodes:"), 0, 5);
        grid.add(nodesListView, 0, 6);
        grid.add(new Label("Clusters:"), 1, 5);
        grid.add(clustersListView, 1, 6);
        
        // Adding event handlers
        clusterButton.setOnAction(event -> runClustering());
        showNetworkButton.setOnAction(event -> showNetwork());

        // Setting up the stage
        Scene scene = new Scene(grid, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
private void runClustering() {
    // Parse user input
    int numberOfNodes = Integer.parseInt(numberOfNodesField.getText());
    int simulationDuration = Integer.parseInt(simulationDurationField.getText());
    double communicationRadius = Double.parseDouble(communicationRadiusField.getText());

    // Create nodes
    List<Node> nodes = createNodes(numberOfNodes);

    // Create network
    Network network = new Network(numberOfNodes, "Random", simulationDuration, communicationRadius);

 // Run LEACH clustering
    LeachAlgorithm leachAlgorithm = new LeachAlgorithm();
    Pair<List<List<Node>>, List<Node>> clusteringResult = leachAlgorithm.performClustering(network, nodes, 1);

    List<List<Node>> clusters = clusteringResult.getKey();
    List<Node> clusterHeads = clusteringResult.getValue();

    // Display results in the UI
    ObservableList<Node> nodesObservableList = FXCollections.observableArrayList(nodes);
    nodesListView.setItems(nodesObservableList);

    ObservableList<List<Node>> clustersObservableList = FXCollections.observableArrayList(clusters);
    clustersListView.setItems(clustersObservableList);

    
 
    
    // Show graphical interface
    showGraphicalInterface(nodes, clusters, clusterHeads);
 // Broadcast messages from cluster heads to nodes
    System.out.println("Cluster Head Broadcasting:");
    broadcastClusterHeadMessages(clusters, clusterHeads);
 // Nodes send acceptance messages to cluster heads
    System.out.println("Nodes Sending Acceptance Messages:");
    sendAcceptanceMessages(clusters, clusterHeads);
 // Afficher la liste des clusters dans la console avec le cluster head de chaque cluster
    printClustersWithClusterHead(clusters, clusterHeads);
}

private void broadcastClusterHeadMessages(List<List<Node>> clusters, List<Node> clusterHeads) {
    
    for (int i = 0; i < clusters.size(); i++) {
        Node clusterHead = clusterHeads.get(i);
        List<Node> cluster = clusters.get(i);

        // Broadcast message from cluster head to nodes in the cluster
        String message = " message envoyé :I am your Cluster Head (ID: " + clusterHead.getId() + ")";
        System.out.println(message);
        for (Node node : cluster) { 
            node.sendMessage(message);
            System.out.println("Node " + node.getId() + " received message: " + message);
        }
    }
}
private void sendAcceptanceMessages(List<List<Node>> clusters, List<Node> clusterHeads) {
   
    for (int i = 0; i < clusters.size(); i++) {
        Node clusterHead = clusterHeads.get(i);
        List<Node> cluster = clusters.get(i);

        // Nodes in the cluster send acceptance messages to the cluster head
        for (Node node : cluster) {
            node.sendAcceptanceMessage(clusterHead);
        }
    }
}
    private List<Node> createNodes(int numberOfNodes) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 1; i <= numberOfNodes; i++) {
            nodes.add(new Node(i, Math.random() * 100, "SimpleModel", "SimpleModel"));
        }
        return nodes;
    }
    
    private void showNetwork() {
        // Afficher le réseau avant le clustering
        int numberOfNodes = Integer.parseInt(numberOfNodesField.getText());
        List<Node> nodes = createNodes(numberOfNodes);

        // Afficher le réseau dans la console
        printNetwork(nodes);

        // Afficher le réseau graphique si vous le souhaitez
        showGraphicalNetwork(nodes);
    }
    
    private void printNetwork(List<Node> nodes) {
        System.out.println("Network Before Clustering:");
        for (Node node : nodes) {
            System.out.println(node);
        }
    }
    
    private void showGraphicalNetwork(List<Node> nodes) {
        Stage graphicalStage = new Stage();
        graphicalStage.setTitle("Graphical Network Before Clustering");

        GridPane graphicalPane = new GridPane();
        graphicalPane.setPadding(new Insets(10, 10, 10, 10));
        graphicalPane.setVgap(8);
        graphicalPane.setHgap(10);

        // Afficher les nœuds comme des cercles colorés
        for (Node node : nodes) {
            Circle circle = new Circle(7, Color.BLUE); // Vous pouvez ajuster le rayon et la couleur du cercle
            graphicalPane.add(circle, node.getX(), node.getY());
        }

        Scene graphicalScene = new Scene(graphicalPane, 600, 400);
        graphicalStage.setScene(graphicalScene);
        graphicalStage.show();
    }

    
    private void printClustersWithClusterHead(List<List<Node>> clusters, List<Node> clusterHeads) {
        System.out.println("Liste des clusters avec le cluster head :");
        for (int i = 0; i < clusters.size(); i++) {
            List<Node> cluster = clusters.get(i);
            Node clusterHead = clusterHeads.get(i);

            System.out.println("Cluster " + i + " - Cluster Head: " + clusterHead);
            for (int j = 0; j < cluster.size(); j++) {
                System.out.println("  Node " + j + ": " + cluster.get(j));
            }
            System.out.println();
        }
    }

    private void showGraphicalInterface(List<Node> nodes, List<List<Node>> clusters, List<Node> clusterHeads) {
        Stage graphicalStage = new Stage();
        graphicalStage.setTitle("Graphical Interface");

        GridPane graphicalPane = new GridPane();
        graphicalPane.setPadding(new Insets(10, 10, 10, 10));
        graphicalPane.setVgap(8);
        graphicalPane.setHgap(10);

        // Display nodes
        for (Node node : nodes) {
            Circle circle = new Circle(7, node.getColor());
            graphicalPane.add(circle, node.getX(), node.getY());
        }

        // Display clusters and highlight cluster heads
        for (int i = 0; i < clusters.size(); i++) {
            List<Node> cluster = clusters.get(i);

            // Get the color of the first node in the cluster
            Color clusterColor = cluster.isEmpty() ? Color.BLACK : cluster.get(0).getColor();

            // Display nodes in the cluster
            for (Node node : cluster) {
                Circle clusterCircle = new Circle(7, clusterColor);
                graphicalPane.add(clusterCircle, node.getX(), node.getY());
            }

            // Highlight cluster head with a larger circle and border
            Node clusterHead = clusterHeads.get(i);

            // Position initiale du cercle du cluster head
            double initialX = clusterHead.getX();
            double initialY = clusterHead.getY();

            // Créer le cercle pour le clignotement
            Circle headCircle = new Circle(10, clusterColor);
            headCircle.setStroke(Color.BLACK); // Couleur de la bordure
            headCircle.setStrokeWidth(2); // Largeur de la bordure
            graphicalPane.add(headCircle, (int) initialX, (int) initialY);


            // Animation de clignotement
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), headCircle);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setCycleCount(Timeline.INDEFINITE);
            fadeTransition.setAutoReverse(true);

            // Animation pour recentrer le cercle pendant le clignotement
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), headCircle);
            translateTransition.setByX(-headCircle.getRadius());
            translateTransition.setByY(-headCircle.getRadius());
            translateTransition.setCycleCount(Timeline.INDEFINITE);
            translateTransition.setAutoReverse(true);

            // Ajouter les animations à une séquence pour qu'elles se déclenchent en même temps
            SequentialTransition sequentialTransition = new SequentialTransition(
                    new ParallelTransition(fadeTransition, translateTransition)
            );
            sequentialTransition.play();
        }

        Scene graphicalScene = new Scene(graphicalPane, 600, 400);
        graphicalStage.setScene(graphicalScene);
        graphicalStage.show();
    }



}
