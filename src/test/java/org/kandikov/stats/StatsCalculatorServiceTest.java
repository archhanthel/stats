package org.kandikov.stats;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class StatsCalculatorServiceTest {

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

		service.update(new Transaction());

		assertThat(service.getStatistics().getCount()).isEqualTo(1L);
	}

	@Test
	public void getStatistics_whenMoreThanOneTransaction_returnsSummOfTransactions() {
		StatsCalculatorService service = new StatsCalculatorService();

		service.update(new Transaction());
		service.update(new Transaction());

		assertThat(service.getStatistics().getCount()).isEqualTo(2L);
	}

	@Test
	public void getStatistics_whenTransactionsExist_returnsHighestAmountAsMax() {
		StatsCalculatorService service = new StatsCalculatorService();

		Transaction lower = new Transaction();
		Transaction mid = new Transaction();
		Transaction higher = new Transaction();


		lower.setAmount(21.10);
		higher.setAmount(42.42);
		mid.setAmount(33.05);

		service.update(higher);
		service.update(lower);
		service.update(mid);

		assertThat(service.getStatistics().getMax()).isEqualTo(higher.getAmount());
	}

	@Test
	public void getStatistics_whenTransactionsExist_returnsLowestAmountAsMin() {
		StatsCalculatorService service = new StatsCalculatorService();

		Transaction lower = new Transaction();
		Transaction mid = new Transaction();
		Transaction higher = new Transaction();


		lower.setAmount(21.10);
		higher.setAmount(42.42);
		mid.setAmount(33.05);

		service.update(higher);
		service.update(lower);
		service.update(mid);

		assertThat(service.getStatistics().getMin()).isEqualTo(lower.getAmount());
	}
}
