/*
 * Copyright 2013 ForgeRock AS.
 *
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
 */
package org.forgerock.amutils;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormType;
import java.util.Map;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.forgerock.openam.amutils.decode.JCEEncryption;
import org.forgerock.openam.amutils.decode.SessionUtils;

public class DecodingPage extends TemplatePage {

    private String sessionId;
    private String sessionResult = "";
    private String encKey;
    private String encryptedText;
    private String cryptResult = "";

    public DecodingPage(final PageParameters parameters) {
        super(parameters);
        addSessionComponents();
        addCryptComponents();
    }

    private void addSessionComponents() {
        final Label sessionResultLabel = new Label("sessionResult", new PropertyModel(this, "sessionResult"));
        sessionResultLabel.setEscapeModelStrings(false);
        sessionResultLabel.setOutputMarkupId(true);
        add(sessionResultLabel);
        Form<Void> form = new BootstrapForm<Void>("session").type(FormType.Inline);
        form.add(new TextField<>("sessionId", new PropertyModel<String>(this, "sessionId")));
        form.add(new AjaxButton("sessionButton") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Map<String, String> extensions = SessionUtils.getExtensions(sessionId);
                if (extensions.isEmpty()) {
                    sessionResult = "Unable to parse session ID";
                } else {
                    sessionResult = "<ul class=\"unstyled\">";
                    for (Map.Entry<String, String> entry : extensions.entrySet()) {
                        sessionResult += "<li>" + entry.getKey() + ": " + entry.getValue() + "</li>";
                    }
                    sessionResult += "</ul>";
                }
                target.add(sessionResultLabel);
            }
        });
        add(form);
    }

    private void addCryptComponents() {
        final Label label = new Label("cryptResult", new PropertyModel(this, "cryptResult"));
        label.setOutputMarkupPlaceholderTag(true);
        label.setVisible(false);
        label.setEscapeModelStrings(false);
        add(label);
        Form<Void> form = new BootstrapForm<Void>("crypt").type(FormType.Horizontal);
        form.add(new TextField<>("encryptionKey", new PropertyModel<String>(this, "encKey")));
        form.add(new TextArea<>("text", new PropertyModel<String>(this, "encryptedText")));
        form.add(new AjaxButton("encryptButton") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                cryptResult = new JCEEncryption(encKey).encrypt(encryptedText);
                label.setVisible(true);
                target.add(label);
            }
        });
        form.add(new AjaxButton("decryptButton") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                cryptResult = new JCEEncryption(encKey).decrypt(encryptedText);
                label.setVisible(true);
                target.add(label);
            }
        });
        add(form);
    }
}
