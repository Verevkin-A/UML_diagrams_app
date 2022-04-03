package userInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import classDiagram.ClassDiagram;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import parser.Parser;

public class Controller {
    private final FileChooser fileChooser = new FileChooser();
    private ClassDiagram cd = new ClassDiagram();

    public Controller() {
        this.fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
    }

    @FXML
    private void loadDiagram() throws IOException {
        File file = this.fileChooser.showOpenDialog(null);

        System.out.println(file.getAbsolutePath());     // TODO call read method
        // read input
        try {
            String filepath = file.getAbsolutePath();
            String diagString = Files.readString(Paths.get(filepath));
            this.cd = Parser.decodeJSON(diagString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveDiagram() throws IOException {
        File file = this.fileChooser.showSaveDialog(null);
        // check if file have extension
        if (!file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + ".json");
        }

        System.out.println(file.getAbsolutePath());     // TODO call save method
        try {
            FileWriter outFile = new FileWriter(file.getAbsolutePath());
            String output = Parser.encodeJSON(this.cd);
            outFile.write(output);
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showHelp() throws IOException {
        // TODO help window
    }
}
