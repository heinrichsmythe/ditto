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
package org.eclipse.ditto.model.placeholders;


import java.util.Optional;

/**
 * The ExpressionResolver is able to:
 * <ul>
 * <li>resolve {@link Placeholder}s in a passed {@code template} (based on {@link PlaceholderResolver}</li>
 * <li>execute optional pipeline stages in a passed {@code template}</li>
 * </ul>
 * As a result, a resolved String is returned.
 * For example, following expressions can be resolved:
 * <ul>
 * <li>{@code {{ thing:id }} }</li>
 * <li>{@code {{ header:device_id }} }</li>
 * <li>{@code {{ topic:full }} }</li>
 * <li>{@code {{ thing:name | fn:substring-before(':') | fn:default(thing:name) }} }</li>
 * <li>{@code {{ header:unknown | fn:default('fallback') }} }</li>
 * </ul>
 */
public interface ExpressionResolver {

    /**
     * Resolves a complete expression template starting with a {@link Placeholder} followed by optional pipeline stages
     * (e.g. functions).
     *
     * @param expressionTemplate the expressionTemplate to resolve {@link Placeholder}s and and execute optional
     * pipeline stages
     * @param allowUnresolved whether it should be allowed that unresolved placeholder may be present after processing
     * @return the resolved String or the original {@code expressionTemplate} if {@code allowUnresolved} was set to
     * {@code true} and placeholders could not be resolved.
     * @throws org.eclipse.ditto.model.connectivity.UnresolvedPlaceholderException thrown if {@code allowUnresolved} was
     * set to {@code false} and the passed in {@code expressionTemplate} could not be resolved
     * @throws PlaceholderFunctionTooComplexException thrown if the {@code expressionTemplate} contains a placeholder
     * function chain which is too complex (e.g. too much chained function calls)
     */
    String resolve(String expressionTemplate, boolean allowUnresolved);

    /**
     * Resolves a single {@link Placeholder} with the passed full {@code placeholder} name (e.g.: {@code thing:id} or
     * {@code header:correlation-id}.
     *
     * @param placeholder the placeholder to resolve.
     * @return the resolved placeholder if it could be resolved, empty Optional otherwise.
     */
    Optional<String> resolveSinglePlaceholder(String placeholder);

}
