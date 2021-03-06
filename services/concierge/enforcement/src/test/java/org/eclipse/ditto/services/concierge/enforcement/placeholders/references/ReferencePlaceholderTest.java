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

package org.eclipse.ditto.services.concierge.enforcement.placeholders.references;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.eclipse.ditto.json.JsonPointer;
import org.eclipse.ditto.json.assertions.DittoJsonAssertions;
import org.eclipse.ditto.signals.commands.base.exceptions.GatewayPlaceholderReferenceNotSupportedException;
import org.junit.Test;

/**
 * Tests {@link ReferencePlaceholder}.
 */
public class ReferencePlaceholderTest {

    @Test
    public void fromCharSequence() {
        final Optional<ReferencePlaceholder> referencePlaceholder =
                ReferencePlaceholder.fromCharSequence("{{ref:things/namespace:thingid/policyId}}");

        assertThat(referencePlaceholder).isPresent();
        assertThat(referencePlaceholder.get().getReferencedEntityId()).isEqualTo("namespace:thingid");
        DittoJsonAssertions.assertThat(referencePlaceholder.get().getReferencedField())
                .isEqualTo(                JsonPointer.of("policyId"));
        assertThat(referencePlaceholder.get().getReferencedEntityType()).isEqualTo(
                ReferencePlaceholder.ReferencedEntityType.THINGS);
    }

    @Test
    public void fromCharSequenceWithSpacesAfterBraces() {
        final Optional<ReferencePlaceholder> referencePlaceholder =
                ReferencePlaceholder.fromCharSequence("{{ ref:things/namespace:thingid/policyId }}");

        assertThat(referencePlaceholder).isPresent();
        assertThat(referencePlaceholder.get().getReferencedEntityId()).isEqualTo("namespace:thingid");
        DittoJsonAssertions.assertThat(referencePlaceholder.get().getReferencedField())
                .isEqualTo(                JsonPointer.of("policyId"));
        assertThat(referencePlaceholder.get().getReferencedEntityType()).isEqualTo(
                ReferencePlaceholder.ReferencedEntityType.THINGS);
    }

    @Test
    public void fromCharSequenceWithInvalidPlaceholderIsEmpty() {
        assertThat(ReferencePlaceholder.fromCharSequence("{{things/namespace:thingid/policyId}}")).isNotPresent();
        assertThat(ReferencePlaceholder.fromCharSequence("{{ref:thingsnamespace:thingid/policyId}}")).isNotPresent();
        assertThat(ReferencePlaceholder.fromCharSequence("{{ref:things/ namespace:thingid/policyId}}")).isNotPresent();
        assertThat(ReferencePlaceholder.fromCharSequence("{{ref:things/namespace:thingid}}")).isNotPresent();

        assertThat(ReferencePlaceholder.fromCharSequence(null)).isNotPresent();
    }

    @Test
    public void fromCharSequenceWithDeepPointer() {
        final Optional<ReferencePlaceholder> referencePlaceholder =
                ReferencePlaceholder.fromCharSequence(
                        "{{ref:things/namespace:thingid/features/properties/policyFeature/policyId}}");

        assertThat(referencePlaceholder).isPresent();
        assertThat(referencePlaceholder.get().getReferencedEntityId()).isEqualTo("namespace:thingid");
        DittoJsonAssertions.assertThat(referencePlaceholder.get().getReferencedField()).isEqualTo(
                JsonPointer.of("features/properties/policyFeature/policyId"));
        assertThat(referencePlaceholder.get().getReferencedEntityType()).isEqualTo(
                ReferencePlaceholder.ReferencedEntityType.THINGS);
    }

    @Test
    public void fromCharSequenceWithUnsupportedEntityTypeThrowsException() {
        assertThatThrownBy(() -> ReferencePlaceholder.fromCharSequence("{{ref:topologies/namespace:thingid/policyId}}"))
                .isInstanceOf(GatewayPlaceholderReferenceNotSupportedException.class);
    }

}
