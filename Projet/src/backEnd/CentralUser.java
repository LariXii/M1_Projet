package backEnd;

public class CentralUser implements Comparable<CentralUser> {
    private String userName;
    private double score;

    public CentralUser(String userName,double score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String toString(){
        return "["+userName+"]"+"("+score+")";
    }

    @Override
    public int compareTo(CentralUser centralUser) {
        if(this.getScore() < centralUser.getScore()){
            return 1;
        }
        else{
            if(this.getScore() > centralUser.getScore()){
                return -1;
            }
            else{
                return this.getUserName().compareTo(centralUser.getUserName());
            }
        }
    }
}
