Projekt do predmetu IJA
Editor diagramu trid a sekvencniho diagramu

Aleksandr Verevkin (xverev00)
Marek Dohnal (xdohna48)

Informace k prekladu a spustení aplikace:

Stazeni knihoven do adresare lib:
    mvn dependency:copy-dependencies

Sestaveni:
    mvn install

Vytvoreni JAR souboru:
    mvn package

Spusteni JAR souboru:
    java -jar dest/ija-app.jar

Spusteni aplikace:
    mvn javafx:run

Spusteni testu:
    mvn test

Generovani dokumentace:
    mvn javadoc:javadoc