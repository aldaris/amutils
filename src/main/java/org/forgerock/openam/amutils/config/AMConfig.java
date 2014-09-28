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

import org.forgerock.openam.amutils.config.processors.ServiceProcessor;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.sax.SAXSource;
import org.forgerock.amutils.sms.Service;
import org.forgerock.amutils.sms.ServicesConfiguration;
import org.forgerock.openam.amutils.config.processors.PlatformServiceProcessor;
import org.forgerock.util.xml.XMLUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static org.forgerock.openam.amutils.config.ConfigConstants.*;

public class AMConfig implements Serializable {

    private final Map<String, ServiceProcessor> serviceProcessors = new HashMap<>();
    private static final Map<String, Class<? extends ServiceProcessor>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put(PLATFORM_SERVICE, PlatformServiceProcessor.class);
    }

    public AMConfig(InputStream is) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("org.forgerock.amutils.sms");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SAXParser saxParser = XMLUtils.getSafeSAXParser(true);
            JAXBElement<ServicesConfiguration> sce = unmarshaller.unmarshal(
                    new SAXSource(saxParser.getXMLReader(), new InputSource(is)), ServicesConfiguration.class);
            ServicesConfiguration sc = sce.getValue();
            for (Service service : sc.getService()) {
                Class<? extends ServiceProcessor> processorClass = REGISTRY.get(service.getName());
                if (processorClass != null) {
                    try {
                        ServiceProcessor processor = processorClass.newInstance();
                        processor.initialize(service);
                    } catch (IllegalAccessException | InstantiationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            System.out.println(serviceProcessors);
        } catch (JAXBException | SAXException | ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public ServiceProcessor getServiceProcessor(String serviceName) {
        return serviceProcessors.get(serviceName);
    }
}
