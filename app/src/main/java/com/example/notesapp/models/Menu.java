package com.example.notesapp.models;

public class Menu {
    private long menuId;
    private String menuName;
    private String menuIcon;
    private String url;

    public Menu(long menuId, String menuName, String menuIcon, String url) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.url = url;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
