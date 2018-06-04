package org.kandikov.stats;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class StatsCalculatorService {
	private static final Integer CAPACITY = 60;
	private Map<Integer, TransactionStatistics> frames = new ConcurrentHashMap<>(CAPACITY);

	StatsCalculatorService() {
		for (int i = 0; i < CAPACITY; i++) {
			frames.put(i, new TransactionStatistics());
		}
	}

	public TransactionStatistics getStatistics() {
		TransactionStatistics stats = new TransactionStatistics();

		for (int i = 0; i < CAPACITY; i++) {
			TransactionStatistics frame = frames.get(i);

			if (isValid(frame)) {
				if (stats.getMin() > frame.getMin() || stats.getMin() == 0)
					stats.setMin(frame.getMin());
				if (stats.getMax() < frame.getMax())
					stats.setMax(frame.getMax());
				stats.setCount(stats.getCount() + frame.getCount());
				stats.setSum(stats.getSum() + frame.getSum());
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

		if (amount > stats.getMax())
			stats.setMax(amount);

		if (amount < stats.getMin() || stats.getCount() == 0)
			stats.setMin(amount);

		stats.setSum(stats.getSum() + amount);
		stats.setCount(stats.getCount() + 1);
		stats.setLastUpdated(transaction.getTimestamp());

		return stats;
	}
}
