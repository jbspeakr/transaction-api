package me.brennenstuhl.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.time.LocalDateTime;

@KeySpace("transaction")
@Data
@RequiredArgsConstructor
public class Transaction {
  private @Id @JsonIgnore Long transactionId;
  private @JsonProperty("parent_id") Long parentId;
  private @LastModifiedDate @JsonIgnore LocalDateTime lastModifiedDate;
  private final Double amount;
  private final String type;

  Transaction() {
    this.amount = null;
    this.parentId = null;
    this.type = null;
  }
}
