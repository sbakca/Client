package sussex.ase.android.group5.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import sussex.ase.android.group5.util.IPAddress;

import android.util.Log;

public class Login {

	public boolean Login(String username, String password) {
		
		String uriAPI = IPAddress.IP + "/ASE_SERVER/login.jsp";
		String strRet = "";

		try {

		      DefaultHttpClient httpclient = new DefaultHttpClient();
		      HttpResponse response;
		      HttpPost httpost = new HttpPost(uriAPI);
		      List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		      nvps.add(new BasicNameValuePair("uid", username)); 
		      nvps.add(new BasicNameValuePair("upw", password)); 
		      
		      httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		      
		      response = httpclient.execute(httpost);
		      HttpEntity entity = response.getEntity();
		      //entity = response.getEntity();
		      
		     // Log.d(TAG, "HTTP POST getStatusLine: " + response.getStatusLine());
		      
		      /* HTML POST response BODY */
		      strRet = EntityUtils.toString(entity);
		     // Log.i(TAG, strRet);
		      strRet = strRet.trim().toLowerCase();
		      
		      List<Cookie> cookies = httpclient.getCookieStore().getCookies();
		      if (entity != null)
		      {
		        entity.consumeContent();
		      }
		      
		    //  Log.d(TAG, "HTTP POST Initialize of cookies."); 
		      cookies = httpclient.getCookieStore().getCookies(); 
		      if (cookies.isEmpty())
		      {
		       // Log.d(TAG, "HTTP POST Cookie not found.");
		       // Log.i(TAG, entity.toString());
		      }
		      else
		      {
		        for (int i = 0; i < cookies.size(); i++)
		        {
		        //  Log.d(TAG, "HTTP POST Found Cookie: " + cookies.get(i).toString()); 
		        } 
		      }
		      
		      
		      if(strRet.equals("1"))
		      {
		        Log.i("TEST", "YES");
		        return true;
		      }
		      else
		      {
		        Log.i("TEST", "NO");
		        return false;
		      }
		    }
		    catch(Exception e)
		    {
		      e.printStackTrace();
		      return false;
		    }
	}
}
