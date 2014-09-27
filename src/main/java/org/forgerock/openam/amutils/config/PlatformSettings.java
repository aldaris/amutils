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
 * Copyright 2013-2014 ForgeRock AS.
 */
package org.forgerock.openam.amutils.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.wicket.util.collections.MultiMap;
import org.forgerock.amutils.sms.GlobalConfiguration;
import org.forgerock.amutils.sms.Service;
import org.forgerock.amutils.sms.SubConfiguration;

import static org.forgerock.openam.amutils.config.ConfigConstants.SERVERS;

public class PlatformSettings {

    private final List<Server> servers = new ArrayList<>(10);
    private final Map<String, Site> sites = new HashMap<>(5);

    public static void init(Service platformService) {
        GlobalConfiguration globalConfig = platformService.getConfiguration().getGlobalConfiguration().get(0);
        for (SubConfiguration subConfig : globalConfig.getSubConfiguration()) {
            if (SERVERS.equals(subConfig.getName())) {
                processServer(subConfig);
            } else {
                processSubConfig(subConfig);
            }
        }
    }

    private static void processServer(SubConfiguration serversConfig) {
        for (SubConfiguration server : serversConfig.getSubConfiguration()) {

        }
    }

    private static void processSubConfig(SubConfiguration subConfig) {
        for (SubConfiguration subConfiguration : subConfig.getSubConfiguration()) {
        }
    }

    private static final class Server {

        private final String serverId;
        private final Site parentSite;
        private final MultiMap<String, String> settings;

        public Server(String serverId, Site parentSite, MultiMap<String, String> settings) {
            this.serverId = serverId;
            this.parentSite = parentSite;
            this.settings = settings;
        }
    }

    private static final class Site {

        private final String siteId;
        private final Set<SecondarySite> secondarySites;

        public Site(String siteId, Set<SecondarySite> secondarySites) {
            this.siteId = siteId;
            this.secondarySites = secondarySites;
        }
    }

    private static final class SecondarySite {

    }
}
