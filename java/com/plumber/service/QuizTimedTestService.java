package com.pgneet.service;

import java.util.List;

import com.pgneet.entity.InfoData;
import com.pgneet.entity.Qbtt;
import com.pgneet.entity.Save;
import com.pgneet.entity.Selectedquestions;

public interface QuizTimedTestService {
	
	public List<InfoData> getInfoData();
	public boolean getTestCompleted(int referid);
	public Selectedquestions getSelectedquestions(Qbtt requst);
	public boolean saveAndExit(Save request);

}
