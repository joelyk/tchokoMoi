package com.tp.tchoko;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * TchokoMoi — MainActivity
 * Écran principal : formulaire d'envoi + historique
 *
 * Jour 1 : Interface utilisateur          ✅
 * Jour 2 : SharedPreferences + Intent     ← aujourd'hui
 * Jour 3 : Bluetooth réel                 → à venir
 */
public class MainActivity extends AppCompatActivity {

    // ── Widgets ──────────────────────────────────────────────
    private TextView tvSolde;
    private EditText etMontant, etMessage, etDestinataire;
    private Spinner  spinnerDevise;
    private Button   btnEnvoyer, btnRechercher, btnReception;
    private ListView lvHistorique;

    // ── Données ───────────────────────────────────────────────
    private double solde = 500.00;
    private ArrayAdapter<String> historiqueAdapter;
    private ArrayList<String>   listeHistorique = new ArrayList<>();

    // ── SharedPreferences ─────────────────────────────────────
    private static final String PREFS_NOM = "tchoko_prefs";

    // ─────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Lier les vues XML ↔ Java
        tvSolde        = findViewById(R.id.tvSolde);
        etMontant      = findViewById(R.id.etMontant);
        etMessage      = findViewById(R.id.etMessage);
        spinnerDevise  = findViewById(R.id.spinnerDevise);
        btnEnvoyer     = findViewById(R.id.btnEnvoyer);
        btnRechercher  = findViewById(R.id.btnRechercher);
        lvHistorique   = findViewById(R.id.lvHistorique);
        etDestinataire = findViewById(R.id.etDestinataire);

        // ══════════════════════════════════════════════════════
        // TODO — Exercice 5 (Jour 2) : lier btnReception
        // btnReception = findViewById(R.id.btnReception);
        // ══════════════════════════════════════════════════════

        // 2. Initialiser le Spinner des devises
        String[] devises = { "XAF FCFA", "EUR", "USD", "GBP" };
        ArrayAdapter<String> deviseAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            devises
        );
        spinnerDevise.setAdapter(deviseAdapter);

        // 3. Initialiser le ListView
        historiqueAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            listeHistorique
        );
        lvHistorique.setAdapter(historiqueAdapter);

        // 4. Listeners boutons
        btnRechercher.setOnClickListener(v -> {
            Toast.makeText(this, "Recherche Bluetooth...", Toast.LENGTH_SHORT).show();
            btnEnvoyer.setEnabled(true);
        });

        btnEnvoyer.setOnClickListener(v -> validerEtEnvoyer());

        // ══════════════════════════════════════════════════════
        // TODO — Exercice 5 (Jour 2) : OnClickListener btnReception
        // Intent intent = new Intent(this, ReceptionActivity.class);
        // startActivity(intent);
        // ══════════════════════════════════════════════════════
    }

    // ─────────────────────────────────────────────────────────
    @Override
    protected void onResume() {
        // ══════════════════════════════════════════════════════
        // TODO — Exercice 3a (Jour 2) : appeler super + chargerDonnees()
        // ══════════════════════════════════════════════════════
        super.onResume();
    }

    // ─────────────────────────────────────────────────────────
    @Override
    protected void onPause() {
        // ══════════════════════════════════════════════════════
        // TODO — Exercice 3b (Jour 2) : appeler super + sauvegarderDonnees()
        // ══════════════════════════════════════════════════════
        super.onPause();
    }

    // ─────────────────────────────────────────────────────────
    /**
     * Valide la saisie et affiche une confirmation AlertDialog.
     */
    private void validerEtEnvoyer() {
        String montantStr = etMontant.getText().toString().trim();

        // Validation 1 — champ vide
        if (montantStr.isEmpty()) {
            Toast.makeText(this, "Saisis un montant !", Toast.LENGTH_SHORT).show();
            return;
        }

        double montant = Double.parseDouble(montantStr);
        String devise  = spinnerDevise.getSelectedItem().toString();
        String message = etMessage.getText().toString().trim();
        String nom     = etDestinataire.getText().toString().trim();

        // Validation 2 — montant négatif ou nul
        if (montant <= 0) {
            Toast.makeText(this, "Le montant doit être positif !", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validation 3 — solde insuffisant
        if (montant > solde) {
            Toast.makeText(this, "Solde insuffisant !", Toast.LENGTH_LONG).show();
            return;
        }

        // Validation 4 — montant maximum
        if (montant > 50000) {
            Toast.makeText(this, "Montant maximum autorisé : 50 000", Toast.LENGTH_LONG).show();
            return;
        }

        // Confirmation AlertDialog
        String titre = nom.isEmpty() ? "Confirmer le tchoko" : "Tchoko pour " + nom;

        new AlertDialog.Builder(this)
            .setTitle(titre)
            .setMessage("Envoyer " + (int)montant + " " + devise + " ?"
                + (message.isEmpty() ? "" : "\nMessage : " + message))
            .setPositiveButton("Envoyer", (d, w) -> effectuerTransfert(montant, devise, message))
            .setNegativeButton("Annuler", null)
            .show();
    }

    // ─────────────────────────────────────────────────────────
    /**
     * Effectue le transfert localement.
     * Jour 3 : remplacer par l'envoi Bluetooth réel.
     */
    private void effectuerTransfert(double montant, String devise, String message) {
        solde -= montant;
        tvSolde.setText(String.format("Solde : %.0f %s", solde, devise));

        String entree = "Envoyé : " + (int)montant + " " + devise;
        if (!message.isEmpty()) entree += " — " + message;
        listeHistorique.add(0, entree);
        historiqueAdapter.notifyDataSetChanged();

        etMontant.setText("");
        etMessage.setText("");

        Toast.makeText(this, "Tchoko envoyé ! 💸", Toast.LENGTH_LONG).show();

        /*
         * TODO — Jour 3 : remplacer ce Toast par l'envoi Bluetooth
         * bluetoothHelper.envoyer(montant + "|" + devise + "|" + message);
         */
    }

    // ─────────────────────────────────────────────────────────
    private void sauvegarderDonnees() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NOM, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // ══════════════════════════════════════════════════════
        // TODO — Exercice 1a (Jour 2) : sauvegarder le solde
        // Clé : "solde" | le solde est un double → caster en float
        // ══════════════════════════════════════════════════════

        // ══════════════════════════════════════════════════════
        // TODO — Exercice 2a (Jour 2) : sauvegarder listeHistorique
        // Joindre avec "||" → String.join("||", listeHistorique)
        // Clé : "historique"
        // ══════════════════════════════════════════════════════

        editor.apply();
    }

    // ─────────────────────────────────────────────────────────
    private void chargerDonnees() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NOM, MODE_PRIVATE);

        // ══════════════════════════════════════════════════════
        // TODO — Exercice 1b (Jour 2) : charger le solde
        // Clé : "solde" | Valeur par défaut : 500.0f
        // Mettre à jour tvSolde après la lecture
        // ══════════════════════════════════════════════════════

        // ══════════════════════════════════════════════════════
        // TODO — Exercice 2b (Jour 2) : charger listeHistorique
        // 1. Lire String "historique" (défaut : "")
        // 2. listeHistorique.clear()
        // 3. Si non vide : split("\\|\\|") et ajouter chaque partie
        // 4. historiqueAdapter.notifyDataSetChanged()
        // ══════════════════════════════════════════════════════
    }
}
