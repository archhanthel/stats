package org.kandikov.stats;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StatsCalculatorService {
	private TransactionStatistics stats = new TransactionStatistics();
	private ArrayList<Transaction> transactions = new ArrayList<>();

	public TransactionStatistics getStatistics() {
		stats = new TransactionStatistics();
		DateTime dateTime = new DateTime();

		for (Transaction transaction : transactions) {
			if (transaction.getTimestamp() > dateTime.minusSeconds(60).getMillis())
				calculateStats(transaction);
		}
		return stats;
	}

	public void update(Transaction transaction) {
		transactions.add(transaction);
	}

	private void calculateStats(Transaction transaction) {
		double amount = transaction.getAmount();
		stats.setSum(stats.getSum() + amount);

		if (amount > stats.getMax())
			stats.setMax(amount);

		if (amount < stats.getMin() || stats.getCount() == 0)
			stats.setMin(amount);

		long count = stats.getCount();
		stats.setCount(++count);
	}
}
