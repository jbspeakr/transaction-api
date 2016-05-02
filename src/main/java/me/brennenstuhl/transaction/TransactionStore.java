package me.brennenstuhl.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class TransactionStore {

  @Autowired
  TransactionRepository transactionRepository;

  public Optional<Transaction> load(Long transactionId) {
    final Transaction transaction = transactionRepository.findOne(transactionId);
    return ofNullable(transaction);
  }

  public void save(Transaction transaction){
    transactionRepository.save(transaction);
  }
}
