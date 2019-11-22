import java.time.LocalDate;

public class Tweet {
    private long idTweet;
    private String idUser;
    private LocalDate dateTweet;
    private String textTweet;
    private String idReTweet = null;

    public Tweet(long idTweet, String idUser, LocalDate dateTweet, String textTweet, String idReTweet) {
        this.idTweet = idTweet;
        this.idUser = idUser;
        this.dateTweet = dateTweet;
        this.textTweet = textTweet;
        this.idReTweet = idReTweet;
    }
/********************************************************************************
 *                                  Getters                                     *
********************************************************************************/
    public long getIdTweet() {
        return idTweet;
    }

    public String getIdUser() {
        return idUser;
    }

    public LocalDate getDateTweet() {
        return dateTweet;
    }

    public String getTextTweet() {
        return textTweet;
    }

    public String getIdReTweet() {
        return idReTweet;
    }

/********************************************************************************
*                                  Setters                                     *
********************************************************************************/
    public void setIdTweet(long idTweet) {
        this.idTweet = idTweet;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setDateTweet(LocalDate dateTweet) {
        this.dateTweet = dateTweet;
    }

    public void setTextTweet(String textTweet) {
        this.textTweet = textTweet;
    }

    public void setIdReTweet(String idReTweet) {
        this.idReTweet = idReTweet;
    }
/**********************************************************************************/
    @Override
    public String toString() {
        return "Tweet{" +
                "idTweet=" + idTweet +
                ", idUser='" + idUser + '\'' +
                ", dateTweet=" + dateTweet +
                ", textTweet='" + textTweet + '\'' +
                ", idReTweet='" + idReTweet + '\'' +
                '}';
    }

}
