public class pageRank {
    private String userName;
    private double score;

    public pageRank(String userName,double score) {

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



}
