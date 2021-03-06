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

import akka.contrib.persistence.mongodb.CanSuffixCollectionNames;

/**
 * Class that does nothing for configuring Akka persistence MongoDB plugin suffix builder to do nothing.
 */
@SuppressWarnings("unused")
public final class NamespaceSuffixCollectionNamesDisabled implements CanSuffixCollectionNames {

    @Override
    public String getSuffixFromPersistenceId(final String persistenceId) {
        return "";
    }

    @Override
    public String validateMongoCharacters(final String input) {
        return NamespaceSuffixCollectionNames.doValidateMongoCharacters(input);
    }
}
