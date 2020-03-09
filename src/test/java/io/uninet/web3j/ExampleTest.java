package io.uninet.web3j;

import io.uninet.web3j.constant.ActionType;
import io.uninet.web3j.constant.AssetId;
import io.uninet.web3j.constant.ChainHost;
import io.uninet.web3j.constant.ChainId;
import io.uninet.web3j.payload.PayloadProvider;
import io.uninet.web3j.response.TransactionReceipt;
import io.uninet.web3j.tx.Transaction;
import io.uninet.web3j.tx.TransactionManager;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

public class ExampleTest {

    private UnichainWeb3j unichainWeb3J = UnichainWeb3j.build(new HttpService(ChainHost.TEST));

    @Test
    public void testCreateAccount() throws IOException, TransactionException {
        Credentials accout4testweb3jCredentials = Credentials.create("786d968206fdfab98b26c9e2ebfa061c09abc28c7c39da023b0630");
        TransactionManager uniTransactionManager = new TransactionManager.Builder().unichainWeb3j(unichainWeb3J).credentials(accout4testweb3jCredentials).chainId(ChainId.TEST).build();
        //create uniTransaction
        Transaction uniTransaction = new Transaction();
        uniTransaction.setActionType(ActionType.CREATE_NEW_ACCOUNT);
        uniTransaction.setAccountName("unichain.founder");
        uniTransaction.setToAccountName("unichain.account");
        uniTransaction.setAssetId(AssetId.UNI);
        uniTransaction.setAmount(new BigInteger("1000000000000000000").multiply(new BigInteger("1510000")));

        String accountName = "minernodetest11";
        String publicKey = "0x04f24a1bf55d6635aba52292057d2ecbdae77b4e0628234cc7f9b6b2e7b53f9164d5fed23852c2c952ccc3e0ffe2ca109bd2ec738a5c315e7ccd32b931521199a2";
        //send transaction
        TransactionReceipt uniTransactionReceipt = uniTransactionManager.sendRawTransaction(uniTransaction,
                PayloadProvider.createAccountPayload(accountName, "unichain.founder", publicKey, "my wallet"));
        System.out.println(uniTransactionReceipt);
    }

    @Test
    public void testTransfer() throws IOException, TransactionException {
        Credentials accout4testweb3jCredentials = Credentials.create("786d968206fdfab98b26c9e2ebfa061c09abc28c7c39da023b0630");
        TransactionManager uniTransactionManager = new TransactionManager.Builder().unichainWeb3j(unichainWeb3J).credentials(accout4testweb3jCredentials).chainId(ChainId.TEST).build();
        //create uniTransaction
        Transaction uniTransaction = new Transaction();
        uniTransaction.setActionType(ActionType.TRANSFER);
        uniTransaction.setAccountName("accout4testweb3j");
        uniTransaction.setToAccountName("accout4testweb3j.test1");
        uniTransaction.setAssetId(AssetId.UNI);
        uniTransaction.setAmount(new BigInteger("10000000000000000"));//0.01uni
        //send transaction
        TransactionReceipt uniTransactionReceipt = uniTransactionManager.sendRawTransaction(uniTransaction, PayloadProvider.createTransferPayload());
        System.out.println(uniTransactionReceipt);
    }

}
