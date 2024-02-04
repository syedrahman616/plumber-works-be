package com.plumber.constants;

public interface DatabaseConstants {

	public String USR_COUNT_SELECT_QRY = "select count(*) from user where email=:USR_EMAIL or usr_name=:USR_NAME ";

	public String USR_INSERT_QRY = "insert into user(password ,usr_name ,email,roles,status,is_verified,social_login ) values(:PASSWORD ,:USR_EMAIL ,:USR_EMAIL,JSON_OBJECT('ROLE','STUDENT'),true,false ,:SOCIAL_LOGIN)";

	public String USR_SOCIAL_INSERT_QRY = "insert into user(email,usr_name,roles,social_login,status,is_verified ) values(:USR_EMAIL,:USR_NAME,JSON_OBJECT('ROLE','STUDENT'),:SOCIAL_LOGIN,true,true)";

	public String PASSWORD_UPDATE_QRY = "UPDATE user set password = :PASSWORD WHERE email = :USR_EMAIL";

	public String UPDATE_MOBIL_VERIFIED_QRY = "UPDATE user SET mobile_verified=:MOBIL_VERIFIED WHERE linkme_id=:ID";

	public String UPDATE_EMAIL_VERIFIED_QRY = "UPDATE user SET is_verified=:verified WHERE id=:ID";

	public String OTP_INSERT_QRY = "INSERT INTO t_linkme_otp(request_id, otp, created_time, expiry_time) values(:request_id, :OTP, :CREATE_TIME, :EXPIRY_TIME)";

	public String OTP_GET_QRY = "select otp from t_linkme_otp WHERE request_id = :request_id";

	public String UPDATE_PASSWORD_QRY = "UPDATE user SET password=:CHANGE_PASSWORD where id=:user_Id";

	public String INSERT_PAYMENT_QRY = "insert into payment (subscription_id, user_id, invoice_id, payment_date, expiry, amount, status) values (:SUBSCRIPTION_ID,:USR_ID,0,curdate(),curdate(),:AMOUNT,false)";

	public String GET_PROFILE_QRY = "select * from profile where user_id=:USR_ID;";

	public String UPDATE_PAYMENT_ID_QRY = "update invoice set remarks=:payReference where id=:invoiveId";

	public String GET_PAYMENT_ID_QRY = "select remarks from invoice where id=:id and user_id=:userId";

	public String UPDATE_INVOICE_STATUS = "update invoice set status=true,paiddate=curdate() where user_id=:userId and id=:id";

	public String UPDATE_PAYMENT_TABLE_QRY = "update  payment set subscription_id=:mockId,invoice_id=:invoiceId ,status=true,payment_id=:reference,signature=:signature, payment_date=curdate(),expiry=:expriry where id=:id and user_id=:userId";

	public String GET_VALITIDY_DAYD = "select validity from subscription where id=:mockId";

	public String INSERT_PLAN_VALIDITY_QRY = "insert into planvalidity (subscription_id, exam_id, user_id, created_at, expiry) values (:mockId,:exam_id,:userId,:createdDate,:expriry)";

	public String UPDATE_PLAN_VALIDITY = "insert into planvalidity (subscription_id, exam_id, user_id, created_at, expiry) values (:mockId,:exam_id,:userId,:createdDate,:expriry)";

	public String INSERT_INVOICE_QRY = "insert into invoice (user_id, invoicedate, full_name, address1, address2, city, postal_code, state, country, mobile, status, remarks, amount, paiddate) "
			+ "values (:userId,curdate(),:fullName,:add1,:add2,:city,:post,:state,:country,:mobile,true,:reference,:amount,curdate())";

	public String PLAN_VALIDITY_UPDATE = "update planvalidity set expiry=:expriry,modify_at=:createdDate where id=:planvalidityId and subscription_id=:mockId and user_id=:userId";

	public String GET_PROFILE_UPDATE = "select * from profile where user_id=:userId";

	public String GET_STATE_NAME = "select state from state where state_code=:stateId";

	public String ADMIN_GET_EXAM = "select * from exam order by id asc";

	public String ADMIN_GET_STATE = "select * from state order by id asc";

	public String ADMIN_GET_MAINCATEGORY = "select * from main_category where delete_status is null order by id asc";

	public String ADMIN_GET_FAQ = "select * from faq order by id asc";
	
	public String ADMIN_GET_QUESTION_COUNT_CHECK = "select count(*) from question where id=:question_id";
	
	public String ADMIN_QBANSWER_COUNT_CHECK = "select count(*) from answer where question_id=:question_id and delete_status is null";
	
	public String ADMIN_TEXTBOOK_REF_COUNT_CHECK = "select count(*) from textbook where id=:ID";
	
	public String ADMIN_MOCK_QUES_COUNT_CHECK = "select count(*) from mock_question where id=:QID and delete_status is null";
	
	public String ADMIN_MOCK_TEXTBOOK_REF_COUNT_CHECK = "select count(*) from mock_textbook where id=:ID and delete_status is null";
	
	public String ADMIN_MOCK_TEXTBOOK = "select * from mock_textbook where id=:ID and delete_status is null";
	
	public String SELECT_ALL_HIGHYEILD_CATEGORY = "select * from highyield_category";
	
	public String GET_ALL_HIGHYIELD_TOPICS = "select * from highyield_topic where delete_status is null";
	
	public String GET_INVOICE_BY_USER = "select * from invoice where user_id=:user_Id order by id desc";
	
	public String GET_MOCK_ANS = "select id from mock_question where status=:status;";
	
	public String GET_SUBSCRIPTION_ID = "select id from subscription where exam_id=2 and status=:status;";
	
	public String MOCK_COMPLETE_COUNT_CHECK = "select count(id) from meinfo where id=:referId and user_id=:userId;";
	
	public String MEINFO_GET_STATUS = "select  status as STATUS from meinfo where id=:referId and user_id=:userId";
	
	public String ADD_FLAG_GET_STATUS = "select status from meflag where mock_question_id=:id and user_id=:userid;";
	
	public String MEFLAG_UPDATE = "update meflag set status=:flag where mock_question_id =:id and user_id=:userid;";
	
	public String INSERT_MEFLAG = "insert into meflag (mock_question_id,user_id,status) values (:id,:userid,:flag);";
	
	public String GET_MOCK_THUMB = "select count(status=1) as COUNT from methumb where mock_question_id=:ques_id and user_id=:userId";
	
	public String MEFLAG_COUNT_CHECK = "select count(status) from meflag where mock_question_id =:id and user_id=:userid;";
	
	public String MOCK_QUESTION_ID_SELECTEDBYID = "select (mock_question_id) from mettscoredata where user_id=:userId and meinfo_id =:referId;";
	
	public String MOCK_QUESTION_SELECTED_BYID = "select tq.startnumber as START_NUM,tq.mark as MARK,tf.status as FLAG from mettscoredata tq left join meflag tf on tq.mock_question_id=tf.mock_question_id where tq.user_id=:userId and tq.meinfo_id=:referId;";
	
	public String MEINFO_COUNT_CHECK = "select count(id) from meinfo where id=:qbttinfo_id and user_id=:userId;";
	
	public String MEINFO_TIMEBAL_GET = "select timebalance from meinfo where user_id=:userId and id=:referId;";
	
	public String METT_STARTNUMBER_GET = "select startnumber from mett where user_id=:userId and meinfo_id=:referId and mock_question_id=:quesId;";
	
	public String MESCOREDATA_UPDATE = "update mettscoredata set mark=:marked where meinfo_id=:qbttinfo and  user_id=:user_id and startnumber=:start_num;";
	
	public String MEINFO_UPDATE = "update meinfo set timebalance=:time_balance,status=:status where id=:qbttinfo_id and user_id=:userId;";
	
	public String MOCK_INFO_UPDATE = "update meinfo set timebalance=:time_balance,status=:status where id=:referId and user_id=:userId;";
	
	public String GET_SOCRE_DATA_MOCK = "select tq.startnumber as START_NUM,tq.mark as MARK,tf.status as FLAG from mettscoredata tq left join meflag tf on tq.user_id=tf.user_id and tq.mock_question_id=tf.mock_question_id where tq.meinfo_id in (:refer_id);";
	
	public String INSERT_METTSCOREDATA = "insert into mettscoredata (meinfo_id, mock_question_id, user_id, startnumber, mark) values (:refer_id,:ques_Number,:user_id,:indexStart,:mark);";
	
	public String GET_QUESTION_ID_METTSCOREDATA = "select mock_question_id as id from mettscoredata where meinfo_id=:MockSubId and user_id=:user_id;";
	
	public String INSERT_MEINFO = "insert into meinfo (user_id, subscription_id, noq, lot, timebalance, date, status) values (:user_id,:subcription,:noq,:lot,:time,:date,:status)";
	
	public String GET_MOCK_INFO = "select * from meinfo where user_id=:userId and subscription_id=:subscriptionId order by id desc;";
	
	public String GET_INVOICE_USER_DETAILS = "select ti.id,ti.full_name,ti.address1,ti.address2,ti.city,ti.postal_code,ti.invoicedate,ti.mobile, te.exam,ts.amount,ts.name "
			+ "from invoice ti left join payment tp on tp.invoice_id=ti.id left join subscription ts on ts.id=tp.subscription_id left join exam te on te.id=ts.exam_id "
			+ "where if('admin'=:userId ,ti.user_id,ti.user_id=:userId) and ti.id=:invoiceId  and ts.status=1;";
	
	public String ADMIN_GET_TOPICS = "select tt.id, tt.status, tt.topic ,tc.category "
			+ "from highyield_topic tt, highyield_category tc where tt.highyield_category_id=tc.id and IF(:id IS NOT NULL, tc.id = :id, 1) and tt.delete_status is null and tc.delete_status is null order by tt.id asc";
	
	public String ADMIN_GET_INVOICE = "select tv.id,tu.email ,tv.mobile ,tv.status,tv.amount ,tv.remarks ,tv.invoicedate from invoice tv ,user tu where tv.user_id=tu.id order by tv.id asc";
	
	public String ADMIN_MOCK_ANS_COUNT_CHECK = "select count(*) from mock_answer where mock_question_id=:QID and delete_status is null";
	
	public String ADMIN_MOCK_ANS = "select * from mock_answer where mock_question_id=:QID and delete_status is null order by id asc";
	
	public String ADMIN_GET_TEXTBOOK_MOCK = "select mt.id, mt.status ,tm.main_category from mock_textbook mt left join main_category tm on mt.main_category_id=tm.id where mt.delete_status is null and tm.delete_status is null order by mt.id asc";
	
	public String ADMIN_MOCK_QUESTION_GET = "select tm.main_category,tmq.id,tmq.question,tmq.status"
			+ " from mock_question tmq left join main_category tm on tmq.main_category_id=tm.id where tm.delete_status is null and tmq.delete_status is null order by tmq.id asc";
	
	public String ADMIN_MOCK_QUES = "select * from mock_question tq,main_category tm where tq.main_category_id=tm.id and tq.id=:QID and tm.delete_status is null and tq.delete_status is null";

	public String ADMIN_TOPIC_CATEGORY = "select * ,(select count(highyield_category_id) from highyield_topic tt where tt.highyield_category_id=tc.id and tt.delete_status is null) as total_topic from highyield_category tc where tc.delete_status is null order by id asc";

	public String ADMIN_GET_CATEGORY = "select * from main_category tm,category tc where tm.id=tc.main_category_id and tm.delete_status is null and tc.delete_status is null and if(:id is null ,true ,tc.main_category_id=:id) order by tc.id asc";

	public String ADMIN_GET_SUBCATEGORY = "select ts.id,ts.category_id, ts.sub_category ,ts.status,tc.category from sub_category ts, category tc where ts.category_id=tc.id and ts.delete_status is null and tc.delete_status is null and if(:id is null ,true ,ts.category_id=:id) order by ts.id asc";

	public String ADMIN_GET_SUBSCRIPTION = "select ts.id,ts.name,ts.amount, ts.validity ,te.exam ,te.id ,ts.status ,ts.offer_name ,ts.offer_percentage,ts.offer_amount ,ts.offer_status from subscription ts, exam te where ts.exam_id=te.id and ts.delete_status is null order by ts.id asc";

	public String ADMIN_GET_MEMBERS = "select tu.id,tu.email,tp.mobile,tp.city, tp.rdate ,tp.full_name from user tu , profile tp where tu.id=tp.user_id and roles not like '%SUPER_ADMIN%'";

	public String ADMIN_SUB_DEL_CHECK = "select count(*) from exam te,subscription ts ,payment tp where te.id=ts.exam_id and ts.id=tp.subscription_id and expiry >=curdate() and te.id=:EXAM_ID";

	public String ADMIN_SUBSCRIPTION_COUNT_CHECK = "select count(*) from subscription ts, payment tp where ts.id=tp.subscription_id and expiry >=curdate() and ts.id=:SUB_ID";

	public String ADMIN_GET_QUESTIONS = "select tm.main_category,tc.category,ts.sub_category,tq.id,tq.question,if(tq.reference is not null or LENGTH(tq.reference) != 0 ,true,false) as reference ,if(tq.myreference is not null or LENGTH(tq.myreference) != 0,true ,false) as myreference ,tq.status"
			+ " from question tq left join sub_category ts on tq.sub_category_id=ts.id "
			+ " left join category tc on ts.category_id=tc.id left join main_category tm on tc.main_category_id=tm.id "
			+ " where tq.delete_status is null and tc.delete_status is null and tm.delete_status is null and ts.delete_status is null order by tq.id asc";

	public String ADMIN_GET_QUESTION_REF = "select tm.main_category ,tm.id,tc.category ,tc.id,ts.id,ts.sub_category,tq.id,tq.question,tq.reference ,tq.myreference ,tq.status "
			+ " from question tq left join sub_category ts on tq.sub_category_id=ts.id "
			+ " left join category tc on ts.category_id=tc.id left join main_category tm on tc.main_category_id=tm.id where tq.id=:question_id"
			+ " and tq.delete_status is null and tc.delete_status is null and tm.delete_status is null and ts.delete_status is null order by tq.id asc";

	public String ADMIN_GET_QBANSWER = "select * from answer where question_id=:question_id and delete_status is null order by id asc";

	public String ADMIN_GET_TEXTBOOK = "select tm.main_category,tc.category,ts.sub_category,tb.id,if(tb.content is null or LENGTH(tb.content) =0 ,false,true) as explaination ,if(myreference is null or LENGTH(myreference) =0,false ,true) as myreference, tb.status "
			+ " from textbook tb left join sub_category ts on tb.sub_category_id=ts.id"
			+ " left join category tc on ts.category_id=tc.id left join main_category tm on tc.main_category_id=tm.id "
			+ " where tb.delete_status is null and tc.delete_status is null and tm.delete_status is null and ts.delete_status is null order by tb.id asc";

	public String ADMIN_GET_TEXTBOOK_REF = "select tm.main_category,tc.category,ts.sub_category,tb.id ,ts.id,tb.content ,tb.myreference, tb.status "
			+ " from textbook tb left join sub_category ts on tb.sub_category_id=ts.id "
			+ " left join category tc on ts.category_id=tc.id left join main_category tm on tc.main_category_id=tm.id where tb.id=:ID "
			+ " and tb.delete_status is null and tc.delete_status is null and tm.delete_status is null and ts.delete_status is null order by tb.id asc";

	public String ADMIN_DASHBOARD = "SELECT (SELECT COUNT(DISTINCT user_id) AS total_subscriber from payment) AS total_subscriber, "
			+ "(select sum(amount) from payment) as total_income, "
			+ "(SELECT SUM(amount) FROM payment WHERE payment_date = CURDATE()) AS today_income, "
			+ "(SELECT COUNT(DISTINCT user_id) FROM payment tp , user tu WHERE tp.user_id = tu.id AND payment_date = CURDATE()) AS today_subscriber ";
}
