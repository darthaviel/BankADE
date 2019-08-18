package gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
        caja = new Label("CAJA");
        estado = new Label("ESTADO");
        transaccion = new Label("TRANSACCION");
        monto = new Label("MONTO DE TRANSACCION");
        entrega = new Label("MONTO ENTREGADO");
        monto_faltante = new Label("MONTO FALTANTE");
        indicador_ciclos = new Label("CICLOS");
        espaciador = new Pane();
        espaciador.setMinHeight(30);
        text = new VBox(spacer0, caja, estado, transaccion, monto, entrega, monto_faltante, espaciador, indicador_ciclos);
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
            String s;
            try {
                s = in.readUTF();
                if (s.contains("E")) {
                    Platform.runLater(() -> sta.getScene().setRoot(root1));
                }
            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
