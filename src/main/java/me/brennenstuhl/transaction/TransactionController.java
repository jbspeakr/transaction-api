package me.brennenstuhl.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/transactionservice")
public class TransactionController {
  private static final String STATUS_OK_RESPONSE_BODY = "{\"status\": \"ok\"}";

  private TransactionStore transactionStore;

  @Autowired
  public TransactionController(final TransactionStore transactionStore) {
    this.transactionStore = transactionStore;
  }

  @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Transaction> getTransaction(@PathVariable final Long transactionId) {
    final Optional<Transaction> transaction = transactionStore.load(transactionId);
    if (transaction.isPresent()) {
      return new ResponseEntity<>(transaction.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/transaction/{transactionId}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> putTransaction(@PathVariable final Long transactionId, @RequestBody Transaction transaction) {
    transaction.setId(transactionId);
    transactionStore.save(transaction);
    return new ResponseEntity<>(STATUS_OK_RESPONSE_BODY, HttpStatus.OK);
  }

  @RequestMapping(value = "/types/{type}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> putTransaction(@PathVariable final String type) {
    final List<Long> transactionIDs = transactionStore.loadByType(type);
    return new ResponseEntity<>(transactionIDs, HttpStatus.OK);
  }

  @RequestMapping(value = "/sum/{transactionId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> putTransaction(@PathVariable Long transactionId) {
    final Double sum = transactionStore.sumLinkedTransactions(transactionId);
    return new ResponseEntity<>("{\"sum\":" + sum + "}", HttpStatus.OK);
  }
}
