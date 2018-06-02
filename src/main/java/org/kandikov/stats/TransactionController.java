package org.kandikov.stats;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class TransactionController {

	@RequestMapping(value = "/transactions", method = POST, consumes = "application/json", produces = "application/json")
	@ResponseStatus(CREATED)
	String transactions() {
		return null;
	}
}
