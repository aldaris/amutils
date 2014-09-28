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
package org.forgerock.openam.amutils.config.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.wicket.util.collections.MultiMap;
import org.forgerock.amutils.sms.GlobalConfiguration;
import org.forgerock.amutils.sms.Service;
import org.forgerock.amutils.sms.SubConfiguration;

import static org.forgerock.openam.amutils.config.ConfigConstants.*;

public class PlatformServiceProcessor extends ServiceProcessor {

    private final Map<String, Server> servers = new HashMap<>(10);
    private final Map<String, Site> sites = new HashMap<>(5);

    @Override
    public void initialize(Service platformService) {
        GlobalConfiguration globalConfig = platformService.getConfiguration().getGlobalConfiguration().get(0);
        for (SubConfiguration subConfig : globalConfig.getSubConfiguration()) {
            if (SERVERS.equals(subConfig.getName())) {
                processServers(subConfig);
            } else {
                processSubConfig(subConfig);
            }
        }
    }

    private void processServers(SubConfiguration serversConfig) {
        for (SubConfiguration server : serversConfig.getSubConfiguration()) {
            String serverName = server.getName();
            MultiMap<String, String> properties = processAttributes(server.getAttributeValuePair());
            String serverId = properties.getFirstValue("serverid");
            servers.put(serverId, new Server(serverName, properties));
        }
    }

    private static void processSubConfig(SubConfiguration subConfig) {
        for (SubConfiguration subConfiguration : subConfig.getSubConfiguration()) {
        }
    }

    private static final class Server {

        private final String serverName;
        private final String serverId;
        private final String parentSiteName;
        private final List<String> settings;
        private final String serverConfigXml;

        public Server(String serverName, MultiMap<String, String> properties) {
            this.serverName = serverName;
            serverId = properties.getFirstValue("serverid");
            parentSiteName = properties.getFirstValue("parentsiteid");
            serverConfigXml = properties.getFirstValue("serverconfigxml");
            List<String> sortedSettings = new ArrayList<>(properties.get("serverconfig"));
            Collections.sort(sortedSettings);
            settings = sortedSettings;
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
