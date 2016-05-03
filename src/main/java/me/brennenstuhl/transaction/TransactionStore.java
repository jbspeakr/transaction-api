package me.brennenstuhl.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Optional.ofNullable;

@Service
public class TransactionStore {

  @Autowired
  TransactionRepository transactionRepository;

  public Optional<Transaction> load(final Long transactionId) {
    final Transaction transaction = transactionRepository.findOne(transactionId);
    return ofNullable(transaction);
  }

  public void save(final Transaction transaction) {
    transactionRepository.save(transaction);
  }

  public List<Long> loadByType(final String type) {
    final Iterable<Transaction> transactions = transactionRepository.findAll();
    return StreamSupport.stream(transactions.spliterator(), false)
        .filter(transaction -> transaction.getType().equals(type))
        .map(Transaction::getTransactionId)
        .collect(Collectors.toList());
  }
}
