package org.kandikov.stats;

import org.joda.time.DateTime;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.time.DateTimeZone.UTC;

public class StatsCalculatorServiceTest {

	private final DateTime dateTime = new DateTime(UTC);

	@Test
	public void getStatistics_whenNoTransactionsExist_returnsDefaultValues() {
		StatsCalculatorService service = new StatsCalculatorService();

		TransactionStatistics actual = service.getStatistics();

		assertThat(actual.getAvg()).isEqualTo(0L);
		assertThat(actual.getCount()).isEqualTo(0);
		assertThat(actual.getMax()).isEqualTo(0);
		assertThat(actual.getMin()).isEqualTo(0);
		assertThat(actual.getSum()).isEqualTo(0);
	}

	@Test
	public void getStatistics_whenTransactionsExist_returnsNumberOfTransActions() {
		StatsCalculatorService service = new StatsCalculatorService();
		Transaction transaction = new Transaction();
		transaction.setTimestamp(dateTime.getMillis());
		transaction.setAmount(42.00);

		service.update(transaction);

		assertThat(service.getStatistics().getCount()).isEqualTo(1L);
	}

	@Test
	public void getStatistics_whenMoreThanOneTransaction_returnsSumOfTransactions() {
		StatsCalculatorService service = new StatsCalculatorService();
		Transaction first = new Transaction();
		first.setAmount(22.20);
		first.setTimestamp(dateTime.getMillis());
		Transaction second = new Transaction();
		second.setAmount(20.22);
		second.setTimestamp(dateTime.minusSeconds(1).getMillis());

		service.update(first);
		service.update(second);

		assertThat(service.getStatistics().getCount()).isEqualTo(2L);
	}

	@Test
	public void getStatistics_whenTransactionsExist_returnsHighestAmountAsMax() {
		StatsCalculatorService service = new StatsCalculatorService();

		Transaction lower = new Transaction();
		lower.setAmount(21.10);
		lower.setTimestamp(dateTime.minusSeconds(1).getMillis());
		Transaction mid = new Transaction();
		mid.setTimestamp(dateTime.minusSeconds(2).getMillis());
		mid.setAmount(33.05);
		Transaction higher = new Transaction();
		higher.setTimestamp(dateTime.getMillis());
		higher.setAmount(42.42);


		service.update(higher);
		service.update(lower);
		service.update(mid);

		assertThat(service.getStatistics().getMax()).isEqualTo(higher.getAmount());
	}

	@Test
	public void getStatistics_whenTransactionsExist_returnsLowestAmountAsMin() {
		StatsCalculatorService service = new StatsCalculatorService();

		Transaction lower = new Transaction();
		lower.setAmount(21.10);
		lower.setTimestamp(dateTime.getMillis());
		Transaction mid = new Transaction();
		mid.setAmount(33.05);
		mid.setTimestamp(dateTime.minusSeconds(1).getMillis());
		Transaction higher = new Transaction();
		higher.setAmount(42.42);
		higher.setTimestamp(dateTime.minusSeconds(2).getMillis());


		service.update(higher);
		service.update(lower);
		service.update(mid);

		assertThat(service.getStatistics().getMin()).isEqualTo(lower.getAmount());
	}

	@Test
	public void getStatistics_whenTransactionsExist_returnsSumOfAllAmounts() {
		StatsCalculatorService service = new StatsCalculatorService();

		Transaction first = new Transaction();
		first.setAmount(22.20);
		first.setTimestamp(dateTime.getMillis());
		Transaction second = new Transaction();
		second.setAmount(20.22);
		second.setTimestamp(dateTime.minusSeconds(1).getMillis());

		service.update(first);
		service.update(second);

		assertThat(service.getStatistics().getSum()).isEqualTo(42.42);
	}

	@Test
	public void getStatistics_whenTransactionsExist_returnsAverageAmountOfAllTransactions() {
		StatsCalculatorService service = new StatsCalculatorService();


		Transaction first = new Transaction();
		first.setAmount(43.43);
		first.setTimestamp(dateTime.getMillis());

		Transaction second = new Transaction();
		second.setAmount(41.41);
		second.setTimestamp(dateTime.minusSeconds(1).getMillis());

		service.update(first);
		service.update(second);

		assertThat(service.getStatistics().getAvg()).isEqualTo(42.42);
	}

	@Test
	public void getStatistics_whenTransactionsExistInLast60Seconds_returnsCorrectStats() throws InterruptedException {
		StatsCalculatorService service = new StatsCalculatorService();

		Transaction minuteOld = new Transaction();
		Transaction current = new Transaction();

		minuteOld.setAmount(41.00);
		minuteOld.setTimestamp(dateTime.minusSeconds(59).getMillis());
		current.setAmount(42.00);
		current.setTimestamp(dateTime.getMillis());

		service.update(minuteOld);
		service.update(current);

		sleep(1000);

		assertThat(service.getStatistics().getCount()).isEqualTo(1);
		assertThat(service.getStatistics().getSum()).isEqualTo(42);
	}

	@Test
	public void getStatistics_whenTimeStampsAreIdentical_returnsCorrectStats() {
		StatsCalculatorService service = new StatsCalculatorService();
		long sameTime = dateTime.getMillis();

		Transaction first = new Transaction();
		first.setAmount(43.43);
		first.setTimestamp(sameTime);

		Transaction second = new Transaction();
		second.setAmount(41.41);
		second.setTimestamp(sameTime);

		service.update(first);
		service.update(second);

		assertThat(service.getStatistics().getAvg()).isEqualTo(42.42);

	}


}
