/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan Pablo
 */
import java.rmi.*; 
public interface RMIInterface extends Remote 
{ 
    // Declaring the method prototype 
    public String get_items(String search) throws RemoteException; 
    public String get_prices(String search) throws RemoteException;
    public String get_quantity(String search) throws RemoteException;
}