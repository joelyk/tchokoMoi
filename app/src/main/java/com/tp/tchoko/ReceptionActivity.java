package com.tp.tchoko;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * TchokoMoi — ReceptionActivity
 * Écran récepteur : affiche le transfert reçu via Bluetooth.
 *
 * Jour 2 : layout + navigation ← aujourd'hui
 * Jour 3 : thread d'écoute Bluetooth → à venir
 */
public class ReceptionActivity extends AppCompatActivity {

    // ══════════════════════════════════════════════════════
    // TODO — Exercice 6a (Jour 2) : déclarer les 4 widgets
    // private TextView tvStatut, tvMontantRecu, tvMessageRecu;
    // private Button btnRetour;
    // ══════════════════════════════════════════════════════

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        // ══════════════════════════════════════════════════
        // TODO — Exercice 6b (Jour 2) : lier les 4 vues
        // tvStatut      = findViewById(R.id.tvStatut);
        // tvMontantRecu = findViewById(R.id.tvMontantRecu);
        // tvMessageRecu = findViewById(R.id.tvMessageRecu);
        // btnRetour     = findViewById(R.id.btnRetour);
        // ══════════════════════════════════════════════════

        // ══════════════════════════════════════════════════
        // TODO — Exercice 6c (Jour 2) : btnRetour → finish()
        // btnRetour.setOnClickListener(v -> finish());
        // ══════════════════════════════════════════════════

        /*
         * TODO — Jour 3 : ajouter le thread d'écoute Bluetooth
         * bluetoothHelper.attendreConnexion(this::afficherTransfertRecu);
         */
    }

    // ─────────────────────────────────────────────────────────
    /**
     * Appelé par le thread Bluetooth (Jour 3) quand un transfert arrive.
     * Format attendu : "montant|devise|message"
     * Exemple       : "200|XAF FCFA|Remboursement repas"
     */
    private void afficherTransfertRecu(String donnees) {
        // TODO — Jour 3 : parser donnees et mettre à jour tvStatut / tvMontantRecu / tvMessageRecu
        // runOnUiThread(() -> { ... });
    }
}
