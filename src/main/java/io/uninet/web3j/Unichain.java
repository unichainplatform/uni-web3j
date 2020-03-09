package io.uninet.web3j;

import io.uninet.web3j.request.CallTransaction;
import io.uninet.web3j.response.*;
import io.uninet.web3j.response.*;
import org.web3j.protocol.core.Request;

public interface Unichain {

    Request<?, CallResult> call(CallTransaction transaction);

    Request<?, NonceResult> getNonce(String accountName);

    Request<?, SendTransactionResult> sendRawTransaction(String transactionData);

    Request<?, GetTransactionReceiptResult> getTransactionReceipt(String transactionHash);

    Request<?, SendTransactionResult> createPendingTransactionFilter();

    Request<?, PendingTransactionResult> getFilterChanges(String filterId);

}
