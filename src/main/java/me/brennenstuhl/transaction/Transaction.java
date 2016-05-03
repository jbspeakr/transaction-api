package me.brennenstuhl.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

@KeySpace("transaction")
@Data
@RequiredArgsConstructor
public class Transaction {
  private @NonNull @Id @JsonIgnore Long transactionId;
  private @NonNull @JsonProperty("amount") Double amount;
  private @NonNull @JsonProperty("type") String type;
  private @JsonProperty("parent_id") Long parentId;

  Transaction() {
    this.amount = null;
    this.parentId = null;
    this.type = null;
  }
}
