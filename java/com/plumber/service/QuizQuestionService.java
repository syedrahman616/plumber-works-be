package com.pgneet.service;

import java.util.List;

import com.pgneet.entity.APIResponseGet;
import com.pgneet.entity.AddFlag;
import com.pgneet.entity.CategoryParam;
import com.pgneet.entity.Exam;
import com.pgneet.entity.ExamPayload;
import com.pgneet.entity.Faq;
import com.pgneet.entity.FinalCountPost;
import com.pgneet.entity.FinalCountPost.CountId;
import com.pgneet.entity.GetCategoryCount;
import com.pgneet.entity.GetQuestion;
import com.pgneet.entity.Getthumb;
import com.pgneet.entity.Improve;
import com.pgneet.entity.MyNote;
import com.pgneet.entity.Note;
import com.pgneet.entity.QbttUpdate;
import com.pgneet.entity.QuestionId;
import com.pgneet.entity.Thumb;

public interface QuizQuestionService {
	public GetCategoryCount getCategorycount();
	public FinalCountPost getFinalCountPost(CategoryParam request);
	public List<CountId> getCountId(CategoryParam request);
	public String getNote(QuestionId request);
	public APIResponseGet getAPIResponseGet(GetQuestion getQues);
	public APIResponseGet getExam(ExamPayload payload);
	public List<Faq> getFaq();
	public boolean addFlag(AddFlag request);
	public boolean updateScore(QbttUpdate request);
	public boolean updateMynote(MyNote request);
	public Getthumb updateThumb(Thumb request);
	public boolean insertImprove(Improve request);

}
