# Application d'aide décisionnelle pour des centres de santé

## Description
  Cette application est un projet de groupe réalisé dans le cadre de cours de deuxième année de BUT informatique de Villeurbanne Doua.
  
  Elle permet à un utilisateur de générer une carte de district aléatoire, d'importer un district depuis un fihcier .csv, visualiser ces districts grâce à une interface, pouvoir calculer des itinéraires entre centres de santé selon différents critères et afficher certaines informations importantes sur ces centres, les véhicules faisant les itinéraires ou encore les routes entre les centres.
  
  Dans ce projet un district est représenté par un graphe, ses sommets représentent les différents centres de santé du district et ont un type (maternité/bloc opératoire/centre de nutrition) et chaque arête reliant ces sommets est une route comportant une distance en km, une durée en minute et une fiabilité (de 0 à 10 avec 10 pour une probabilité de rencontrer des problèmes sur la route très faible).
  
  Les graphes sont visualisés grâce à la librairie Graphstream.

## Fonctionnalités
 - Générer un district aléatoire
 - Importer un district depuis un fichier .csv
 - Afficher les districts et les informations les caractérisant (infos des centres et routes, nombre de centres par type, seuil de dangerosité des routes…)
 - Calculer un itinéraire entre deux centres (chemin le plus rapide/chemin le plus sûr et rapide) et pouvoir l'afficher
 - Comporter un algorithme permettant de définir un véhicule faisant le tour de tous les centres le plus rapidement possible
 - Comporter un algorithme permettant à deux véhicules de faire le tour de tous les centres en minimisant le temps mis par le plus lent des deux
 - Comporter une IHM ergonomique, lisible et le plus fidèle possible à une maquette créée sur Figma au début du projet, cet IHM permet à l'utilisateur de choisir l'affichage et les actions qu'il veut utiliser

## Outils
 - Figma
 - Netbeans
 - Git
## Langage
 - Java
