
package bankade;

import gestion_primaria.GestionBancaria;

/**
 *
 * @author l
 */
public class BankADE {

    
    public static void main(String[] args) {
        
        new Thread(new GestionBancaria()).start();
        
    }
    
}
