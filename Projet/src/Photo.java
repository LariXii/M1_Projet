
import java.time.LocalDate;
import java.util.Scanner;

public class Photo extends News{
	private String photo;
	private String format;
	private Resolution resolution;
	private boolean couleur;

	Photo(){
		super();
		setPhoto();
		setFormat();
		setCouleur();		
		setResolution();
	}
	Photo(String photo,String format,Resolution res, boolean couleur){
		super();
		this.photo = photo;
		this.format = format;
		this.couleur = couleur;
		this.resolution = res;
	}
	Photo(String titre, String auteur, LocalDate date, String url, String photo,String format,Resolution res, boolean couleur){
		super(titre,date,auteur,url);
		this.photo = photo;
		this.format = format;
		this.couleur = couleur;
		this.resolution = res;
	}

	public void afficher() {
		System.out.print(toString());
	}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public void setPhoto() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Photo :");
		String photo = scan.next();
		setPhoto(photo);
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public void setFormat() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Format :");
		String format = scan.next();
		setFormat(format);
	}
	public Resolution getResolution() {
		return resolution;
	}
	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}
	public void setResolution(int x, int y) {
		this.resolution = new Resolution(x,y);
	}
	public void setResolution() {
		Scanner scan = new Scanner(System.in);
		System.out.print("Résolution\nx : ");
		int x = scan.nextInt();
		System.out.print("y : ");
		int y = scan.nextInt();
		setResolution(x,y);
	}
	public boolean isCouleur() {
		return couleur;
	}
	public void setCouleur(boolean couleur) {
		this.couleur = couleur;
	}
	public void setCouleur() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Couleur :");
		boolean couleur = scan.nextBoolean();
		setCouleur(couleur);
	}	
	public String toString() {
		return super.toString() + " | Format : " + this.format + " | Résolution : " + this.resolution.toString() + " | Photo en couleur : " + ((couleur)?"Oui":"Non");
	}

}
