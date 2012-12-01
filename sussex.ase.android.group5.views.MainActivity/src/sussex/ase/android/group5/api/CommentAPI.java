package sussex.ase.android.group5.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import sussex.ase.android.group5.models.Comment;
import sussex.ase.android.group5.util.IPAddress;

public class CommentAPI {

	public ArrayList<Comment> GetCommentsByShopkey(String shopkey) {
		ArrayList<Comment> result = new ArrayList<Comment>();
		String uriAPI = IPAddress.IP + "/ASE_SERVER/getcomments.jsp";
		String strRet = "";

		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			HttpPost httpost = new HttpPost(uriAPI);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("s_key", shopkey));
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			strRet = EntityUtils.toString(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		strRet=strRet.replaceAll("[\\n\\r]","");
		String[] tempList = strRet.split(".:,");
		if (tempList != null) {
			int n = 0;
			while (n < tempList.length) {
					Comment c = new Comment(tempList[n],tempList[n+1],"" ,
							Integer.valueOf(tempList[n+2]), Integer.valueOf(tempList[n+3]), Integer.valueOf(tempList[n+4]));
					result.add(c);
					n=n+5;
				
			}
		}
		return result;
	}

	public void LikeCommentById(int commentId) {
		String uriAPI = IPAddress.IP + "/ASE_SERVER/like.jsp";
		try {

			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			HttpPost httpost = new HttpPost(uriAPI);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("cid", String.valueOf(commentId)));
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DislikeCommentById(int commentId) {
		String uriAPI = IPAddress.IP + "/ASE_SERVER/dislike.jsp";

		try {

			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			HttpPost httpost = new HttpPost(uriAPI);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("cid", String.valueOf(commentId)));
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
