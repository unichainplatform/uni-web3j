package io.uninet.web3j.tx;

import io.uninet.web3j.UnichainWeb3j;
import io.uninet.web3j.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

public class PollTransactionReceiptProcessor extends TransactionReceiptProcessor {

    private final long sleepDuration;
    private final int attempts;

    public PollTransactionReceiptProcessor(UnichainWeb3j unichainWeb3J) {
        this(unichainWeb3J, 10000, 60);
    }

    public PollTransactionReceiptProcessor(UnichainWeb3j unichainWeb3J, long sleepDuration, int attempts) {
        super.unichainWeb3J = unichainWeb3J;
        this.sleepDuration = sleepDuration;
        this.attempts = attempts;
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash) throws IOException, TransactionException {
        return getTransactionReceipt(transactionHash, sleepDuration, attempts);
    }

    private TransactionReceipt getTransactionReceipt(
            String transactionHash, long sleepDuration, int attempts)
            throws IOException, TransactionException {

        Optional<TransactionReceipt> transactionDetails = sendTransactionReceiptRequest(transactionHash);
        for (int i = 0; i < attempts; i++) {
            if (!transactionDetails.isPresent()) {
                try {
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    throw new TransactionException(e);
                }
                transactionDetails = sendTransactionReceiptRequest(transactionHash);
            } else {
                return transactionDetails.get();
            }
        }
        throw new TransactionException("Transaction receipt was not generated after "
                + ((sleepDuration * attempts) / 1000
                + " seconds for transaction: " + transactionHash));
    }
}
