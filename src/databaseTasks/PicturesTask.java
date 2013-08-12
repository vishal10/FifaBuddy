package databaseTasks;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesRequest;
import com.amazonaws.services.simpledb.model.GetAttributesResult;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

/**
 * This task is responsible for uploading and downloading a users profile
 * picture whenever required.
 * 
 * @author Vishal
 * 
 */
public class PicturesTask extends AsyncTask<String, Void, String> {

	private static AmazonSimpleDBClient client;

	@Override
	protected String doInBackground(String... params) {
		client = new SimpleDBAccess().getClient();
		if (params[0].equals("1")) {
			String username = params[1];
			String image = params[2];
			clearImage(username);
			if (saveImage(username, image))
				return "saved";
		} else if (params[0].equals("2")) {
			String username = params[1];
			String im = getImage(username);
			if (!im.equals("")) {
				return im;
			}
		}
		return "not saved";
	}

	private String getImage(String username) {
		GetAttributesRequest gar = new GetAttributesRequest("Pictures",
				username);
		GetAttributesResult result = client.getAttributes(gar);
		if (result == null || result.getAttributes().size() == 0) {
			return "";
		}
		List<Attribute> attrs = result.getAttributes();
		String image = "";
		ArrayList<String> imageParts = new ArrayList<String>(attrs.size());
		for (int i = 0; i < attrs.size(); i++) {
			imageParts.add("");
		}
		for (Attribute a : attrs) {
			if (a.getName().startsWith("Picture")) {
				int loc = Integer.parseInt(a.getName().substring(7)) - 1;
				String part = a.getValue();
				imageParts.set(loc, part);
			}
		}
		for (int i = 0; i < imageParts.size(); i++) {
			image += imageParts.get(i);
		}
		return image;
	}

	private void clearImage(String username) {
		GetAttributesRequest gar = new GetAttributesRequest("Pictures",
				username);
		GetAttributesResult result = client.getAttributes(gar);
		if (result == null || result.getAttributes().size() == 0) {
			return;
		}
		DeleteAttributesRequest dar = new DeleteAttributesRequest("Pictures",
				username);
		client.deleteAttributes(dar);
	}

	private boolean saveImage(String username, String image) {
		int size = 1024;
		int count = 1;
		String test = "";
		while (((count - 1) * size) < image.length()) {
			String partImage = "";
			if (image.length() > size * count) {
				partImage = image.substring(size * (count - 1), size * count);
			} else {
				partImage = image.substring(size * (count - 1));
			}
			test += partImage;
			try {
				ReplaceableAttribute pic = new ReplaceableAttribute("Picture"
						+ count, partImage, Boolean.FALSE);
				List<ReplaceableAttribute> attrs = new ArrayList<ReplaceableAttribute>(
						1);
				attrs.add(pic);
				PutAttributesRequest par = new PutAttributesRequest("Pictures",
						username, attrs);
				client.putAttributes(par);
				count++;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return (test.equals(image));
	}
}
