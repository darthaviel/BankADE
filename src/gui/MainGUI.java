/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author l
 */
public class MainGUI extends Application implements Runnable {

    Label caja,
            estado,
            transaccion,
            monto,
            entrega,
            monto_faltante,
            indicador_ciclos;
    TextField ciclos;
    HBox root,
            root1;
    VBox home
            ,text;
    Button startsim,
            exit;
    Pane espaciador,
            caja_fondo,
            caja_numero,
            cliente,
            spacer0,
            spacer1;

    @Override
    public void run() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        startsim.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                stage.getScene().setRoot(root1);
            }
        });
        exit.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
            }
        });
        
        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        exit = new Button("Exit");
        spacer0 = new Pane();
        spacer1 = new Pane();
        caja = new Label("CAJA");
        estado = new Label("ESTADO");
        transaccion = new Label("TRANSACCION");
        monto = new Label("MONTO DE TRANSACCION");
        entrega = new Label("MONTO ENTREGADO");
        monto_faltante = new Label("MONTO FALTANTE");
        indicador_ciclos = new Label("CICLOS");
        espaciador = new Pane();
        espaciador.setMinHeight(30);
        //espaciador.setStyle("-fx-background-color: TRANSAPRENT");
        text = new VBox(spacer0, caja, estado, transaccion, monto, entrega, monto_faltante, espaciador, indicador_ciclos);
        text.setSpacing(10);
        text.setMinSize(300, 300);
        text.setMaxSize(300, 300);
        text.setSpacing(10);
        //text.setStyle("-fx-background-color: #FFFFFF");
        caja_numero = new Pane();
        cliente = new Pane();
        caja_fondo = new Pane(caja_numero, cliente);
        caja_fondo.setMinSize(300, 300);
        caja_fondo.setMaxSize(300, 300);
        caja_fondo.setStyle("-fx-background-color: #000000");
        root1 = new HBox(exit, text, caja_fondo);
        root1.setStyle("-fx-background-color: #FFFFFF");
        root1.setSpacing(10);
        root1.setMinSize(620, 300);
        root1.setMaxSize(620, 300);
        
        ciclos = new TextField();
        ciclos.setAlignment(Pos.CENTER);
        startsim = new Button("Iniciar simulacion");
        
        home = new VBox(ciclos,startsim);
        root = new HBox(home);
        root.setMinSize(620, 300);
        root.setMaxSize(620, 300);
        
    }

}
