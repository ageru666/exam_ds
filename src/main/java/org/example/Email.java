package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Email implements Serializable, Comparable<Email> {

    private String id;
    private String subject;
    private String sender;
    private String recipient;
    private Date date;
    private String content;
    private boolean isSpam;

    public Email(String id, String subject, String sender, String recipient, Date date, String content, boolean isSpam) {
        this.id = id;
        this.subject = subject;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.content = content;
        this.isSpam = isSpam;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public void setSpam(boolean spam) {
        isSpam = spam;
    }

    @Override
    public int compareTo(Email other) {
        return this.date.compareTo(other.date);
    }

    @Override
    public String toString() {
        return "id: " + id +
                "\nsubject: " + subject +
                "\nsender: " + sender +
                "\nrecipient: " + recipient +
                "\ndate: " + date +
                "\ncontent: " + content +
                "\nisSpam: " + isSpam;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public static List<Email> getList() {
        return new ArrayList<>() {
            {
                add(new Email("02", "Meeting", "john@example.com", "alice@example.com", new Date(), "Discussing project details", false));
                add(new Email("01", "Vacation Plans", "alice@example.com", "bob@example.com", new Date(), "Let's plan the vacation", false));
                add(new Email("03", "Special Offer", "spam@example.com", "user@example.com", new Date(), "You've won a prize!", true));

            }
        };
    }
}
