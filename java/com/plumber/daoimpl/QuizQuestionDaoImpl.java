package com.pgneet.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.pgneet.dao.QuizQuestionDao;
import com.pgneet.entity.APIResponseGet;
import com.pgneet.entity.AddFlag;
import com.pgneet.entity.Answer;
import com.pgneet.entity.CategoryParam;
import com.pgneet.entity.Categorycount;
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
import com.pgneet.entity.SubCount;
import com.pgneet.entity.Thumb;

@Repository
public class QuizQuestionDaoImpl implements QuizQuestionDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public GetCategoryCount getCategoryCount() {
		GetCategoryCount response = new GetCategoryCount<>();
		MapSqlParameterSource param = new MapSqlParameterSource();
		int tCount = jdbcTemplate.queryForObject("select count(*) as RCOUNT from qbexam ", param, Integer.class);
		int mCount = jdbcTemplate.queryForObject("select count(*) as COUNT from main_category  ", param, Integer.class);

		List<Categorycount> count = jdbcTemplate.query(
				"select sum(ans_status=1) as RIGHT_ANS, sum(ans_status=0) as WRONG_ANS, count(*) as COUNT,main_category,tmc.id,user_id from main_category tmc LEFT JOIN qbexam tqbe ON tmc.id=tqbe.main_category_id and tqbe.user_id=4  group by main_category_id,tmc.ID order by id asc",
				new CategoeryMapper());
		int correct = 0;
		int wrong = 0;
		for (Categorycount total : count) {
			correct += total.getCorrectAnswer();
			wrong += total.getWrongAnswer();
		}
		response.setTotalCorrect(correct);
		response.setTotalWrong(wrong);
		response.setFinalcount((mCount * 50) - tCount);
		response.setResult(count);
		return response;
	}

	private static final class CategoeryMapper implements RowMapper<Categorycount> {

		@Override
		public Categorycount mapRow(ResultSet rs, int rowNum) throws SQLException {
			Categorycount category = new Categorycount();
			category.setId(rs.getInt("id"));
			category.setCategory(rs.getString("main_category"));
			category.setTotalcount(50);
			int count = rs.getString("user_id") != null ? rs.getInt("COUNT") : 0;
			category.setCount(count);
			category.setCorrectAnswer(rs.getInt("RIGHT_ANS"));
			category.setWrongAnswer(rs.getInt("WRONG_ANS"));
			return category;
		}

	}

	@Override
	public FinalCountPost getFinalCountPost(CategoryParam request) {
		int value = request.getDropvalue();
		List<Integer> categoryValue = request.getCategory();
		FinalCountPost response = new FinalCountPost<>();
		int categorySubCount = 0;
		if (value == 1) {
			for (Integer i : categoryValue) {
				int c = i;
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("c", c);
				int count = jdbcTemplate.queryForObject(
						"select count(*) from main_category tmc , qbexam tqbe WHERE tmc.id=tqbe.main_category_id and tqbe.user_id=4  and tmc.id in (:c)",
						param, Integer.class);
				if (count > 0) {
					response = jdbcTemplate.queryForObject(
							"select count(*) as COUNT,main_category,tm.id from main_category tm, qbexam tq where tm.id=tq.main_category_id  and tm.id in (:c) group by tm.ID;",
							param, new CountMapper());
					int subCount = response.getFinalcount();
					int tempCount = 50 - subCount;
					categorySubCount = categorySubCount + tempCount;
					response.setFinalcount(categorySubCount);
				} else {
					categorySubCount = categorySubCount + 50;
					response.setFinalcount(categorySubCount);
				}
			}

		} else if (value == 2) {
			for (Integer i : categoryValue) {
				int c = i;
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("c", c);
				int count = jdbcTemplate.queryForObject(
						"select count(*) from main_category tmc , qbexam tqbe WHERE tmc.id=tqbe.main_category_id and tqbe.user_id=4  and tmc.id in (:c)",
						param, Integer.class);
				if (count > 0) {
					SubCount intResponse = jdbcTemplate.queryForObject(
							"select sum(ans_status=1) as WRONG_ANS,main_category,tmc.id from main_category tmc , qbexam tqbe WHERE tmc.id=tqbe.main_category_id and tqbe.user_id=4  and tmc.id in (:c) group by tmc.ID;",
							param, new SubCountMapper());
					int subCount = intResponse.getFinalCount();
					int tempCount = 50 - subCount;
					categorySubCount = categorySubCount + tempCount;
					response.setFinalcount(categorySubCount);
				} else {
					categorySubCount = categorySubCount + 50;
					response.setFinalcount(categorySubCount);
				}
			}

		}
		return response;

	}

	final class SubCountMapper implements RowMapper<SubCount> {

		@Override
		public SubCount mapRow(ResultSet rs, int rowNum) throws SQLException {
			SubCount subCount = new SubCount();
			subCount.setFinalCount(rs.getInt("WRONG_ANS"));
			return subCount;
		}

	}

	final class CountMapper implements RowMapper<FinalCountPost> {

		@Override
		public FinalCountPost mapRow(ResultSet rs, int rowNum) throws SQLException {
			FinalCountPost post = new FinalCountPost<>();
			int fCount = rs.getInt("COUNT");
			post.setFinalcount(fCount);
			return post;
		}

	}

	@Override
	public List<CountId> getCountId(CategoryParam request) {
		int value = request.getDropvalue();
		List<Integer> categoryValue = request.getCategory();
		List<CountId> response = new ArrayList<>();
		if (value == 1) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			for (Integer i : categoryValue) {
				param.addValue("c", i);
				List<CountId> questionId = jdbcTemplate.query(
						"select tq.ques_id from main_category tm,category tc,sub_category ts,question tq where tm.id=tc.main_category_id and tc.id=ts.category_id and ts.id=tq.sub_category_id and  tm.id in(:c) order by tm.id limit 50;",
						param, new QuestionIdMapper());
				List<CountId> attentedQuestion = jdbcTemplate.query(
						"select question_id from qbexam where main_category_id in (:c) order by question_id asc limit 50;",
						param, new AttentQuestionIdMapper());
				List<CountId> remainingQuesId = new ArrayList<>(questionId);
				remainingQuesId.removeAll(attentedQuestion);
				response.addAll(remainingQuesId);

			}
		} else if (value == 2) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			for (Integer i : categoryValue) {
				param.addValue("c", i);
				List<CountId> questionId = jdbcTemplate.query(
						"select tq.ques_id from main_category tm,category tc,sub_category ts,question tq where tm.id=tc.main_category_id and tc.id=ts.category_id and ts.id=tq.sub_category_id and  tm.id in(:c) order by tm.id limit 50;",
						param, new QuestionIdMapper());
				List<CountId> correctAnsQuesId = jdbcTemplate.query(
						"select tq.ques_id as question_id from main_category tm,category tc,sub_category ts,question tq,qbexam tqb where tq.ques_id=tqb.question_id  and tm.id=tc.main_category_id and tc.id=ts.category_id and ts.id=tq.sub_category_id and  tm.id in(:c) and tqb.user_id=4 and ans_status=1 order by tq.ques_id limit 50;",
						param, new AttentQuestionIdMapper());
				List<CountId> remainingQuesId = new ArrayList<>(questionId);
				remainingQuesId.removeAll(correctAnsQuesId);
				response.addAll(remainingQuesId);

			}
		}
		return response;
	}

	private final class AttentQuestionIdMapper implements RowMapper<CountId> {

		@Override
		public CountId mapRow(ResultSet rs, int rowNum) throws SQLException {
			CountId id = new FinalCountPost().new CountId();
			id.setId(rs.getInt("question_id"));
			return id;
		}

	}

	public final class QuestionIdMapper implements RowMapper<CountId> {

		@Override
		public CountId mapRow(ResultSet rs, int rowNum) throws SQLException {
			CountId id = new FinalCountPost().new CountId();
			id.setId(rs.getInt("ques_id"));
			return id;
		}

	}

	@Override
	public String getNote(QuestionId request) {
		int id = request.getQuestionid();
		int userId = 4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("quesId", id);
		param.addValue("userId", userId);
		int check = jdbcTemplate.queryForObject(
				"select count(message) from qbmynote where question_id =:quesId and user_id=:userId;", param,
				Integer.class);
		if (check > 0) {
			String msg = jdbcTemplate.queryForObject(
					"select message from qbmynote where question_id =:quesId and user_id=:userId;", param,
					String.class);
			return msg;
		}

		return "No Notes";
	}

	@Override
	public APIResponseGet getResponse(GetQuestion getQues) {
		APIResponseGet response = new APIResponseGet<>();
		int userId = 4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		if (getQues.getReferid() == null) {
			if (getQues.getDropvalue() == 1) {
				List<Integer> quesData = getQues.getQuestiondata();
				int startNum = getQues.getStartnumber() - 1;
				int quesId = quesData.get(startNum);
				param.addValue("ques_id", quesId);
				/*
				 * int flagCheck = jdbcTemplate.
				 * queryForObject("select count(*) from qbflag where question_id=:ques_id and user_id=:userId;"
				 * , param, Integer.class); if(flagCheck==0) { jdbcTemplate.
				 * update("insert into qbfalg (question_id, user_id, flag_status) values (:ques_id,:userId,false);"
				 * , param); }
				 */
				int count = jdbcTemplate.queryForObject(
						"select count(*) as COUNT from qbexam te left join question tq on te.question_id=tq.ques_id left join answer ta on te.selected_answer_id=ta.id  left join profile tp on te.user_id=tp.user_id where te.question_id in (:ques_id) order by tq.ques_id;",
						param, Integer.class);
				if (count > 0) {
					response = jdbcTemplate.queryForObject(
							"select * from qbexam te left join question tq on te.question_id=tq.ques_id left join answer ta on te.selected_answer_id=ta.id left join profile tp on te.user_id=tp.user_id where te.question_id in (:ques_id) and te.user_id=4 order by tq.ques_id;",
							param, new QuestionMapper());
				} else {
					response = jdbcTemplate.queryForObject(
							"select *,ques_id as question_id from question where ques_id=:ques_id;", param,
							new QuestionMapper());
				}
			} else if (getQues.getDropvalue() == 2) {
				List<Integer> quesData = getQues.getQuestiondata();
				int startNum = getQues.getStartnumber() - 1;
				int quesId = quesData.get(startNum);
				param.addValue("ques_id", quesId);
				response = jdbcTemplate.queryForObject(
						"select *,ques_id as question_id from question where ques_id=:ques_id;", param,
						new QuestionNewMapper());
			}
		} else if (getQues.getDropvalue() == 0) {
			List<Integer> quesData = getQues.getQuestiondata();
			int index = getQues.getStartnumber() - 1;
			int question_id = quesData.get(index);
			int startNumber = getQues.getStartnumber();
			int referId = Integer.parseInt(getQues.getReferid());
			int user_id = 4;
			param.addValue("user_id", user_id);
			param.addValue("referId", referId);
			param.addValue("question_id", question_id);
			param.addValue("startNumber", startNumber);
			int count = jdbcTemplate.queryForObject(
					"select count(question_id) as COUNT from qbtt where question_id=:question_id and user_id=:user_id and qbttinfo_id=:referId and startnumber=:startNumber;",
					param, Integer.class);
			if (count > 0) {
				response = jdbcTemplate.queryForObject(
						"select *,tqt.status as ANS_STATUS from qbtt  tqt,question tq where tqt.question_id=tq.ques_id and tqt.startnumber=:startNumber and tqt.qbttinfo_id =:referId and tqt.question_id=:question_id;",
						param, new QbttQuesMapper());
			} else {
				response = jdbcTemplate.queryForObject(
						"select *,ques_id as question_id from question where ques_id=:question_id;", param,
						new QuestionNewMapper());
			}
		}

		return response;
	}

	private final class QbttQuesMapper implements RowMapper<APIResponseGet> {

		@Override
		public APIResponseGet mapRow(ResultSet rs, int rowNum) throws SQLException {
			APIResponseGet qbtt = new APIResponseGet<>();
			qbtt.setResult(rs.getObject("question"));
			int question_Id = rs.getInt("question_id");
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("question_Id", question_Id);
			List<Answer> ansList = jdbcTemplate.query(
					"select ta.id as SELECTED_ANS_ID,ta.answer as ANSWER, ta.status as Q_STA,tq.status as MARK_STATUS,tq.question_id as QUESTION_ID, tq.selected_answer_id from qbtt tq, answer ta where tq.question_id=ta.question_id and tq.user_id =4 and tq.question_id =:question_Id;",
					param, new QbttAnswerMapper());
			qbtt.setAnswer(ansList);
			qbtt.setQuestionid(question_Id);
			qbtt.setAnsweredstatus(true);
			qbtt.setReference(rs.getString("reference"));
			qbtt.setMarkstatus(rs.getBoolean("ANS_STATUS"));
			int count = jdbcTemplate.queryForObject(
					"SELECT count(*) FROM qbflag where question_id =:question_Id", param, Integer.class);
			if (count > 0) {
				boolean flag = jdbcTemplate.queryForObject(
						"select flag_status from qbflag where question_id =:question_Id", param, Boolean.class);
				qbtt.setFlagstatus(flag);
			} else {
				qbtt.setFlagstatus(false);
			}

			return qbtt;
		}

	}

	private final class QbttAnswerMapper implements RowMapper<Answer> {

		@Override
		public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Answer ans = new Answer();
			ans.setId(rs.getInt("SELECTED_ANS_ID"));
			int selected_id = ans.getId();
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("selected_id", selected_id);
			int selected = jdbcTemplate.queryForObject(
					"select count(*) from qbtt where selected_answer_id =:selected_id", param, Integer.class);

			int ques_id = rs.getInt("QUESTION_ID");
			param.addValue("quesId", ques_id);
			int totalUser = jdbcTemplate.queryForObject("select count(*) from qbtt where question_id =:quesId", param,
					Integer.class);
			ans.setAnswer(rs.getString("ANSWER"));
			ans.setRightanswer(rs.getBoolean("Q_STA"));
			int quesCheck = jdbcTemplate.queryForObject("select count(*) from qbexam where question_id =:quesId", param,
					Integer.class);
			if (selected_id == (rs.getInt("selected_answer_id"))) {
				ans.setSelectedanswer(true);
			}
			double percentage = (double) selected / totalUser * 100;
			int percent = (int) percentage;
			ans.setPercentage(percent);

			return ans;
		}

	}

	private final class QuestionNewMapper implements RowMapper<APIResponseGet> {

		@Override
		public APIResponseGet mapRow(ResultSet rs, int rowNum) throws SQLException {
			APIResponseGet mapper = new APIResponseGet<>();
			mapper.setResult(rs.getObject("question"));
			int questionId = rs.getInt("question_id");
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("quesId", questionId);
			List<Answer> ansList = new ArrayList<>();
			ansList = jdbcTemplate.query(
					"select *, ta.id as SELECTED_ANS_ID,ta.status as Q_STA from question tq, answer ta where tq.ques_id=ta.question_id and tq.ques_id=:quesId;",
					param, new AnswerNewMapper());
			mapper.setAnswer(ansList);
			mapper.setReference(rs.getString("reference"));
			mapper.setQuestionid(rs.getInt("ques_id"));
			mapper.setAnsweredstatus(false);
			mapper.setMarkstatus(false);
			mapper.setFlagstatus(false);
			return mapper;
		}

	}

	private final class AnswerNewMapper implements RowMapper<Answer> {

		@Override
		public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Answer ans = new Answer();
			ans.setId(rs.getInt("SELECTED_ANS_ID"));
			ans.setAnswer(rs.getString("answer"));
			ans.setRightanswer(rs.getBoolean("Q_STA"));
			ans.setSelectedanswer(false);
			int selected_id = ans.getId();
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("selected_id", selected_id);
			int selected = jdbcTemplate.queryForObject(
					"select count(*) from qbexam where selected_answer_id =:selected_id", param, Integer.class);
			int ques_id = rs.getInt("question_id");
			param.addValue("quesId", ques_id);
			int totalUser = jdbcTemplate.queryForObject("select count(*) from qbexam where question_id =:quesId", param,
					Integer.class);
			double percentage = (double) selected / totalUser * 100;
			int percent = (int) percentage;
			ans.setPercentage(percent);
			return ans;
		}

	}

	private final class QuestionMapper implements RowMapper<APIResponseGet> {

		@Override
		public APIResponseGet mapRow(ResultSet rs, int rowNum) throws SQLException {
			APIResponseGet get = new APIResponseGet<>();
			get.setResult(rs.getObject("question"));
			int questionId = rs.getInt("question_id");
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("quesId", questionId);
			int ques_id_check = jdbcTemplate.queryForObject("select count(*) from qbexam where question_id =:quesId",
					param, Integer.class);
			List<Answer> ansList = new ArrayList<>();
			if (ques_id_check > 0) {
				ansList = jdbcTemplate.query(
						"select *,ta.id as SELECTED_ANS_ID,ta.status as Q_STA from qbexam tq, answer ta where tq.question_id=ta.question_id and tq.user_id =4 and tq.question_id =:quesId;",
						param, new AnswerMapper());
				get.setAnswer(ansList);
			} else {
				ansList = jdbcTemplate.query(
						"select *, ta.id as SELECTED_ANS_ID,ta.status as Q_STA from question tq, answer ta where tq.ques_id=ta.question_id and tq.ques_id=:quesId;",
						param, new AnswerMapper());
				get.setAnswer(ansList);
			}
			get.setReference(rs.getString("reference"));
			get.setQuestionid(rs.getInt("question_id"));

			if (ques_id_check > 0) {
				get.setAnsweredstatus(true);
				get.setMarkstatus(rs.getBoolean("ans_status"));
			} else {
				get.setAnsweredstatus(false);
				get.setMarkstatus(false);
			}
			int count = jdbcTemplate.queryForObject("SELECT count(*) FROM qbflag where question_id =:quesId",
					param, Integer.class);
			if (count > 0) {
				boolean flag = jdbcTemplate.queryForObject("select flag_status from qbflag where question_id =:quesId",
						param, Boolean.class);
				get.setFlagstatus(flag);
			} else {
				get.setFlagstatus(false);
			}

			return get;
		}
	}

	private final class AnswerMapper implements RowMapper<Answer> {

		@Override
		public Answer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Answer ans = new Answer();
			ans.setId(rs.getInt("SELECTED_ANS_ID"));
			int selected_id = ans.getId();
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("selected_id", selected_id);
			int selected = jdbcTemplate.queryForObject(
					"select count(*) from qbexam where selected_answer_id =:selected_id", param, Integer.class);

			int ques_id = rs.getInt("question_id");
			param.addValue("quesId", ques_id);
			int totalUser = jdbcTemplate.queryForObject("select count(*) from qbexam where question_id =:quesId", param,
					Integer.class);
			ans.setAnswer(rs.getString("answer"));
			ans.setRightanswer(rs.getBoolean("Q_STA"));
			int quesCheck = jdbcTemplate.queryForObject("select count(*) from qbexam where question_id =:quesId", param,
					Integer.class);
			if (quesCheck > 0) {
				if ((selected_id == (rs.getInt("selected_answer_id")))) {
					ans.setSelectedanswer(true);

				} else {
					ans.setSelectedanswer(false);
				}
			} else {
				ans.setSelectedanswer(false);
			}
			double percentage = (double) selected / totalUser * 100;
			int percent = (int) percentage;
			ans.setPercentage(percent);
			return ans;
		}

	}

	@Override
	public APIResponseGet getExam(ExamPayload payload) {

		APIResponseGet response = new APIResponseGet<>();
		MapSqlParameterSource param = new MapSqlParameterSource();
		if ((payload.getReferid() == null) && (payload.getStartnumber() == null)) {
			int selected_ans_id = Integer.parseInt(payload.getSelectedanswer());
			int quesId = payload.getQuestionid();
			param.addValue("ques_id", quesId);
			int ans_id = jdbcTemplate.queryForObject(
					"select id as ANS_ID from answer where status=1 and question_id in (:ques_id);", param,
					Integer.class);
			int main_cat_id = jdbcTemplate.queryForObject(
					"select tm.id as MAIN_CAT_ID from main_category tm,category tc, sub_category ts,question tq where tm.id=tc.main_category_id and tc.id=ts.category_id and ts.id=tq.sub_category_id and tq.ques_id in(:ques_id);",
					param, Integer.class);
			int user_id = 4;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String localDate = LocalDate.now().format(formatter);
			boolean ans_status;
			if (ans_id == selected_ans_id) {
				ans_status = true;
			} else {
				ans_status = false;
			}
			param.addValue("ans_id", ans_id);
			param.addValue("selected_ans_id", selected_ans_id);
			param.addValue("main_cat_id", main_cat_id);
			param.addValue("user_id", user_id);
			param.addValue("localDate", localDate);
			param.addValue("ans_status", ans_status);
			jdbcTemplate.update(
					"insert into qbexam (question_id,answer_id,selected_answer_id,main_category_id,user_id,ans_status,exam_date) values(:ques_id,:ans_id,:selected_ans_id,:main_cat_id,:user_id,:ans_status,:localDate);",
					param);

			int count = jdbcTemplate.queryForObject(
					"select count(*) as COUNT from qbexam te left join question tq on te.question_id=tq.ques_id left join answer ta on te.selected_answer_id=ta.id  left join profile tp on te.user_id=tp.user_id where te.question_id in (:ques_id) order by tq.ques_id;",
					param, Integer.class);
			if (count > 0) {
				response = jdbcTemplate.queryForObject(
						"select * from qbexam te left join question tq on te.question_id=tq.ques_id left join answer ta on te.selected_answer_id=ta.id left join profile tp on te.user_id=tp.user_id where te.question_id in (:ques_id) and te.user_id=4 order by tq.ques_id;",
						param, new QuestionMapper());
			} else {
				response = jdbcTemplate.queryForObject(
						"select *,ques_id as question_id from question where ques_id=:ques_id;", param,
						new QuestionMapper());
			}
		} else if (payload.getStartnumber() != null) {
			int startNumber = Integer.parseInt(payload.getStartnumber());
			int referId = Integer.parseInt(payload.getReferid());
			int selected_ans_id = Integer.parseInt(payload.getSelectedanswer());
			int quesId = payload.getQuestionid();
			param.addValue("ques_id", quesId);
			int ans_id = jdbcTemplate.queryForObject(
					"select id as ANS_ID from answer where status=1 and question_id in (:ques_id);", param,
					Integer.class);
			int main_cat_id = jdbcTemplate.queryForObject(
					"select tm.id as MAIN_CAT_ID from main_category tm,category tc, sub_category ts,question tq where tm.id=tc.main_category_id and tc.id=ts.category_id and ts.id=tq.sub_category_id and tq.ques_id in(:ques_id);",
					param, Integer.class);
			int user_id = 4;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String localDate = LocalDateTime.now().format(formatter);
			boolean ans_status;
			if (ans_id == selected_ans_id) {
				ans_status = true;
			} else {
				ans_status = false;
			}
			param.addValue("ans_id", ans_id);
			param.addValue("selected_ans_id", selected_ans_id);
			param.addValue("main_cat_id", main_cat_id);
			param.addValue("user_id", user_id);
			param.addValue("localDate", localDate);
			param.addValue("ans_status", ans_status);
			param.addValue("referId", referId);
			param.addValue("startNumber", startNumber);
			int counCheck = jdbcTemplate.queryForObject("select count(*) from qbtt where qbttinfo_id=:referId and user_id=:user_id and question_id=:ques_id;", param, Integer.class);
			if(counCheck == 0) {
			jdbcTemplate.update(
					"insert into qbtt (question_id, answer_id, selected_answer_id, main_category_id, user_id, qbttinfo_id, status, exam_date, startnumber) values (:ques_id,:ans_id,:selected_ans_id,:main_cat_id,:user_id,:referId,:ans_status,:localDate,:startNumber);",
					param);
			}else {
				
			}
			response = jdbcTemplate.queryForObject(
					"select *,tqt.status as ANS_STATUS from qbtt  tqt,question tq where tqt.question_id=tq.ques_id and tqt.startnumber=:startNumber and tqt.qbttinfo_id =:referId;",
					param, new QbttQuesMapper());

		}
		return response;
	}

	@Override
	public List<Faq> getFaq() {
		List<Faq> faq = jdbcTemplate.query("select * from faq", new FaqMapper());
		return faq;
	}

	private static final class FaqMapper implements RowMapper<Faq> {

		@Override
		public Faq mapRow(ResultSet rs, int rowNum) throws SQLException {
			Faq response = new Faq();
			response.setId(rs.getInt("id"));
			response.setQuestion(rs.getString("question"));
			response.setAnswer(rs.getString("answer"));
			return response;
		}

	}

	@Override
	public boolean addFlag(AddFlag request) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		int id = request.getQuestionid();
		int userid = 4;
		boolean flag = request.isFlagstatus();
		param.addValue("id", id);
		param.addValue("userid", userid);
		param.addValue("flag", flag);
		int check = jdbcTemplate.queryForObject("select count(flag_status) from qbflag where question_id =:id and user_id=:userid;", param, Integer.class);
		if(check == 0) {
		jdbcTemplate.update("insert into qbflag (question_id,user_id,flag_status) values (:id,:userid,:flag);", param);
		}else {
			jdbcTemplate.update("update qbflag set flag_status=:flag where question_id =:id and user_id=:userid;", param);
		}
		boolean response = jdbcTemplate.queryForObject("select flag_status from qbflag where question_id=:id and user_id=:userid;", param,
				boolean.class);
		return response;
	}

	@Override
	public boolean updateScore(QbttUpdate request) {
		boolean response = false;
		int qbttinfo = request.getReferid();
		int start_num = request.getStartnumber();
		int marked = request.getMark();
		int user_id = 4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("qbttinfo", qbttinfo);
		param.addValue("user_id", user_id);
		param.addValue("start_num", start_num);
		param.addValue("marked", marked);
		jdbcTemplate.update(
				"update qbttscoredata set mark=:marked where qbttinfo_id=:qbttinfo and  user_id=:user_id and startnumber=:start_num;",
				param);
		int check = jdbcTemplate.queryForObject(
				"select count(mark) from qbttscoredata  where startnumber=:start_num and user_id =:user_id and qbttinfo_id in (:qbttinfo);",
				param, Integer.class);
		if (check > 0) {
			return response = true;
		}
		return response;
	}

	@Override
	public boolean updateMynote(MyNote request) {
		boolean response = false;
		String note = request.getMynote();
		int userId = 4;
		int question_id = request.getQuestionid();
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("question_id", question_id);
		param.addValue("note", note);
		param.addValue("userId", userId);
		jdbcTemplate.update("insert into qbmynote (question_id, user_id, message) values (:question_id,:userId,:note);",
				param);
		int check = jdbcTemplate.queryForObject(
				"select count(message) from qbmynote where question_id=:question_id and user_id=:userId;", param,
				Integer.class);
		if (check > 0) {
			return response = true;
		}

		return response;
	}

	@Override
	public Getthumb updateThumb(Thumb request) {
		boolean status = request.isStatus();
		int questionId = request.getQuestionid();
		int userId = 4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("status", status);
		param.addValue("userId", userId);
		param.addValue("questionId", questionId);
		int check = jdbcTemplate.queryForObject("select count(question_id) from qbthumb where question_id=:questionId and user_id=:userId;" , param, Integer.class);
		if(check > 0) {
			jdbcTemplate.update("update qbthumb set status=:status where question_id=:questionId and user_id=:userId;" , param);
		}else {
		jdbcTemplate.update("insert into qbthumb (question_id, user_id, status ) values (:questionId,:userId,:status);", param);
		}
		Getthumb response = new Getthumb<>();
		boolean thumb_status = jdbcTemplate.queryForObject("select (status) from qbthumb where question_id=:questionId and user_id=:userId;", param, Boolean.class);
		if(thumb_status == true) {
			response.setLikecount(1);
			response.setDislikecount(0);
		}else {
		response.setLikecount(0);
		response.setDislikecount(1);
		}
		return response;
	}

	@Override
	public boolean insertImprove(Improve request) {
		String getImprove = request.getMyimprove();
		int question_id = request.getQuestionid();
		int userId = 4;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("question_id", question_id);
		param.addValue("userId", userId);
		param.addValue("getImprove", getImprove);
		jdbcTemplate.update("insert into qbimprove (id, name) values (:question_id,:getImprove);", param);
		return false;
	}

}
