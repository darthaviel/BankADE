package gestion_primaria;

import elementos_de_banco.Caja;
import elementos_de_banco.Cliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import alt_tda.lista.LISTA;

public class GestionBancaria implements Runnable {

    private int numero_ciclos;
    private int delay = 3000;
    private LISTA cajas = new LISTA();
    Socket socket;
    ServerSocket server;
    InputStream in;
    OutputStream out;
    DataOutputStream dout;
    DataInputStream din;

    int PORT = 2250;

    String comunicacion;

    @Override
    public void run() {
        init();
        while (true) {
            ciclo();
            estadisticas();
        }
    }

    private void init() {
        try {
            server = new ServerSocket(PORT);
            socket = server.accept();
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < 6; i++) {
            cajas.INSERTA(new Caja(cajas.FIN()), cajas.FIN());
        }
    }

    private void ciclo() {
        String s = "";
        while (true) {
            try {
                s = din.readUTF();
                if (s.equals("x")) {
                    System.exit(0);
                } else {
                    numero_ciclos = Integer.parseInt(s);
                    comunicacion = "E";
                }
                break;
            } catch (IOException ex) {

            }
        }
        int numero_caja;
        Caja caja_en_gestion;
        for (int j = 0; numero_ciclos > j; j++) {
            numero_caja = ((int) (Math.random() * 6)) + 1;
            caja_en_gestion = (Caja) cajas.RECUPERA(numero_caja);
            caja_en_gestion.agregarCliente(new Cliente(numero_caja, (int) (Math.random() * 2), ((int) (Math.random() * 10000)) + 1));
            System.out.println("\nNUEVO CLIENTE AGREGADO A LA CAJA " + numero_caja + "\n");
            comunicacion = comunicacion + "N|" + numero_caja + "|" + (j + 1) + "|0|0|0|0|0";
            try {
                dout.writeUTF(comunicacion);
                dout.flush();
                if (din.readUTF().equals("x")) {
                    System.exit(0);
                }
                Thread.sleep(delay);
            } catch (IOException ex) {
                Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(comunicacion);
            comunicacion = "";
            cajas.SUPRIME(numero_caja);
            cajas.INSERTA(caja_en_gestion, numero_caja);
            for (int i = 0; i < 6; i++) {
                caja_en_gestion = (Caja) cajas.RECUPERA(i + 1);
                cajas.SUPRIME(i + 1);
                caja_en_gestion.atender();
                cajas.INSERTA(caja_en_gestion, i + 1);
                comunicacion = "C|" + (i + 1) + "|" + caja_en_gestion.resumenGestion();
                try {
                    dout.writeUTF(comunicacion);
                    dout.flush();
                    System.out.println(comunicacion);
                    comunicacion = "";
                    if (din.readUTF().equals("x")) {
                        System.exit(0);
                    }
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void estadisticas() {
        comunicacion = "R|";
        Caja caja_en_gestion = null;
        System.out.println("Ciclos concluidos\n\n\n");
        for (int i = 0; i < 6; i++) {
            caja_en_gestion = (Caja) cajas.RECUPERA(i + 1);
            cajas.SUPRIME(i + 1);
            caja_en_gestion.mostrarEstadistica();
            comunicacion = comunicacion + caja_en_gestion.estadisticaCaja() + "|";
            cajas.INSERTA(caja_en_gestion, i + 1);
        }
        comunicacion = comunicacion + "T";
        try {
            dout.writeUTF(comunicacion);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
