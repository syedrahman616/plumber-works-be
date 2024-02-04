package com.pgneet.response;

import com.pgneet.entity.APIResChart;
import com.pgneet.entity.APIResponse;
import com.pgneet.entity.APIResponseGet;
import com.pgneet.entity.APIResponseMy;
import com.pgneet.entity.APIResponseResultDate;
import com.pgneet.entity.AddFlag;
import com.pgneet.entity.Chartsummary;
import com.pgneet.entity.Exam;
import com.pgneet.entity.FinalCountPost;
import com.pgneet.entity.GetCategoryCount;
import com.pgneet.entity.Getthumb;
import com.pgneet.entity.Resultdata;
import com.pgneet.entity.Selectedquestions;

public class ResponseBuilder {

	public static APIResponse<Object> build(Object result) {
		APIResponse<Object> response = new APIResponse<>();
		response.setResult(result);
		return response;
	}

	public static APIResponseMy<Object> build(APIResponseMy res) {
		return res;

	}

	public static Chartsummary<Object> build(Chartsummary chat) {
		return chat;

	}

	public static Getthumb<Object> build(Getthumb thumb) {
		return thumb;
	}

	public static Selectedquestions<Object> build(Selectedquestions select) {
		return select;
	}

	public static FinalCountPost<Object> build(FinalCountPost count) {
		return count;
	}

	public static GetCategoryCount<Object> build(GetCategoryCount result) {
		return result;

	}

	public static Resultdata<Object> build(Resultdata data) {
		return data;
	}

	public static APIResponseResultDate<Object> build(APIResponseResultDate date) {
		return date;
	}

	public static APIResChart<Object> buildChart(Object result, Object myscore) {
		APIResChart<Object> response = new APIResChart<>();
		response.setResult(result);
		response.setMyscore(myscore);
		return response;
	}

	public static APIResponseGet<Object> build(APIResponseGet result) {
		return result;
	}

	public static Exam<Object> build(Exam result) {
		return result;

	}

}
