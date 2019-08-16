/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author l
 */
public class MainGUI extends Application implements Runnable{
    Label caja;
    Label estado;
    Label transaccion;
    Label monto;
    Label entrega;
    Label monto_faltante;
    Label indicador_ciclos;
    HBox dual;
    VBox text;
    Pane espaciador;
    Pane caja_fondo;
    Pane caja_numero;
    Pane cliente;
    

    @Override
    public void run() {
        Application.launch();
    }

    @Override
    public void start(Stage arg0) throws Exception {
        Scene scene = new Scene(dual);
        arg0.setScene(scene);
        arg0.show();
    }
    
    @Override
    public void init(){
        caja = new Label("CAJA");
        estado = new Label("ESTADO");
        transaccion = new Label("TRANSACCION");
        monto = new Label("MONTO DE TRANSACCION");
        entrega = new Label("MONTO ENTREGADO");
        monto_faltante = new Label("MONTO FALTANTE");
        indicador_ciclos = new Label("CICLOS");
        espaciador = new Pane();
        espaciador.setMinHeight(30);
        text = new VBox(caja,estado,transaccion,monto,entrega,monto_faltante,espaciador,indicador_ciclos);
        text.setSpacing(10);
        text.setMinWidth(800);
        text.setMinHeight(300);
        caja_numero = new Pane();
        cliente = new Pane();
        caja_fondo = new Pane(caja_numero,cliente);
        dual = new HBox(text,caja_fondo);
        dual.setMaxHeight(420);
        dual.setMaxWidth(650);
    }

}
