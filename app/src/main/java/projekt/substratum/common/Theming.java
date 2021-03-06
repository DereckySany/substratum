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

package projekt.substratum.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import projekt.substratum.Substratum;
import projekt.substratum.activities.launch.ThemeLaunchActivity;

import static projekt.substratum.common.References.SUBSTRATUM_PACKAGE;
import static projekt.substratum.common.References.TEMPLATE_GET_KEYS;
import static projekt.substratum.common.References.TEMPLATE_THEME_MODE;
import static projekt.substratum.common.References.hashPassthrough;
import static projekt.substratum.common.Systems.checkPackageSupport;

public enum Theming {
    ;

    /**
     * Refresh installed themes shared preferences
     *
     * @param context Self explanatory, bud.
     */
    public static void refreshInstalledThemesPref(Context context) {
        SharedPreferences.Editor editor = Substratum.getPreferences().edit();

        // Initial parse of what is installed on the device
        Set<String> installedThemes = new TreeSet<>();
        List<ResolveInfo> themes = Packages.getThemes(context);
        for (ResolveInfo theme : themes) {
            installedThemes.add(theme.activityInfo.packageName);
        }
        editor.putStringSet("installed_themes", installedThemes);
        editor.apply();
    }

    /**
     * Launch a specific theme
     *
     * @param context     Self explanatory, bud.
     * @param packageName Theme to be launched
     */
    public static void launchTheme(Context context,
                                   String packageName) {
        if (context.getPackageName().equals(SUBSTRATUM_PACKAGE)) {
            Intent themeIntent = themeIntent(
                    context,
                    packageName,
                    TEMPLATE_THEME_MODE);
            context.startActivity(themeIntent);
        }
    }

    /**
     * Grab the theme's keys
     *
     * @param context     Self explanatory, bud.
     * @param packageName Theme to obtain keys for
     */
    public static void getThemeKeys(Context context,
                                    String packageName) {
        if (context.getPackageName().equals(SUBSTRATUM_PACKAGE)) {
            Intent themeIntent = themeIntent(
                    context,
                    packageName,
                    TEMPLATE_GET_KEYS);
            try {
                context.startActivity(themeIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Grab the theme's intent
     *
     * @param context      Self explanatory, bud.
     * @param packageName  Theme to receive intent for
     * @param actionIntent Intent to be verified with a series of data
     * @return Returns an intent to launch the theme
     */
    private static Intent themeIntent(Context context,
                                      String packageName,
                                      String actionIntent) {
        if (context.getPackageName().equals(SUBSTRATUM_PACKAGE)) {
            String TAG = "ThemeLauncher";
            Substratum.log(TAG, "Creating new intent");
            Intent intentActivity;
            if (actionIntent.equals(TEMPLATE_GET_KEYS)) {
                intentActivity = new Intent();
            } else {
                intentActivity = new Intent(context, ThemeLaunchActivity.class);
            }
            intentActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentActivity.putExtra(Internal.THEME_PACKAGE, packageName);
            Substratum.log(TAG, "Assigning action to intent...");
            intentActivity.setAction(actionIntent);
            Substratum.log(TAG, "Assigning package name to intent...");
            intentActivity.setPackage(packageName);
            intentActivity.putExtra(Internal.THEME_CALLER, context.getPackageName());
            Substratum.log(TAG, "Checking for theme system type...");
            intentActivity.putExtra(Internal.THEME_OMS, !Systems.checkOMS(context));
            intentActivity.putExtra(Internal.NOTIFICATION, false);
            Substratum.log(TAG, "Obtaining APK signature hash...");
            intentActivity.putExtra(Internal.THEME_HASHPASSTHROUGH, hashPassthrough(context));
            Substratum.log(TAG, "Checking for certification...");
            intentActivity.putExtra(Internal.THEME_CERTIFIED, checkPackageSupport(context, false));
            Substratum.log(TAG, "Starting Activity...");
            return intentActivity;
        } else {
            return null;
        }
    }
}