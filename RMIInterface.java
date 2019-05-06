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
import java.util.Map;
public interface RMIInterface extends Remote 
{ 
    // Declaring the method prototype 
    public String[] get_items(String search) throws RemoteException; 
    public String[] get_prices(String search) throws RemoteException;
    public String[] get_quantity(String search) throws RemoteException;
    public Boolean check_out(Map<Integer, String> carrito) throws RemoteException;
    public Boolean register(String username, String hash, String card, String money) throws RemoteException;
}