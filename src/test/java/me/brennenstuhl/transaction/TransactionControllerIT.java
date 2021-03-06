package me.brennenstuhl.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TransactionApplication.class)
@WebAppConfiguration
public class TransactionControllerIT {
  private static Transaction defaultTransaction = new Transaction(1L, 1.0, "test");
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private TransactionStore transactionStore;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    givenStoredTransaction();
  }

  private void givenStoredTransaction() {
    transactionStore.save(defaultTransaction);
  }

  @Test
  public void getTransactionByIdEndpointShouldBeAccessible() throws Exception {
    mockMvc.perform(get("/transactionservice/transaction/" + defaultTransaction.getId()))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void putTransactionEndpointShouldBeAccessible() throws Exception {
    mockMvc.perform(put("/transactionservice/transaction/" + defaultTransaction.getId())
        .content("{ \"amount\": 5000, \"type\": \"cars\" }")
        .contentType(APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void getSumOfTransactionAmountsByIdEndpointShouldBeAccessible() throws Exception {
    mockMvc.perform(get("/transactionservice/sum/" + defaultTransaction.getId()))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void getTransactionIdsByTypeEndpointShouldBeAccessible() throws Exception {
    mockMvc.perform(get("/transactionservice/types/" + defaultTransaction.getType()))
        .andExpect(status().is2xxSuccessful());
  }
}