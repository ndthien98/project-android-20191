package Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String name;
    private String email;
    private boolean ismale;
    private int age;
    private List<String> list_friends = new ArrayList<>();


    public User() {

    }

    public void addFriend(String email) {
        if(list_friends==null) list_friends = new ArrayList<>();
        if (!list_friends.contains(email))
            list_friends.add(email);
    }

    public User(String uid, String name, String email, boolean ismale, int age) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.ismale = ismale;
        this.age = age;
        list_friends = new ArrayList<>();
//        list_friends.add("sample1@gmail.com");
//        list_friends.add("sample2@gmail.com");
//        list_friends.add("sample3@gmail.com");

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsmale() {
        return ismale;
    }

    public void setIsmale(boolean ismale) {
        this.ismale = ismale;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getList_friends() {
        return list_friends;
    }

    public void setList_friends(List<String> list_friends) {
        this.list_friends = list_friends;
    }
}
