package com.pgneet.dao;

import java.util.List;

import com.pgneet.entity.APIResponseResultDate;
import com.pgneet.entity.CateRsultData;
import com.pgneet.entity.QbResultData;
import com.pgneet.entity.Qbresultcategory;
import com.pgneet.entity.Qdetails;
import com.pgneet.entity.Result;
import com.pgneet.entity.ResultDataPayload;
import com.pgneet.entity.Resultdata;
import com.pgneet.entity.SearchCategory;

public interface ResultDao {
	
	public List<QbResultData> getQbResultData(Qdetails request);
	public List<Qbresultcategory> getQbresultcategory(int dropvalue);
	public List<Result> getResult(ResultDataPayload data);
	public List<CateRsultData> getSearchCategoryResultData(SearchCategory request);
	public APIResponseResultDate getAPIResponseResultDate();
	public Resultdata getResultdata(int referid);

}
