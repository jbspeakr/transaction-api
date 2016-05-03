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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {
  public static final int NOT_EXISTING_TRANSACTION_ID = 123;
  static double test = 1.0;
  private static Transaction defaultTransaction = new Transaction(1L, test, "test");
  private MockMvcRequestSpecification apiRequest;

  @Mock
  private TransactionStore transactionStore;

  @Before
  public void setUp() throws Exception {
    apiRequest = given().standaloneSetup(new TransactionController(transactionStore)).config(newConfig().jsonConfig(jsonConfig().numberReturnType(DOUBLE)));
    givenRespondingTransactionStore();
  }

  @Test
  public void shouldReturnTransactionIfAvailable() throws Exception {
    apiRequest.when()
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

  private void givenRespondingTransactionStore() {
    when(transactionStore.load(defaultTransaction.getTransactionId()))
        .thenReturn(Optional.of(defaultTransaction));
  }
}