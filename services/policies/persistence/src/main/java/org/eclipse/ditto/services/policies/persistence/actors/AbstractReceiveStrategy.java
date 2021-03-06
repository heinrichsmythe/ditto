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
package org.eclipse.ditto.services.policies.persistence.actors;

import static java.util.Objects.requireNonNull;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.ditto.services.utils.akka.LogUtil;
import org.eclipse.ditto.signals.commands.base.Command;

import akka.event.DiagnosticLoggingAdapter;
import akka.japi.pf.FI;

/**
 * This {@link ReceiveStrategy} provides already an implementation of {@link #getMatchingClass()} as well as a default
 * implementation of {@link #getPredicate()} and {@link #getUnhandledFunction()}. The predicate always evaluates to
 * {@code true} which means that the "apply function" of this strategy is used. The behaviour of the "apply function"
 * has to be implemented by subclasses. The "unhandled function" does nothing by default.
 *
 * @param <T> type of the class this strategy matches against.
 */
@NotThreadSafe
public abstract class AbstractReceiveStrategy<T> implements ReceiveStrategy<T> {

    private final Class<T> matchingClass;
    private final DiagnosticLoggingAdapter logger;

    /**
     * Constructs a new {@code AbstractReceiveStrategy} object.
     *
     * @param theMatchingClass the class of the message this strategy reacts to.
     * @param theLogger the logger to use for logging.
     * @throws NullPointerException if {@code theMatchingClass} is {@code null}.
     */
    protected AbstractReceiveStrategy(final Class<T> theMatchingClass, final DiagnosticLoggingAdapter theLogger) {
        matchingClass = requireNonNull(theMatchingClass, "The matching class must not be null!");
        logger = requireNonNull(theLogger, "The logger must not be null!");
    }

    protected void apply(final T message) {
        if (message instanceof Command) {
            final Command command = (Command) message;
            LogUtil.enhanceLogWithCorrelationId(logger, command.getDittoHeaders().getCorrelationId());
            if (logger.isDebugEnabled()) {
                logger.debug("Applying command '{}': {}", command.getType(), command.toJsonString());
            }
        }

        doApply(message);
    }

    protected abstract void doApply(T message);

    @Override
    public Class<T> getMatchingClass() {
        return matchingClass;
    }

    @Override
    public FI.TypedPredicate<T> getPredicate() {
        return command -> true;
    }

    @Override
    public FI.UnitApply<T> getApplyFunction() {
        return this::apply;
    }

    @Override
    public FI.UnitApply<T> getUnhandledFunction() {
        return msg -> {
            // unhandled
        };
    }
}
