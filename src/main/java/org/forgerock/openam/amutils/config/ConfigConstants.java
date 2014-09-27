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

import java.util.Arrays;
import java.util.List;

public class ConfigConstants {

    public static final String SERVERS = "com-sun-identity-servers";
    public static final String SITES = "com-sun-identity-sites";
    public static final String PLATFORM_SERVICE = "iPlanetAMPlatformService";
    public static final List<String> BORING_SERVICES = Arrays.asList("sunAMAuthFederationService",
            "iPlanetAMNamingService", "sunCoreTokenConfigService", "sunIdentityServerLibertyPPService",
            "sunFMCOTConfigService", "sunFMSAML2MetadataService", "sunFAMIDFFConfiguration",
            "sunfmSAML2SOAPBindingService", "sunAMRealmService",
            "sunFMWSFederationMetadataService", "iPlanetAMLoggingService", "RestSecurity",
            "sunIdentityServerAuthnService", "sunFAMSTSService", "dashboardService", "sunEntitlementIndexes",
            "iPlanetAMClientDetection", "iPlanetAMWebAgentService", "sunAMDelegationService", "sunIdentityAgentService",
            "iPlanetG11NSettings", "SunAMClientData", "sunFAMFederationCommon",
            "sunCoreTokenStoreService", "sunAMAuthSAEService", "iPlanetAMEntrySpecificService",
            "iPlanetAMPasswordResetService", "iPlanetAMAdminConsoleService",
            "sunIdentityLocaleService", "sunAMAuthWSSAuthModuleService", "sunIdentityFilteredRoleService",
            "openProvisioning", "sunIdentityServerSOAPBinding", "iPlanetAMMonitoringService", "banking",
            "sunEntitlementService", "iPlanetAMSAMLService", "AgentService", "OAuth2Provider",
            "sunMultiFederationProtocol", "sunFAMSAML2Configuration", "MailServer", "sunFMIDFFMetadataService",
            "iPlanetAMPolicyConfigService", "sunIdentityServerDiscoveryService",
            "iPlanetAMPolicyService", "iPlanetAMUserService");
    public static final List<String> INTERESTING_SERVICES = Arrays.asList(PLATFORM_SERVICE,
            "iPlanetAMSessionService", "iPlanetAMAuthConfiguration", "sunIdentityRepositoryService");
    public static final List<String> AUTH_SERVICES = Arrays.asList("iPlanetAMAuthWindowsDesktopSSOService",
            "sunAMAuthDataStoreService", "iPlanetAMAuthOATHService", "iPlanetAMAuthPersistentCookieService",
            "iPlanetAMAuthDevicePrintModuleService", "iPlanetAMAuthRadiusService", "sunAMAuthMSISDNService",
            "sunAMAuthAdaptiveService", "sunAMAuthJDBCService", "iPlanetAMAuthCertService",
            "iPlanetAMAuthSecurIDService", "iPlanetAMAuthLDAPService", "iPlanetAMAuthNTService",
            "sunAMAuthOAuthService", "sunAMAuthADService", "iPlanetAMAuthService", "sunAMAuthHOTPService",
            "iPlanetAMAuthAnonymousService", "iPlanetAMAuthMembershipService", "iPlanetAMAuthHTTPBasicService");
}
