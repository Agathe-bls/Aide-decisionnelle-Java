/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package saejava;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author Agathe
 */
public class MonGraphe {
    //ICI JE CRéERAIS LA FONCTION POUR LE GRAPHE ALéATOIRE
    //Mattis import 
 
    public static Graph createRandom(int N){
        Graph graph = new SingleGraph("G3");
        Generator gen = new RandomGenerator(3);//REGARDER QUOI METTRE EN AVG pour 
        gen.addSink(graph);
        gen.begin();
        for(int i=graph.getNodeCount(); i<N; i++) //ATTENTION AU DEBUT DU I
            gen.nextEvents();
        gen.end();
        
        return graph;
}
