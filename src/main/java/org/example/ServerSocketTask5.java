package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class Callback {

    public boolean shouldEnd = false;
}
class EmailIterator implements Runnable {

    private final Socket socket;
    private final Callback callback;
    private final List<Email> emails;

    public EmailIterator(Socket socket, Callback callback, List<Email> emails) {
        this.callback = callback;
        this.socket = socket;
        this.emails = emails;
    }

    @Override
    public void run() {
        try {
            InputStreamReader ois = new InputStreamReader(socket.getInputStream());

            System.out.println("Receiving input");

            BufferedReader reader = new BufferedReader(ois);
            String message = reader.readLine();
            System.out.println("Command " + message);
            String[] splitMessage = message.split("#");
            String commandIndex = splitMessage[0];

            System.out.println("Command " + splitMessage[0]);

            if (commandIndex.equals("0")) {
                System.out.println("Close command");
                callback.shouldEnd = true;
                return;
            }

            List<Email> result = new ArrayList<>();

            switch (commandIndex) {
                case "1" -> {
                    // Display emails received after a certain date
                    String dateStr = splitMessage[1];
                    Date date = parseDate(dateStr);

                    if (date != null) {
                        for (Email email : emails) {
                            if (email.getDate().after(date)) {
                                result.add(email);
                            }
                        }
                    } else {
                        System.out.println("Invalid date format. Ignoring the command.");
                    }
                }
                case "2" -> {
                    // Display emails sent by a specific sender
                    String sender = splitMessage[1];
                    for (Email email : emails) {
                        if (email.getSender().equalsIgnoreCase(sender)) {
                            result.add(email);
                        }
                    }
                }
                case "3" -> {
                    // Display emails in alphabetic order (by subject)
                    result.addAll(emails);
                    Collections.sort(result);
                }
                case "4" -> {
                    // Create a new email
                    String id = splitMessage[1];
                    String subject = splitMessage[2];
                    String sender = splitMessage[3];
                    String recipient = splitMessage[4];
                    String dateStr = splitMessage[5];
                    Date date = parseDate(dateStr);
                    String content = splitMessage[6];
                    boolean isSpam = Boolean.parseBoolean(splitMessage[7]);

                    Email newEmail = new Email(id, subject, sender, recipient, date, content, isSpam);
                    emails.add(newEmail);
                }
                case "5" -> {
                    // Update an existing email
                    String idToUpdate = splitMessage[1];
                    String newSubject = splitMessage[2];
                    String newContent = splitMessage[3];
                    boolean newIsSpam = Boolean.parseBoolean(splitMessage[4]);

                    for (Email email : emails) {
                        if (email.getId().equals(idToUpdate)) {
                            email.setSubject(newSubject);
                            email.setContent(newContent);
                            email.setSpam(newIsSpam);
                        }
                    }
                }
                case "6" -> {
                    // Delete an email
                    String idToDelete = splitMessage[1];
                    Iterator<Email> iterator = emails.iterator();
                    while (iterator.hasNext()) {
                        Email email = iterator.next();
                        if (email.getId().equals(idToDelete)) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }



            System.out.println(result);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            oos.close();
            ois.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}


public class ServerSocketTask5 {

    private static final int PORT = 9876;

    public static void main(String[] args) throws IOException {
        var server = new ServerSocket(PORT);
        var callback = new Callback();

        var emails = Email.getList();

        while (!callback.shouldEnd) {
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();

            EmailIterator iterator = new EmailIterator(socket, callback, emails);

            iterator.run();
        }

        System.out.println("Shutting down Socket server!!");
        server.close();
    }
}
