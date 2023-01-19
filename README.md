# Leaderboard

Ce projet est une application de leaderboard basée sur Spring Boot. 
Il utilise une structure de données de type Binary Tree Balanced pour maintenir une liste triée de joueurs en fonction de leurs points.
Ce type de structure de données permet des operations d'accès et écriture rapides même avec un grand nombre d'éléments.
Les joueurs peuvent être ajoutés, supprimés et mis à jour dans le leaderboard via une API REST.
Les accès au leaderboard sont "thread safe" tout en gardant une certaine fléxibilité pour les accès en lecture (Read/Write locks).
Un cache applicatif est également présent afin de ne re-calculer le leaderboard qu'à chaque modification de ce dernier.

### Pré-requis
- Java 11 ou supérieur
- Kotlin 1.6.21 ou supérieur

### Dépendances et documentations

* [Spring Boot](https://spring.io/projects/spring-boot)
* [H2 (base de données embarquée)](https://en.wikipedia.org/wiki/H2_(DBMS))
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.1/reference/htmlsingle/#web)
* [JPA (gestion des données)](https://docs.spring.io/spring-boot/docs/3.0.1/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Configuration
- Exécutez la commande suivante dans le répertoire racine du projet pour construire l'application.
- Cette commande execute les tests également.
```bash
./gradlew clean build
```
- Pour exécuter les tests de l'application uniquement, utilisez la commande
```bash
./gradlew test
```
- Pour lancer l'application, exécutez la commande
```bash
./gradlew bootRun
```
- Le serveur se lance par defaut sur le port 8080.
Si vous souhaitez changer ce dernier, éditer le fichier application.yml, comme ceci
vous permettra de démarrer sur le port 9000 :
```
server:
    port: 9000
```
### Utilisation
L'API REST est disponible à l'adresse `http://localhost:8080/swagger-ui/index.html` 

Vous pourrez intéragir avec le leaderboard avec les méthodes suivantes:

- Ajouter un joueur: POST /addPlayer/{name}
- Récupérer un joueur: GET /getPlayer/{name}
- Supprimer un joueur: DELETE /removePlayer/{name}
- Mettre à jour les points d'un joueur: PUT /updatePoints/{name}
- Récupérer le leaderboard: GET /leaderboard
- Supprimer tous les joueurs: DELETE /removeAll


### Base de données

Pour accéder à la base de données H2 utilisée dans cette application, vous pouvez utiliser l'URL suivante en utilisant un navigateur web :

http://localhost:8080/h2-console

Il vous sera demandé de saisir les informations de connexion suivantes :

```
JDBC URL : jdbc:h2:mem:leaderboards
Nom d'utilisateur : sa
Mot de passe : (laisser vide)
```
Une fois connecté, vous pourrez accéder à la console H2 pour effectuer des requêtes SQL sur la base de données, visualiser les données stockées et effectuer des modifications si nécessaire.

Il est important de noter que la base de données H2 utilisée dans cette application est une base de données en mémoire, qui est réinitialisée chaque fois que l'application est redémarrée. Les données ne sont donc pas persistantes. Si vous souhaitez stocker les données de manière permanente, vous devrez configurer une base de données externe (comme MySQL ou PostgreSQL) et mettre à jour les informations de connexion dans les propriétés de l'application.

### Liens additionnels

* [Documentation Officielle Gradle](https://docs.gradle.org)
* [Guide de réference pour Plugin Gradle Spring Boot](https://docs.spring.io/spring-boot/docs/3.0.1/gradle-plugin/reference/html/)


