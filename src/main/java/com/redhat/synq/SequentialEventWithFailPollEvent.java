/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of synq.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.redhat.synq;

import java.util.concurrent.TimeUnit;

public class SequentialEventWithFailPollEvent<T> extends SequentialEvent<T> implements
        FailPollEvent<T> {
    
    public SequentialEventWithFailPollEvent(Event<?> original, 
            FailPollEvent<? extends T> additional) {
        super(original, additional);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public FailPollEvent<T> after(Runnable action) {
        return new SequentialEventWithFailPollEvent<T>(original,
                new SequentialEventWithFailPollEvent<>((t, u) -> {
                    action.run();
                    return null;
                }, (FailPollEvent<T>) additional));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public FailPollEvent<T> withException(Throwable throwable) {
        ((FailEvent<T>) additional).withException(throwable);
        return this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public FailPollEvent<T> pollingEvery(long pollingInterval, TimeUnit pollingUnit) {
        ((PollEvent<T>) additional).pollingEvery(pollingInterval, pollingUnit);
        
        return this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public FailPollEvent<T> ignoring(Class<? extends Exception> exception) {
        ((PollEvent<T>) additional).ignoring(exception);
        
        return this;
    }
}
