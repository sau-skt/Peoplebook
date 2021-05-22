package appdev.com.peoplebook.Models;

public class FriendRequestReceiveModel {
    String status, username, name, profileimage;

    public FriendRequestReceiveModel() {
    }

    public FriendRequestReceiveModel(String status, String username, String name, String profileimage) {
        this.status = status;
        this.username = username;
        this.name = name;
        this.profileimage = profileimage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
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
}
