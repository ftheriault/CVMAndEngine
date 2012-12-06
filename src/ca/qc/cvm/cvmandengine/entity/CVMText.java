package ca.qc.cvm.cvmandengine.entity;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.util.color.Color;

public class CVMText {
	private int posX;
	private int posY;
	private String displayText;
	private int size;
	private Color color;
	
	private Font font;
	private Text text;
	
	public CVMText(int posX, int posY, int size, String displayText, Color color) {
		this.posX = posX;
		this.posY = posY;
		this.displayText = displayText;
		this.size = size;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public String getDisplayText() {
		return displayText;
	}

	public int getSize() {
		return size;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
}
