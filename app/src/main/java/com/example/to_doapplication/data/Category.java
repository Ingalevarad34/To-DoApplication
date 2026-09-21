package com.example.to_doapplication.data;

import java.util.UUID;

public class Category {
    private String id;
    private String name;
    private long colorHex;
    private String iconName;
    private String userId;

    public Category() {
        this.id = UUID.randomUUID().toString();
        this.name = "";
        this.colorHex = 0xFFFFFFFFL;
        this.iconName = "";
        this.userId = "";
    }

    public Category(String id, String name, long colorHex, String iconName, String userId) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.name = name;
        this.colorHex = colorHex;
        this.iconName = iconName;
        this.userId = userId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getColorHex() { return colorHex; }
    public void setColorHex(long colorHex) { this.colorHex = colorHex; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
