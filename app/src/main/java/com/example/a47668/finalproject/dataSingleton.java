package com.example.a47668.finalproject;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by 47668 on 2017/4/27.
 */
public class dataSingleton {
    private static dataSingleton ourInstance = new dataSingleton();
    private ArrayList<Object> postIDList;
    private ArrayList<Object> titleList;
    private ArrayList<Object> postContentList;
    private ArrayList<Object> locationList;
    private ArrayList<Object> dateList;
    private ArrayList<Object> rewardList;
    private ArrayList<Object> userIDList;
    private ArrayList<Object> isSolvedList;
    private ArrayList<Object> nameList;
    private int myUserId;
    private String myUserName;
    private String fbToken;
    private String fbEmail;
    private Bitmap fbPhoto;
    public static dataSingleton getInstance() {
        return ourInstance;
    }

    public dataSingleton() {
        postIDList = new ArrayList<>();
        titleList = new ArrayList<>();
        postContentList = new ArrayList<>();
        locationList = new ArrayList<>();
        dateList = new ArrayList<>();
        rewardList = new ArrayList<>();
        userIDList = new ArrayList<>();
        isSolvedList = new ArrayList<>();
        nameList = new ArrayList<>();
        myUserId = 4;
        myUserName = "clay";

    }
    public void setPostId(ArrayList<Object> postId){
        postIDList = postId;
    }
    public ArrayList<Object> getPostId(){
        return postIDList;
    }
    public void addPostId(){
        int len = postIDList.size();
        postIDList.add(len+1);
    }

    public void setTitle(ArrayList<Object> title){
        titleList = title;
    }
    public ArrayList<Object> getTitle(){
        return titleList;
    }
    public void addTitle(String newTitle){
        titleList.add(newTitle);
    }

    public void setPostContent(ArrayList<Object> postContent){
        postContentList = postContent;
    }
    public ArrayList<Object> getPostContent(){
        return postContentList;
    }
    public void addPostContent(String newContent){
        postContentList.add(newContent);
    }

    public void setLocation(ArrayList<Object> location){
        locationList = location;
    }
    public ArrayList<Object> getLocation(){
        return locationList;
    }
    public void addLocation(String newLocation){
        locationList.add(newLocation);
    }

    public void setDate(ArrayList<Object> date){
        dateList = date;
    }
    public ArrayList<Object> getDate(){
        return dateList;
    }
    public void addDate(String newDate){
        dateList.add(newDate);
    }

    public void setReward(ArrayList<Object> reward){
        rewardList = reward;
    }
    public ArrayList<Object> getReward(){
        return rewardList;
    }
    public void addReward(int newReward){
        rewardList.add(newReward);
    }

    public void setUserId(ArrayList<Object> userId){
        userIDList = userId;
    }
    public ArrayList<Object> getUserId(){
        return userIDList;
    }
    public void addUserId(int newUserId){
        userIDList.add(newUserId);
    }

    public void setIsSolved(ArrayList<Object> isSolved){
        isSolvedList = isSolved;
    }
    public ArrayList<Object> getIsSolved(){
        return isSolvedList;
    }
    public void addIsSolved(){
        isSolvedList.add("false");
    }

    public void setName(ArrayList<Object> name){
        nameList = name;
    }
    public ArrayList<Object> getName(){
        return nameList;
    }
    public void addName(String newName){
        nameList.add(newName);
    }



    public void setMyUserId(int myuserid){
        myUserId = myuserid;
    }
    public int getMyUserId(){
        return myUserId;
    }

    public void setMyUserName(String myusername){
        this.myUserName = myusername;
    }
    public String getMyUserName(){
        return myUserName;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public void setFbEmail(String email) {
        fbEmail = email;
    }
    public String getFbEmail(){
        return fbEmail;
    }

    public void setFbPhoto(Bitmap fbPhoto) {
        this.fbPhoto = fbPhoto;
    }
    public Bitmap getFbPhoto(){
        return fbPhoto;
    }
}
