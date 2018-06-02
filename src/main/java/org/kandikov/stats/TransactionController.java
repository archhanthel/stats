package org.kandikov.stats;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TransactionController {

	private final StatsCalculatorService calculator;

	@Autowired
	public TransactionController(StatsCalculatorService calculator) {
		this.calculator = calculator;
	}

	@RequestMapping(value = "/transactions", method = POST, consumes = "application/json", produces = "application/json")
	Object transactions(@RequestBody Transaction transaction) {
		DateTime dateTime = new DateTime(DateTimeZone.UTC);

		if (transaction.getTimestamp() < dateTime.minusMinutes(1).getMillis() || transaction.getTimestamp() > dateTime.getMillis())
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		calculator.update(transaction);

		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/statistics", method = GET, produces = "application/json")
	TransactionStatistics statistics() {
		return calculator.getStatistics();
	}
}
