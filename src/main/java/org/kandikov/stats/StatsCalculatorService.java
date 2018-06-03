package org.kandikov.stats;

import org.springframework.stereotype.Service;

@Service
public class StatsCalculatorService {
	private TransactionStatistics stats = new TransactionStatistics();

	public TransactionStatistics getStatistics() {
		return stats;
	}

	public void update(Transaction transaction) {
		double amount = transaction.getAmount();
		stats.setSum(stats.getSum()+amount);

		if(stats.getCount()==0)
			stats.setAvg(amount);
		else
			stats.setAvg(stats.getSum()/(stats.getCount()+1));

		if (amount > stats.getMax())
			stats.setMax(amount);

		if (amount < stats.getMin() || stats.getCount() == 0)
			stats.setMin(amount);

		long count = stats.getCount();
		stats.setCount(++count);
	}
}
