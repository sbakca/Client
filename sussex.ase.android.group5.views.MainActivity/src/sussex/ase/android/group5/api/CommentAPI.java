package sussex.ase.android.group5.api;

import java.util.ArrayList;

import sussex.ase.android.group5.models.Comment;

public class CommentAPI {

	public ArrayList<Comment> GetCommentsByShopkey(String shopkey) {

		ArrayList<Comment> result = new ArrayList<Comment>();
		for (int i = 0; i < 25; i++) {
			Comment c = new Comment(String.valueOf(i), String.valueOf(i),
					String.valueOf(i), i, i,i);
			result.add(c);
		}
		return result;
	}
	
	public void LikeCommentById(int commentId)
	{
		System.out.println("liked"+commentId);
	}
	
	public void DislikeCommentById(int commentId)
	{
		System.out.println("disliked"+commentId);

	}

}
