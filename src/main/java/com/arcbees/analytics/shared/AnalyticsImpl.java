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

package com.arcbees.analytics.shared;

import com.arcbees.analytics.shared.options.AnalyticsOptions;
import com.arcbees.analytics.shared.options.ContentOptions;
import com.arcbees.analytics.shared.options.CreateOptions;
import com.arcbees.analytics.shared.options.EventsOptions;
import com.arcbees.analytics.shared.options.ExceptionOptions;
import com.arcbees.analytics.shared.options.SocialOptions;
import com.arcbees.analytics.shared.options.TimingOptions;

public abstract class AnalyticsImpl implements Analytics {
    private final String userAccount;

    protected AnalyticsImpl(final String userAccount) {
        this.userAccount = userAccount;
    }

    protected Throwable clipUmbrellaExceptions(final Throwable e) {
        return e;
    }

    @Override
    public CreateOptions create() {
        return create(userAccount);
    }

    @Override
    public TimingOptions endTimingEvent(final String timingCategory, final String timingVariableName) {
        return endTimingEvent(null, timingCategory, timingVariableName);
    }

    private String getExceptionStackTraceAsString(final Throwable e) {
        final Throwable exceptionToTrack = clipUmbrellaExceptions(e);
        final StringBuilder sb = new StringBuilder();
        for (final StackTraceElement ste: exceptionToTrack.getStackTrace()) {
            sb.append(ste.toString()).append("\n");
        }
        return sb.toString();
    }

    protected String getTimingKey(final String timingCategory, final String timingVariableName) {
        return timingCategory + ":" + timingVariableName;
    }

    @Override
    public AnalyticsOptions send(final HitType hitType) {
        return send(null, hitType);
    }

    @Override
    public EventsOptions sendEvent(final String category, final String action) {
        return sendEvent(null, category, action);
    }

    @Override
    public EventsOptions sendEvent(final String trackerName, final String category, final String action) {
        return send(trackerName, HitType.EVENT).eventsOptions(category, action);
    }

    @Override
    public ExceptionOptions sendException(final String trackerName, final Throwable e) {

        return send(trackerName, HitType.EXCEPTION).exceptionOptions().exceptionDescription(getExceptionStackTraceAsString(e));
    }

    @Override
    public ExceptionOptions sendException(final Throwable e) {
        return sendException(null, e);
    }

    @Override
    public ContentOptions sendPageView() {
        return sendPageView(null);
    }

    @Override
    public ContentOptions sendPageView(final String trackerName) {
        return send(trackerName, HitType.PAGE_VIEW).contentOptions();
    }

    @Override
    public SocialOptions sendSocial(final String socialNetwork, final String socialAction,
            final String socialTarget) {
        return sendSocial(null, socialNetwork, socialAction, socialTarget);
    }

    @Override
    public SocialOptions sendSocial(final String trackerName, final String socialNetwork, final String socialAction,
            final String socialTarget) {
        return send(trackerName, HitType.SOCIAL).socialOptions(socialNetwork, socialAction, socialTarget);
    }

    @Override
    public TimingOptions sendTiming(final String timingCategory, final String timingVar, final int timingValue) {
        return sendTiming(null, timingCategory, timingVar, timingValue);
    }

    @Override
    public TimingOptions sendTiming(final String trackerName, final String timingCategory, final String timingVar,
            final int timingValue) {
        return send(trackerName, HitType.TIMING).timingOptions(timingCategory, timingVar, timingValue);
    }
}
