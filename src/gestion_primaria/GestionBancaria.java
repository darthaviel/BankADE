package gestion_primaria;

import elementos_de_banco.Caja;
import elementos_de_banco.Cliente;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
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

    @Override
    public void run() {
        init();
        menu();
        ciclo();
        estadisticas();
    }

    private void init() {
        try {
            server = new ServerSocket(PORT);
            socket = server.accept();
            System.out.println("conectado con interfaz");
            in = new BufferedInputStream(socket.getInputStream());
            out = new BufferedOutputStream(socket.getOutputStream());
            din = new DataInputStream(in);
            dout = new DataOutputStream(out);
        } catch (IOException ex) {
            Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < 6; i++) {
            cajas.INSERTA(new Caja(cajas.FIN()), cajas.FIN());
        }
    }

    private void menu() {
        try {
            String s;
            s = din.readUTF();
            System.out.println(s);
            numero_ciclos = Integer.parseInt(s);
        } catch (IOException ex) {
            Logger.getLogger(GestionBancaria.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*Scanner scanint = new Scanner(System.in);
            while (true) {
            System.out.println("Cantidad de ciclos");
            try {
            numero_ciclos = scanint.nextInt();
            if (numero_ciclos > 0) {
            break;
            }
            } catch (Exception e) {
            scanint.next();
            }
            System.out.println("Ingrese una cantidad valida \n\n");
            }*/

    }

    private void ciclo() {
        int numero_caja;
        Caja caja_en_gestion;
        for (; numero_ciclos > 0; numero_ciclos--) {
            numero_caja = ((int) (Math.random() * 6)) + 1;
            caja_en_gestion = (Caja) cajas.RECUPERA(numero_caja);
            caja_en_gestion.agregarCliente(new Cliente(numero_caja, (int) (Math.random() * 2), ((int) (Math.random() * 10000)) + 1));
            System.out.println("\nNUEVO CLIENTE AGREGADO A LA CAJA " + numero_caja + "\n");
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
