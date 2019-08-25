package elementos_de_banco;

import alt_tda.cola.COLA;
import alt_tda.pila.PILA;

public class Caja {

    private PILA b500 = new PILA();
    private PILA b100 = new PILA();
    private PILA b50 = new PILA();
    private PILA b20 = new PILA();
    private PILA b10 = new PILA();
    private PILA b5 = new PILA();
    private PILA b2 = new PILA(); 
    private PILA b1 = new PILA();
    private String resumen,
            adicionales,
            estadistica;

    private COLA clientes_en_cola = new COLA();

    private int caja,
            clientes_atendidos,
            clientes_en_espera,
            detalle_500,
            detalle_100,
            detalle_50,
            detalle_20,
            detalle_10,
            detalle_5,
            detalle_2,
            detalle_1;

    private long monto_retiros,
            monto_depositos = -100000;

    private boolean cambio_denominacion = false;

    private Cliente cliente_en_ventanilla = null;

    public Caja() {
    }

    public Caja(int caja) {
        this.caja = caja;
        distribuir_billetes(100000, false);
    }

    public void agregarCliente(Cliente cliente) {
        clientes_en_cola.PONE_EN_COLA(cliente);
    }

    public void atender() {
        System.out.println("\nAtendiendo en caja " + caja);
        if (cliente_en_ventanilla != null) {
            System.out.println("Atendiendo cliente en ventanilla.");
            resumen = "2|";
            atenderCaso();

        } else if (!clientes_en_cola.VACIA()) {
            cliente_en_ventanilla = (Cliente) clientes_en_cola.FRENTE();
            clientes_en_cola.SACA_DE_COLA();
            System.out.println("Atendiendo nuevo cliente.");
            resumen = "1|";
            atenderCaso();
        } else {
            System.out.println("Sin clientes para atender.");
            resumen = "0|-1|0|0|0|0";
        }
    }

    private void distribuir_billetes(long i, boolean j) {
        if (j) {
            while (i != 0) {
                if (i >= 1) {
                    b1.METE(new Billete(1));
                    i -= 1;
                }
                if (i >= 2) {
                    b2.METE(new Billete(2));
                    i -= 2;
                }
                if (i >= 5) {
                    b5.METE(new Billete(5));
                    i -= 5;
                }
                if (i >= 10) {
                    b10.METE(new Billete(10));
                    i -= 10;
                }
                if (i >= 20) {
                    b20.METE(new Billete(20));
                    i -= 20;
                }
                if (i >= 50) {
                    b50.METE(new Billete(50));
                    i -= 50;
                }
                if (i >= 100) {
                    b100.METE(new Billete(100));
                    i -= 100;
                }
                if (i >= 500) {
                    b500.METE(new Billete(500));
                    i -= 500;
                }
            }
        } else {
            while (i != 0) {
                if (i >= 500) {
                    b500.METE(new Billete(500));
                    i -= 500;
                    monto_depositos += 500;
                }
                if (i >= 100) {
                    b100.METE(new Billete(100));
                    i -= 100;
                    monto_depositos += 100;
                }
                if (i >= 50) {
                    b50.METE(new Billete(50));
                    i -= 50;
                    monto_depositos += 50;
                }
                if (i >= 20) {
                    b20.METE(new Billete(20));
                    i -= 20;
                    monto_depositos += 20;
                }
                if (i >= 10) {
                    b10.METE(new Billete(10));
                    i -= 10;
                    monto_depositos += 10;
                }
                if (i >= 5) {
                    b5.METE(new Billete(5));
                    i -= 5;
                    monto_depositos += 5;
                }
                if (i >= 2) {
                    b2.METE(new Billete(2));
                    i -= 2;
                    monto_depositos += 2;
                }
                if (i >= 1) {
                    b1.METE(new Billete(1));
                    i -= 1;
                    monto_depositos += 1;
                }
            }
        }
    }

    private boolean entregar_billetes(int i) {
        int ii = i;
        while (i != 0) {
            if ((!b500.VACIA()) && (i >= 500)) {
                b500.SACA();
                i -= 500;
                monto_retiros += 500;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 500);
            }
            if ((!b100.VACIA()) && (i >= 100)) {
                b100.SACA();
                i -= 100;
                monto_retiros += 100;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 100);
            }
            if ((!b50.VACIA()) && (i >= 50)) {
                b50.SACA();
                i -= 50;
                monto_retiros += 50;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 50);
            }
            if ((!b20.VACIA()) && (i >= 20)) {
                b20.SACA();
                i -= 20;
                monto_retiros += 20;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 20);
            }
            if ((!b10.VACIA()) && (i >= 10)) {
                b10.SACA();
                i -= 10;
                monto_retiros += 10;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 10);
            }
            if ((!b5.VACIA()) && (i >= 5)) {
                b5.SACA();
                i -= 5;
                monto_retiros += 5;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 5);
            }
            if ((!b2.VACIA()) && (i >= 2)) {
                b2.SACA();
                i -= 2;
                monto_retiros += 2;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 2);
            }
            if ((!b1.VACIA()) && (i >= 1)) {
                b1.SACA();
                i -= 1;
                monto_retiros += 1;
                cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 1);
            }

            if (b1.VACIA() && b2.VACIA() && b5.VACIA() && b10.VACIA() && b20.VACIA() && b50.VACIA() && b100.VACIA() && b500.VACIA() && (i > 0)) {
                System.out.println("Caja sin fondos, solicitando fondos a la boveda. Por favor espere un momento...");
                adicionales = "3";
                return false;
            }

            if (ii == i) {
                System.out.println("Petición de cambio de denominaciones, espere un momento...");
                cambio_denominacion = true;
                adicionales = "4";
                return false;
            } else {
                ii = i;
            }
        }
        return true;
    }

    private void atenderCaso() {
        switch (cliente_en_ventanilla.getTransaccion()) {
            case 0:
                System.out.println("Transacción: Depósito\n"
                        + "Monto faltante: " + cliente_en_ventanilla.getMonto_de_transaccion());
                resumen = resumen + "0|" + cliente_en_ventanilla.getMonto_de_transaccion() + "|";
                if (cliente_en_ventanilla.getMonto_de_transaccion() > 500) {
                    distribuir_billetes(500, false);
                    cliente_en_ventanilla.setMonto_de_transaccion(cliente_en_ventanilla.getMonto_de_transaccion() - 500);
                    resumen = resumen + "500|" + cliente_en_ventanilla.getMonto_de_transaccion() + "|0";
                    System.out.println("Cliente en atención.\n"
                            + "Monto faltante: " + cliente_en_ventanilla.getMonto_de_transaccion());
                    System.out.println("Fondos en caja: " + (efectivoCaja()));

                } else {
                    resumen = resumen + cliente_en_ventanilla.getMonto_de_transaccion() + "|0|1";
                    distribuir_billetes(cliente_en_ventanilla.getMonto_de_transaccion(), false);
                    cliente_en_ventanilla = null;
                    clientes_atendidos++;
                    System.out.println("Cliente atendido con éxito.");
                    System.out.println("Fondos en caja: " + (efectivoCaja()));
                }
                break;
            case 1:
                resumen = resumen + "1|";
                if (cambio_denominacion) {
                    resumen = resumen + "-1|-1|-1|0";
                    System.out.println("Realizando cambio de denominaciones, espere un momento...");
                    contarBilletesDenominacion();
                    distribuir_billetes(efectivoCaja(), true);
                    anularDetalles();
                    cambio_denominacion = false;
                } else if (efectivoCaja() == 0) {
                    resumen = resumen + "-1|-1|-1|1";
                    System.out.println("Caja sin fondos... Espere mientras se reabastece la caja...");
                    pedirBoveda();
                } else {
                    int ini = cliente_en_ventanilla.getMonto_de_transaccion();
                    resumen = resumen + ini + "|";
                    System.out.println("Transacción: Retiro\n"
                            + "Monto faltante: " + cliente_en_ventanilla.getMonto_de_transaccion());
                    if (ini > 500) {
                        System.out.println("Cliente en atención.");
                        entregar_billetes(500);
                        System.out.println("Monto faltante: " + cliente_en_ventanilla.getMonto_de_transaccion());
                        System.out.println("Fondos en caja: " + (efectivoCaja()));
                        resumen = resumen + (ini - cliente_en_ventanilla.getMonto_de_transaccion()) + "|" + cliente_en_ventanilla.getMonto_de_transaccion() + "|"+adicionales;
                    } else {
                        entregar_billetes(cliente_en_ventanilla.getMonto_de_transaccion());
                        int i = cliente_en_ventanilla.getMonto_de_transaccion();
                        resumen = resumen + (ini-i) + "|" + i + "|";
                        if (cliente_en_ventanilla.getMonto_de_transaccion() == 0) {
                            resumen = resumen + "2";
                            cliente_en_ventanilla = null;
                            clientes_atendidos++;
                            System.out.println("Cliente atendido con éxito.");
                            System.out.println("Fondos en caja: " + (efectivoCaja()));
                        }else{
                            resumen = resumen + adicionales;
                        }
                    }
                }
                break;
        }
    }

    public void mostrarEstadistica() {
        contarClientesEsper();
        contarBilletesDenominacion();
        efectivoCaja();
        System.out.println("ESTADISTICAS CAJA " + caja);
        System.out.println("Clientes atendidos:     " + clientes_atendidos);
        System.out.println("Clientes en espera:     " + clientes_en_espera);
        System.out.println("Efectivo en caja:       " + (efectivoCaja()));
        System.out.println("Monto total retiros:    " + monto_retiros);
        System.out.println("Monto total depositos:  " + monto_depositos);
        System.out.println("Cantidad billetes 1:    " + detalle_1);
        System.out.println("Cantidad billetes 2:    " + detalle_2);
        System.out.println("Cantidad billetes 5:    " + detalle_5);
        System.out.println("Cantidad billetes 10:   " + detalle_10);
        System.out.println("Cantidad billetes 20:   " + detalle_20);
        System.out.println("Cantidad billetes 50:   " + detalle_50);
        System.out.println("Cantidad billetes 100:  " + detalle_100);
        System.out.println("Cantidad billetes 500:  " + detalle_500 + "\n\n");
        estadistica = clientes_atendidos+"#"+clientes_en_espera+"#"+efectivoCaja()+"#"+monto_retiros+"#"+monto_depositos+"#"+detalle_1+"#"+detalle_2+"#"+detalle_5+"#"+detalle_10+"#"+detalle_20+"#"+detalle_50+"#"+detalle_100+"#"+detalle_500;
    }

    private void contarClientesEsper() {
        while (!clientes_en_cola.VACIA()) {
            clientes_en_espera++;
            clientes_en_cola.SACA_DE_COLA();
        }
        if (cliente_en_ventanilla != null) {
            clientes_en_espera++;
        }
    }

    private void contarBilletesDenominacion() {
        while (!b1.VACIA()) {
            detalle_1++;
            b1.SACA();
        }
        while (!b2.VACIA()) {
            detalle_2++;
            b2.SACA();
        }
        while (!b5.VACIA()) {
            detalle_5++;
            b5.SACA();
        }
        while (!b10.VACIA()) {
            detalle_10++;
            b10.SACA();
        }
        while (!b20.VACIA()) {
            detalle_20++;
            b20.SACA();
        }
        while (!b50.VACIA()) {
            detalle_50++;
            b50.SACA();
        }
        while (!b100.VACIA()) {
            detalle_100++;
            b100.SACA();
        }
        while (!b500.VACIA()) {
            detalle_500++;
            b500.SACA();
        }
    }

    private long efectivoCaja() {
        return (100000 - monto_retiros + monto_depositos);
    }

    private void anularDetalles() {
        detalle_1 = 0;
        detalle_2 = 0;
        detalle_5 = 0;
        detalle_10 = 0;
        detalle_20 = 0;
        detalle_50 = 0;
        detalle_100 = 0;
        detalle_500 = 0;
    }

    private void pedirBoveda() {
        monto_depositos -= 100000;
        distribuir_billetes(100000, false);
    }

    public String resumenGestion() {

        return resumen;
    }
    
    public String estadisticaCaja(){
        return estadistica;
    }
}
