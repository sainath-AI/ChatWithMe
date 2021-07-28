package com.masai.chatwithme;

public class Users {

    String Uid;
    String name;
    String email;
    String imageUri;
    String status;

    public Users() {
    }

    public Users(String uid, String name, String email, String imageUri,String status) {
        this.Uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {

        return Uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
