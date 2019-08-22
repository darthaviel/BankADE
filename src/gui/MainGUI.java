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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainGUI extends Application implements Runnable {

    private Label[] prosout = new Label[10];
    private Label[][] resumout = new Label[7][16];

    private VBox[] vresumout = new VBox[7];
    private VBox[] base = new VBox[2];
    private VBox[] subroot = new VBox[2];

    private Pane[] espaciadores = new Pane[4];
    private Pane[] impane = new Pane[3];

    private TextField ciclos;

    private HBox[] root = new HBox[3];

    private Button[] btn = new Button[7];

    private Socket socket;

    private DataOutputStream dout;
    private DataInputStream din;

    private Scene scene;
    private Scene scene1;
    private Scene scene2;

    private Stage sta;
    private Stage sta1;
    private Stage sta2;

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

        for (int i = 0; i < prosout.length; i++) {
            prosout[i] = new Label();
        }

        for (int i = 0; i < resumout.length; i++) {
            for (int j = 0; j < resumout[i].length; j++) {
                resumout[i][j] = new Label();
            }
        }

        for (int i = 0; i < btn.length; i++) {
            btn[i] = new Button();
        }

        for (int i = 0; i < espaciadores.length; i++) {
            espaciadores[i] = new Pane();
        }

        for (int i = 0; i < impane.length; i++) {
            impane[i] = new Pane();
        }

        for (int i = 0; i < vresumout.length; i++) {
            vresumout[i] = new VBox(resumout[i][0], resumout[i][1], resumout[i][2], resumout[i][3], resumout[i][4], resumout[i][5], resumout[i][6], resumout[i][7], resumout[i][8], resumout[i][9], resumout[i][10], resumout[i][11], resumout[i][12], resumout[i][13], resumout[i][14], resumout[i][15]);
        }

        btn[0].setAlignment(Pos.CENTER);
        btn[0].setText("X");
        btn[0].setMinSize(30, 30);

        espaciadores[0].setMinWidth(180);
        espaciadores[1].setMinHeight(100);
        ciclos = new TextField();
        ciclos.setAlignment(Pos.CENTER);
        ciclos.setTextFormatter(textFormatter);
        ciclos.setMinWidth(200);
        btn[1].setText("Iniciar Simulacion");
        btn[1].setMaxWidth(200);
        btn[1].setTextAlignment(TextAlignment.CENTER);

        base[0] = new VBox(espaciadores[1], ciclos, btn[1]);
        base[0].setMinWidth(200);
        base[0].setSpacing(10);
        root[0] = new HBox(btn[0], espaciadores[0], base[0]);
        root[0].setMinSize(620, 300);
        root[0].setMaxSize(620, 300);

        espaciadores[3].setMinHeight(30);

        base[1] = new VBox(espaciadores[2], prosout[0], prosout[1], prosout[2], prosout[3], prosout[4], prosout[5], prosout[6], espaciadores[3], prosout[7]);
        base[1].setSpacing(10);
        base[1].setMinSize(300, 300);
        base[1].setMaxSize(300, 300);

        impane[0].setMinSize(300, 300);
        impane[0].setMaxSize(300, 300);

        btn[2].setText("X");
        btn[2].setTextAlignment(TextAlignment.CENTER);

        impane[0].setStyle("-fx-background-color: #000000");

        root[1] = new HBox(btn[2], base[1], impane[0]);
        root[1].setStyle("-fx-background-color: #FFFFFF");
        root[1].setSpacing(10);
        root[1].setMinSize(620, 300);
        root[1].setMaxSize(620, 300);

    }

    @Override
    public void run() {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        btn[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
            }
        });

        btn[1].setOnAction(e -> startSim());

        btn[2].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
            }
        });
        
        btn[3].setOnAction(e -> {
             Platform.runLater(() -> sta1.hide());
              Platform.runLater(() ->sta.getScene().setRoot(root[2])); 
        });

        scene = new Scene(root[0]);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        sta = stage;
        sta.initStyle(StageStyle.TRANSPARENT);
        sta.setScene(scene);
        sta.show();
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
                System.out.println("gui " + z);
                StringTokenizer s = new StringTokenizer(z, "|");
                String[] c = new String[8];
                for (int i = 0; i < 8; i++) {
                    c[i] = s.nextToken();
                }

                switch (c[0]) {
                    case "EN":
                        Platform.runLater(() -> sta.getScene().setRoot(root[1]));
                        Platform.runLater(() -> prosout[0].setText("Nuevo cliente agregado"));
                        Platform.runLater(() -> prosout[1].setText("Caja: " + c[1]));
                        Platform.runLater(() -> prosout[2].setText(""));
                        Platform.runLater(() -> prosout[3].setText(""));
                        Platform.runLater(() -> prosout[4].setText(""));
                        Platform.runLater(() -> prosout[5].setText(""));
                        Platform.runLater(() -> prosout[6].setText(""));
                        Platform.runLater(() -> prosout[7].setText("Ciclo: " + c[2]));
                        break;
                    case "N":
                        Platform.runLater(() -> prosout[0].setText("Nuevo cliente agregado"));
                        Platform.runLater(() -> prosout[1].setText("Caja: " + c[1]));
                        Platform.runLater(() -> prosout[2].setText(""));
                        Platform.runLater(() -> prosout[3].setText(""));
                        Platform.runLater(() -> prosout[4].setText(""));
                        Platform.runLater(() -> prosout[5].setText(""));
                        Platform.runLater(() -> prosout[6].setText(""));
                        Platform.runLater(() -> prosout[7].setText("Ciclo: " + c[2]));
                        break;
                    case "C":
                        Platform.runLater(() -> prosout[0].setText("Caja: " + c[1]));
                        switch (c[2]) {
                            case "0":
                                Platform.runLater(() -> prosout[1].setText("Sin clientes"));
                                Platform.runLater(() -> prosout[2].setText(""));
                                Platform.runLater(() -> prosout[3].setText(""));
                                Platform.runLater(() -> prosout[4].setText(""));
                                Platform.runLater(() -> prosout[5].setText(""));
                                Platform.runLater(() -> prosout[6].setText(""));
                                break;
                            case "1":
                                Platform.runLater(() -> prosout[1].setText("Atendiendo nuevo cleinte"));
                                break;
                            case "2":
                                Platform.runLater(() -> prosout[1].setText("Atendiendo cliente en ventanilla"));
                                break;
                        }
                        switch (c[3]) {
                            case "0":
                                Platform.runLater(() -> prosout[2].setText("Deposito"));
                                Platform.runLater(() -> prosout[3].setText("Monto: " + c[4]));
                                Platform.runLater(() -> prosout[4].setText("Entrega: " + c[5]));
                                Platform.runLater(() -> prosout[5].setText("Faltante: " + c[6]));
                                switch (c[7]) {
                                    case "0":
                                        Platform.runLater(() -> prosout[6].setText(""));
                                        break;
                                    case "1":
                                        Platform.runLater(() -> prosout[6].setText("Cliente atendido"));
                                        break;
                                }
                                break;
                            case "1":
                                Platform.runLater(() -> prosout[2].setText("Retiro"));
                                switch (c[4]) {
                                    case "-1":
                                        Platform.runLater(() -> prosout[3].setText(""));
                                        Platform.runLater(() -> prosout[4].setText(""));
                                        Platform.runLater(() -> prosout[5].setText(""));
                                        break;
                                    default:
                                        Platform.runLater(() -> prosout[3].setText("Monto: " + c[4]));
                                        Platform.runLater(() -> prosout[4].setText("Entrega: " + c[5]));
                                        Platform.runLater(() -> prosout[5].setText("Faltante: " + c[6]));
                                }
                                switch (c[7]) {
                                    case "0":
                                        Platform.runLater(() -> prosout[6].setText("Realizando cambio denominacion"));
                                        break;
                                    case "1":
                                        Platform.runLater(() -> prosout[6].setText("Reabasteciendo caja"));
                                        break;
                                    case "2":
                                        Platform.runLater(() -> prosout[6].setText("Cliente atendido"));
                                        break;
                                    case "3":
                                        Platform.runLater(() -> prosout[6].setText("Caja sin fondos"));
                                        break;
                                    case "4":
                                        Platform.runLater(() -> prosout[6].setText("Caja sin cambio"));
                                        break;
                                }
                                break;
                        }
                        break;
                    case "R":
                        Platform.runLater(() -> prosout[8].setText("Simulacion Terminada"));
                        Platform.runLater(() -> btn[3].setText("Ver Estadisticas"));
                        Platform.runLater(() -> subroot[0] = new VBox(prosout[8], btn[3]));
                        Platform.runLater(() -> scene1 = new Scene(subroot[0]));
                        Platform.runLater(() -> sta1 = new Stage());
                        Platform.runLater(() -> sta1.initStyle(StageStyle.TRANSPARENT));
                        Platform.runLater(() -> sta1.setScene(scene1));
                        Platform.runLater(() -> sta1.show());
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
