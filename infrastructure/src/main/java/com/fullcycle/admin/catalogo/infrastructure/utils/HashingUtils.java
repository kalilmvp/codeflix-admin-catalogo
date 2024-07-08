package com.fullcycle.admin.catalogo.infrastructure.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * @author kalil.peixoto
 * @date 7/8/24 22:18
 * @email kalilmvp@gmail.com
 */
public final class HashingUtils {
    private static final HashFunction CHECKSUM = Hashing.crc32c();

    private HashingUtils(){}

    public static String checksum(final byte[] checksum) {
        return CHECKSUM.hashBytes(checksum).toString();
    }
}
