Projekt do předmětu IJA
Editor diagramu tříd a sekvenčního diagramu

Autoři:
    Aleksandr Verevkin (xverev00)
    Marek Dohnal (xdohna48)
Datum:
    2022-04-12

Popis implementace:
    V implementaci jsme se mírně odchýlili od požadavků specifikovaných v Úkolu 2.
    Aplikace podporuje načtení a uložení JSON souborů obsahujících pouze třídy.
    Uživatel může vytvořit třídu pomocí tlačítka "Create class" a následného kliknutí na prázdné pole.
    Pomocí vyskakovacího okna může editovat parametry třídy. Třída je uložena v seznamu tříd v pravém sloupci,
    kde je možné ji znovu editovat, nebo smazat.

    Funkcionalita knihovny je demonstrována několika testy v souboru ParserTest.java. Testy demonstrují možnost
    načítat, editovat, a ukládat JSON soubory s třídami a uzly.

    Aplikaci je možné spustit pomocí padkage manageru Maven pomocí níže uvedeného příkazu mvn javafx:run,
    nebo vytvořením JAR souboru a jeho následným spuštěním. Způsob vytvoření archivu JAR je uveden níže.

    Funkcionalita jednotlivých modulů je blíže popsána v programové dokumentaci, kterou lze generovat níže uvedeným
    příkazem.

Informace k překladu a spuštění aplikace:
    Stažení knihoven do adresáře lib:
        mvn dependency:copy-dependencies

    Sestavení:
        mvn install

    Vytvoření JAR souboru včetně sestavení:
        mvn package

    Spuštění JAR souboru na Windows:
        java -jar dest/ija-app.jar

    Spuštění JAR souboru na Linuxu:
        java -Djdk.gtk.version=2 -jar dest/ija-app.jar

    Spuštění aplikace:
        mvn javafx:run

    Spuštění testů:
        mvn test

    Generování dokumentace:
        mvn javadoc:javadoc