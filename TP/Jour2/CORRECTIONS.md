# TchokoMoi — Corrections Jour 2
## Solutions complètes + réponses aux questions de cours

> ⚠️ Ce document est distribué **après** la séance du Jour 2.  
> Branche `solution` maintenant accessible sur https://github.com/joelyk/tchokoMoi

---

## ✅ Exercice 1 — Sauvegarder et charger le solde

### 1a. `sauvegarderDonnees()` — solde

```java
editor.putFloat("solde", (float) solde);
```

### 1b. `chargerDonnees()` — solde

```java
solde = prefs.getFloat("solde", 500.0f);
tvSolde.setText(String.format("Solde : %.0f XAF", solde));
```

### Pourquoi le cast `(float)` ?

`SharedPreferences` ne supporte pas `double` directement. Le cast `(float) solde` convertit le `double` en `float` (légère perte de précision au-delà de 7 décimales, imperceptible pour un solde affiché sans décimales). Le cast inverse se fait automatiquement à la lecture.

---

## ✅ Exercice 2 — Sauvegarder et charger l'historique

### 2a. Sauvegarde dans `sauvegarderDonnees()`

```java
editor.putString("historique", String.join("||", listeHistorique));
```

### 2b. Chargement dans `chargerDonnees()`

```java
listeHistorique.clear();
String h = prefs.getString("historique", "");
if (!h.isEmpty()) {
    for (String ligne : h.split("\\|\\|")) {
        listeHistorique.add(ligne);
    }
}
historiqueAdapter.notifyDataSetChanged();
```

### Pourquoi `clear()` avant d'ajouter ?

Sans `clear()`, à chaque appel de `chargerDonnees()` (rotation, retour depuis ReceptionActivity...) les éléments s'accumulent en double dans la liste. Vider d'abord garantit que la liste contient exactement ce qui est sauvegardé.

---

## ✅ Exercice 3 — Cycle de vie

```java
@Override
protected void onResume() {
    super.onResume();      // toujours en premier
    chargerDonnees();
}

@Override
protected void onPause() {
    super.onPause();       // toujours en premier
    sauvegarderDonnees();
}
```

### Pourquoi `onResume()` et non `onCreate()` ?

`onCreate()` n'est appelé qu'une seule fois à la création initiale de l'Activity. Si l'utilisateur revient depuis `ReceptionActivity` (après avoir appuyé sur "← Retour"), `onCreate()` **n'est pas rappelé**, mais `onResume()` l'est. Utiliser `onResume()` garantit que les données sont toujours fraîches à chaque retour sur l'écran.

---

## ✅ Exercice 4 — Layout `activity_reception.xml`

```xml
<TextView
    android:id="@+id/tvStatut"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="En attente de transfert..."
    android:textSize="18sp"
    android:gravity="center"
    android:layout_marginBottom="32dp"/>

<TextView
    android:id="@+id/tvMontantRecu"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text=""
    android:textSize="48sp"
    android:textStyle="bold"
    android:textColor="#1E8449"
    android:gravity="center"/>

<TextView
    android:id="@+id/tvMessageRecu"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text=""
    android:textSize="16sp"
    android:textStyle="italic"
    android:textColor="#666666"
    android:gravity="center"
    android:layout_marginTop="8dp"/>

<Button
    android:id="@+id/btnRetour"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="← Retour"
    android:layout_marginTop="48dp"/>
```

---

## ✅ Exercice 5 — Intent vers ReceptionActivity

```java
btnReception.setOnClickListener(v -> {
    Intent intent = new Intent(this, ReceptionActivity.class);
    startActivity(intent);
});
```

### Pourquoi `ReceptionActivity.class` et pas `new ReceptionActivity()` ?

Android gère lui-même le cycle de vie des Activities — on ne les instancie jamais directement. On passe la **classe** à l'Intent, et Android crée l'instance en appliquant le cycle de vie complet (`onCreate`, `onStart`, `onResume`...). Créer une instance manuellement bypasse tout ce mécanisme et provoquerait un crash.

---

## ✅ Exercice 6 — `ReceptionActivity.java` complet

```java
public class ReceptionActivity extends AppCompatActivity {

    private TextView tvStatut, tvMontantRecu, tvMessageRecu;
    private Button   btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        tvStatut      = findViewById(R.id.tvStatut);
        tvMontantRecu = findViewById(R.id.tvMontantRecu);
        tvMessageRecu = findViewById(R.id.tvMessageRecu);
        btnRetour     = findViewById(R.id.btnRetour);

        btnRetour.setOnClickListener(v -> finish());
    }
}
```

### Différence `finish()` vs `startActivity(MainActivity.class)`

| `finish()` | `startActivity(MainActivity.class)` |
|-----------|--------------------------------------|
| Dépile l'Activity courante | Crée une **nouvelle** instance de MainActivity |
| Retourne au contexte précédent | Empile une 2ème MainActivity par-dessus |
| Mémoire libérée | Mémoire doublée, état potentiellement dupliqué |
| ✅ Solution correcte | ❌ À éviter pour "revenir en arrière" |

---

## ✅ Questions bilan — Réponses complètes

### Q1 — `apply()` vs `commit()` ?

| | `apply()` | `commit()` |
|--|-----------|------------|
| Exécution | Asynchrone (en arrière-plan) | Synchrone (bloque le thread) |
| Retour | Aucun | `boolean` (succès ou échec) |
| Thread UI | Ne bloque pas ✅ | Bloque si lent ⚠️ |
| **Quand utiliser** | **Toujours (usage normal)** | Seulement si on a besoin du résultat immédiat |

**Préférer `apply()`** dans 99% des cas — plus performant et sans risque de bloquer l'interface.

### Q2 — Pourquoi `onResume()` plutôt que `onCreate()` ?

`onResume()` est appelé **à chaque fois que l'Activity redevient active** : à la création initiale, après une rotation, et au retour depuis une autre Activity. `onCreate()` n'est appelé qu'une fois. En utilisant `onResume()`, les données sont toujours fraîches même après un retour depuis `ReceptionActivity`.

### Q3 — `finish()` vs bouton Retour physique ?

`finish()` et le bouton Retour physique font **exactement la même chose** : dépiler l'Activity courante. La différence : `finish()` est appelé depuis le code (on peut le déclencher conditionnellement, dans un listener...), le bouton Retour physique est déclenché par l'utilisateur. On peut surcharger `onBackPressed()` pour personnaliser ce comportement.

### Q4 — 2 données à ne pas stocker dans SharedPreferences en production

1. **Mots de passe / tokens d'authentification** → utiliser Android Keystore ou EncryptedSharedPreferences
2. **Données bancaires / numéros de carte** → jamais en local, toujours côté serveur sécurisé

### Q5 — Back stack et startActivity() sans fin

La **back stack** est une pile d'Activities gérée par Android. Chaque `startActivity()` empile une nouvelle Activity. Sans `finish()`, la pile grossit indéfiniment → **fuite mémoire** et comportement du bouton Retour imprévisible (l'utilisateur doit appuyer plusieurs fois pour vraiment quitter).

---

## 🔭 Aperçu du Jour 3

```
Jour 3 — Bluetooth réel
────────────────────────────────────────
✦ BluetoothAdapter    → vérifier et activer le BT
✦ BluetoothDevice     → lister les appareils associés
✦ BluetoothSocket     → connexion RFCOMM (canal série)
✦ OutputStream        → envoyer les données
✦ Thread secondaire   → écouter en arrière-plan
✦ runOnUiThread       → mettre à jour l'UI depuis un Thread
```

> 💡 Le Bluetooth Android bloque le thread — tout doit se passer dans un **Thread secondaire**. C'est le grand défi du Jour 3.
