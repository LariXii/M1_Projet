import java.util.Scanner;

public class ArticlePresse extends News{
	private String texte;
	private boolean electronique;

	ArticlePresse(){
		super();
		this.setTexte();
		this.setElectronique();
	}
	
	public void afficher() {
		System.out.print(toString());
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Texte :");
		String texte = scan.nextLine();
		setTexte(texte);
	}
	public void setTexte(String texte) {
		this.texte = texte;
	}

	public boolean isElectronique() {
		return electronique;
	}

	public void setElectronique(boolean electronique) {
		this.electronique = electronique;
	}
	public void setElectronique() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Electronique :");
		boolean electronique = scan.nextBoolean();
		setElectronique(electronique);
	}

	public String toString() {
		return super.toString() + " | Texte : " + this.texte + " | Disponible en format électronique : " + ((this.electronique)?"Oui":"Non");
	}
}
