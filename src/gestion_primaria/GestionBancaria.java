package gestion_primaria;

import elementos_de_banco.Caja;
import elementos_de_banco.Cliente;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import tda.lista.LISTA;

/**
 *
 * @author l
 */
public class GestionBancaria implements Runnable {

    private int numero_ciclos;
    private LISTA cajas = new LISTA();

    @Override
    public void run() {
        init();
        menu();
        ciclo();
        estadisticas();
    }

    private void init() {
        for (int i = 0; i < 6; i++) {
            cajas.INSERTA(new Caja(cajas.FIN()), cajas.FIN());
        }
    }

    private void menu() {
        Scanner scanint = new Scanner(System.in);
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
        }
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
        for (int i = 0; i < 1; i++) {
            System.out.println(i);
            caja_en_gestion = (Caja) cajas.RECUPERA(i+1);
            cajas.SUPRIME(i+1);
            caja_en_gestion.mostrarEstadistica();
            cajas.INSERTA(caja_en_gestion, i+1);
            
        }
    }

}
