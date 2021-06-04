package com.example.myapp.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String mail;
    public String MD5password;
    public User(){

    }
    public User(String name,String mail,String mp){

    }
    void UpdateName(String n){
     name=n;
    }
    void UpdateMail(String m) {
    mail=m;
    }
    void UpdatePassWord(String mp){
    MD5password=mp;
    }
}
