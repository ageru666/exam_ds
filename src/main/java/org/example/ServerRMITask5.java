package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRMITask5 {
    private static final int PORT = 1099;

    public static void main(String[] args) {
        try {

            Registry registry = LocateRegistry.createRegistry(PORT);

            EmailService emailService = new EmailServiceImpl(Email.getList());

            registry.rebind("EmailService", emailService);

            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
