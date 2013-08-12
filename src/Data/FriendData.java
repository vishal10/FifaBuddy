package Data;

import android.graphics.Bitmap;

/**
 * This class is used to represent a list item in the Friends activity for the
 * project. It stores the friends name and profile picture to be displayed on
 * the list.
 * 
 * @author Vishal
 * 
 */
public class FriendData {
	private String name;
	private Bitmap image;

	public FriendData() {
	}

	public FriendData(String n) {
		setName(n);
	}

	public FriendData(String n, Bitmap i) {
		setName(n);
		setImage(i);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

}
