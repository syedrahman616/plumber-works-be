package com.pgneet.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgneet.dao.QuizTimedTestDao;
import com.pgneet.entity.FinalCountPost;
import com.pgneet.entity.FinalCountPost.CountId;
import com.pgneet.entity.InfoData;
import com.pgneet.entity.Qbtt;
import com.pgneet.entity.Save;
import com.pgneet.entity.Scoredata;
import com.pgneet.entity.Selectedquestions;
import com.pgneet.service.QuizTimedTestService;

@Service
public class QuizTimedTestImpl implements QuizTimedTestService {
	
	@Autowired
	private QuizTimedTestDao repo;

	@Override
	public List<InfoData> getInfoData() {
		 return repo.showInfoData();
		
	}

	@Override
	public Selectedquestions getSelectedquestions(Qbtt request) {
		return repo.showSelectedquestions(request);
		 
	}

	@Override
	public boolean getTestCompleted(int referid) {
		return repo.getTestCompleted(referid);
	
	}

	@Override
	public boolean saveAndExit(Save request) {
		return repo.saveAndExit(request);
	}

	

}
