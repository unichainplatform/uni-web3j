package io.uninet.web3j;

import org.web3j.protocol.Web3jService;

public interface UnichainWeb3j extends Unichain {

    static UnichainWeb3j build(Web3jService web3jService) {
        return new JsonRpc2_0UnichainWeb3j(web3jService);
    }

}
