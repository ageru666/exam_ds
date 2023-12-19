package org.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmailServiceImpl extends UnicastRemoteObject implements EmailService {

    private final List<Email> emails;

    public EmailServiceImpl(List<Email> emails) throws RemoteException {
        this.emails = emails;
    }

    @Override
    public List<Email> processRequest(String request) throws RemoteException {
        List<Email> result = new ArrayList<>();

        String[] splitRequest = request.split("#");
        String commandIndex = splitRequest[0];

        switch (commandIndex) {
            case "1": // Display emails received after a certain date
                String dateStr = splitRequest[1];
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
                break;

            case "2": // Display emails sent by a specific sender
                String sender = splitRequest[1];
                for (Email email : emails) {
                    if (email.getSender().equalsIgnoreCase(sender)) {
                        result.add(email);
                    }
                }
                break;

            case "3": // Display emails in alphabetic order (by subject)
                result.addAll(emails);
                Collections.sort(result);
                break;

            case "4": {
                // Create a new email
                String id = splitRequest[1];
                String newSubject = splitRequest[2];
                String newSender = splitRequest[3];
                String recipient = splitRequest[4];
                String newDateStr = splitRequest[5];
                Date newDate = parseDate(newDateStr);
                String content = splitRequest[6];
                boolean isSpam = Boolean.parseBoolean(splitRequest[7]);

                Email newEmail = new Email(id, newSubject, newSender, recipient, newDate, content, isSpam);
                emails.add(newEmail);
                break;
            }
            case "5": {
                // Update an existing email
                String idToUpdate = splitRequest[1];
                String newSubject = splitRequest[2];
                String newContent = splitRequest[3];
                boolean newIsSpam = Boolean.parseBoolean(splitRequest[4]);

                for (Email email : emails) {
                    if (email.getId().equals(idToUpdate)) {
                        email.setSubject(newSubject);
                        email.setContent(newContent);
                        email.setSpam(newIsSpam);
                    }
                }
                break;
            }
            case "6": {
                // Delete an email
                String idToDelete = splitRequest[1];
                Iterator<Email> iterator = emails.iterator();
                while (iterator.hasNext()) {
                    Email email = iterator.next();
                    if (email.getId().equals(idToDelete)) {
                        iterator.remove();
                        break;
                    }
                }
                break;
            }

            default:
                System.out.println("Unknown command. Ignoring the request.");
                break;
        }

        return result;
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

