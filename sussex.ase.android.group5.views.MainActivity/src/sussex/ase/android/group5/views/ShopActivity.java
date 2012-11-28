package sussex.ase.android.group5.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.androidhive.googleplacesandmaps.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ShopActivity extends Activity {

	List<Map<String, Object>> contents =  new ArrayList<Map<String, Object>>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        
        
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PIC", R.drawable.pic);
			map.put("TITLE", "Test Title"+i);
			map.put("CONTENT", "Test Content");
			contents.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.list_item,
				new String[] { "PIC", "TITLE", "CONTENT" }, new int[] {
						R.id.listitem_pic, R.id.listitem_title,
						R.id.listitem_content });

		ListView list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_shop, menu);
        return true;
    }
}
