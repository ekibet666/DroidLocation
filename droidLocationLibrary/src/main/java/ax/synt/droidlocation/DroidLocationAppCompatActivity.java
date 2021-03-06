package ax.synt.droidlocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public abstract class DroidLocationAppCompatActivity extends AppCompatActivity implements DroidLocationListener {
    private DroidLocationDelegate droidLocationDelegate;

    private static final int ENABLE_LOCATION_SERVICES_REQUEST = 101;

    protected Location getLastKnownLocation() {
        return droidLocationDelegate.getLastKnownLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        droidLocationDelegate = new DroidLocationDelegate(this,this);
        droidLocationDelegate.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //droidLocationDelegate.showLocationSettingDialog(requestCode);
        droidLocationDelegate.doWeStartLocationService(resultCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        droidLocationDelegate.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        droidLocationDelegate.onDestroy();
    }

    protected void requestLocationUpdates(DroidLocationRequest droidLocationRequest) {
        droidLocationDelegate.requestLocationUpdates(droidLocationRequest, ENABLE_LOCATION_SERVICES_REQUEST);
    }


    protected void requestSingleLocationFix(DroidLocationRequest droidLocationRequest) {
        droidLocationDelegate.requestSingleLocationFix(droidLocationRequest, ENABLE_LOCATION_SERVICES_REQUEST);
    }

    protected void requestAddressServices(Location location){
        droidLocationDelegate.startIntentService(location);
    }

    protected void stopLocationUpdates() {
        droidLocationDelegate.stopLocationUpdates();
    }

    @Deprecated
    // warning: use the one for intent service
    public static String getAddress(Context context, Double latitude, Double longitude, boolean country, boolean fullAddress) {
        String add = "";
        Geocoder geoCoder = new Geocoder(((Activity) context).getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                if (country) {
                    add = addresses.get(0).getCountryName();
                } else if (fullAddress) {
                    add = addresses.get(0).getFeatureName() + "," + addresses.get(0).getSubLocality() + "," + addresses.get(0).getSubAdminArea() + "," + addresses.get(0).getPostalCode() + "," + addresses.get(0).getCountryName();
                } else {
                    add = addresses.get(0).getLocality();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return add.replaceAll(",null", "");
    }

}