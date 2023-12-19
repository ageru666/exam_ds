package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientSocketTask5 {

    private static final int PORT = 9876;

    public static void logEmails(List<Email> emails) {
        if (emails.isEmpty()) {
            System.out.println("None!");
            return;
        }

        for (var email : emails) {
            System.out.println(email);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Socket socket;
        PrintWriter oos;
        ObjectInputStream ois;

        InetAddress host = InetAddress.getLocalHost();
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println(
                    """
                    Choose option:
                    1 - Display emails received after a certain date
                    2 - Display emails sent by a specific sender
                    3 - Display emails in alphabetic order
                    4 - Create a new email
                    5 - Update an existing email
                    6 - Delete an email
                    0 - Exit
                    """
            );

            socket = new Socket(host.getHostName(), PORT);
            oos = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Sending request to Socket Server");

            int commandIndex = scan.nextInt();

            if (commandIndex == 0) {
                System.out.println("Sending close Request");

                oos.println(commandIndex);
                oos.flush();

                break;
            }

            switch (commandIndex) {
                case 1 -> {
                    // Display emails received after a certain date
                    System.out.println("Enter date in yyyy-MM-dd format:");
                    String date = scan.next();

                    String message = commandIndex + "#" + date;

                    oos.println(message);
                    System.out.println(message);
                    oos.flush();
                }
                case 2 -> {
                    // Display emails sent by a specific sender
                    System.out.println("Enter sender's email:");
                    String sender = scan.next();

                    String message = commandIndex + "#" + sender;

                    oos.println(message);
                    System.out.println(message);
                    oos.flush();
                }
                case 3 -> {
                    String message = commandIndex + "";

                    oos.println(message);
                    oos.flush();
                }
                case 4 -> {
                    System.out.println("Enter new email details:");

                    System.out.println("Enter ID:");
                    String id = scan.next();

                    System.out.println("Enter subject:");
                    String subject = scan.next();

                    System.out.println("Enter sender:");
                    String sender = scan.next();

                    System.out.println("Enter recipient:");
                    String recipient = scan.next();

                    System.out.println("Enter date in yyyy-MM-dd format:");
                    String dateStr = scan.next();

                    System.out.println("Enter content:");
                    String content = scan.next();

                    System.out.println("Enter true if email is spam, false otherwise:");
                    boolean isSpam = scan.nextBoolean();

                    String message = commandIndex + "#" + id + "#" + subject + "#" + sender + "#" +
                            recipient + "#" + dateStr + "#" + content + "#" + isSpam;

                    oos.println(message);
                    System.out.println(message);
                    oos.flush();
                }
                case 5 -> {
                    System.out.println("Enter email ID to update:");
                    String idToUpdate = scan.next();

                    System.out.println("Enter new subject:");
                    String newSubject = scan.next();

                    System.out.println("Enter new content:");
                    String newContent = scan.next();

                    System.out.println("Enter true if email is spam, false otherwise:");
                    boolean newIsSpam = scan.nextBoolean();

                    String message = commandIndex + "#" + idToUpdate + "#" + newSubject + "#" + newContent + "#" + newIsSpam;

                    oos.println(message);
                    System.out.println(message);
                    oos.flush();
                }
                case 6 -> {
                    System.out.println("Enter email ID to delete:");
                    String idToDelete = scan.next();

                    String message = commandIndex + "#" + idToDelete;

                    oos.println(message);
                    System.out.println(message);
                    oos.flush();
                }
            }

            System.out.println("Results: ");
            ois = new ObjectInputStream(socket.getInputStream());

            logEmails((ArrayList<Email>) ois.readObject());

            ois.close();
            oos.close();

            Thread.sleep(100);
        }

        oos.println(3);

        System.out.println("Exiting the client.");
    }
}
