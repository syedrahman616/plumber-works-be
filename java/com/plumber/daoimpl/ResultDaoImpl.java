package com.pgneet.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pgneet.dao.ResultDao;
import com.pgneet.entity.APIResponseResultDate;
import com.pgneet.entity.CateRsultData;
import com.pgneet.entity.ListDetail;
import com.pgneet.entity.QbResultData;
import com.pgneet.entity.Qbresultcategory;
import com.pgneet.entity.Qbresultdatareview;
import com.pgneet.entity.Qdetails;
import com.pgneet.entity.Result;
import com.pgneet.entity.ResultDataPayload;
import com.pgneet.entity.Resultdata;
import com.pgneet.entity.SearchCategory;

@Repository
public class ResultDaoImpl implements ResultDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<QbResultData> getQbResultData(Qdetails request) {
		List<QbResultData> finalList = new ArrayList<>();
		List<ListDetail> list = request.getReviewdata();
		for (ListDetail quesReq : list) {
			int startnumber = quesReq.getStartnumber();
			int questionid = quesReq.getQuestionid();
			boolean flag = quesReq.isFlag();
			int userId = 4;
			int mark = quesReq.getMark();
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("questionid", questionid);
			param.addValue("flag", flag);
			param.addValue("userId", userId);
			int check = jdbcTemplate.queryForObject(
					"select count(question_id) as COUNT  from qbflag where question_id=:questionid and user_id=:userId;",
					param, Integer.class);
			if (check > 0) {
				jdbcTemplate.update(
						"update qbflag set flag_status=:flag where question_id=:questionid and user_id=:userId;",
						param);
			} else {
				jdbcTemplate.update(
						"insert into qbflag (question_id,flag_status,user_id) values (:questionid,:flag,:userId);",
						param);
			}
			QbResultData response = jdbcTemplate.queryForObject(
					"select tq.question as QUESTION,te.ans_status as ANS_STATUS from qbexam te, question tq, qbflag tf where te.question_id=tq.ques_id and tq.ques_id=tf.question_id and te.question_id =:questionid and te.user_id=:userId order by te.question_id;",
					param, new QbresultMapper());
			response.setId(startnumber);
			response.setStartnumber(startnumber);
			response.setFlag(flag);
			finalList.add(response);
		}
		return finalList;

	}

	private final class QbresultMapper implements RowMapper<QbResultData> {

		@Override
		public QbResultData mapRow(ResultSet rs, int rowNum) throws SQLException {
			QbResultData qbResult = new QbResultData();
			qbResult.setQuestion(rs.getString("QUESTION"));
			qbResult.setStatus(rs.getBoolean("ANS_STATUS"));
			return qbResult;
		}

	}

	@Override
	public List<Qbresultcategory> getQbresultcategory(int dropvalue) {
		List<Qbresultcategory> response = new ArrayList<>();
		int id = dropvalue;
		if (id == 1) {
			response = jdbcTemplate.query("select * from main_category", new ResultCategoryMapper());
			return response;
		}
		return response;
	}

	private static final class ResultCategoryMapper implements RowMapper<Qbresultcategory> {

		@Override
		public Qbresultcategory mapRow(ResultSet rs, int rowNum) throws SQLException {
			Qbresultcategory qbResult = new Qbresultcategory();
			qbResult.setId(rs.getInt("id"));
			qbResult.setMainCategory(rs.getString("main_category"));
			qbResult.setStatus(rs.getBoolean("status"));
			return qbResult;
		}

	}

	@Override
	public List<Result> getResult(ResultDataPayload data) {
		List<ListDetail> getInfo = data.getReviewdata();
		List<Integer> cate_info = data.getCategory();
		List<Integer> mainCategory = new ArrayList<>();
		List<Result> response = new ArrayList<>();
		Result setToResponse = new Result();
		List<Result> list_response = new ArrayList<>();
		MapSqlParameterSource param = new MapSqlParameterSource();
		for(ListDetail detail : getInfo) {
			int quesId = detail.getQuestionid();
			int userId = 4;
			param.addValue("quesId", quesId);
			param.addValue("userId", userId);
			Integer cat_id = jdbcTemplate.queryForObject("select main_category_id from qbexam where user_id=:userId and question_id=:quesId;", param, Integer.class);
			mainCategory.add(cat_id);
		}
		List<Integer> containList = new ArrayList<>(cate_info);
		containList.retainAll(mainCategory);
		for(Integer i : containList) {
			int main_cate = i;
			param.addValue("mainCategoryId", i);
			 list_response = jdbcTemplate.query("select ( @rownum :=@rownum +1) as ID,te.question_id as QUES_ID,te.main_category_id as MAIN_ID,te.ans_status as ANS,tq.question as QUESTION, tf.flag_status as FLAG from qbexam te left join question tq on te.question_id=tq.ques_id left join qbflag tf on tf.question_id=tq.ques_id and tq.ques_id=te.question_id CROSS JOIN (SELECT @rownum := 0) r where te.user_id=:userId and te.main_category_id in (:mainCategoryId);", param, new QbexamMapper());
		for(ListDetail detail : getInfo) {
			for(Result r : list_response) {
				param.addValue("question", r.getQuestion());
				int ques_id = jdbcTemplate.queryForObject("select ques_id from question where question=:question", param, Integer.class);
			if(detail.getQuestionid()== ques_id) {
				param.addValue("questionId", ques_id);
				setToResponse = jdbcTemplate.queryForObject("select te.question_id as QUES_ID,te.main_category_id as MAIN_ID,te.ans_status as ANS,tq.question as QUESTION, tf.flag_status as FLAG from qbexam te left join question tq on te.question_id=tq.ques_id left join qbflag tf on tf.question_id=tq.ques_id and tq.ques_id=te.question_id  where te.user_id=:userId and te.main_category_id in (:mainCategoryId) and te.question_id=:questionId;", param, new QbexamMapper());
				setToResponse.setId(detail.getStartnumber());
				setToResponse.setStartnumber(detail.getStartnumber());
				response.add(setToResponse);
			}
			}
		}
		}
		

		return response;
	}
	private final class QbexamMapper implements RowMapper<Result>{

		@Override
		public Result mapRow(ResultSet rs, int rowNum) throws SQLException {
			Result result = new Result();
			result.setQuestion(rs.getString("QUESTION"));
			result.setStatus(rs.getBoolean("ANS"));
			result.setFlag(rs.getBoolean("FLAG"));
			return result;
		}
		
	}


	@Override
	public APIResponseResultDate getAPIResponseResultDate() {
		APIResponseResultDate response = new APIResponseResultDate<>();
		int userId =4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		List<Qbresultdatareview> qbReview = new ArrayList<>();
		qbReview = jdbcTemplate.query("select te.id as ID,te.question_id as QUES_ID,te.main_category_id as MAIN_ID,te.ans_status as ANS,tq.question as QUESTION, tf.flag_status as FLAG from qbexam te left join question tq on te.question_id=tq.ques_id left join qbflag tf on tf.question_id=tq.ques_id and tq.ques_id=te.question_id where te.user_id=:userId;",param, new ReviewMapper());
		response.setResult(qbReview);
		int max = jdbcTemplate.queryForObject("select count(*) from qbexam where user_id=:userId;", param, Integer.class);
		response.setMaxnumber(max);
		double score = jdbcTemplate.queryForObject("select round(((sum(ans_status=1)/count(*))*100),2) from qbexam where user_id=:userId;", param, Double.class);
		response.setTotalscore(score);
		return response;
	}
	private final class ReviewMapper implements RowMapper<Qbresultdatareview>{

		@Override
		public Qbresultdatareview mapRow(ResultSet rs, int rowNum) throws SQLException {
			Qbresultdatareview review = new Qbresultdatareview();
			review.setId(rs.getInt("ID"));
			review.setQuestion(rs.getString("QUESTION"));
			review.setQuestionid(rs.getInt("QUES_ID"));
			review.setStatus(rs.getBoolean("ANS"));
			review.setCategory(rs.getInt("MAIN_ID"));
			String flag = rs.getString("FLAG");
			if(flag != null) {
				int f = Integer.parseInt(flag);
				if(f==0) {
					review.setFlag(false);
				}else {
					review.setFlag(true);
				}
			}else {
				review.setFlag(false);
			}
			review.setStartnumber(rs.getInt("id"));
			return review;
		}
		
	}

	@Override
	public Resultdata getResultdata(int referid) {
		int userId = 4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("referid", referid);
		Resultdata response = new Resultdata<>();
		List<CateRsultData> data = new ArrayList<>();
		
		response = jdbcTemplate.queryForObject("select count(*) as MAX_QUES,ti.noq as TOTAL,round((count(*)/ti.noq)*100,2) as SCORE from qbtt tq, qbttinfo ti where tq.qbttinfo_id=ti.id and tq.user_id=:userId and tq.qbttinfo_id=:referid;", param,new ScoreMapper());
		data = jdbcTemplate.query("select ( @rownum :=@rownum +1) as ID,tq.question_id,te.question,tq.main_category_id,tq.status,tq.startnumber,tf.flag_status from qbtt tq left join question te on tq.question_id=te.ques_id left join qbflag tf on tf.question_id=tq.question_id CROSS JOIN (SELECT @rownum := 0) r where tq.user_id=:userId and tq.qbttinfo_id=:referid;",param, new CateReviewMapper());;
		response.setResult(data);
		return response;
	}
	private final class ScoreMapper implements RowMapper<Resultdata>{

		@Override
		public Resultdata mapRow(ResultSet rs, int rowNum) throws SQLException {
			Resultdata result = new Resultdata<>();
			result.setTotalscore(rs.getDouble("SCORE"));
			result.setMaxnumber(rs.getInt("MAX_QUES"));
			result.setTotalquestions(rs.getInt("TOTAL"));
			return result;
		}
		
	}


	@Override
	public List<CateRsultData> getSearchCategoryResultData(SearchCategory request) {
		int refId = request.getReferid();
		int userId = 4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("refId", refId);
		List<Integer> cat_List = request.getCategory();
		List<Integer> cat_Id = jdbcTemplate.queryForList("select tq.main_category_id from qbtt tq left join question te on tq.question_id=te.ques_id left join qbflag tf on tf.question_id=tq.question_id CROSS JOIN (SELECT @rownum := 0) r where tq.user_id=:userId and tq.qbttinfo_id=:refId;", param,Integer.class);
		List<CateRsultData> response = new ArrayList<>();
		List<Integer> selectedId = new ArrayList<>(cat_List);
		selectedId.retainAll(cat_Id);
		for(Integer i : selectedId) {
			param.addValue("categeory", i);
			List<CateRsultData> data = jdbcTemplate.query("select ( @rownum :=@rownum +1) as ID,tq.question_id,te.question,tq.main_category_id,tq.status,tq.startnumber,tf.flag_status from qbtt tq left join question te on tq.question_id=te.ques_id left join qbflag tf on tf.question_id=tq.question_id CROSS JOIN (SELECT @rownum := 0) r where tq.user_id=:userId and tq.qbttinfo_id=:refId and tq.main_category_id=:categeory;",param, new CateReviewMapper());
			response.addAll(data);
		}
	
		return response;
	}
	private class CateReviewMapper implements RowMapper<CateRsultData>{

		@Override
		public CateRsultData mapRow(ResultSet rs, int rowNum) throws SQLException {
			CateRsultData data = new CateRsultData();
			data.setId(rs.getInt("ID"));
			data.setQuestionid(rs.getInt("question_id"));
			data.setQuestion(rs.getString("question"));
			data.setStartnumber(rs.getInt("startnumber"));
			data.setStatus(rs.getBoolean("status"));
			String flag = rs.getString("flag_status");
			if(flag != null) {
				int set = Integer.parseInt(flag);
				if(set == 1) {
					data.setFlag(true);
				}else {
					data.setFlag(false);
				}
			}else {
				data.setFlag(false);
			}
			return data;
		}
		
	}
}
