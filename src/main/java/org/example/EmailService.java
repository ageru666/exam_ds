package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface EmailService extends Remote {
    List<Email> processRequest(String request) throws RemoteException;

}

