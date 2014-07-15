/**
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.arcbees.analytics.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class GoogleAnalyticsModule extends AbstractGinModule {
    public static class Builder {
        private final String userAccount;

        public Builder(final String userAccount) {
            this.userAccount = userAccount;
        }

        public GoogleAnalyticsModule build() {
            return new GoogleAnalyticsModule(userAccount);
        }
    }

    private String userAccount;

    private GoogleAnalyticsModule(final String userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(GaAccount.class).to(userAccount);
        bind(GoogleAnalyticsImpl.class).in(Singleton.class);
        bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class);
    }
}