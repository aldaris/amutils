/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2005 Sun Microsystems Inc. All Rights Reserved
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://opensso.dev.java.net/public/CDDLv1.0.html or
 * opensso/legal/CDDLv1.0.txt
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at opensso/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * $Id: SessionID.java,v 1.10 2009/10/02 23:45:42 qcheng Exp $
 *
 */

/**
 * Portions Copyrighted 2011-2013 ForgeRock AS
 */
package org.forgerock.openam.amutils.decode;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.forgerock.amutils.tools.Base64;

/**
 *
 * @author aldaris
 */
public class SessionUtils {

    private static final String S1 = "S1";
    private static final String SK = "SK";
    private static final String SI = "SI";
    private static final String SERVER_ID = "Server ID";
    private static final String SITE_ID = "Site ID";
    private static final String STORAGE_KEY = "Storage Key";

    public static Map<String, String> getExtensions(String sid) {
        Map<String, String> results = new LinkedHashMap<>();
        if (sid == null) {
            return Collections.EMPTY_MAP;
        }
        try {
            if (sid.indexOf("*") != -1) {
                sid = c66DecodeCookieString(sid);
            }
            int outerIndex = sid.lastIndexOf("@");
            if (outerIndex == -1) {
                return Collections.EMPTY_MAP;
            }

            String outer = sid.substring(outerIndex + 1);
            int tailIndex = outer.indexOf("#");

            if (tailIndex != -1) {
                String extensionPart = outer.substring(0, tailIndex);
                DataInputStream extensionStr = new DataInputStream(new ByteArrayInputStream(Base64.decode(extensionPart)));
                Map<String, String> extMap = new HashMap<>();

                while (true) {
                    String extName;
                    try {
                        extName = extensionStr.readUTF();
                    } catch (EOFException e) {
                        break;
                    }
                    String extValue = extensionStr.readUTF();
                    extMap.put(extName, extValue);
                }
                String primaryID = extMap.get(S1);
                String siteID = extMap.get(SI);
                String storageKey = extMap.get(SK);
                if (primaryID == null) {
                    results.put(SERVER_ID, siteID);
                } else {
                    results.put(SERVER_ID, primaryID);
                    results.put(SITE_ID, siteID);
                }
                if (storageKey != null) {
                    results.put(STORAGE_KEY, storageKey);
                }
            }
        } catch (IOException ioe) {
        }
        return results;
    }

    private static String c66DecodeCookieString(String urlEncodedString) {
        if (urlEncodedString == null || urlEncodedString.length() == 0) {
            return urlEncodedString;
        }
        int length = urlEncodedString.length();
        char[] chars = new char[length];
        boolean firstStar = true;
        for (int i = 0; i < length; i++) {
            char c = urlEncodedString.charAt(i);
            if (c == '-') {
                chars[i] = '+';
            } else if (c == '_') {
                chars[i] = '/';
            } else if (c == '.') {
                chars[i] = '=';
            } else if (c == '*') {
                if (firstStar) {
                    firstStar = false;
                    chars[i] = '@';
                } else {
                    chars[i] = '#';
                }
            } else {
                chars[i] = c;
            }
        }
        return new String(chars);
    }
}
