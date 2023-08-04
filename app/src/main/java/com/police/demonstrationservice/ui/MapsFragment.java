package com.police.demonstrationservice.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.police.demonstrationservice.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private int code = 0;

    boolean End = false;
    UiSettings uiSettings;


    String SearchedAddress = null;

    Bundle result = new Bundle();


    private LatLng M_Location;
    private LatLng D_Location;


    private Marker marker;

    LatLng temp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        return inflater.inflate(R.layout.fragment_maps2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

    }

    public boolean setCode() {

        if (code == 0) {
            code = 1;
            result.putString("M_add", getM());
            Toast.makeText(requireContext(), "현재 위치 설정 완료", Toast.LENGTH_LONG).show();

            return true;

        } else if (code == 1) {
            if (D_Location != null) {
                Location loc1 = new Location("");
                loc1.setLatitude(M_Location.latitude);
                loc1.setLongitude(M_Location.longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(D_Location.latitude);
                loc2.setLongitude(D_Location.longitude);

                result.putString("D_add", Integer.toString((int) loc1.distanceTo(loc2)));
                ((Current_Place_M) getActivity()).DataHandle(result);
                Toast.makeText(requireContext(), "집회 위치 설정 완료", Toast.LENGTH_LONG).show();
                return true;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("현재 위치와 집회장소가 같습니다.");
                builder.setMessage("진행하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.putString("D_add", "0");
                        ((Current_Place_M) getActivity()).DataHandle(result);
                        Toast.makeText(requireContext(), "집회 위치 설정 완료", Toast.LENGTH_LONG).show();
                        ((Current_Place_M) getActivity()).OkFinsih();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.create().show();

            }

        }
        return End;
    }


    public String getM() {
        return getCurrentAddress(M_Location.latitude, M_Location.longitude);
    }


    @Override
    public void onResume() {
        super.onResume();
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    };


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);

        map.setOnMapClickListener(latLng -> {
            if (marker != null) {
                marker.remove();
            }
            MarkerOptions mOptions = new MarkerOptions();
            temp = new LatLng(latLng.latitude, latLng.longitude);
            mOptions.position(temp).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            marker = map.addMarker(mOptions); //맵상에 마커 하나만 존재하기 위해서 저장합니다.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, googleMap.getCameraPosition().zoom));
            ((Current_Place_M) getActivity()).setText(getCurrentAddress(temp.latitude, temp.longitude));

            if (code == 0) {
                M_Location = temp;
            }

            if (code == 1) {
                D_Location = temp;
            }


        });
    }

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                        Boolean fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                        Boolean coarseLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_COARSE_LOCATION, false);
                        if (fineLocationGranted != null && fineLocationGranted) {
                            // Precise location access granted.
                            startLocationUpdates();
                        } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            // Only approximate location access granted.
                            startLocationUpdates();
                        } else {
                            // No location access granted.
                            requireActivity().finish();
                            Toast.makeText(requireContext(), "권한요청이 거부되어 일부 서비스를 이용 할 수 없습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
            );

    private void startLocationUpdates() {
        // 위치 업데이트를 시작합니다.
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                for (int i = 0; i < 1; i++) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, gpsLocationListener);
                }

            }
            updateMapToCurrentLocation(); //현재 위치로 맵에서 이동
        }
    }

    private void stopLocationUpdates() {
        // 위치 업데이트를 중지합니다.
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void updateMapToCurrentLocation() {
        // 현재 위치를 받아와 맵으로 이동합니다.
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                if(SearchedAddress == null){
                    LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    M_Location = currentLatLng;
                    Log.d("테스트", "마커 찍기");
                    marker = googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
                    ((Current_Place_M) getActivity()).setText(getCurrentAddress(currentLatLng.latitude, currentLatLng.longitude));
                }else {
                    getCoordinatesFromAddress(SearchedAddress);
                }

            }
        }
    }


    public void  setSearchedAddress(String data){
        SearchedAddress = data;


        getCoordinatesFromAddress(data);;



    }

    private void getCoordinatesFromAddress(String address) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);

            if (addressList != null && !addressList.isEmpty()) {
                Address location = addressList.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng temp = new LatLng(latitude,longitude);
                if(marker != null){
                    marker.remove();
                }
                if(code == 0){
                    M_Location = temp;
                }else {
                    D_Location = temp;
                }
                MarkerOptions mOptions = new MarkerOptions();
                mOptions.position(temp).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                marker = googleMap.addMarker(mOptions); //맵상에 마커 하나만 존재하기 위해서 저장합니다.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, googleMap.getCameraPosition().zoom));


            } else {
                Log.e("TAG", "주소를 찾을 수 없습니다.");
            }

        } catch (IOException e) {
            Log.e("TAG", "Error: " + e.getMessage());
        }
    }


    private String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                StringTokenizer st = new StringTokenizer(address.getAddressLine(0));
                st.nextToken();
                while (st.hasMoreElements()) {
                    stringBuilder.append(st.nextToken());
                    stringBuilder.append(" ");
                }


                return stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
