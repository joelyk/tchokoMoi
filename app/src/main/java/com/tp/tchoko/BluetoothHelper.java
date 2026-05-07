package com.tp.tchoko;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * TchokoMoi — BluetoothHelper
 * Encapsule toute la logique Bluetooth de l'application.
 *
 * TODO — Jour 3 complet
 *
 * Méthodes à implémenter :
 *   - isBluetoothDisponible()
 *   - isBluetoothActive()
 *   - getAppareilsAssocies()
 *   - connecterA(BluetoothDevice)
 *   - envoyer(String)
 *   - attendreConnexion()
 *   - fermer()
 */
public class BluetoothHelper {

    // UUID standard SPP (Serial Port Profile) — ne pas modifier
    private static final UUID MY_UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter       bluetoothAdapter;
    private BluetoothSocket        bluetoothSocket;
    private BluetoothServerSocket  serverSocket;

    public BluetoothHelper() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    // TODO — Jour 3 : implémenter les méthodes ci-dessous

}
