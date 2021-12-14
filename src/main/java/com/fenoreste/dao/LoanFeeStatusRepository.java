package com.fenoreste.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fenoreste.entity.Loan_Fee_Status;

public interface LoanFeeStatusRepository extends CrudRepository<Loan_Fee_Status, Long> {

	@Query(value="SELECT * FROM loan_fee_statusb WHERE id=?1",nativeQuery = true)
	Loan_Fee_Status findLoanFeeStatusById(Integer id);
}
