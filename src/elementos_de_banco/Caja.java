/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elementos_de_banco;

import tda.cola.COLA;
import tda.pila.PILA;

/**
 *
 * @author l
 */
public class Caja {

    private PILA b500 = new PILA();
    private PILA b100 = new PILA();
    private PILA b50 = new PILA();
    private PILA b20 = new PILA();
    private PILA b10 = new PILA();
    private PILA b5 = new PILA();
    private PILA b2 = new PILA();
    private PILA b1 = new PILA();

    private COLA clientes_en_cola = new COLA();

    private int clientes_atendidos = 0;

    private int caja;

    private Cliente cliente_en_ventanilla = null;

    private boolean fondos = true;

    public Caja() {
    }

    public Caja(int caja) {
        this.caja = caja;
        distribuir_billetes(100000);
    }

    public void agregarCliente(Cliente cliente) {
        clientes_en_cola.PONE_EN_COLA(cliente);
    }

    public void atender() {
        if (cliente_en_ventanilla != null) {
            switch (cliente_en_ventanilla.getTransaccion()) {
                case 0:
                    if (cliente_en_ventanilla.getMonto_de_transaccion() > 500) {
                        distribuir_billetes(500);
                        cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 500);
                    } else {
                        distribuir_billetes(cliente_en_ventanilla.getMonto_de_transaccion());
                        cliente_en_ventanilla = null;
                    }
                    break;
                case 1:
                    if (fondos) {
                        if (cliente_en_ventanilla.getMonto_de_transaccion() > 500) {
                            if (!entregar_billetes(500)) {

                            } else {

                            }
                        } else if (entregar_billetes(cliente_en_ventanilla.getMonto_de_transaccion())) {

                        }
                    }

                    break;
            }
            if (cliente_en_ventanilla.getMonto_de_transaccion() == 0) {
                cliente_en_ventanilla = null;

            }
        } else if (!clientes_en_cola.VACIA()) {
            cliente_en_ventanilla = (Cliente) clientes_en_cola.FRENTE();
            clientes_en_cola.SACA_DE_COLA();
            switch (cliente_en_ventanilla.getTransaccion()) {
                case 0:
                    if (cliente_en_ventanilla.getMonto_de_transaccion() > 500) {
                        distribuir_billetes(500);
                        cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 500);
                    } else {
                        distribuir_billetes(cliente_en_ventanilla.getMonto_de_transaccion());
                        cliente_en_ventanilla = null;
                    }
                    break;
                case 1:
                    if (fondos) {
                        if (cliente_en_ventanilla.getMonto_de_transaccion() > 500) {
                            if (!entregar_billetes(500)) {

                            } else {

                            }
                        } else if (entregar_billetes(cliente_en_ventanilla.getMonto_de_transaccion())) {

                        }
                    }

                    break;

            }
        }
    }

    private void distribuir_billetes(int i) {
        while (i != 0) {
            if (i >= 500) {
                b500.METE(new Billete(500));
                i -= 500;
            }
            if (i >= 100) {
                b100.METE(new Billete(100));
                i -= 100;
            }
            if (i >= 50) {
                b50.METE(new Billete(50));
                i -= 50;
            }
            if (i >= 20) {
                b20.METE(new Billete(20));
                i -= 20;
            }
            if (i >= 10) {
                b10.METE(new Billete(10));
                i -= 10;
            }
            if (i >= 5) {
                b5.METE(new Billete(5));
                i -= 5;
            }
            if (i >= 2) {
                b2.METE(new Billete(2));
                i -= 2;
            }
            if (i >= 1) {
                b1.METE(new Billete(1));
                i -= 1;
            }
        }

    }

    private boolean entregar_billetes(int i) {
        while (i != 0) {
            if ((!b500.VACIA()) && (i >= 500)) {
                b500.SACA();
                i -= 500;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 500);
            }
            if ((!b100.VACIA()) && (i >= 100)) {
                b100.SACA();
                i -= 100;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 100);
            }
            if ((!b50.VACIA()) && (i >= 50)) {
                b50.SACA();
                i -= 50;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 50);
            }
            if ((!b20.VACIA()) && (i >= 20)) {
                b20.SACA();
                i -= 20;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 20);
            }
            if ((!b10.VACIA()) && (i >= 10)) {
                b10.SACA();
                i -= 10;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 10);
            }
            if ((!b5.VACIA()) && (i >= 5)) {
                b5.SACA();
                i -= 5;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 5);
            }
            if ((!b2.VACIA()) && (i >= 2)) {
                b2.SACA();
                i -= 2;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 2);
            }
            if ((!b1.VACIA()) && (i >= 1)) {
                b1.SACA();
                i -= 1;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 1);
            }

            if (b1.VACIA() && b2.VACIA() && b5.VACIA() && b10.VACIA() && b20.VACIA() && b50.VACIA() && b100.VACIA() && b500.VACIA() && (cliente_en_ventanilla.getMonto_de_transaccion() > 0)) {
                fondos = false;
                return false;
            }
        }
        return true;
    }

}
