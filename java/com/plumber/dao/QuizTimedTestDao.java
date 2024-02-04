package com.pgneet.dao;

import java.util.List;

import com.pgneet.entity.InfoData;
import com.pgneet.entity.Qbtt;
import com.pgneet.entity.Save;
import com.pgneet.entity.Selectedquestions;

public interface QuizTimedTestDao {
	
	public List<InfoData> showInfoData();
	public Selectedquestions showSelectedquestions(Qbtt request);
	public boolean getTestCompleted(int referid);
	public boolean saveAndExit(Save request);
}
