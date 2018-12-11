package com.hqjy.pay;

import java.util.List;
import java.util.Map;

import com.hqjy.pay.BorrowMoneyEntity;

public interface BorrowMoneyService {
	BorrowMoneyEntity queryObject(Map<String, Object> map);
	void save(BorrowMoneyEntity borrowMoney);

	void update(BorrowMoneyEntity borrowMoney);
}
