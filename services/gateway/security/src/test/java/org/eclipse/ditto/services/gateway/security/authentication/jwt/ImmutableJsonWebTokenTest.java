/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.services.gateway.security.authentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mutabilitydetector.unittesting.AllowedReason.assumingFields;
import static org.mutabilitydetector.unittesting.AllowedReason.provided;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertInstancesOf;
import static org.mutabilitydetector.unittesting.MutabilityMatchers.areImmutable;

import org.eclipse.ditto.json.JsonParseException;
import org.eclipse.ditto.model.base.auth.AuthorizationSubject;
import org.eclipse.ditto.signals.commands.base.exceptions.GatewayAuthenticationFailedException;
import org.eclipse.ditto.signals.commands.base.exceptions.GatewayJwtInvalidException;
import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Unit test for {@link ImmutableJsonWebToken}.
 * Tokens can be decrypted at https://jwt.io
 */
public final class ImmutableJsonWebTokenTest {

    private static final String TOKEN_WITH_REQUIRED_FIELDS =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJzdWIiOiJkaXR0byIsImp0aSI6ImUwMTI1YTZmLTNkMTctNDE0Mi04ZjA4LTk0MzFmMGJhY2FjZSIsImlhdCI6MTUwNDc2NjMwNiwiZXhwIjoxNTA0NzY5OTA2fQ.x29HhiY8YyQ5ODukfVsQAKl-q_KbAAWzQWp5G7gNSUY";

    @Test
    public void assertImmutability() {
        assertInstancesOf(ImmutableJsonWebToken.class,
                areImmutable(),
                provided(AuthorizationSubject.class).areAlsoImmutable(),
                assumingFields("authorizationSubjects", "authorizationSubjectsWithPrefixes")
                        .areSafelyCopiedUnmodifiableCollectionsWithImmutableElements());
    }

    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier.forClass(ImmutableJsonWebToken.class)
                .usingGetClass()
                .withRedefinedSuperclass()
                .verify();
    }

    /** */
    @Test
    public void tryToCreateInstanceFromEmptyAuthorizationString() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> ImmutableJsonWebToken.fromAuthorizationString(""))
                .withMessage("The Authorization String must not be empty!")
                .withNoCause();
    }

    @Test
    public void tryToParseTokenFromMissingAuthorization() {
        assertThatExceptionOfType(GatewayAuthenticationFailedException.class)
                .isThrownBy(() -> ImmutableJsonWebToken.fromAuthorizationString("Authorization"))
                .withMessage("The Authorization Header is invalid!")
                .withNoCause();
    }

    @Test
    public void tryToParseTokenFromInvalidAuthorization() {
        assertThatExceptionOfType(GatewayJwtInvalidException.class)
                .isThrownBy(() -> ImmutableJsonWebToken.fromAuthorizationString("Authorization foo"));
    }

    @Test
    public void parseTokenFromValidAuthorization() {
        final JsonWebToken jsonWebToken = ImmutableJsonWebToken.fromAuthorizationString("Authorization " +
                TOKEN_WITH_REQUIRED_FIELDS);

        assertThat(jsonWebToken.getIssuer()).isEqualTo("https://accounts.google.com");
    }

}
