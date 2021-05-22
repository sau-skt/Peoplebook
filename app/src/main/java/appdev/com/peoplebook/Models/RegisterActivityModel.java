package appdev.com.peoplebook.Models;

public class RegisterActivityModel {
    String name, username, password, aboutme, hobbies, dateofbirth;
    long phone;

    public RegisterActivityModel(){}

    public RegisterActivityModel(String name, String username, String password, String aboutme, String hobbies, String dateofbirth, long phone) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.aboutme = aboutme;
        this.hobbies = hobbies;
        this.dateofbirth = dateofbirth;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
