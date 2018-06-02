package org.kandikov.stats;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.joda.time.DateTimeZone.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void postTransactions_whenValidTransaction_returns201WithEmptyBody() throws Exception {

		DateTime dateTime = new DateTime(UTC);

		String transaction = "{\"amount\": 12.3, \"timestamp\": " + dateTime.getMillis()+"}";
		this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
				.content(transaction)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(""))
				.andExpect(status().is(201));
	}

	@Test
	public void postTransaction_whenTransactionIsOlderThan60seconds_returns204WithEmptyBody() throws Exception {
		DateTime dateTime = new DateTime(UTC);

		String transaction = "{\"amount\": 12.3, \"timestamp\": " + dateTime.minusMinutes(1).getMillis() + "}";

		this.mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
				.content(transaction)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(""))
				.andExpect(status().is(204));
	}
}
