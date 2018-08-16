/*
 * Copyright (c) 2017 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 *
 * Contributors:
 *    Bosch Software Innovations GmbH - initial contribution
 */
package org.eclipse.ditto.model.base.entity;

import java.time.Instant;
import java.util.Optional;

import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.json.JsonField;
import org.eclipse.ditto.json.JsonFieldSelector;
import org.eclipse.ditto.json.JsonObject;
import org.eclipse.ditto.model.base.json.FieldType;
import org.eclipse.ditto.model.base.json.JsonSchemaVersion;
import org.eclipse.ditto.model.base.json.Jsonifiable;

/**
 * Base type for top level entities.
 *
 * @param <T> The type of the Revision of the entity.
 */
@Immutable
public interface Entity<T extends Revision<T>> extends Jsonifiable.WithFieldSelectorAndPredicate<JsonField> {

    /**
     * Returns the ID of this entity.
     *
     * @return the ID of this entity.
     */
    Optional<String> getId();

    /**
     * Returns the current revision of this Thing.
     *
     * @return the current revision of this Thing.
     */
    Optional<T> getRevision();

    /**
     * Returns the modified timestamp of this Thing.
     *
     * @return the timestamp.
     */
    Optional<Instant> getModified();

    /**
     * Returns all non hidden marked fields of this object.
     *
     * @return a JSON object representation of this object including only non hidden marked fields.
     */
    @Override
    default JsonObject toJson() {
        return toJson(FieldType.notHidden());
    }

    @Override
    default JsonObject toJson(final JsonSchemaVersion schemaVersion, final JsonFieldSelector fieldSelector) {
        return toJson(schemaVersion, FieldType.regularOrSpecial()).get(fieldSelector);
    }
}
