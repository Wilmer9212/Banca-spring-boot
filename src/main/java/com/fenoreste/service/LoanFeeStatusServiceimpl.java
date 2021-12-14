package com.fenoreste.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fenoreste.dao.LoanFeeStatusRepository;
import com.fenoreste.entity.Loan_Fee_Status;

@Service
public class LoanFeeStatusServiceimpl implements ILoanFeeStatusService {

	@Autowired
	LoanFeeStatusRepository loanFeeStatusDao;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public Loan_Fee_Status findLoanFeeStatusById(Integer id) {		
		return loanFeeStatusDao.findLoanFeeStatusById(id);
	}

}
