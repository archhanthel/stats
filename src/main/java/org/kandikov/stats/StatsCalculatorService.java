package org.kandikov.stats;

import org.springframework.stereotype.Service;

@Service
public class StatsCalculatorService {
	private TransactionStatistics stats = new TransactionStatistics();

	public TransactionStatistics getStatistics() {
		return stats;
	}

	public void update(Transaction transaction) {
		stats.setSum(transaction.getAmount());
		stats.setAvg(transaction.getAmount());
		stats.setMax(transaction.getAmount());
		stats.setMin(transaction.getAmount());
		stats.setCount(1);
	}
}
