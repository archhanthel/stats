package org.kandikov.stats;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;


@Service
public class StatsCalculatorService {
	private static final Integer CAPACITY = 60;
	private ConcurrentHashMap<Integer, TransactionStatistics> frames = new ConcurrentHashMap<>(CAPACITY);

	StatsCalculatorService() {
		for (int i = 0; i < CAPACITY; i++) {
			frames.put(i, new TransactionStatistics());
		}
	}

	public TransactionStatistics getStatistics() {
		TransactionStatistics stats = new TransactionStatistics();

		for (int i = 0; i < CAPACITY; i++) {
			TransactionStatistics stat = frames.get(i);
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

	public void update(Transaction transaction) {
		int index = new DateTime(transaction.getTimestamp()).getSecondOfMinute();

		frames.computeIfPresent(index, (key, frame) -> {
			if (isValid(frame)) {
				return updateFrame(transaction, frame);
			} else {
				return updateFrame(transaction, new TransactionStatistics());
			}
		});

	}

	private boolean isValid(TransactionStatistics transactionStatistics) {
		return transactionStatistics.getLastUpdated() > new DateTime().minusSeconds(60).getMillis();
	}

	private TransactionStatistics updateFrame(Transaction transaction, TransactionStatistics stats) {
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
