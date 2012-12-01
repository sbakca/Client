package sussex.ase.android.group5.models;

public class Comment {

	private String username;

	private boolean isliked;
	
	public boolean IsLiked()
	{
		return isliked;
	}
	private boolean isdisliked;
	public boolean IsDisliked()
	{
		return isdisliked;
	}
	public String getUsername() {
		return username;
	}

	public String getContent() {
		return content;
	}

	public String getProfile() {
		return profile;
	}

	public String getLikeNumber() {
		return String.valueOf(likeNumber);
	}

	public String getDislikeNumber() {
		return String.valueOf(dislikeNumber);
	}

	public void Like() {
		likeNumber++;
		isliked= true;
	}

	public void Dislike() {
		dislikeNumber++;
		isdisliked = true;
	}

	private String content;
	private String profile;
	private int likeNumber;
	private int dislikeNumber;

	private int id;
	
	public int getId() {
		return id;
	}
	public Comment(String un, String cn, String p, int ln, int disn,int i) {
		username = un;
		content = cn;
		profile = p;
		likeNumber = ln;
		dislikeNumber = disn;
		id=i;
		isliked = false;
		isdisliked = false;
	}
}
