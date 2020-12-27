package by.hilum.yteam.Models;

public class Group {

    /**
     * Group Name
     */
    public String NAME = "";

    /**
     * Group Invitation Code
     */
    public String CODE = "";

    /**
     * Group ID
     */
    public int ID = 0;

    /**
     * Group Owner ID
     */
    public int OWNER = 0;

    /**
     * Main Constructor
     *
     * @param name  String
     * @param id    String
     * @param owner String
     * @param code  String
     */
    public Group(String name, String id, String owner, String code) {
        NAME = name;
        ID = Integer.parseInt(id);
        OWNER = Integer.parseInt(owner);
        CODE = code;
    }
}
