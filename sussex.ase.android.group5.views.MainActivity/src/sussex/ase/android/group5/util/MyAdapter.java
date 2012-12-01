package sussex.ase.android.group5.util;

import java.util.ArrayList;
import sussex.ase.android.group5.api.CommentAPI;
import sussex.ase.android.group5.models.Comment;
import com.androidhive.googleplacesandmaps.R;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	CommentAPI commentApi = new CommentAPI();
	ArrayList<Comment> list = null;
	Context context;
	LayoutInflater inflater;
	ViewHolder hodle;

	public MyAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setList(ArrayList<Comment> list) {
		this.list = list;
		// textViewsCanche = new ArrayList<TextView>();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.comment_item, null);
			hodle = new ViewHolder();
			hodle.username = (TextView) convertView
					.findViewById(R.id.username_txt);
			hodle.comment = (TextView) convertView
					.findViewById(R.id.comment_txt);
			hodle.tlike = (TextView) convertView.findViewById(R.id.like_txt);
			hodle.tdislike = (TextView) convertView
					.findViewById(R.id.dislike_txt);
			hodle.bLike = (ImageButton) convertView.findViewById(R.id.like_btn);
			hodle.bDis = (ImageButton) convertView
					.findViewById(R.id.dislike_btn);
			hodle.profile = (ImageView) convertView
					.findViewById(R.id.userimageView);
			convertView.setTag(hodle);
		} else {
			hodle = (ViewHolder) convertView.getTag();
		}
		SetHolde(position);
		return convertView;
	}

	private void SetHolde(int position) {
		final int positionTemp = position;
		hodle.username.setText(list.get(position).getUsername());
		hodle.comment.setText(list.get(position).getContent());
		hodle.tlike.setText(list.get(position).getLikeNumber());
		hodle.tdislike.setText(list.get(position).getDislikeNumber());
		hodle.profile.setImageResource(R.drawable.pic);
		hodle.bDis.setEnabled(!list.get(position).IsDisliked());
		hodle.bLike.setEnabled(!list.get(position).IsLiked());
		hodle.bLike.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UpdateLikeNumber(positionTemp);
			}
		});
		hodle.bDis.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UpdateDisLikeNumber(positionTemp);
			}
		});
	}

	private void UpdateLikeNumber(int i) {
		if(list.get(i).IsLiked())
		{
			return;
		}
		list.get(i).Like();
		new LikeOrDislikeTask().execute((new String[] { "1",
				String.valueOf(list.get(i).getId()) }));
		this.notifyDataSetChanged();
	}

	private void UpdateDisLikeNumber(int i) {
		list.get(i).Dislike();
		new LikeOrDislikeTask().execute((new String[] { "0",
				String.valueOf(list.get(i).getId()) }));
		this.notifyDataSetChanged();
	}

	class LikeOrDislikeTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			try {
				if (args[0].equals("0")) {
					commentApi.DislikeCommentById(Integer.valueOf(args[1]));
				}
				else
				{
					commentApi.LikeCommentById(Integer.valueOf(args[1]));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
		}

	}
}
