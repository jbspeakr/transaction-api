package me.brennenstuhl.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionStoreTest {
  public static final long NON_EXISTING_TRANSACTION_ID = 123L;
  private Transaction parentTransaction;
  private Transaction childTransaction;

  @Mock
  private TransactionRepository transactionRepository;
  @InjectMocks
  private TransactionStore transactionStore;

  @Before
  public void setUp() throws Exception {
    parentTransaction = new Transaction(1L, 1.0, "test");
    childTransaction = new Transaction(2L, 11.0, "car");
    childTransaction.setParentId(parentTransaction.getTransactionId());

    when(transactionRepository.findOne(parentTransaction.getTransactionId()))
        .thenReturn(parentTransaction);
    when(transactionRepository.findOne(childTransaction.getTransactionId()))
        .thenReturn(childTransaction);
    when(transactionRepository.findAll())
        .thenReturn(newArrayList(parentTransaction,childTransaction));
  }

  @Test
  public void shouldFindOnlyTransactionsWithCorrespondingType() throws Exception {
    final List<Long> transactionIds = transactionStore.loadByType(parentTransaction.getType());
    assertThat(transactionIds, hasItem(parentTransaction.getTransactionId()));
    assertThat(transactionIds, not(hasItem(childTransaction.getTransactionId())));
  }

  @Test
  public void shouldReturnZeroIfNoTransactionAvailable() throws Exception {
    final Double sum = transactionStore.sumLinkedTransactions(NON_EXISTING_TRANSACTION_ID);
    assertThat(sum, is(0.));
  }

  @Test
  public void shouldReturnInitialAmountIfNoParentAvailable() throws Exception {
    final Double sum = transactionStore.sumLinkedTransactions(parentTransaction.getTransactionId());
    assertThat(sum, is(parentTransaction.getAmount()));
  }

  @Test
  public void shouldSumUpAmountsOfChildAndLinkedParents() throws Exception {
    final Double sum = transactionStore.sumLinkedTransactions(childTransaction.getTransactionId());
    assertThat(sum, is(childTransaction.getAmount() + parentTransaction.getAmount()));
  }
}