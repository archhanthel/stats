package org.kandikov.stats;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionStatisticsTest {

	@Test
	public void getAvg_whenCountNonZero_ReturnsAverage() {

		TransactionStatistics statistics = new TransactionStatistics();

		statistics.setCount(2);
		statistics.setSum(84.84);

		assertThat(statistics.getAvg()).isEqualTo(42.42);
	}

	@Test
	public void getAvg_whenCountIsZero_ReturnsZero() {
		TransactionStatistics statistics = new TransactionStatistics();

		assertThat(statistics.getAvg()).isEqualTo(0);
	}
}
