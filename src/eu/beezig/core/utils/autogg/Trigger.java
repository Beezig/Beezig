package eu.beezig.core.utils.autogg;

public class Trigger {

    private String description, shortcode, trigger;
    private int type;
    private boolean enabled;

    public Trigger(String description, String shortcode, String trigger, int type) {
        this.description = description;
        this.shortcode = shortcode;
        this.trigger = trigger;
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public String getShortcode() {
        return shortcode;
    }

    public String getTrigger() {
        return trigger;
    }

    public int getType() {
        return type;
    }
}
