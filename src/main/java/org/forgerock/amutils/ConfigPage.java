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
import java.io.ByteArrayInputStream;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.lang.Bytes;
import org.forgerock.openam.amutils.config.AMConfig;

/**
 *
 * @author aldaris
 */
public class ConfigPage extends TemplatePage {

    public ConfigPage() {
        final FileUploadField fileUploadField = new FileUploadField("configUpload");
        BootstrapForm<Void> uploadForm = new BootstrapForm<Void>("uploadForm") {

            @Override
            protected void onSubmit() {
                FileUpload fileUpload = fileUploadField.getFileUpload();

                new AMConfig(new ByteArrayInputStream(fileUpload.getBytes()));
            }
        };
        uploadForm.type(FormType.Inline).setMultiPart(true);
        uploadForm.setMaxSize(Bytes.megabytes(20l));

        uploadForm.add(fileUploadField);
        uploadForm.add(new UploadProgressBar("progress", uploadForm, fileUploadField));
        add(uploadForm);
    }
}
