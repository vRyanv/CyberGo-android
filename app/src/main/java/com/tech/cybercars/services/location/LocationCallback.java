package com.tech.cybercars.services.location;

import android.location.Location;
import android.os.Bundle;

public interface LocationCallback {
    public interface Change{
        void OnChanged(Location location);
    }

    public interface Provider{
        void OnEnabled(String provider);
        void OnDisabled(String provider);
    }

    public interface Status{
        void OnChange(String provider, int status, Bundle extras);
    }
}
