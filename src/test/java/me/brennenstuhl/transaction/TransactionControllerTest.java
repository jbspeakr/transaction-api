package me.brennenstuhl.transaction;

import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.jayway.restassured.config.JsonConfig.jsonConfig;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static com.jayway.restassured.module.mockmvc.config.RestAssuredMockMvcConfig.newConfig;
import static com.jayway.restassured.path.json.config.JsonPathConfig.NumberReturnType.DOUBLE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {
  public static final double DEFAULT_SUM = 22.0;
  private static final Long NOT_EXISTING_TRANSACTION_ID = 123L;
  private static final String TEST_TYPE = "testType";
  private static Transaction defaultTransaction = new Transaction(1L, 1.0, "test");
  private MockMvcRequestSpecification apiRequest;

  @Mock
  private TransactionStore transactionStore;

  @Before
  public void setUp() throws Exception {
    apiRequest = given().standaloneSetup(new TransactionController(transactionStore));
    givenRespondingTransactionStore();
  }

  private void givenRespondingTransactionStore() {
    when(transactionStore.load(defaultTransaction.getTransactionId()))
        .thenReturn(Optional.of(defaultTransaction));
    when(transactionStore.load(NOT_EXISTING_TRANSACTION_ID))
        .thenReturn(Optional.empty());
    when(transactionStore.loadByType(defaultTransaction.getType()))
        .thenReturn(singletonList(defaultTransaction.getTransactionId()));
    when(transactionStore.loadByType(TEST_TYPE))
        .thenReturn(emptyList());
    when(transactionStore.sumLinkedTransactions(defaultTransaction.getTransactionId()))
        .thenReturn(DEFAULT_SUM);
  }

  @Test
  public void shouldReturnTransactionIfAvailable() throws Exception {
    apiRequest.config(newConfig().jsonConfig(jsonConfig().numberReturnType(DOUBLE))).when()
        .get("/transactionservice/transaction/" + defaultTransaction.getTransactionId())
        .then()
        .statusCode(OK.value())
        .body("type", equalTo(defaultTransaction.getType()))
        .body("parent_id", equalTo(defaultTransaction.getParentId()))
        .body("amount", equalTo(defaultTransaction.getAmount()));
  }

  @Test
  public void shouldReturnStatusNotFoundIfTransactionNotAvailable() throws Exception {
    apiRequest.when()
        .get("/transactionservice/transaction/" + NOT_EXISTING_TRANSACTION_ID)
        .then()
        .statusCode(NOT_FOUND.value());
  }

  @Test
  public void shouldReturnStatusOkIfPutWasSuccessful() throws Exception {
    apiRequest
        .body("{ \"amount\": 5000, \"type\": \"cars\" }")
        .contentType(JSON)
        .when()
        .put("/transactionservice/transaction/10")
        .then()
        .statusCode(OK.value())
        .body("status", equalTo("ok"));
  }

  @Test
  public void shouldReturnIdsOfTransactionsWithCorrespondingIds() throws Exception {
    apiRequest.when()
        .get("/transactionservice/types/" + defaultTransaction.getType())
        .then()
        .statusCode(OK.value())
    .body("", contains(1));
  }

  @Test
  public void shouldReturnEmptyListIfNoTransactionsForType() throws Exception {
    apiRequest.when()
        .get("/transactionservice/types/" + TEST_TYPE)
        .then()
        .statusCode(OK.value())
        .body("", emptyIterable());
  }

  @Test
  public void shouldReturn() throws Exception {
    apiRequest.config(newConfig().jsonConfig(jsonConfig().numberReturnType(DOUBLE))).when()
        .get("/transactionservice/sum/" + defaultTransaction.getTransactionId())
        .then()
        .statusCode(OK.value())
        .body("sum", equalTo(DEFAULT_SUM));
  }
}