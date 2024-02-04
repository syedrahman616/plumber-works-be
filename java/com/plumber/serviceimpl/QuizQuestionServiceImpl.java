package com.pgneet.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgneet.dao.QuizQuestionDao;
import com.pgneet.entity.APIResponseGet;
import com.pgneet.entity.AddFlag;
import com.pgneet.entity.Answer;
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
import com.pgneet.service.QuizQuestionService;

@Service
public class QuizQuestionServiceImpl implements QuizQuestionService {

	@Autowired
	private QuizQuestionDao repo;

	@Override
	public GetCategoryCount getCategorycount() {
		return repo.getCategoryCount();
	}

	@Override
	public FinalCountPost getFinalCountPost(CategoryParam request) {
		return repo.getFinalCountPost(request);

	}

	@Override
	public List<CountId> getCountId(CategoryParam request) {
		return repo.getCountId(request);
	}

	@Override
	public String getNote(QuestionId request) {
		return repo.getNote(request);
		
	}

	@Override
	public APIResponseGet getAPIResponseGet(GetQuestion getQues) {
		return repo.getResponse(getQues);
	}

	@Override
	public APIResponseGet getExam(ExamPayload payload) {
		return repo.getExam(payload);
	}

	@Override
	public List<Faq> getFaq() {
		return repo.getFaq();
	}

	@Override
	public boolean addFlag(AddFlag request) {
		return repo.addFlag(request);
	}

	@Override
	public boolean updateScore(QbttUpdate request) {
		return repo.updateScore(request);
	}

	@Override
	public boolean updateMynote(MyNote request) {
		return repo.updateMynote(request);
	}

	@Override
	public Getthumb updateThumb(Thumb request) {
		return repo.updateThumb(request);
	}

	@Override
	public boolean insertImprove(Improve request) {
		
		return repo.insertImprove(request);
	}

}
