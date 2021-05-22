package appdev.com.peoplebook.Models;

public class FindFriendsModel {
    String name, profileimage, username;

    public FindFriendsModel(){}

    public FindFriendsModel(String name, String profileimage, String username) {
        this.name = name;
        this.profileimage = profileimage;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
