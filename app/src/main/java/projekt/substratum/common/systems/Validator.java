/*
 * Copyright (c) 2016-2017 Projekt Substratum
 * This file is part of Substratum.
 *
 * Substratum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Substratum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Substratum.  If not, see <http://www.gnu.org/licenses/>.
 */

package projekt.substratum.common.systems;

import android.content.Context;
import android.content.res.Resources;

public enum Validator {
    ;

    public static final Boolean VALIDATE_WITH_LOGS = false;

    public static boolean checkResourceObject(final Context context,
                                              final String package_name,
                                              final String type,
                                              final String object_name) {
        try {
            final Resources res = context.getPackageManager().getResourcesForApplication
                    (package_name);
            final int object = res.getIdentifier(
                    package_name + ':' + type + '/' + object_name, type, package_name);
            return object != 0;
        } catch (final Exception e) {
            // Suppress warning
        }
        return false;
    }
}