/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package saejava;

import org.graphstream.graph.Graph;

/**
 *
 * @author zynga
 */
public class SaeJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Neuille");
        Graph gtest = CarteDistrictAleatoire.createRandom(30,5);
        gtest.display();
    }
    
}
