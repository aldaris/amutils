/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 * 
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 * 
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 * 
 * Copyright 2014 ForgeRock AS.
 */
package org.forgerock.openam.amutils.config;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.forgerock.openam.amutils.decode.JCEEncryption;

public final class BootstrapData implements Serializable {

    private final String directoryServer;
    private final String deploymentUrl;
    private final String adminUser;
    private final String adminPassword;
    private final String baseDN;
    private final String directoryUser;
    private final String directoryPassword;

    private BootstrapData(String directoryServer, String deploymentUrl, String adminUser, String adminPassword,
            String baseDN, String directoryUser, String directoryPassword) {
        this.directoryServer = directoryServer;
        this.deploymentUrl = deploymentUrl;
        this.adminUser = adminUser;
        this.adminPassword = adminPassword;
        this.baseDN = baseDN;
        this.directoryUser = directoryUser;
        this.directoryPassword = directoryPassword;
    }

    public static BootstrapData valueOf(String bootstrapUrl) {
        try {
            int protocolIdx = bootstrapUrl.indexOf("://");
            int pathIdx = bootstrapUrl.indexOf("/", protocolIdx + "://".length());
            String ldapServer = bootstrapUrl.substring(0, pathIdx);
            String urlWithoutHost = bootstrapUrl.substring(pathIdx);

            URI uri = new URI(urlWithoutHost);
            Map<String, String> map = queryStringToMap(uri.getQuery());
            return new BootstrapData(ldapServer, uri.getPath().substring(1), map.get("user"),
                    JCEEncryption.DEFAULT.decrypt(map.get("pwd")), map.get("dsbasedn"), map.get("dsmgr"),
                    JCEEncryption.DEFAULT.decrypt(map.get("dspwd")));
        } catch (URISyntaxException | UnsupportedEncodingException | RuntimeException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static Map<String, String> queryStringToMap(String queryString) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        StringTokenizer st = new StringTokenizer(queryString, "&");

        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            int idx = s.indexOf('=');
            map.put(s.substring(0, idx), URLDecoder.decode(s.substring(idx + 1), "UTF-8"));
        }
        return map;
    }
}
