package org.kandikov.stats;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

	@RequestMapping(value = "/transactions"
			, method = RequestMethod.POST
			, consumes = "application/json"
			, produces = "application/json")
	String transactions(){
		return null;
	}
}
