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

package projekt.substratum.services.binder;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import projekt.substratum.IInterfacerInterface;
import projekt.substratum.Substratum;
import projekt.substratum.common.Systems;

import static projekt.substratum.common.References.INTERFACER_BINDED;
import static projekt.substratum.common.References.INTERFACER_PACKAGE;

public class InterfacerBinderService extends Service implements ServiceConnection {

    private static final String TAG = "InterfacerBinderService";
    private static InterfacerBinderService binderService;
    private IInterfacerInterface interfacerInterface;
    private boolean bound;

    public static InterfacerBinderService getInstance() {
        return binderService;
    }

    public IInterfacerInterface getInterfacerInterface() {
        return interfacerInterface;
    }

    private void bindInterfacer() {
        if (Systems.isBinderInterfacer(this) && !bound) {
            Intent intent = new Intent(INTERFACER_BINDED);
            intent.setPackage(INTERFACER_PACKAGE);
            bindService(intent, this, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binderService = this;
        bindInterfacer();
    }

    @Override
    public void onDestroy() {
        interfacerInterface = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        interfacerInterface = IInterfacerInterface.Stub.asInterface(service);
        bound = true;
        Substratum.log(TAG, "Substratum has successfully binded with the Interfacer module.");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        interfacerInterface = null;
        bound = false;
        Substratum.log(TAG, "Substratum has successfully unbinded with the Interfacer module.");
    }
}