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
package org.forgerock.amutils.web.codec;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.forgerock.amutils.web.TemplatePage;
import org.forgerock.openam.amutils.config.BootstrapData;
import org.forgerock.openam.amutils.decode.JCEEncryption;
import org.forgerock.openam.amutils.decode.SessionUtils;

public class DecodingPage extends TemplatePage {

    private String sessionId;
    private List<String> extensions;
    private String encKey;
    private String encryptedText;
    private String bootstrapContent = "";
    private String cryptoResult = "";

    public DecodingPage(final PageParameters parameters) {
        super(parameters);
        addSessionComponents();
        addBootstrapComponents();
        addCryptoComponents();
    }

    private void addSessionComponents() {
        final WebMarkupContainer wmc = new WebMarkupContainer("wmc");
        wmc.setOutputMarkupId(true);
        wmc.add(new ListView<String>("extensions", new PropertyModel<>(this, "extensions")) {

            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(new Label("extension", item.getModelObject()));
            }
        });
        add(wmc);

        Form<Void> form = new BootstrapForm<Void>("session").type(FormType.Inline);
        form.add(new TextField<>("sessionId", new PropertyModel<String>(this, "sessionId")));
        form.add(new AjaxButton("sessionButton") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                extensions = new ArrayList<>(SessionUtils.getReadableExtensions(sessionId));
                if (target != null) {
                    target.add(wmc);
                }
            }
        });
        add(form);
    }

    private void addBootstrapComponents() {
        Form<Void> form = new BootstrapForm<Void>("bootstrap").type(FormType.Inline);
        form.add(new TextArea<>("bootstrapContent", new PropertyModel<>(this, "bootstrapContent")));
        final WebMarkupContainer wmc = new WebMarkupContainer("bootstrapContainer");
        LoadableDetachableModel<List<BootstrapData>> listViewModel = new LoadableDetachableModel<List<BootstrapData>>() {

            @Override
            protected List<BootstrapData> load() {
                List<BootstrapData> ret = new ArrayList<>();
                if (bootstrapContent != null && !bootstrapContent.isEmpty()) {
                    String[] lines = bootstrapContent.split("\n");
                    for (String line : lines) {
                        try {
                            ret.add(BootstrapData.valueOf(line));
                        } catch (IllegalArgumentException iae) {
                            iae.printStackTrace();
                        }
                    }
                }
                return ret;
            }
        };
        final ListView<BootstrapData> listView = new ListView<BootstrapData>("servers", listViewModel) {

            @Override
            protected void populateItem(ListItem<BootstrapData> item) {
                item.setModel(new CompoundPropertyModel<>(item.getModelObject()));
                item.add(new Label("directoryServer"));
                item.add(new Label("deploymentUrl"));
                item.add(new Label("adminUser"));
                item.add(new Label("adminPassword"));
                item.add(new Label("baseDN"));
                item.add(new Label("directoryUser"));
                item.add(new Label("directoryPassword"));
            }
        };
        wmc.add(listView);
        wmc.setOutputMarkupPlaceholderTag(true);
        wmc.setVisible(false);
        add(wmc);
        form.add(new AjaxButton("parse") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                wmc.setVisible(!listView.getModelObject().isEmpty());
                if (target != null) {
                    target.add(wmc);
                }
            }
        });
        add(form);
    }

    private void addCryptoComponents() {
        final Label label = new Label("cryptoResult", new PropertyModel(this, "cryptoResult"));
        label.setOutputMarkupPlaceholderTag(true);
        label.setVisible(false);
        add(label);
        Form<Void> form = new BootstrapForm<Void>("crypto").type(FormType.Horizontal);
        final TextField<String> encryptionKey = new TextField<>("encryptionKey",
                new PropertyModel<String>(this, "encKey"));
        encryptionKey.setOutputMarkupId(true);
        form.add(encryptionKey);
        form.add(new AjaxLink("defaultKey") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                encKey = JCEEncryption.DEFAULT_PASSWORD;
                if (target != null) {
                    target.add(encryptionKey);
                }
            }
        });
        form.add(new TextArea<>("text", new PropertyModel<String>(this, "encryptedText")));
        form.add(new AjaxButton("encryptButton") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                cryptoResult = new JCEEncryption(encKey).encrypt(encryptedText);
                label.setVisible(true);
                if (target != null) {
                    target.add(label);
                }
            }
        });
        form.add(new AjaxButton("decryptButton") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                cryptoResult = new JCEEncryption(encKey).decrypt(encryptedText);
                label.setVisible(true);
                if (target != null) {
                    target.add(label);
                }
            }
        });
        add(form);
    }
}
