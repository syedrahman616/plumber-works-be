package com.pgneet.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pgneet.dao.QuizTimedTestDao;
import com.pgneet.entity.InfoData;
import com.pgneet.entity.Qbtt;
import com.pgneet.entity.QbttData;
import com.pgneet.entity.QuestionId;
import com.pgneet.entity.ResponseDate;
import com.pgneet.entity.Save;
import com.pgneet.entity.Scoredata;
import com.pgneet.entity.Selectedquestions;

@Repository
public class QuizTimedTesDaoImpl implements QuizTimedTestDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<InfoData> showInfoData() {
		List<InfoData> response = jdbcTemplate.query("select * from qbttinfo", new InfoDataMapper());
		return response;
	}

	private static final class InfoDataMapper implements RowMapper<InfoData> {

		@Override
		public InfoData mapRow(ResultSet rs, int rowNum) throws SQLException {
			InfoData info = new InfoData();
			info.setId(rs.getInt("id"));
			info.setNoq(rs.getInt("noq"));
			info.setLot(rs.getInt("lot"));
			info.setTimebalance(rs.getInt("timebalance"));
			String dateString = rs.getString("date");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
			ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
			String zoneId = zonedDateTime.getZone().toString();
			ZoneOffset zoneOffset = zonedDateTime.getOffset();
			int timeZoneType = (zoneOffset.getTotalSeconds())/3600;
			ResponseDate date = new ResponseDate(dateString, timeZoneType, zoneId);
			info.setDate(date);
			info.setStatus(rs.getInt("status"));
			return info;
		}

	}

	@Override
	public Selectedquestions showSelectedquestions(Qbtt request) {
		Selectedquestions response = new Selectedquestions<>();
		KeyHolder key = new GeneratedKeyHolder();
		List<QuestionId> answerId = jdbcTemplate.query("select tq.ques_id as QUES_ID from main_category tm , category tc ,sub_category ts ,question tq where tm.id=tc.main_category_id and tc.id=ts.category_id and ts.id=tq.sub_category_id order by tq.ques_id asc;", new QuestionIdMapper());
		List<QuestionId> shuffledId = new ArrayList<>(answerId);
		Collections.shuffle(shuffledId);
		int noq = request.getNoq();
		int lot = request.getLot();
		int time = lot*60;
		int status = 1;
		int user_id = 4;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String date = LocalDateTime.now().format(formatter);
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("user_id", user_id);
		param.addValue("noq", noq);
		param.addValue("lot", lot);
		param.addValue("time", time);
		param.addValue("status", status);
		param.addValue("date", date);
		jdbcTemplate.update("insert into qbttinfo (user_id,noq,lot,timebalance,date,status) values (:user_id,:noq,:lot,:time,:date,:status)", param, key);
		int refer_id = key.getKey().intValue();
		response.setReferid(refer_id);
		List<QuestionId> idList = shuffledId.subList(0, noq);
		response.setResult(idList);
		List<QbttData> toDb = new ArrayList<>();
		
		int count = 0;
		int mark = 5;
		
		for(int startNum = 0;startNum<idList.size();startNum++) {
			int indexStart = startNum + 1;
			QuestionId questionId = idList.get(startNum);
			int ques_Number = questionId.getQuestionid();
			QbttData data=new QbttData();
			data.setMark(mark);
			data.setQbttInfo(refer_id);
			data.setQuestion_id(ques_Number);
			data.setStartnumber(indexStart);
			data.setUser_id(user_id);
			toDb.add(data);
			
		}

		MapSqlParameterSource[] param1 = new MapSqlParameterSource[toDb.size()];
		for(QbttData d:toDb) {
			param1[count] = new MapSqlParameterSource();
			param1[count].addValue("refer_id", d.getQbttInfo());
			param1[count].addValue("user_id", d.getUser_id());
			param1[count].addValue("indexStart", d.getStartnumber());
			param1[count].addValue("mark", d.getMark());
			param1[count].addValue("ques_Number", d.getQuestion_id());
			count++;
			
		}
		param.addValue("refer_id", refer_id);
		jdbcTemplate.batchUpdate("insert into qbttscoredata (qbttinfo_id,user_id,startnumber,mark,question_id) values (:refer_id,:user_id,:indexStart,:mark,:ques_Number);",param1);
		List<Scoredata> score = jdbcTemplate.query(
				"select tq.startnumber as START_NUM,tq.mark as MARK,tf.flag_status as FLAG from qbttscoredata tq left join qbflag tf on tq.user_id=tf.user_id and tq.question_id=tf.question_id where tq.qbttinfo_id in (:refer_id);",param,
				new ScoreDataMapper());
		response.setScoredata(score);
		
		return response;
	}
	private static final class QuestionIdMapper implements RowMapper<QuestionId>{

		@Override
		public QuestionId mapRow(ResultSet rs, int rowNum) throws SQLException {
			QuestionId id = new QuestionId();
			id.setQuestionid(rs.getInt("QUES_ID"));
			return id;
		}
		
	}

	private static final class ScoreDataMapper implements RowMapper<Scoredata> {

		@Override
		public Scoredata mapRow(ResultSet rs, int rowNum) throws SQLException {
			Scoredata score = new Scoredata();
			score.setStartnumber(rs.getInt("START_NUM"));
			score.setMark(rs.getInt("MARK"));
			score.setFlag(rs.getBoolean("FLAG"));
			return score;
		}
	}

	@Override
	public boolean getTestCompleted(int referid) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		int id = referid;
		int status = 2;
		int userId =4;
		int time_balance = 0;
		param.addValue("referId", id);
		param.addValue("userId", userId);
		param.addValue("time_balance", time_balance);
		param.addValue("status", status);
		int check = jdbcTemplate.queryForObject("select count(id) from qbttinfo where id=:referId;", param, Integer.class);
		if(check>0) {
			jdbcTemplate.update("update qbttinfo set timebalance=:time_balance,status=:status where id=:referId and user_id=:userId;" , param);
		}
		int statusCheck = jdbcTemplate.queryForObject("select  status as STATUS from qbttinfo where id=:referId",param,Integer.class);
		boolean response = false;
		if(statusCheck == 2) {
			return response=true;
		}
		return response;
	}

	@Override
	public boolean saveAndExit(Save request) {
		int qbttinfo_id = request.getReferid();
		int time_balance = request.getTimebalance();
		int userId = 4;
		int status = 1;
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("qbttinfo_id", qbttinfo_id);
		param.addValue("userId", userId);
		param.addValue("status", status);
		param.addValue("time_balance", time_balance);
		boolean response = false;
		int check = jdbcTemplate.queryForObject("select count(id) from qbttinfo where id=:qbttinfo_id and user_id=:userId;", param, Integer.class);
		if(check>0) {
			jdbcTemplate.update("update qbttinfo set timebalance=:time_balance,status=:status where id=:qbttinfo_id and user_id=:userId;", param);
			response = true;
		}
		
		return response;
	}

	
}
