// Copyright 2016 Sebastian Kuerten
//
// This file is part of app-version-util.
//
// app-version-util is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// app-version-util is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with app-version-util. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.android.appversions;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.util.Log;

public class VersionUpdateChecker {

  private static final String LOG_TAG = "appversion";

  private final String prefKey;

  private SharedPreferences preferences;
  private int storedVersion;
  private int currentVersion;

  private boolean versionUpdate;

  public VersionUpdateChecker(Context context)
  {
    prefKey = context.getString(R.string.pref_key_last_seen_version);

    preferences = PreferenceManager.getDefaultSharedPreferences(context);

    storedVersion = preferences.getInt(prefKey, 0);
    currentVersion = 0;

    try {
      PackageInfo pinfo = context.getPackageManager().getPackageInfo(
          context.getPackageName(), 0);
      currentVersion = pinfo.versionCode;
      versionUpdate = currentVersion != storedVersion;
    } catch (NameNotFoundException e) {
      Log.e(LOG_TAG, "unable to get version info");
    }

    Log.i(LOG_TAG, "stored version: " + storedVersion);
    Log.i(LOG_TAG, "current version: " + currentVersion);

    if (versionUpdate) {
      Log.i(LOG_TAG, "version update");
    } else {
      Log.i(LOG_TAG, "version did not change");
    }
  }

  public int getStoredVersion()
  {
    return storedVersion;
  }

  public int getCurrentVersion()
  {
    return currentVersion;
  }

  public boolean isVersionUpdate()
  {
    return versionUpdate;
  }

  public void storeCurrentVersion()
  {
    Editor edit = preferences.edit();
    edit.putInt(prefKey, currentVersion);
    edit.commit();
  }

}
