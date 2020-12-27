package by.hilum.yteam.Models;

import com.stfalcon.chatkit.commons.models.IUser;

public class Author implements IUser {
    /**
     * Author ID
     */
    private String id;
    /**
     * Author Name
     */
    private String name;
    /**
     * Author Photo
     */
    private String avatar;

    /**
     * Author Main Constructor
     *
     * @param id     String
     * @param name   String
     * @param avatar String
     */
    public Author(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }
}
