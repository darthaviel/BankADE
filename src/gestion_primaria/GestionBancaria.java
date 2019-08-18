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
import tda.lista.LISTA;

public class GestionBancaria implements Runnable {

    private int numero_ciclos;
    private LISTA cajas = new LISTA();
    Socket socket;
    ServerSocket server;
    InputStream in;
    OutputStream out;
    DataOutputStream dout;
    DataInputStream din;

    int PORT = 1025;

    String comunicacion;
    //Nuevo cliente
    // EN|... variante para cambio de root node
    // N|#ciclo|#caja
    //Caja atendiendo
    // C|#caja|#estado|#transaccion|#monto|#entrega|#faltante|#adicionales

    @Override
    public void run() {
        init();
        ciclo();
        estadisticas();
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
        try {
            String s;
            s = din.readUTF();
            System.out.println(s);
            numero_ciclos = Integer.parseInt(s);
            comunicacion = "E";
        } catch (IOException ex) {
            Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
        int numero_caja;
        Caja caja_en_gestion;
        for (int j = 0; numero_ciclos > j; j++) {
            numero_caja = ((int) (Math.random() * 6)) + 1;
            caja_en_gestion = (Caja) cajas.RECUPERA(numero_caja);
            caja_en_gestion.agregarCliente(new Cliente(numero_caja, (int) (Math.random() * 2), ((int) (Math.random() * 10000)) + 1));
            System.out.println("\nNUEVO CLIENTE AGREGADO A LA CAJA " + numero_caja + "\n");
            comunicacion = comunicacion + "N|" + j + "|" + numero_caja;
            try {
                dout.writeUTF(comunicacion);
                dout.flush();
            } catch (IOException ex) {
                Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }

            cajas.SUPRIME(numero_caja);
            cajas.INSERTA(caja_en_gestion, numero_caja);
            for (int i = 0; i < 6; i++) {
                caja_en_gestion = (Caja) cajas.RECUPERA(i + 1);
                cajas.SUPRIME(i + 1);
                caja_en_gestion.atender();
                cajas.INSERTA(caja_en_gestion, i + 1);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void estadisticas() {
        Caja caja_en_gestion = null;
        System.out.println("Ciclos concluidos\n\n\n");
        for (int i = 0; i < 6; i++) {
            caja_en_gestion = (Caja) cajas.RECUPERA(i + 1);
            cajas.SUPRIME(i + 1);
            caja_en_gestion.mostrarEstadistica();
            cajas.INSERTA(caja_en_gestion, i + 1);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
