package by.hilum.yteam.Models;

import com.stfalcon.chatkit.commons.models.IMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements IMessage {
    /**
     * Message ID
     */
    private String id;
    /**
     * Message Text
     */
    private String text;
    /**
     * Message Creation Time
     */
    private String createdAt;
    /**
     * Message Author
     */
    private Author author;

    /**
     * Main Constructor
     *
     * @param id        String
     * @param text      String
     * @param createdAt String
     * @param author    Author
     */
    public Message(String id, String text, String createdAt, Author author) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.author = author;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Author getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdAt);
        } catch (ParseException e) {
            return new Date(0);
        }

    }
}
