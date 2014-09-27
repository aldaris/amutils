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
package org.forgerock.amutils.web.wicket;

import java.util.List;
import java.util.Map;
import org.apache.wicket.model.IModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.forgerock.amutils.web.utils.KeyValue;

public class MapView<V> extends ListView<KeyValue<String, V>> {

    public MapView(final String id, final Map<String, V> map) {
        super(id, KeyValue.<String, V>convertMapToKeyValues(map));
    }

    public MapView(final String id, final IModel<List<KeyValue<String, V>>> model) {
        super(id, model);
    }

    @Override
    protected void populateItem(final ListItem<KeyValue<String, V>> item) {
        KeyValue<String, V> keyValue = item.getModelObject();
        item.add(new Label("key", new StringResourceModel(keyValue.getKey(), this, null)));
        item.add(new Label("value", keyValue.getValue().toString()));
    }
}
