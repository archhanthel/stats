package org.kandikov.stats;

import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class StatsCalculatorService {
	private Transaction[] transactions = new Transaction[60];
	private TransactionStatistics[] lastMinuteStats = new TransactionStatistics[60];
	private final Integer capacity = 60;
	private ConcurrentHashMap<Integer, TransactionStatistics> concurrentStats = new ConcurrentHashMap<>(capacity);

	StatsCalculatorService() {
		for (int i = 0; i < capacity; i++) {
			concurrentStats.put(i, new TransactionStatistics());
		}
	}

	public TransactionStatistics getStatistics() {
		TransactionStatistics stats = new TransactionStatistics();

		for (int i = 0; i < capacity; i++) {
			TransactionStatistics stat = concurrentStats.get(i);

			long minuteAgo = new DateTime().minusSeconds(60).getMillis();

			long lastUpdated = stat.getLastUpdated();
			if (lastUpdated > minuteAgo) {
				stats.setSum(stats.getSum() + stat.getSum());
				if (stats.getMin() > stat.getMin() || stats.getMin() == 0)
					stats.setMin(stat.getMin());
				if (stats.getMax() < stat.getMax())
					stats.setMax(stat.getMax());
				stats.setCount(stats.getCount() + stat.getCount());
			}
		}

		return stats;
	}

	@Async
	public void update(Transaction transaction) {
		int secondOfMinute = new DateTime(transaction.getTimestamp()).getSecondOfMinute();
		System.out.println(transaction.getTimestamp());
		System.out.println(new DateTime().minusSeconds(60).getMillis());
		if (concurrentStats.get(secondOfMinute).getLastUpdated() > new DateTime().minusSeconds(60).getMillis())
			concurrentStats.put(secondOfMinute, calculateStats(transaction, concurrentStats.get(secondOfMinute)));
		else
			concurrentStats.put(secondOfMinute, calculateStats(transaction, new TransactionStatistics()));

	}

	private TransactionStatistics calculateStats(Transaction transaction, TransactionStatistics stats) {
		double amount = transaction.getAmount();
		stats.setSum(stats.getSum() + amount);

		if (amount > stats.getMax())
			stats.setMax(amount);

		if (amount < stats.getMin() || stats.getCount() == 0)
			stats.setMin(amount);

		long count = stats.getCount();
		stats.setCount(++count);
		stats.setLastUpdated(transaction.getTimestamp());
		return stats;
	}
}
