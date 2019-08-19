package gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainGUI extends Application implements Runnable {

    private Label caja,
            estado,
            transaccion,
            monto,
            entrega,
            monto_faltante,
            adicional,
            indicador_ciclos;
    private TextField ciclos;
    private HBox root,
            root1;
    private VBox home, text;
    private Button startsim,
            exit;
    private Pane espaciador,
            caja_fondo,
            caja_numero,
            cliente,
            spacer0,
            spacer1;
    private Socket socket;
    private DataOutputStream dout;
    private DataInputStream din;
    private Scene scene;
    private Stage sta;

    @Override
    public void run() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        startsim.setOnAction(e -> startSim());

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
            }
        });

        scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        sta = stage;
        sta.initStyle(StageStyle.TRANSPARENT);
        sta.setScene(scene);
        sta.show();
    }

    @Override
    public void stop() {
        try {
            dout.writeUTF("x");
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void init() {
        try {
            Thread.sleep(1000);
            socket = new Socket("localhost", 1025);
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);

        exit = new Button("Exit");
        spacer0 = new Pane();
        spacer1 = new Pane();
        caja = new Label("");
        estado = new Label("");
        transaccion = new Label("");
        monto = new Label("");
        entrega = new Label("");
        monto_faltante = new Label("");
        adicional = new Label("");
        indicador_ciclos = new Label("");
        espaciador = new Pane();
        espaciador.setMinHeight(30);
        text = new VBox(spacer0, caja, estado, transaccion, monto, entrega, monto_faltante, adicional, espaciador, indicador_ciclos);
        text.setSpacing(10);
        text.setMinSize(300, 300);
        text.setMaxSize(300, 300);
        text.setSpacing(10);
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
        ciclos.setTextFormatter(textFormatter);
        startsim = new Button("Iniciar simulacion");

        home = new VBox(ciclos, startsim);
        root = new HBox(home);
        root.setMinSize(620, 300);
        root.setMaxSize(620, 300);

    }

    private void startSim() {
        String s;
        s = ciclos.getText();
        if (s.trim().length() > 0) {
            if (Integer.parseInt(s) > 0) {
                try {
                    dout.writeUTF(s);
                    dout.flush();
                    Runnable sim = () -> runSim(din, dout);
                    Thread bgthread = new Thread(sim);
                    bgthread.setDaemon(true);
                    bgthread.start();
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void runSim(DataInputStream in, DataOutputStream out) {
        while (true) {

            try {
                String z = in.readUTF();
                out.writeUTF("k");
                out.flush();
                System.out.println("gui "+z);
                StringTokenizer s = new StringTokenizer(z, "|");
              
                switch (s.nextToken()) {
                    case "EN":
                        Platform.runLater(() -> sta.getScene().setRoot(root1));
                        Platform.runLater(() -> caja.setText("Nuevo cliente agregado"));
                        Platform.runLater(() -> estado.setText("Caja: " + s.nextToken()));
                        Platform.runLater(() -> transaccion.setText(""));
                        Platform.runLater(() -> monto.setText(""));
                        Platform.runLater(() -> entrega.setText(""));
                        Platform.runLater(() -> monto_faltante.setText(""));
                        Platform.runLater(() -> adicional.setText(""));
                        Platform.runLater(() -> indicador_ciclos.setText("cilo: " + s.nextToken()));
                        break;
                    case "N":
                        Platform.runLater(() -> caja.setText("Nuevo cliente agregado"));
                        Platform.runLater(() -> estado.setText("Caja: " + s.nextToken()));
                        Platform.runLater(() -> transaccion.setText(""));
                        Platform.runLater(() -> monto.setText(""));
                        Platform.runLater(() -> entrega.setText(""));
                        Platform.runLater(() -> monto_faltante.setText(""));
                        Platform.runLater(() -> adicional.setText(""));
                        Platform.runLater(() -> indicador_ciclos.setText("cilo: " + s.nextToken()));
                        break;
                    case "C":
                        Platform.runLater(() -> caja.setText("Caja: " + s.nextToken()));
                        String st = s.nextToken();
                        switch (st) {
                            case "0":
                                Platform.runLater(() -> estado.setText("Sin clientes"));
                                Platform.runLater(() -> transaccion.setText(""));
                                Platform.runLater(() -> monto.setText(""));
                                Platform.runLater(() -> entrega.setText(""));
                                Platform.runLater(() -> monto_faltante.setText(""));
                                Platform.runLater(() -> adicional.setText(""));
                                break;
                            case "1":
                                Platform.runLater(() -> estado.setText("Atendiendo nuevo cleinte"));
                                switch (s.nextToken()) {
                                    case "0":
                                        Platform.runLater(() -> transaccion.setText("Deposito"));
                                        Platform.runLater(() -> monto.setText("Monto: " + s.nextToken()));
                                        Platform.runLater(() -> entrega.setText("Entrega: " + s.nextToken()));
                                        Platform.runLater(() -> monto_faltante.setText("Faltante: " + s.nextToken()));
                                        switch (s.nextToken()) {
                                            case "0":
                                                Platform.runLater(() -> adicional.setText(""));
                                                break;
                                            case "1":
                                                Platform.runLater(() -> adicional.setText("Cliente atendido"));
                                                break;

                                        }
                                        break;
                                    case "1":
                                        Platform.runLater(() -> transaccion.setText("Retiro"));
                                        String t = s.nextToken();
                                        switch (t) {
                                            case "-1":
                                                Platform.runLater(() -> monto.setText(""));
                                                Platform.runLater(() -> entrega.setText(""));
                                                s.nextToken();
                                                Platform.runLater(() -> monto_faltante.setText(""));
                                                s.nextToken();
                                                break;
                                            default:
                                                Platform.runLater(() -> monto.setText("Monto: " + t));
                                                Platform.runLater(() -> entrega.setText("Entrega: " + s.nextToken()));
                                                Platform.runLater(() -> monto_faltante.setText("Faltante: " + s.nextToken()));
                                        }
                                        switch (s.nextToken()) {
                                            case "0":
                                                Platform.runLater(() -> adicional.setText("Realizando cambio denominacion"));
                                                break;
                                            case "1":
                                                Platform.runLater(() -> adicional.setText("Reabasteciendo caja"));
                                                break;
                                            case "2":
                                                Platform.runLater(() -> adicional.setText("Cliente atendido"));
                                                break;
                                            case "3":
                                                Platform.runLater(() -> adicional.setText("Caja sin fondos"));
                                                break;
                                            case "4":
                                                Platform.runLater(() -> adicional.setText("Caja sin cambio"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                            case "2":
                                Platform.runLater(() -> estado.setText("Atendiendo cliente en ventanilla"));
                                switch (s.nextToken()) {
                                    case "0":
                                        Platform.runLater(() -> transaccion.setText("Deposito"));
                                        Platform.runLater(() -> monto.setText("Monto: " + s.nextToken()));
                                        Platform.runLater(() -> entrega.setText("Entrega: " + s.nextToken()));
                                        Platform.runLater(() -> monto_faltante.setText("Faltante: " + s.nextToken()));
                                        switch (s.nextToken()) {
                                            case "0":
                                                Platform.runLater(() -> adicional.setText(""));
                                                break;
                                            case "1":
                                                Platform.runLater(() -> adicional.setText("Cliente atendido"));
                                                break;

                                        }
                                        break;
                                    case "1":
                                        Platform.runLater(() -> transaccion.setText("Retiro"));
                                        String t = s.nextToken();
                                        switch (t) {
                                            case "-1":
                                                Platform.runLater(() -> monto.setText(""));
                                                Platform.runLater(() -> entrega.setText(""));
                                                s.nextToken();
                                                Platform.runLater(() -> monto_faltante.setText(""));
                                                s.nextToken();
                                                break;
                                            default:
                                                Platform.runLater(() -> monto.setText("Monto: " + t));
                                                Platform.runLater(() -> entrega.setText("Entrega: " + s.nextToken()));
                                                Platform.runLater(() -> monto_faltante.setText("Faltante: " + s.nextToken()));
                                        }
                                        switch (s.nextToken()) {
                                            case "0":
                                                Platform.runLater(() -> adicional.setText("Realizando cambio denominacion"));
                                                break;
                                            case "1":
                                                Platform.runLater(() -> adicional.setText("Reabasteciendo caja"));
                                                break;
                                            case "2":
                                                Platform.runLater(() -> adicional.setText("Cliente atendido"));
                                                break;
                                            case "3":
                                                Platform.runLater(() -> adicional.setText("Caja sin fondos"));
                                                break;
                                            case "4":
                                                Platform.runLater(() -> adicional.setText("Caja sin cambio"));
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                    case "":
                        //Platform.runLater(() -> );
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
