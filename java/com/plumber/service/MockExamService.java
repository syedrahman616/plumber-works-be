package com.pgneet.service;

import java.util.List;

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

public interface MockExamService {
	List<InfoData> getMockInfo();
	Selectedquestions getMockSelectedQuestions(Mett requst);
	boolean getMockTestCompleted(int id);
	boolean saveAndExitMock(Save request);
	boolean updateMockScoredate(QbttUpdate request);
	String getMockNote(QuestionId getQues);
	APIResponseGet<Object> getMockExam(ExamPayload payload);
	APIResponseGet<Object> getMockQuestion(GetQuestion getQues);
	Resultdata getMockResultData(int referid);
	List<CateRsultData> getMockSearchCategoryResultData(SearchCategory request);
	Getthumb getMockThumb(int ques_Id);
}
