import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public abstract class News implements Comparable<News>,Serializable{
	protected String titre;
	protected LocalDate date = LocalDate.now();
	protected String auteur;
	protected String url;

	public abstract void afficher();
	
	News(){
		setTitre();
		setDate();
		setAuteur();
		setURL();
	}
	
	News(String titre, LocalDate date, String auteur, String url){
		this.titre = titre;
		this.url = url;
		this.auteur = auteur;
		this.date = date;
	}

	public String getTitre() {
		return this.titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}

	public void setTitre() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Titre :");
		String title = scan.nextLine();
		setTitre(title);
	}

	public LocalDate getDate() {
		return this.date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Scanner scan = new Scanner(System.in);
		System.out.println("Saississez une date au format dd/MM/yyyy : ");
		String s_date = scan.next();
		date = LocalDate.parse(s_date,formatter);
		setDate(date);
	}

	public String getAuteur() {
		return this.auteur;
	}
	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public void setAuteur() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Auteur :");
		String auteur = scan.nextLine();
		setAuteur(auteur);
	}

	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public void setURL() {
		Scanner scan = new Scanner(System.in);
		System.out.println("URL :");
		String url = scan.nextLine();
		setUrl(url);
	}

	public String toString() {
		return "Titre : " + this.titre + " | Date : " + this.date + " | Auteur : " + this.auteur + " | URL : " + this.url ;
	}

	public int compareTo(News n) {
		int res = this.date.compareTo(n.getDate());
		if(res == 0) {
			return this.titre.compareTo(n.getTitre());
		}
		else {
			return res;
		}
	}
}
