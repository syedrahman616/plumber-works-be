package com.pgneet.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.pgneet.dao.InvoiceDao;
import com.pgneet.entity.GetAllInvoice;
import com.pgneet.entity.ResponseDate;

@Repository
public class InvoiceDaoImpl implements InvoiceDao {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<GetAllInvoice> listAllInvoice() {
		
		List<GetAllInvoice> response = jdbcTemplate.query("select * from invoice", new AllInvoiceMapper());
		return response;
	}
	
	private static final class AllInvoiceMapper implements RowMapper<GetAllInvoice>{

		@Override
		public GetAllInvoice mapRow(ResultSet rs, int rowNum) throws SQLException {
			GetAllInvoice response = new GetAllInvoice();
			response.setId(rs.getInt("id"));
			String dateString = rs.getString("invoicedate");
			 LocalDate localDate = LocalDate.parse(dateString);
			 ZonedDateTime zoneDateTime = ZonedDateTime.of(localDate.atStartOfDay(), ZoneId.systemDefault());
			DateTimeFormatter formate = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss.SSSSSS");
			String dateToString = zoneDateTime.format(formate);
			String timeZ_type = zoneDateTime.getOffset().toString();
			int timeZoneType = 3;
			String ZoneUtc = zoneDateTime.getZone().getId();
			ResponseDate invoiceDate = new ResponseDate(dateToString, timeZoneType, ZoneUtc);
			response.setInvoicedate(invoiceDate);
			response.setFullName(rs.getString("full_name"));
			response.setAddress1(rs.getString("address1"));
			response.setAddress2(rs.getString("address2"));
			response.setCity(rs.getString("city"));
			response.setPostalCode(rs.getString("postal_code"));
			response.setState(rs.getString("state"));
			response.setCountry(rs.getString("country"));
			response.setMobile(rs.getString("mobile"));
			response.setStatus(rs.getBoolean("status"));
			response.setRemarks(rs.getString("remarks"));
			response.setAmount(rs.getDouble("amount"));
			
			String paid = rs.getString("paiddate");
			 LocalDate localPaidDate = LocalDate.parse(paid);
			 ZonedDateTime paidDateTime = ZonedDateTime.of(localPaidDate.atStartOfDay(), ZoneId.systemDefault());
			DateTimeFormatter formater = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:ss.SSSSSS");
			String date_String = paidDateTime.format(formater);
			String timeZonetype = paidDateTime.getOffset().toString();
			//int timeZ_Type = Integer.parseInt(timeZonetype);
			int t = (paidDateTime.getOffset().getTotalSeconds())/3600;
			String Utc = paidDateTime.getZone().getId();
			ResponseDate paidDate = new ResponseDate(date_String, t, Utc);
			response.setPaiddate(paidDate);
			return response;
		}
		
	}

}
