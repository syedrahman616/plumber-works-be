package com.pgneet.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgneet.dao.MockExamDao;
import com.pgneet.entity.APIResponseGet;
import com.pgneet.entity.CateRsultData;
import com.pgneet.entity.ExamPayload;
import com.pgneet.entity.GetQuestion;
import com.pgneet.entity.Getthumb;
import com.pgneet.entity.InfoData;
import com.pgneet.entity.Mett;
import com.pgneet.entity.QbttUpdate;
import com.pgneet.entity.QuestionId;
import com.pgneet.entity.Resultdata;
import com.pgneet.entity.Save;
import com.pgneet.entity.SearchCategory;
import com.pgneet.entity.Selectedquestions;
import com.pgneet.service.MockExamService;

@Service
public class MockExamServiceImpl implements MockExamService {
	
	@Autowired
	private MockExamDao repo;

	@Override
	public List<InfoData> getMockInfo() {
		
		return repo.getMockInfo();
	}

	@Override
	public Selectedquestions getMockSelectedQuestions(Mett requst) {
	
		return repo.getMockSelectedQuestions(requst);
	}

	@Override
	public boolean getMockTestCompleted(int id) {
	
		return repo.getMockTestCompleted(id);
	}

	@Override
	public boolean saveAndExitMock(Save request) {
		
		return repo.saveAndExitMock(request);
	}

	@Override
	public boolean updateMockScoredate(QbttUpdate request) {
	
		return repo.updateMockScoredate(request);
	}

	@Override
	public String getMockNote(QuestionId getQues) {
		
		return repo.getMockNote(getQues);
	}

	@Override
	public APIResponseGet<Object> getMockExam(ExamPayload payload) {
		
		return repo.getMockExam(payload);
	}

	@Override
	public APIResponseGet<Object> getMockQuestion(GetQuestion getQues) {
	
		return repo.getMockQuestion(getQues);
	}

	@Override
	public Resultdata getMockResultData(int referid) {
		
		return repo.getMockResultData(referid);
	}

	@Override
	public List<CateRsultData> getMockSearchCategoryResultData(SearchCategory request) {
	
		return repo.getMockSearchCategoryResultData(request);
	}

	@Override
	public Getthumb getMockThumb(int ques_Id) {
		
		return repo.getMockThumb(ques_Id);
	}
	

}
