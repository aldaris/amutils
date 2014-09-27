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
package org.forgerock.amutils.web;

import org.forgerock.amutils.web.codec.DecodingPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.forgerock.amutils.web.config.ConfigPage;
import org.forgerock.amutils.web.wicket.ConfigAwareSession;

public class TemplatePage extends WebPage {

    public TemplatePage() {
        super();
    }

    public TemplatePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Navbar navbar = new Navbar("navbar");
        navbar.setPosition(Navbar.Position.TOP);
        navbar.fluid();
        navbar.setBrandName(localize("project.name"));

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton<>(DecodingPage.class, localize("navbar.decoding")),
                new NavbarButton<>(ConfigPage.class, localize("navbar.config"))));
        add(navbar);
    }

    @Override
    public ConfigAwareSession getSession() {
        return (ConfigAwareSession) super.getSession();
    }
    
    protected IModel<String> localize(String property) {
        return new StringResourceModel(property, this, null);
    }
}
