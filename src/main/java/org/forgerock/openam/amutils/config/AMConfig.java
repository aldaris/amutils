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

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.sax.SAXSource;
import org.apache.wicket.util.collections.MultiMap;
import static org.forgerock.openam.amutils.config.ConfigConstants.*;
import org.forgerock.amutils.sms.Service;
import org.forgerock.amutils.sms.ServicesConfiguration;
import org.forgerock.util.xml.XMLUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AMConfig implements Serializable {

    private Map<String, Service> services = new HashMap<>();
    private final List<ServiceProcessor> serviceProcessors = new ArrayList<>(1);

    public AMConfig(InputStream is) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("org.forgerock.amutils.sms");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SAXParser saxParser = XMLUtils.getSafeSAXParser(true);
            JAXBElement<ServicesConfiguration> sce = unmarshaller.unmarshal(
                    new SAXSource(saxParser.getXMLReader(), new InputSource(is)), ServicesConfiguration.class);
            ServicesConfiguration sc = sce.getValue();
            for (Service service : sc.getService()) {
                if (INTERESTING_SERVICES.contains(service.getName())) {
                    services.put(service.getName(), service);
                }
            }
            System.out.println(services.keySet());
        } catch (JAXBException | SAXException | ParserConfigurationException pe) {
            pe.printStackTrace();
        }
    }

    public Service getService(String serviceName) {
        return services.get(serviceName);
    }

}
