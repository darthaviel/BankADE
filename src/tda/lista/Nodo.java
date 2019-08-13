/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tda.lista;

/**
 *
 * @author hguevara
 */
class Nodo {
    
    private Object dato;
    private int encadenamiento;

    public Nodo() {
        this.dato = null;
        this.encadenamiento = -1;
    }

    public Nodo(Object dato, int encadenamiento) {
        this.dato = dato;
        this.encadenamiento = encadenamiento;
    }

    /**
     * @return the dato
     */
    public Object getDato() {
        return dato;
    }

    /**
     * @param dato the dato to set
     */
    public void setDato(Object dato) {
        this.dato = dato;
    }

    /**
     * @return the encadenamiento
     */
    public int getEncadenamiento() {
        return encadenamiento;
    }

    /**
     * @param encadenamiento the encadenamiento to set
     */
    public void setEncadenamiento(int encadenamiento) {
        this.encadenamiento = encadenamiento;
    }

    @Override
    public String toString() {
        return "Nodo{" + "dato=" + dato + ", encadenamiento=" + encadenamiento + '}';
    }
    
    
}
