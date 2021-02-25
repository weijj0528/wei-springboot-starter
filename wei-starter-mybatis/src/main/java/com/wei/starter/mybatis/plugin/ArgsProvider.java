package com.wei.starter.mybatis.plugin;

/**
 * The interface Args provider.
 */
public interface ArgsProvider {

    /**
     * Insert args object.
     *
     * @return the object
     */
    default Object insertArgs() {
        return null;
    }

    /**
     * Update args object.
     *
     * @return the object
     */
    default Object updateArgs() {
        return null;
    }

    /**
     * Select args object.
     *
     * @return the object
     */
    default Object selectArgs() {
        return null;
    }

}
