/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2005 Sun Microsystems Inc. All Rights Reserved
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://opensso.dev.java.net/public/CDDLv1.0.html or
 * opensso/legal/CDDLv1.0.txt See the License for the specific language
 * governing permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at opensso/legal/CDDLv1.0.txt. If applicable,
 * add the following below the CDDL Header, with the fields enclosed by brackets
 * [] replaced by your own identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * $Id: JCEEncryption.java,v 1.3 2008/10/20 17:24:43 beomsuk Exp $
 *
 */
/*
 * Portions Copyrighted 2010-2013 ForgeRock AS
 */
package org.forgerock.openam.amutils.decode;

import java.nio.charset.Charset;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.forgerock.amutils.tools.Base64;

public class JCEEncryption {

    private static final byte VERSION = 1;
    private static final String CRYPTO_DESCRIPTOR = "PBEWithMD5AndDES";
    private static final String CRYPTO_DESCRIPTOR_PROVIDER = "SunJCE";
    private static final String KEYGEN_ALGORITHM = "PBEWithMD5AndDES";
    private static final String KEYGEN_ALGORITHM_PROVIDER = "SunJCE";
    private static final int DEFAULT_KEYGEN_ALG_INDEX = 2;
    private static final int DEFAULT_ENC_ALG_INDEX = 2;
    private static final int ITERATION_COUNT = 5;
    private static final byte[] salt = {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
    private static final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
    private SecretKey pbeKey;
    private boolean initialized = false;

    public JCEEncryption(String password) {
        try {
            pbeKey = SecretKeyFactory.getInstance(KEYGEN_ALGORITHM, KEYGEN_ALGORITHM_PROVIDER)
                    .generateSecret(new PBEKeySpec(password.toCharArray()));
            initialized = true;
        } catch (Exception ex) {
        }
    }

    public String encrypt(String clearText) {
        return Base64.encodeToString(pbeEncrypt(clearText.getBytes(Charset.forName("UTF-8"))), false);
    }

    public String decrypt(String encText) {
        return new String(pbeDecrypt(Base64.decode(encText)), Charset.forName("UTF-8"));
    }

    private byte[] pbeEncrypt(byte[] clearText) {
        byte[] result = null;
        if (clearText == null || clearText.length == 0) {
            return null;
        }

        if (initialized) {
            try {
                byte type[] = new byte[2];
                type[1] = (byte) DEFAULT_ENC_ALG_INDEX;
                type[0] = (byte) DEFAULT_KEYGEN_ALG_INDEX;

                Cipher pbeCipher = null;
                pbeCipher = Cipher.getInstance(CRYPTO_DESCRIPTOR, CRYPTO_DESCRIPTOR_PROVIDER);

                if (pbeCipher != null) {
                    pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParameterSpec);

                    result = pbeCipher.doFinal(clearText);
                    byte[] iv = pbeCipher.getIV();

                    result = addPrefix(type, iv, result);
                } else {
                }
            } catch (Exception ex) {
            }
        } else {
        }

        return result;
    }

    private static byte[] addPrefix(byte type[], byte iv[], byte share[]) {
        byte data[] = new byte[share.length + 11];

        data[0] = VERSION;
        data[1] = type[0];
        data[2] = type[1];
        System.arraycopy(iv, 0, data, 3, 8);
        System.arraycopy(share, 0, data, 11, share.length);

        return data;
    }

    private byte[] pbeDecrypt(byte[] cipherText) {
        byte[] result = null;
        if (initialized) {
            try {
                byte share[] = cipherText;

                if (share[0] != VERSION) {
                    return null;
                }

                byte raw[] = getRaw(share);

                Cipher pbeCipher;
                pbeCipher = Cipher.getInstance(CRYPTO_DESCRIPTOR, CRYPTO_DESCRIPTOR_PROVIDER);

                if (pbeCipher != null) {
                    pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParameterSpec);

                    result = pbeCipher.doFinal(raw);
                } else {
                }
            } catch (Exception ex) {
            }
        } else {
        }

        return result;
    }

    private static byte[] getRaw(byte share[]) {
        byte data[] = new byte[share.length - 11];

        for (int i = 11; i < share.length; i++) {
            data[i - 11] = share[i];
        }

        return data;
    }
}
