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
package com.arcbees.analytics.client.universalanalytics.fields;

import com.arcbees.analytics.client.universalanalytics.HitType;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class HitFieldBuilder extends FieldBuilder {
    HitFieldBuilder(final JSONObject jsonObject) {
        super(jsonObject);
    }

    /**
     * @param hitType - the type of the hit
     */
    public HitFieldBuilder hitType(final HitType hitType) {
        put("hitType", new JSONString(hitType.getFieldName()));
        return this;
    }

    /**
     * Optional.
     * Specifies that a hit be considered non-interactive.
     * @param nonInteraction <br>
     * Default: none;<br>
     */
    public HitFieldBuilder nonInteractionHit(final boolean nonInteraction) {
        put("nonInteraction", JSONBoolean.getInstance(nonInteraction));
        return this;
    }
}
