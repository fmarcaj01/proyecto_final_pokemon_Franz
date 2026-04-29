package com.pokemon.controles;

import com.pokemon.App;
import com.pokemon.datos.DataLoader;
import com.pokemon.modelo.Pokemon;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ControladorMenu {

    @FXML private FlowPane botRepositorio;
    private Button<Repository> ;

    @FXML private FlowPane containerPokemon;
    private List<Pokemon> listaPokemons;

    @FXML
    public void initialize() {
        listaPokemons = DataLoader.cargarPokemons();
        
        if (listaPokemons == null || listaPokemons.isEmpty()) {
            System.err.println("❌ Menú: La lista de Pokémon está vacía.");
            return;
        }

        containerPokemon.getChildren().clear();
        for (Pokemon p : listaPokemons) {
            containerPokemon.getChildren().add(crearTarjeta(p));
        }
    }

    private VBox crearTarjeta(Pokemon p) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(160, 200);
        card.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 15; -fx-cursor: hand; -fx-border-color: #576574; -fx-border-width: 2; -fx-border-radius: 15;");

        try {
            String ruta = p.getRutaImagen();
            String pathCorrecto = ruta.startsWith("/") ? ruta : "/" + ruta;
            
            InputStream is = getClass().getResourceAsStream(pathCorrecto);
            
            if (is != null) {
                Image img = new Image(is, 100, 100, true, true);
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(100);
                imgView.setFitWidth(100);
                imgView.setPreserveRatio(true);
                card.getChildren().add(imgView);
                System.out.println("✅ Menú: Imagen cargada para " + p.getNombre());
            } else {
                System.err.println("❓ Menú: No se encontró " + pathCorrecto);
                Label errorLabel = new Label("[?]");
                errorLabel.setStyle("-fx-text-fill: red;");
                card.getChildren().add(errorLabel);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error en tarjeta de " + p.getNombre());
            e.printStackTrace();
        }

        Label name = new Label(p.getNombre().toUpperCase());
        name.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        card.getChildren().add(name);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #576574; -fx-background-radius: 15; -fx-border-color: #1dd1a1; -fx-border-width: 2; -fx-border-radius: 15;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #3d3d3d; -fx-background-radius: 15; -fx-border-color: #576574; -fx-border-width: 2; -fx-border-radius: 15;"));

        card.setOnMouseClicked(e -> {
            List<Pokemon> posiblesRivales = listaPokemons.stream()
                    .filter(poke -> !poke.getNombre().equals(p.getNombre()))
                    .collect(Collectors.toList());
            
            Pokemon rival = posiblesRivales.isEmpty() ? p : posiblesRivales.get(new Random().nextInt(posiblesRivales.size()));
            App.abrirCombate(p, rival);
        });

        return card;
    }
}