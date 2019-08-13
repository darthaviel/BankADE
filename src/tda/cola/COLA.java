package tda.cola;

import tda.lista.LISTA;


/**
 *
 * @author l
 */
public class COLA{

    LISTA lista_cola = new LISTA();

    
    public Object FRENTE() {
        return lista_cola.RECUPERA(lista_cola.PRIMERO());
    }

    
    public boolean PONE_EN_COLA(Object dato) {
        if (lista_cola.INSERTA(dato, lista_cola.FIN()) == -1) {
            return false;
        }

        return true;

    }

    
    public boolean SACA_DE_COLA() {
        if(lista_cola.SUPRIME(lista_cola.PRIMERO())==-1)
            return false;
        return true;
    }

    
    public boolean VACIA() {
        return lista_cola.VACIA();
    }

    
    public void ANULA() {
        lista_cola.ANULA();
    }

}
