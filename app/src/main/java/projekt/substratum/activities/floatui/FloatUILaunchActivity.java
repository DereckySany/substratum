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

package projekt.substratum.activities.floatui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import projekt.substratum.R;
import projekt.substratum.common.References;
import projekt.substratum.services.floatui.SubstratumFloatInterface;
import projekt.substratum.services.tiles.FloatUiTile;

import static projekt.substratum.common.Systems.checkUsagePermissions;

public class FloatUILaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Settings.canDrawOverlays(this.getApplicationContext()) &&
                checkUsagePermissions(this.getApplicationContext())) {
            if (!References.isServiceRunning(SubstratumFloatInterface.class,
                    this.getApplicationContext())) {
                this.triggerFloatingHead(true);
            } else {
                this.triggerFloatingHead(false);
            }
        } else {
            Toast.makeText(this, this.getString(R.string.per_app_manual_grant),
                    Toast.LENGTH_LONG).show();
        }
        this.finish();
    }

    private void triggerFloatingHead(final Boolean show) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                this.getApplicationContext());
        final int active = (show) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
        prefs.edit().putInt("float_tile", active).apply();
        FloatUiTile.requestListeningState(this.getApplicationContext(),
                new ComponentName(this.getApplicationContext(), FloatUiTile.class));
        if (show) {
            this.getApplicationContext().startService(new Intent(this.getApplicationContext(),
                    SubstratumFloatInterface.class));
        } else {
            this.stopService(new Intent(this.getApplicationContext(),
                    SubstratumFloatInterface.class));
        }
    }
}