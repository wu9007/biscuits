package org.hv.biscuits.core;

/**
 * @author leyan95
 */
public interface BiscuitsConfig {
    /**
     * Initializes the relationship between the Bundle Action and Permissions
     *
     * @throws Exception e
     */
    void init() throws Exception;

    /**
     * Initializes the DES key
     *
     * @param desKey des key
     * @return config
     */
    BiscuitsConfig setDesKey(String desKey);

    /**
     * Initializes the DES key
     *
     * @param sm4Key sm4 key
     * @return config
     */
    BiscuitsConfig setSm4Key(String sm4Key);
}
