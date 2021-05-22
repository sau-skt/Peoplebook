package appdev.com.peoplebook.Models;

public class PostModel {
    String image, filename, name, profileimage, username;

    public PostModel(){}

    public PostModel(String image, String filename, String name, String profileimage, String username) {
        this.image = image;
        this.filename = filename;
        this.name = name;
        this.profileimage = profileimage;
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
