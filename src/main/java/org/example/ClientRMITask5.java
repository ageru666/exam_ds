package org.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClientRMITask5 {
    private static final int PORT = 1099;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", PORT);

            EmailService emailService = (EmailService) registry.lookup("EmailService");

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
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

                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("Enter date in yyyy-MM-dd format:");
                        String date = scanner.nextLine();
                        List<Email> emailsAfterDate = emailService.processRequest("1#" + date);
                        displayEmails(emailsAfterDate);
                        break;

                    case 2:
                        System.out.println("Enter sender's email:");
                        String sender = scanner.nextLine();
                        List<Email> emailsBySender = emailService.processRequest("2#" + sender);
                        displayEmails(emailsBySender);
                        break;

                    case 3:
                        List<Email> sortedEmails = emailService.processRequest("3");
                        displayEmails(sortedEmails);
                        break;

                    case 4:
                        System.out.println("Enter new email details:");

                        System.out.println("Enter ID:");
                        String id = scanner.nextLine();

                        System.out.println("Enter subject:");
                        String subject = scanner.nextLine();

                        System.out.println("Enter sender:");
                        String emailSender = scanner.nextLine();

                        System.out.println("Enter recipient:");
                        String recipient = scanner.nextLine();

                        System.out.println("Enter date in yyyy-MM-dd format:");
                        String dateStr = scanner.nextLine();

                        System.out.println("Enter content:");
                        String content = scanner.nextLine();

                        System.out.println("Enter true if email is spam, false otherwise:");
                        boolean isSpam = scanner.nextBoolean();

                        String createEmailMessage = String.format("4#%s#%s#%s#%s#%s#%s#%s", id, subject, emailSender, recipient, dateStr, content, isSpam);
                        List<Email> createdEmails = emailService.processRequest(createEmailMessage);
                        displayEmails(createdEmails);
                        break;

                    case 5:
                        System.out.println("Enter email ID to update:");
                        String idToUpdate = scanner.nextLine();

                        System.out.println("Enter new subject:");
                        String newSubject = scanner.nextLine();

                        System.out.println("Enter new content:");
                        String newContent = scanner.nextLine();

                        System.out.println("Enter true if email is spam, false otherwise:");
                        boolean newIsSpam = scanner.nextBoolean();

                        String updateEmailMessage = String.format("5#%s#%s#%s#%s", idToUpdate, newSubject, newContent, newIsSpam);
                        List<Email> updatedEmails = emailService.processRequest(updateEmailMessage);
                        displayEmails(updatedEmails);
                        break;

                    case 6:
                        System.out.println("Enter email ID to delete:");
                        String idToDelete = scanner.nextLine();
                        List<Email> deletedEmails = emailService.processRequest("6#" + idToDelete);
                        displayEmails(deletedEmails);
                        break;

                    case 0:
                        System.out.println("Exiting the client.");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }

            } while (choice != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayEmails(List<Email> emails) {
        if (emails.isEmpty()) {
            System.out.println("No emails to display.");
        } else {
            System.out.println("Emails:");
            for (Email email : emails) {
                System.out.println(email);
            }
        }
    }
}
