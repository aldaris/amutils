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
package org.forgerock.amutils.web.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyValue<K, V> implements Serializable {

    private final K key;
    private final V value;

    private KeyValue(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public static <K, V> List<KeyValue<K, V>> convertMapToKeyValues(Map<K, V> map) {
        final List<KeyValue<K, V>> list = new ArrayList<>(map.size());
        map.forEach((key, value) -> {
            list.add(new KeyValue<>(key, value));
        });
        return list;
    }
}
