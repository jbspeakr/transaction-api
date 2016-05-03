package me.brennenstuhl.transaction;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@Configuration
@EnableMapRepositories("me.brennenstuhl.transaction")
public class TransactionPersistenceConfig {
}
