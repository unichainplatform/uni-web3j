package io.uninet.web3j.tx;

import io.uninet.web3j.UnichainWeb3j;
import io.uninet.web3j.response.GetTransactionReceiptResult;
import io.uninet.web3j.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

public abstract class TransactionReceiptProcessor {

    protected UnichainWeb3j unichainWeb3J;

    public abstract TransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException;

    Optional<TransactionReceipt> sendTransactionReceiptRequest(String transactionHash) throws IOException, TransactionException {
        GetTransactionReceiptResult getTransactionReceiptResult = unichainWeb3J.getTransactionReceipt(transactionHash).send();
        if (getTransactionReceiptResult.hasError()) {
            throw new TransactionException("Error processing request: " + getTransactionReceiptResult.getError().getMessage());
        } else {
            return getTransactionReceiptResult.getReceipt();
        }
    }
}
