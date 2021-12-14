package com.fenoreste.service;

import org.springframework.stereotype.Service;

import com.fenoreste.entity.Loan_Fee_Status;

@Service
public interface ILoanFeeStatusService {

	public Loan_Fee_Status findLoanFeeStatusById(Integer id);
}
