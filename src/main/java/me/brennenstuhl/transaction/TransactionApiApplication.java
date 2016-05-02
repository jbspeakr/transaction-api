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
public class TransactionApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionApiApplication.class, args);
	}

	@Autowired
	TransactionRepository transactionRepository;

	@PostConstruct
	public void init() {
		transactionRepository.save(new Transaction(5000.0, "cars"));
		transactionRepository.save(new Transaction(10000.0, "shopping"));
		transactionRepository.save(new Transaction(7500.0, "cars"));

		System.out.println(transactionRepository.findAll());
	}
}
