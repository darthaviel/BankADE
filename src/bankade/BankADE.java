
package bankade;

import gestion_primaria.GestionBancaria;
import gui.MainGUI;

/**
 *
 * @author l
 */
public class BankADE {

    
    public static void main(String[] args) {
        
        //new Thread(new GestionBancaria()).start();
        new Thread(new MainGUI()).start();
        
    }
    
}
