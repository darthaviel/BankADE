package gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainGUI extends Application implements Runnable {

    private Label[] prosout = new Label[10];
    private Label[][] resumout = new Label[7][16];

    private VBox[] vresumout = new VBox[7];
    private VBox[] base = new VBox[2];
    private VBox[] subroot = new VBox[2];

    private Pane[] espaciadores = new Pane[5];

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

    private Image x,
            start_background;

    private Image[][] cajero_img = new Image[6][2];

    private ImageView xbtn,
            cajero_img_view;

    private ProgressBar pb;

    private DoubleProperty db;

    @Override
    public void init() {
        try {
            Thread.sleep(1000);
            socket = new Socket("localhost", 2250);
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

        Path currentRelativePath = Paths.get("");
        String img_url = "file:///" + currentRelativePath.toAbsolutePath().toString() + System.getProperty("file.separator") + "img" + System.getProperty("file.separator");
        img_url = img_url.replace("\\", "/");
        x = new Image(img_url + "exit.png");
        start_background = new Image(img_url + "start_background.png", 620, 300, false, false);
        xbtn = new ImageView(x);
        xbtn.setFitWidth(15);
        xbtn.setFitHeight(15);

        for (int i = 0; i < 6; i++) {
            cajero_img[i][0] = new Image(img_url + (i + 1) + "_cajeros.png");
            cajero_img[i][1] = new Image(img_url + (i + 1) + "_cajeroc.png");
        }

        cajero_img_view = new ImageView();
        cajero_img_view.setFitWidth(300);
        cajero_img_view.setFitHeight(300);

        for (int i = 0; i < prosout.length; i++) {
            prosout[i] = new Label();
            prosout[i].setTextFill(Color.WHITE);
        }

        for (int i = 0; i < resumout.length; i++) {
            for (int j = 0; j < resumout[i].length; j++) {
                resumout[i][j] = new Label();
                resumout[i][j].setTextFill(Color.WHITE);
            }
        }

        for (int i = 0; i < btn.length; i++) {
            btn[i] = new Button();
        }

        for (int i = 0; i < espaciadores.length; i++) {
            espaciadores[i] = new Pane();
        }

        for (int i = 0; i < vresumout.length; i++) {
            vresumout[i] = new VBox(resumout[i][0], resumout[i][1], resumout[i][2], resumout[i][3], resumout[i][4], resumout[i][5], resumout[i][6], resumout[i][7], resumout[i][8], resumout[i][9], resumout[i][10], resumout[i][11], resumout[i][12], resumout[i][13], resumout[i][14], resumout[i][15]);
        }

        for (int i = 1; i < resumout.length; i++) {
            resumout[i][0].setAlignment(Pos.CENTER);
            resumout[i][0].setText("Caja " + (i));
            resumout[i][0].setMinWidth(100);
        }

        for (int i = 1; i < resumout[0].length; i++) {
            resumout[0][i].setAlignment(Pos.CENTER_LEFT);
        }

        for (int i = 1; i < resumout.length; i++) {
            for (int j = 1; j < resumout[i].length; j++) {
                resumout[i][j].setAlignment(Pos.CENTER_RIGHT);
            }
        }

        btn[0] = new Button("", xbtn);
        btn[0].setStyle("-fx-background-color: transparent");
        btn[0].setMaxSize(10, 10);

        espaciadores[0].setMinWidth(180);
        espaciadores[1].setMinHeight(100);
        ciclos = new TextField();
        ciclos.setAlignment(Pos.CENTER);
        ciclos.setTextFormatter(textFormatter);
        ciclos.setMinWidth(200);
        ciclos.setStyle("-fx-text-inner-color: #FFFFFF; -fx-background-color: #FFFFFF10");

        btn[1].setText("Iniciar Simulacion");
        btn[1].setTextFill(Color.WHITE);
        btn[1].setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF10"), CornerRadii.EMPTY, Insets.EMPTY)));
        btn[1].setTextAlignment(TextAlignment.CENTER);

        base[0] = new VBox(ciclos, btn[1]);
        base[0].setMinWidth(200);
        base[0].setSpacing(10);
        base[0].setAlignment(Pos.CENTER);
        root[0] = new HBox(btn[0], espaciadores[0], base[0]);
        root[0].setMinSize(620, 300);
        root[0].setMaxSize(620, 300);
        root[0].setBackground(new Background(new BackgroundImage(start_background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(620, 300, true, true, true, false))));

        espaciadores[3].setMinHeight(30);

        db = new SimpleDoubleProperty(.0);
        pb = new ProgressBar();
        pb.progressProperty().bind(db);
        pb.setMaxWidth(300);
        pb.setMinWidth(300);
        pb.setStyle("-fx-control-inner-background: #474b59; -fx-accent: #FFFFFF40");

        base[1] = new VBox(espaciadores[2], prosout[0], prosout[1], prosout[2], prosout[3], prosout[4], prosout[5], prosout[6], espaciadores[3], prosout[7], pb);
        base[1].setSpacing(10);
        base[1].setMinSize(300, 300);
        base[1].setMaxSize(300, 300);

        btn[2] = new Button("", xbtn);
        btn[2].setStyle("-fx-background-color: transparent");

        root[1] = new HBox(btn[2], base[1], cajero_img_view);
        root[1].setStyle("-fx-background-color: #474b59");
        root[1].setSpacing(10);
        root[1].setMinSize(620, 300);
        root[1].setMaxSize(620, 300);

        btn[4] = new Button("", xbtn);
        btn[4].setStyle("-fx-background-color: transparent");

        resumout[0][1].setText("Clientes atendidos");
        resumout[0][2].setText("Clientes en espera");
        resumout[0][3].setText("Efectivo en caja");
        resumout[0][4].setText("Total monto retiros");
        resumout[0][5].setText("Total monto depositos");
        resumout[0][6].setText("Refinanciamiento");
        resumout[0][7].setText("Billetes 1");
        resumout[0][8].setText("Billetes 2");
        resumout[0][9].setText("Billetes 5");
        resumout[0][10].setText("Billetes 10");
        resumout[0][11].setText("Billetes 20");
        resumout[0][12].setText("Billetes 50");
        resumout[0][13].setText("Billetes 100");
        resumout[0][14].setText("Billetes 500");
        resumout[0][15].setText("L");

        root[2] = new HBox(btn[4], vresumout[0], vresumout[1], vresumout[2], vresumout[3], vresumout[4], vresumout[5], vresumout[6]);
        root[2].setSpacing(10);
        root[2].setBackground(new Background(new BackgroundFill(Color.rgb(71, 75, 89), CornerRadii.EMPTY, Insets.EMPTY)));

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
                try {
                    dout.writeUTF("x");
                    dout.flush();
                } catch (Exception e) {
                }
            }
        });

        btn[1].setOnAction(e -> startSim());

        btn[2].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
                try {
                    dout.writeUTF("x");
                    dout.flush();
                } catch (Exception e) {
                }
            }
        });

        btn[3].setOnAction(e -> {
            Platform.runLater(() -> sta1.hide());
            Platform.runLater(() -> scene2 = new Scene(root[2]));
            Platform.runLater(() -> sta2 = new Stage());
            Platform.runLater(() -> sta2.setScene(scene2));
            Platform.runLater(() -> sta2.initStyle(StageStyle.TRANSPARENT));
            Platform.runLater(() -> sta2.show());
            Platform.runLater(() -> sta.hide());
        });

        btn[4].setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Platform.exit();
                try {
                    dout.writeUTF("x");
                    dout.flush();
                } catch (Exception e) {
                }
            }
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
            try {
                if (Integer.parseInt(s) > 0) {
                    double m = Integer.parseInt(s);
                    System.out.println(m);
                    m = 1 / (6 * m);
                    final double n = m;
                    System.out.println(n);
                    try {
                        dout.writeUTF(s);
                        dout.flush();
                        Runnable sim = () -> runSim(din, dout, n);
                        Thread bgthread = new Thread(sim);
                        bgthread.setDaemon(true);
                        bgthread.start();
                    } catch (IOException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception e) {
                ciclos.setText("");
            }
        }
    }

    private void runSim(DataInputStream in, DataOutputStream out, double razons) {
        double control = 0;
        while (true) {

            try {
                String z = in.readUTF();
                System.out.println("gui " + z);
                StringTokenizer s = new StringTokenizer(z, "|");
                String[] c = new String[8];
                for (int i = 0; i < 8; i++) {
                    c[i] = s.nextToken();
                }

                switch (c[0]) {
                    case "EN":
                        out.writeUTF("k");
                        out.flush();
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
                        out.writeUTF("k");
                        out.flush();
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
                        out.writeUTF("k");
                        out.flush();

                        control += razons;
                        System.out.println(control);
                        final double ban = control;
                        Platform.runLater(() -> db.set(ban));
                        Platform.runLater(() -> prosout[0].setText("Caja: " + c[1]));
                        switch (c[2]) {
                            case "0":
                                Platform.runLater(() -> prosout[1].setText("Sin clientes"));
                                Platform.runLater(() -> cajero_img_view.setImage(cajero_img[(Integer.parseInt(c[1]) - 1)][0]));
                                Platform.runLater(() -> prosout[2].setText(""));
                                Platform.runLater(() -> prosout[3].setText(""));
                                Platform.runLater(() -> prosout[4].setText(""));
                                Platform.runLater(() -> prosout[5].setText(""));
                                Platform.runLater(() -> prosout[6].setText(""));
                                break;
                            case "1":
                                Platform.runLater(() -> prosout[1].setText("Atendiendo nuevo cleinte"));
                                Platform.runLater(() -> cajero_img_view.setImage(cajero_img[(Integer.parseInt(c[1]) - 1)][1]));
                                break;
                            case "2":
                                Platform.runLater(() -> prosout[1].setText("Atendiendo cliente en ventanilla"));
                                Platform.runLater(() -> cajero_img_view.setImage(cajero_img[(Integer.parseInt(c[1]) - 1)][1]));
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
                        Platform.runLater(() -> prosout[8].setAlignment(Pos.CENTER));
                        Platform.runLater(() -> prosout[8].setMinWidth(200));
                        Platform.runLater(() -> prosout[8].setMinHeight(50));
                        Platform.runLater(() -> btn[3].setText("Ver Estadisticas"));
                        Platform.runLater(() -> btn[3].setAlignment(Pos.CENTER));
                        Platform.runLater(() -> btn[3].setTextFill(Color.WHITE));
                        Platform.runLater(() -> btn[3].setBackground(new Background(new BackgroundFill(Color.web("#FFFFFF40"), CornerRadii.EMPTY, Insets.EMPTY))));
                        Platform.runLater(() -> subroot[0] = new VBox(prosout[8], btn[3]));
                        Platform.runLater(() -> subroot[0].setMinHeight(100));
                        Platform.runLater(() -> subroot[0].setAlignment(Pos.CENTER));
                        Platform.runLater(() -> subroot[0].setBackground(new Background(new BackgroundFill(Color.web("#636775"), CornerRadii.EMPTY, Insets.EMPTY))));
                        Platform.runLater(() -> scene1 = new Scene(subroot[0]));
                        Platform.runLater(() -> sta1 = new Stage());
                        Platform.runLater(() -> sta1.initStyle(StageStyle.TRANSPARENT));
                        Platform.runLater(() -> sta1.setScene(scene1));
                        Platform.runLater(() -> sta1.show());
                        for (int i = 1; i < (c.length - 1); i++) {
                            StringTokenizer ss = new StringTokenizer(c[i], "#");
                            resumout[i][1].setText(ss.nextToken());
                            resumout[i][2].setText(ss.nextToken());
                            resumout[i][3].setText(ss.nextToken());
                            resumout[i][4].setText(ss.nextToken());
                            resumout[i][5].setText(ss.nextToken());
                            resumout[i][6].setText(ss.nextToken());
                            resumout[i][7].setText(ss.nextToken());
                            resumout[i][8].setText(ss.nextToken());
                            resumout[i][9].setText(ss.nextToken());
                            resumout[i][10].setText(ss.nextToken());
                            resumout[i][11].setText(ss.nextToken());
                            resumout[i][12].setText(ss.nextToken());
                            resumout[i][13].setText(ss.nextToken());
                            resumout[i][14].setText(ss.nextToken());
                        }
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
