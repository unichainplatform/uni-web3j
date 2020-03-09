package io.uninet.web3j;

import io.uninet.web3j.request.CallTransaction;
import io.uninet.web3j.response.*;
import io.uninet.web3j.response.*;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;

import java.util.Arrays;
import java.util.Collections;

public class JsonRpc2_0UnichainWeb3j implements UnichainWeb3j {

    protected final Web3jService web3jService;

    public JsonRpc2_0UnichainWeb3j(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    @Override
    public Request<?, CallResult> call(CallTransaction transaction) {
        return new Request<>(
                "uni_call",
                Arrays.asList(transaction, "latest"),
                web3jService,
                CallResult.class);
    }

    @Override
    public Request<?, NonceResult> getNonce(String accountName) {
        return new Request<>(
                "account_getNonce",
                Arrays.asList(accountName),
                web3jService,
                NonceResult.class);
    }

    @Override
    public Request<?, SendTransactionResult> sendRawTransaction(String transactionData) {
        return new Request<>(
                "uni_sendRawTransaction",
                Arrays.asList(transactionData),
                web3jService,
                SendTransactionResult.class);
    }

    @Override
    public Request<?, GetTransactionReceiptResult> getTransactionReceipt(String transactionHash) {
        return new Request<>(
                "uni_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3jService,
                GetTransactionReceiptResult.class);
    }

    @Override
    public Request<?, SendTransactionResult> createPendingTransactionFilter() {
        return new Request<>(
                "uni_newPendingTransactionFilter",
                Collections.emptyList(),
                web3jService,
                SendTransactionResult.class);
    }

    @Override
    public Request<?, PendingTransactionResult> getFilterChanges(String filterId) {
        return new Request<>(
                "uni_getFilterChanges",
                Arrays.asList(filterId),
                web3jService,
                PendingTransactionResult.class);
    }

}
