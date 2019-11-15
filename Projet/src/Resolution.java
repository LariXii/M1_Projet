import java.io.Serializable;

public class Resolution implements Serializable{
	private int x;
	private int y;
	
	Resolution(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	
	public String toString() {
		return this.x + "x" + this.y + " px";
	}
}
