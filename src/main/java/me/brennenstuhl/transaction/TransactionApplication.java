package me.brennenstuhl.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class TransactionApplication {

	@Autowired
	TransactionRepository transactionRepository;

	public static void main(String[] args) {
		SpringApplication.run(TransactionApplication.class, args);
	}

	@PostConstruct
	public void init() {
		final Transaction transaction1 = new Transaction(1L, 5000.0, "cars");
		final Transaction transaction2 = new Transaction(2L, 10000.0, "shopping");
		final Transaction transaction3 = new Transaction(3L, 7500.0, "cars");
		transaction3.setParentId(transaction2.getId());

		transactionRepository.save(transaction2);
		transactionRepository.save(transaction1);
		transactionRepository.save(transaction3);
	}
}
