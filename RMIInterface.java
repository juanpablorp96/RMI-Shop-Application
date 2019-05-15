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
import java.util.List;
import java.util.Map;
public interface RMIInterface extends Remote 
{
    public String[] get_items() throws RemoteException; 
    public String[] get_prices() throws RemoteException;
    public String[] get_quantity() throws RemoteException;
    public Map<String, String> get_cards() throws RemoteException;
    public int get_indexTX() throws RemoteException;
    public void set_indexTX() throws RemoteException;
    public Boolean check_out(Map<Integer, String> carrito, String card) throws RemoteException;
    public Boolean register(String username, String hash) throws RemoteException;
    public Boolean login(String username, String hash, String card) throws RemoteException;
    public void create_transaction(int index, List<String> TX_i) throws RemoteException;
    public void add_operation(int index, String operation) throws RemoteException;
    public boolean rollbackValidation(int index) throws RemoteException;
    public void commitTX(int index) throws RemoteException;
    
}