package com.pgneet.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pgneet.dao.InvoiceDao;
import com.pgneet.entity.GetAllInvoice;
import com.pgneet.entity.ResponseDate;
import com.pgneet.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	
	@Autowired
	private InvoiceDao repo;

	@Override
	public List<GetAllInvoice> showInvoice() {
		
		List<GetAllInvoice> response = repo.listAllInvoice();
		/*
		 * List<GetAllInvoice> response = new ArrayList<>(); GetAllInvoice invoice = new
		 * GetAllInvoice(); ResponseDate invoicedate = new ResponseDate();
		 * invoicedate.setDate("2023-01-05 00:00:00.000000");
		 * invoicedate.setTimezone_type(3); invoicedate.setTimezone("UTC");
		 * invoice.setInvoicedate(invoicedate); ResponseDate paiddate = new
		 * ResponseDate(); paiddate.setDate("2023-01-05 00:00:00.000000");
		 * paiddate.setTimezone_type(3); paiddate.setTimezone("UTC");
		 * invoice.setPaiddate(paiddate); response.add(invoice);
		 */
		return response;
	}

}
