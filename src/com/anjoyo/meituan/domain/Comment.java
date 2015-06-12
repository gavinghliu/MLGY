package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comment {

	private String name;
	private String time;
	private int rank;
	private String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public static List<Comment> parseComment(String res) {
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("comments");
			if (array != null && !array.equals("[]")) {
				commentList  =new ArrayList<Comment>();
				for(int i=0;i<array.length();i++){
					Comment comment = new Comment();
					JSONObject object2 = array.optJSONObject(i);
					
					comment.setName(object2.optString("comment_name"));  
					comment.setTime(object2.optString("comment_time"));
					comment.setContent(object2.optString("comment_content"));
					comment.setRank(object2.getInt("comment_rank"));
					commentList.add(comment);
				}
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return commentList;

	}

}
