/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elementos_de_banco;

/**
 *
 * @author l
 */
public class Cliente {

    int numero_caja,
            transaccion,
            monto_de_transaccion;

    public Cliente(int numero_caja, int transaccion, int monto_de_transaccion) {
        this.numero_caja = numero_caja;
        this.transaccion = transaccion;
        this.monto_de_transaccion = monto_de_transaccion;
    }

    public int getNumero_caja() {
        return numero_caja;
    }

    public int getTransaccion() {
        return transaccion;
    }

    public int getMonto_de_transaccion() {
        return monto_de_transaccion;
    }

    public void setMonto_de_transaccion(int monto_de_transaccion) {
        this.monto_de_transaccion = monto_de_transaccion;
    }
    
    

}
