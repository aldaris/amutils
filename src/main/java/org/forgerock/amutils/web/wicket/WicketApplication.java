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
package org.forgerock.amutils.web.wicket;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.forgerock.amutils.web.config.ConfigPage;
import org.forgerock.amutils.web.codec.DecodingPage;

public class WicketApplication extends WebApplication {

    @Override
    public Class<? extends WebPage> getHomePage() {
        return DecodingPage.class;
    }

    @Override
    public void init() {
        BootstrapSettings bootstrapSettings = new BootstrapSettings();
        bootstrapSettings.useCdnResources(true);
        Bootstrap.install(this, bootstrapSettings);
        getMarkupSettings().setStripWicketTags(true);

        mountPage("/config", ConfigPage.class);
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new ConfigAwareSession(request);
    }
}
