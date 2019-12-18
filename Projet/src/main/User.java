package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Un utilisateur de Twitter comporte un identifiant et une liste de retweet. Cette liste comporte l'id de l'utilisateur qu'il a retweeté et une liste des tweets retweeté
 */
public class User {
    private HashMap<String, ArrayList<Tweet>> listReTweet = new HashMap<>();
    private ArrayList<Tweet> listTweet = new ArrayList<>();
    private String id;
    private int estReTweet = 0;

    public User(String id) {
        this.id = id;
    }

    public HashMap<String, ArrayList<Tweet>> getListReTweet() {
        return listReTweet;
    }

    public void setListReTweet(HashMap<String, ArrayList<Tweet>> listReTweet) {
        this.listReTweet = listReTweet;
    }

    public ArrayList<Tweet> getListTweet() {
        return listTweet;
    }

    public void setListTweet(ArrayList<Tweet> listTweet) {
        this.listTweet = listTweet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEstReTweet() {
        return estReTweet;
    }

    public void setEstReTweet(int estReTweet) {
        this.estReTweet = estReTweet;
    }

    public static Comparator<User> comparatorByWeightDESC(){
        Comparator<User> weightComparator = new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if(u1.getEstReTweet() < u2.getEstReTweet()){
                    return 1;
                }
                else{
                    if(u1.getEstReTweet() > u2.getEstReTweet()){
                        return -1;
                    }
                    else{
                        return u1.getId().compareTo(u2.getId());
                    }
                }
            }
        };
        return weightComparator;
    }

    public String toString(){
        return "["+this.getId()+" à été retweeté "+this.getEstReTweet()+" fois]";
    }
}
