package by.hilum.yteam.Models;

public class Channel {

    /**
     * Channel ID
     */
    public int ID = 0;

    /**
     * Channel Label
     */
    public String LABEL = "";

    /**
     * Channel Owner ID
     */
    public int OWNDER = 0;

    /**
     * Channel Min Mod Value
     */
    public int MIN_MOD_VALUE = 0;

    /**
     * Channel Main Constructor
     *
     * @param id       String
     * @param label    String
     * @param owner_id String
     * @param min_mod  String
     */
    public Channel(String id, String label, String owner_id, String min_mod) {
        ID = Integer.parseInt(id);
        LABEL = label;
        OWNDER = Integer.parseInt(owner_id);
        MIN_MOD_VALUE = Integer.parseInt(min_mod);
    }

}
