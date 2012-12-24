package net.scholagest.objects;

public class Property {
    private String name;
    private String range;
    private String domain;
    private String displayText;

    public Property(String name, String range, String domain, String displayText) {
        this.name = name;
        this.range = range;
        this.domain = domain;
        this.displayText = displayText;
    }

    public String getName() {
        return name;
    }

    public String getRange() {
        return range;
    }

    public String getDomain() {
        return domain;
    }

    public String getDisplayText() {
        return displayText;
    }
}
