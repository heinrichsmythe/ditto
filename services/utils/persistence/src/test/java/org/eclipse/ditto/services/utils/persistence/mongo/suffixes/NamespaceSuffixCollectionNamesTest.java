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
package org.eclipse.ditto.services.utils.persistence.mongo.suffixes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class NamespaceSuffixCollectionNamesTest {

    private NamespaceSuffixCollectionNames sut;

    @Before
    public void setup() {
        NamespaceSuffixCollectionNames.resetConfig();
        sut = new NamespaceSuffixCollectionNames();
    }

    @Test
    public void getSuffixFromPersistenceId() {
        NamespaceSuffixCollectionNames.setConfig(new SuffixBuilderConfig(Collections.singletonList("thing")));
        final String persistenceId = "thing:org.eclipse.ditto:test:thing";

        final String suffix = sut.getSuffixFromPersistenceId(persistenceId);

        final String expectedSuffix = "org.eclipse.ditto";
        assertThat(suffix).isEqualTo(expectedSuffix);
    }

    @Test
    public void getSuffixFromPersistenceIdReturnsEmptySuffixIfPrefixIsNotSupported() {
        NamespaceSuffixCollectionNames.setConfig(new SuffixBuilderConfig(Collections.singletonList("thing")));
        final String persistenceId = "some:org.eclipse.ditto:test:thing";

        final String suffix = sut.getSuffixFromPersistenceId(persistenceId);

        final String expectedSuffix = "";
        assertThat(suffix).isEqualTo(expectedSuffix);
    }

    @Test(expected = NullPointerException.class)
    public void getSuffixFromPersistenceIdThrowsNullpointerExceptionWithoutConfig() {
        final String persistenceId = "thing:org.eclipse.ditto:test:thing";

        sut.getSuffixFromPersistenceId(persistenceId);
    }

    @Test
    public void validateMongoCharacters() {
        NamespaceSuffixCollectionNames.setConfig(new SuffixBuilderConfig(Collections.singletonList("thing")));
        final String invalidInput =
                "This/should\\be.a s|tg\"with$so?me*has:hes<b";

        final String sanitizedString = sut.validateMongoCharacters(invalidInput);

        final String expected = invalidInput.replace('$', '#');
        assertThat(sanitizedString).isEqualTo(expected);
    }

    @Test
    public void validateVeryLongNamespace() {
        NamespaceSuffixCollectionNames.setConfig(new SuffixBuilderConfig(Collections.singletonList("thing")));
        final String invalidInput =
                "this.is.a.namespace.which.is.quite.long.so.that.not.all.characters.would.fit.as.collection.name";

        final String sanitizedString = sut.validateMongoCharacters(invalidInput);

        assertThat(sanitizedString).startsWith(invalidInput.substring(0, 20));
        final String expected = invalidInput.substring(0, NamespaceSuffixCollectionNames.MAX_SUFFIX_CHARS_LENGTH) +
                "@" + Integer.toHexString(invalidInput.hashCode());
        assertThat(sanitizedString).isEqualTo(expected);
        assertThat(sanitizedString).doesNotEndWith(invalidInput);
    }

    @Test(expected = NullPointerException.class)
    public void validateMongoCharactersThrowsNullpointerExceptionWithoutConfig() {
        final String persistenceId = "thing:org.eclipse.ditto:test:thing";

        sut.getSuffixFromPersistenceId(persistenceId);
    }

    @Test(expected = NamespaceSuffixCollectionNames.PersistenceIdInvalidException.class)
    public void getSuffixFromPersistenceIdThrowsIllegalStateException() {
        NamespaceSuffixCollectionNames.setConfig(new SuffixBuilderConfig(Collections.singletonList("thing")));
        final String persistenceId = "thing:::::::";

        sut.getSuffixFromPersistenceId(persistenceId);
    }

    @Test
    public void getSuffixFromPersistenceIdWithNamespaceAndOnlyColons() {
        NamespaceSuffixCollectionNames.setConfig(new SuffixBuilderConfig(Collections.singletonList("thing")));
        final String persistenceId = "thing:org.eclipse.ditto:::::::";

        final String suffix = sut.getSuffixFromPersistenceId(persistenceId);

        final String expectedSuffix = "org.eclipse.ditto";
        assertThat(suffix).isEqualTo(expectedSuffix);
    }
}
