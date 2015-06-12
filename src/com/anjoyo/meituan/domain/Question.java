package com.anjoyo.meituan.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Question {
	private int question_id;
	private String question_time;
	private String question_content;
	private String question_answer;
	private boolean isDone;

	public static ArrayList<Question> parseQuestion(String res) {
		ArrayList<Question> questionList = new ArrayList<Question>();
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("questions");
			if (array != null && !array.equals("[]")) {
				questionList = new ArrayList<Question>();
				for (int i = 0; i < array.length(); i++) {
					Question question = new Question();
					JSONObject object2 = array.optJSONObject(i);

					question.setQuestion_id(object2.optInt("question_id"));
					question.setQuestion_time(object2.optString("question_time"));
					question.setQuestion_content(object2.optString("question_content"));
					question.setDone(object2.getInt("question_done") == 1);
					if (object2.has("question_answer")) {
						question.setQuestion_answer(object2.optString("question_answer"));
					} else {
						question.setQuestion_answer(null);
					}
					questionList.add(question);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return questionList;
	}
	
	public String getQuestion_answer() {
		return question_answer;
	}

	public void setQuestion_answer(String question_answer) {
		this.question_answer = question_answer;
	}


	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_time() {
		return question_time;
	}

	public void setQuestion_time(String question_time) {
		this.question_time = question_time;
	}

	public String getQuestion_content() {
		return question_content;
	}

	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
