package Data;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * In order to convert the bitmap profile picture to a string so that it can be
 * stored in the database, i used an external source. The source is given at:
 * http://androidtrainningcenter.blogspot.com/2012
 * /03/how-to-convert-string-to-bitmap-and.html.
 * 
 * @author Vishal
 * 
 */
public class BitmapStringConvert {

	/**
	 * This function was obtained from the source:
	 * http://androidtrainningcenter.
	 * blogspot.com/2012/03/how-to-convert-string-to-bitmap-and.html
	 * 
	 * @param bitmap
	 * @return converting bitmap and return a string
	 */
	public static String BitMapToString(Bitmap bitmap, Context c) {
		bitmap = scaleDownBitmap(bitmap, 200, c);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	/**
	 * This function was obtained from the source:
	 * http://androidtrainningcenter.
	 * blogspot.com/2012/03/how-to-convert-string-to-bitmap-and.html
	 * 
	 * @param encodedString
	 * @return bitmap (from given string)
	 */
	public static Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			return null;
		}
	}

	/**
	 * Got from :
	 * http://stackoverflow.com/questions/3528735/failed-binder-transaction
	 * 
	 * @param photo
	 * @param newHeight
	 * @param context
	 * @return
	 */
	public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight,
			Context context) {

		final float densityMultiplier = context.getResources()
				.getDisplayMetrics().density;

		int h = (int) (newHeight * densityMultiplier);
		int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

		photo = Bitmap.createScaledBitmap(photo, w, h, true);

		return photo;
	}

}
