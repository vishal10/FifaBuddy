package Data;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fifabuddy.R;

import databaseTasks.PicturesTask;

/**
 * This class is used to implement a custom listview for the Friends Activity. I
 * implemented this by looking at the following example:
 * http://www.javasrilankansupport
 * .com/2012/05/android-listview-example-with-image-and.html
 * 
 * @author Vishal
 * 
 */
public class FriendListBaseAdapter extends BaseAdapter {

	private ArrayList<FriendData> friendsList;
	private LayoutInflater layout;

	public FriendListBaseAdapter(Context context, ArrayList<String> friends) {
		friendsList = new ArrayList<FriendData>();
		layout = LayoutInflater.from(context);
		for (String s : friends) {
			try {
				String image = new PicturesTask().execute("2", s).get();
				Bitmap pic = BitmapStringConvert.StringToBitMap(image);
				FriendData fd = new FriendData(s, pic);
				friendsList.add(fd);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getCount() {
		return friendsList.size();
	}

	@Override
	public Object getItem(int pos) {
		return friendsList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = layout.inflate(R.layout.friendlist_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) view
					.findViewById(R.id.ItemFriendPicture);
			holder.name = (TextView) view.findViewById(R.id.ItemFriendName);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.name.setText(friendsList.get(pos).getName());
		Bitmap pic = friendsList.get(pos).getImage();
		if (pic != null) {
			holder.image.setImageBitmap(pic);
		} else {
			holder.image.setImageResource(R.drawable.profile_picture);
		}
		return view;
	}

	static class ViewHolder {
		TextView name;
		ImageView image;
	}

}
