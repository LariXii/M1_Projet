import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
public class MesNews {
	
	private static BaseDeNews bd;
	private static boolean isCreate = false;
	
	public static void main(String[] args) {
		boolean continuer = true;

		while(continuer) {
			System.out.println("Veuillez choisir une action à effectuer :");
			System.out.println(printMenu());
			System.out.println("> ");
			Scanner scan = new Scanner(System.in);
			int ret = scan.nextInt();

			switch(ret) {
			case 1 :{
				continuer = creer();
			}
			break;
			case 2 :{
				continuer = ouvrir();
			}
			break;
			case 3 :{
				continuer = sauvegarder();	
			}
			break;
			case 4 :{
				continuer = afficher();
			}	
			break;
			case 5 :{
				continuer = inserer();
			}
			break;
			case 6 :{
				continuer = supprimer();
			}
			break;
			case 7 :{
				continuer = rechercher();
			}
			break;
			case 8 :{
				continuer = quitter();
			}
			break;

			}
		}
	}
	
	public static String printMenu() {
		String menu = "Créer : 1\nOuvrir : 2\nQuitter : 3";
		if(isCreate) {
			menu = "Créer : 1\nOuvrir : 2\nSauvegarder : 3\nAfficher : 4\nInsérer : 5\nSupprimer : 6\nRechercher : 7\nQuitter : 8";
		}
		return menu;
	}

	public static boolean creer() {
		if(!isCreate) {
			System.out.print("Base de news crée !");
			bd = new BaseDeNews();
			isCreate = true;
		}
		else {
			System.out.println("Attention il existe déjà une base de news");
			return false;
		}
		return true;
	}

	public static boolean ouvrir() {
		try{
			System.out.print("Nom du fichier à ouvrir : ");
			Scanner scan = new Scanner(System.in);
			String file = scan.next();
			bd.ouvrir(file);	
		}
		catch(FileNotFoundException fnd) {
			System.out.print("Le fichier n'a pas été trouvé");
		}
		catch(IOException ioe) {
			System.out.print("Problème de lecture du fichier");
		}
		catch(ClassNotFoundException cnf) {
			System.out.print("Problème de lecture du fichier");
		}

		return continuer();
	}
	public static boolean sauvegarder() {
		try {
			System.out.print("Nom du fichier : ");
			Scanner scan = new Scanner(System.in);
			String file = scan.next();
			bd.sauvegarder(file);
		}
		catch(IOException e) {
			System.out.print("Le fichier n'existe pas");
		}
		
		return continuer();
	}
	public static boolean afficher() {
		System.out.println(bd.toString());
		return continuer();
	}
	public static boolean inserer() {
		System.out.println("Insertion");
		System.out.println("Que voulez-vous insérer dans la base ? 1 : Article presse || 2 : Photo");
		Scanner scan = new Scanner(System.in);
		int choix = scan.nextInt();
		switch(choix) {
		case 1:{
			News article = new ArticlePresse();
			bd.ajouterNews(article);
		}
		break;
		case 2:{
			News photo = new Photo();
			bd.ajouterNews(photo);
		}
		break;
		default:{
			
		}
		}
		return continuer();
	}
	public static boolean supprimer() {
		System.out.println(bd.toString());
		System.out.println("Veuillez choisir un numéro de news à supprimer :");
		Scanner scan = new Scanner(System.in);
		int num = scan.nextInt();
		bd.supprimerNews(num);
		return continuer();
	}
	public static boolean rechercher() {
		return continuer();
	}
	public static boolean quitter() {
		return !continuer();
	}
	public static boolean continuer() {
		System.out.println("Voulez-vous continuer ? 1 : oui || 2 : non");
		Scanner scan = new Scanner(System.in);
		int ret = scan.nextInt();
		if(ret == 2) {
			return false;
		}
		return true;

	}

}
