package me.brennenstuhl.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/transactionservice")
public class TransactionController {
  private  static final String STATUS_OK_RESPONSE_BODY = "\"status\": \"ok\"";

  @Autowired
  private TransactionStore transactionStore;

  @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Transaction> getTransaction(@PathVariable final Long transactionId) {
    final Optional<Transaction> transaction = transactionStore.load(transactionId);
    if (transaction.isPresent()) {
      return new ResponseEntity<>(transaction.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> putTransaction(@PathVariable final Long transactionId, @RequestBody Transaction transaction) {
    transaction.setTransactionId(transactionId);
    transactionStore.save(transaction);
    return new ResponseEntity<>(STATUS_OK_RESPONSE_BODY, HttpStatus.OK);
  }

}
