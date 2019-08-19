
package bankade;

import gestion_primaria.GestionBancaria;
import gui.MainGUI;

/**
 *
 * @author l
 */
public class BankADE {

    
    public static void main(String[] args) {
        
        Thread backgroundBankAde = new Thread(new GestionBancaria());
        Thread GUIBankAde = new Thread(new MainGUI());
        
        backgroundBankAde.start();
        GUIBankAde.start();
        
        /*while(true){
            if(!GUIBankAde.isAlive()){
                backgroundBankAde.interrupt();
                break;
            }
        }*/
        
    }
    
}
