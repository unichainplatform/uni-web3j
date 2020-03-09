package io.uninet.web3j.tx;

import io.uninet.web3j.UnichainWeb3j;
import io.uninet.web3j.constant.ActionType;
import io.uninet.web3j.constant.ChainId;
import io.uninet.web3j.payload.PayloadProvider;
import io.uninet.web3j.request.CallTransaction;
import io.uninet.web3j.response.CallResult;
import io.uninet.web3j.response.SendTransactionResult;
import io.uninet.web3j.response.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class TransactionManager {

    private Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    private UnichainWeb3j unichainWeb3J;
    private TransactionReceiptProcessor transactionReceiptProcessor;
    private FeePayer feePayer;
    private Credentials credentials;
    private GasProvider uniGasProvider = new GasProvider();
    private int chainId;

    private TransactionManager(UnichainWeb3j unichainWeb3J, TransactionReceiptProcessor transactionReceiptProcessor, FeePayer feePayer, Credentials credentials, GasProvider uniGasProvider, int chainId) {
        this.unichainWeb3J = unichainWeb3J;
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.feePayer = feePayer;
        this.credentials = credentials;
        this.uniGasProvider = uniGasProvider;
        this.chainId = chainId;
    }

    /**
     * 发起合约交易
     *
     * @param transaction
     * @param function
     * @return
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt sendContractTransaction(Transaction transaction, Function function) throws IOException, TransactionException {
        transaction.setActionType(ActionType.CALL_CONTRACT);
        return sendRawTransaction(transaction, Numeric.hexStringToByteArray(FunctionEncoder.encode(function)));
    }

    /**
     * 发起普通交易
     *
     * @param transaction
     * @param payload       {@link PayloadProvider}
     * @return
     * @throws IOException
     * @throws TransactionException
     */
    public TransactionReceipt sendRawTransaction(Transaction transaction, byte[] payload) throws IOException, TransactionException {
        transaction.setNonce(getNonce(transaction.getAccountName()));
        transaction.setGasLimit(uniGasProvider.getGasLimit());
        transaction.setGasPrice(uniGasProvider.getGasPrice());
        transaction.setPayload(payload);
        String message = Numeric.toHexString(TransactionEncoder.signMessage(transaction, chainId, credentials, feePayer));
        SendTransactionResult sendTransactionResult = unichainWeb3J.sendRawTransaction(message).send();
        logger.debug("TransactionHash: {}", sendTransactionResult.getTransactionHash());
        return transactionReceiptProcessor.waitForTransactionReceipt(sendTransactionResult.getTransactionHash());
    }

    /**
     * @param callTransaction
     * @param function
     * @return
     * @throws IOException
     */
    public List<Type> call(CallTransaction callTransaction, Function function) throws IOException {
        callTransaction.setActionType(ActionType.CALL_CONTRACT);
        callTransaction.setAssetId(BigInteger.ZERO);
        callTransaction.setValue(BigInteger.ZERO);
        callTransaction.setData(FunctionEncoder.encode(function));
        CallResult callResult = unichainWeb3J.call(callTransaction).send();
        String result = callResult.getResult();
        return FunctionReturnDecoder.decode(result, function.getOutputParameters());
    }

    protected BigInteger getNonce(String accountName) throws IOException {
        return unichainWeb3J.getNonce(accountName).send().getNonce();
    }


    public static class Builder {
        private UnichainWeb3j unichainWeb3J;
        private TransactionReceiptProcessor transactionReceiptProcessor;
        private FeePayer feePayer;
        private Credentials credentials;
        private GasProvider uniGasProvider;
        private int chainId;

        public Builder unichainWeb3j(UnichainWeb3j unichainWeb3J) {
            this.unichainWeb3J = unichainWeb3J;
            return this;
        }

        public Builder transactionReceiptProcessor(TransactionReceiptProcessor transactionReceiptProcessor) {
            this.transactionReceiptProcessor = transactionReceiptProcessor;
            return this;
        }

        public Builder feePayer(FeePayer feePayer) {
            this.feePayer = feePayer;
            return this;
        }

        public Builder credentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public Builder gasProvider(GasProvider uniGasProvider) {
            this.uniGasProvider = uniGasProvider;
            return this;
        }

        public Builder chainId(int chainId) {
            this.chainId = chainId;
            return this;
        }

        public TransactionManager build() {
            if (transactionReceiptProcessor == null) {
                transactionReceiptProcessor = new PollTransactionReceiptProcessor(unichainWeb3J);
            }
            if (uniGasProvider == null) {
                uniGasProvider = new GasProvider();
            }
            if (chainId == 0) {
                chainId = ChainId.TEST;
            }
            return new TransactionManager(unichainWeb3J, transactionReceiptProcessor, feePayer, credentials, uniGasProvider, chainId);
        }

    }

}
