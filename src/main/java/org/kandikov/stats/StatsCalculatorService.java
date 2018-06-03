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

		if (transaction.getAmount() > stats.getMax())
			stats.setMax(transaction.getAmount());

		if (transaction.getAmount() < stats.getMin() || stats.getCount() == 0)
			stats.setMin(transaction.getAmount());

		long count = stats.getCount();

		stats.setCount(++count);
	}
}
