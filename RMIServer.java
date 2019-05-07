/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Juan Pablo
 */
//program for server application 
import java.rmi.*; 
import java.rmi.registry.*; 
public class RMIServer 
{ 
	public static void main(String args[]) 
	{ 
		try
		{ 
			// Create an object of the interface 
			// implementation class 
			RMIInterface obj = new RMIImplement(); 

			// rmiregistry within the server JVM with 
			// port number 1900 
			LocateRegistry.createRegistry(1900); 

			// Binds the remote object by the name 
			// distribuidos
			Naming.rebind("rmi://localhost:1900" + "/distribuidos",obj); 
		} 
		catch(Exception ae) 
		{ 
			System.out.println(ae); 
		} 
	} 
} 

