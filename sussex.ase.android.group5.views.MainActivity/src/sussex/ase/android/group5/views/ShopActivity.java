package sussex.ase.android.group5.views;

import java.util.List;
import sussex.ase.android.group5.api.CommentAPI;
import sussex.ase.android.group5.util.AddItemizedCurrentOverlay;
import sussex.ase.android.group5.util.MyAdapter;
import com.androidhive.googleplacesandmaps.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Html;
import android.widget.ListView;
import android.widget.TextView;

public class ShopActivity extends MapActivity {

	private double latitude;
	private double longtitude;
	private ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		SetViewText();
		AddLocationTag();
		new LoadCommentTask().execute();
	}

	private void AddLocationTag() {

		MapView mapView = (MapView) findViewById(R.id.shopmapview);
		GeoPoint gp = new GeoPoint((int) (latitude * 1E6),
				(int) (longtitude * 1E6));
		MapController mapctrl = mapView.getController();
		mapctrl.setCenter(gp);
		List<Overlay> mapOverlays = mapView.getOverlays();
		AddItemizedCurrentOverlay itemizedoverlayR = new AddItemizedCurrentOverlay(
				getResources().getDrawable(R.drawable.mark_red),
				ShopActivity.this);
		OverlayItem overlayitem = new OverlayItem(gp, null, null);
		itemizedoverlayR.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlayR);
		itemizedoverlayR.populateNow();

	}

	private void SetViewText() {
		Intent intent = getIntent();
		latitude = intent.getDoubleExtra("lati", 0);
		longtitude = intent.getDoubleExtra("longti", 0);
		TextView tShopname = (TextView) findViewById(R.id.shopname);
		TextView tdescription = (TextView) findViewById(R.id.message);
		tShopname.setText(intent.getStringExtra("title"));
		tdescription.setText(intent.getStringExtra("message"));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class LoadCommentTask extends AsyncTask<String, String, String> {

		MyAdapter adapter =null;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ShopActivity.this);
			pDialog.setMessage(Html.fromHtml("<b>....</b><br/>Loading..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			try {
				CommentAPI commentApi = new CommentAPI();
			 adapter = new MyAdapter(ShopActivity.this);
				adapter.setList(commentApi.GetCommentsByShopkey(String.valueOf(longtitude)
						+","+String.valueOf(latitude)));
				runOnUiThread(new Runnable() {
				     public void run() {
				    	 SetAdapter();
				    }
				});
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		private void SetAdapter()
		{
			ListView list = (ListView) findViewById(R.id.list);
			list.setAdapter(adapter);
		}
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}

	}

}
