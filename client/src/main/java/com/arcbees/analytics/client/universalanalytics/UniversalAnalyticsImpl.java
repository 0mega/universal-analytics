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
package com.arcbees.analytics.client.universalanalytics;

import com.arcbees.analytics.client.GaAccount;
import com.arcbees.analytics.client.universalanalytics.fields.ContentFieldBuilder;
import com.arcbees.analytics.client.universalanalytics.fields.CreateOnlyFieldBuilder;
import com.arcbees.analytics.client.universalanalytics.fields.EventFieldBuilder;
import com.arcbees.analytics.client.universalanalytics.fields.FieldBuilder;
import com.arcbees.analytics.client.universalanalytics.fields.SocialNetworkFieldBuilder;
import com.arcbees.analytics.client.universalanalytics.fields.UserTimingFieldBuilder;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class UniversalAnalyticsImpl implements UniversalAnalytics {
    private final String userAccount;

    @Inject
    UniversalAnalyticsImpl(@GaAccount final String userAccount,
            @Named("uaAutoCreate") final boolean autoCreate) {
        init();
        this.userAccount = userAccount;
        if (autoCreate) {
            create();
        }
    }

    @Override
    public void addAccount(final String trackerName, final String userAccount) {
        create(userAccount).name(trackerName);
    }

    private void call(final JSONValue... params) {
        final JSONArray aryParams = new JSONArray();
        for (final JSONValue p:params) {
            aryParams.set(aryParams.size(), p);
        }
        nativeCall(aryParams.getJavaScriptObject());
    }

    @Override
    public CreateOnlyFieldBuilder create() {
        return create(userAccount);
    }

    @Override
    public CreateOnlyFieldBuilder create(final String userAccount) {
        return new FieldBuilder(new OptionsCallback() {

            @Override
            public void onCallback(final JSONObject options) {
                call(new JSONString("create"), new JSONString(userAccount), options);
            }
        }).createOnlyFields();
    }

    @Override
    public void enablePlugin(final AnalyticsPlugin plugin) {
        call(new JSONString("require"), new JSONString(plugin.getFieldName()));
    }

    private native void init()/*-{
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
        m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })($wnd,$doc,'script','//www.google-analytics.com/analytics.js','__ua');
    }-*/;

    private native void nativeCall(JavaScriptObject params) /*-{
        $wnd.__ua.apply(null, params);
    }-*/;

    @Override
    public FieldBuilder send(final HitType hitType) {
        return send(null, hitType);
    }

    @Override
    public FieldBuilder send(final String trackerName, final HitType hitType) {
        return new FieldBuilder(new OptionsCallback() {

            @Override
            public void onCallback(final JSONObject options) {
                call(new JSONString(trackerName == null ? "send" : trackerName + ".send"),
                        new JSONString(hitType.getFieldName()), options);
            }
        });
    }

    @Override
    public EventFieldBuilder sendEvent(final String category, final String action) {
        return sendEvent(null, category, action);
    }

    @Override
    public EventFieldBuilder sendEvent(final String trackerName, final String category, final String action) {
        return send(trackerName, HitType.EVENT).event(category, action);
    }

    @Override
    public ContentFieldBuilder sendPageView() {
        return sendPageView(null);
    }

    @Override
    public ContentFieldBuilder sendPageView(final String trackerName) {
        return send(trackerName, HitType.PAGE_VIEW).content();
    }

    @Override
    public SocialNetworkFieldBuilder sendSocial(final String socialNetwork, final String socialAction,
            final String socialTarget) {
        return sendSocial(null, socialNetwork, socialAction, socialTarget);
    }

    @Override
    public SocialNetworkFieldBuilder sendSocial(final String trackerName, final String socialNetwork,
            final String socialAction, final String socialTarget) {
        return send(trackerName, HitType.SOCIAL).socialNetwork(socialNetwork, socialAction, socialTarget);
    }

    @Override
    public UserTimingFieldBuilder sendTiming(final String timingCategory,
            final String timingVar, final int timingValue) {
        return sendTiming(null, timingCategory, timingVar, timingValue);
    }

    @Override
    public UserTimingFieldBuilder sendTiming(final String trackerName, final String timingCategory,
            final String timingVar, final int timingValue) {
        return send(trackerName, HitType.TIMING).userTiming(timingCategory, timingVar, timingValue);
    }

    @Override
    public FieldBuilder setGlobalSettings() {
        return new FieldBuilder(new OptionsCallback() {

            @Override
            public void onCallback(final JSONObject options) {
                call(new JSONString("set"), options);
            }

        });
    }

    @Override
    public void trackEvent(final String category, final String action) {
        trackEventWithTracker(null, category, action);
    }

    @Override
    public void trackEvent(final String category, final String action, final String optLabel) {
        trackEventWithTracker(null, category, action, optLabel);
    }

    @Override
    public void trackEvent(final String category, final String action, final String optLabel, final int optValue) {
        trackEventWithTracker(null, category, action, optLabel, optValue);
    }

    @Override
    public void trackEvent(final String category, final String action, final String optLabel, final int optValue,
            final boolean optNonInteraction) {
        trackEventWithTracker(null, category, action, optLabel, optValue, optNonInteraction);
    }

    @Override
    public void trackEventWithTracker(final String trackerName, final String category, final String action) {
        sendEvent(trackerName, category, action);
    }

    @Override
    public void trackEventWithTracker(final String trackerName, final String category, final String action,
            final String optLabel) {
        sendEvent(trackerName, category, action).eventLabel(optLabel);
    }

    @Override
    public void trackEventWithTracker(final String trackerName, final String category, final String action,
            final String optLabel, final int optValue) {
        sendEvent(trackerName, category, action).eventLabel(optLabel).eventValue(optValue);
    }

    @Override
    public void trackEventWithTracker(final String trackerName, final String category, final String action,
            final String optLabel, final int optValue, final boolean optNonInteraction) {
        sendEvent(trackerName, category, action).eventLabel(optLabel).eventValue(optValue).hit()
        .nonInteractionHit(optNonInteraction);
    }

    @Override
    public void trackPageview() {
        sendPageView();
    }

    @Override
    public void trackPageview(final String pageName) {
        trackPageview(null, pageName);
    }

    @Override
    public void trackPageview(final String trackerName, final String pageName) {
        final String pName = pageName.startsWith("/") ? pageName : "/" + pageName;
        sendPageView(trackerName).documentPath(pName);
    }
}
