import java.util.concurrent.Semaphore;

public class MoleculeFactory {

    // Üretim adımları için gerekli semaforlar (senkronizasyon kontrolü)
    private final Semaphore sAvailable = new Semaphore(0);     // S üretildiğinde serbest bırakılır
    private final Semaphore o2Available = new Semaphore(0);    // O2 molekülü üretimi için
    private final Semaphore so2Ready = new Semaphore(0);       // SO2 üretimi tamamlandığında
    private final Semaphore so3Ready = new Semaphore(0);       // SO3 üretimi tamamlandığında
    private final Semaphore hAvailable = new Semaphore(0);     // H atomları için
    private final Semaphore oAvailable = new Semaphore(0);     // H2O için oksijen atomu
    private final Semaphore h2oReady = new Semaphore(0);       // H2O üretimi tamamlandığında

    // Üretilen element/moleküller için genel sayaç (NUM=...)
    private int counter = 0;

    // Hangi molekülün üretildiğini takip etmek için ID (sadece H2SO4 için kullanılıyor)
    private int currentMoleculeId = 0;

    // Şu anda hangi H2SO4 molekülü üretildi, set edilir
    public synchronized void setCurrentMoleculeId(int id) {
        this.currentMoleculeId = id;
    }

    // Her üretim adımı için sıradaki numarayı döner
    private synchronized int nextNum() {
        return ++counter;
    }

    // Her H2SO4 molekülü üretimi öncesinde başlık yazdırır
    public synchronized void logHeader(int moleculeId) {
        System.out.println("\n--- Molekül " + moleculeId + " Üretimi ---");
    }

    // Üretim sırasında her adımı konsola yazdırır
    private synchronized void log(String message) {
        System.out.println("NUM:" + nextNum() + " " + message);
    }

    // S üretimi (thread çalıştığında çağrılır)
    public void createSulfur() {
        log("S Created");
        sAvailable.release(); // SO2 üretimi buna bağlı
    }

    // O2 üretimi (id bilgisiyle ayrı ayrı gösterilir)
    public void createOxygen(int id) {
        log("O2 (" + id + ") Created");
        o2Available.release();  // SO2 veya SO3 için
        oAvailable.release();   // H2O için de aynı O2 kullanılabilir
    }

    // H atomu üretimi
    public void createHydrogen(int id) {
        log("H (" + id + ") Created");
        hAvailable.release();
    }

    // SO2 üretimi için: 1 S + 1 O2 gerekir
    public void createSO2() {
        try {
            sAvailable.acquire();      // S beklenir
            o2Available.acquire();     // O2 beklenir
            log("SO2 Created");
            so2Ready.release();        // SO3 üretimi başlayabilir
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // SO3 üretimi için: SO2 + O2 gerekir
    public void createSO3() {
        try {
            so2Ready.acquire();        // SO2 hazır mı?
            o2Available.acquire();     // O2 beklenir
            log("SO3 Created");
            so3Ready.release();        // H2SO4 üretimi için hazır
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // H2O üretimi için: 2 H + 1 O gerekir (O2 molekülünden 1 atom gibi düşünüldü)
    public void createWater() {
        try {
            hAvailable.acquire();      // 1. H
            hAvailable.acquire();      // 2. H
            oAvailable.acquire();      // O
            log("H2O Created");
            h2oReady.release();        // H2SO4 için hazır
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // H2SO4 üretimi için: SO3 + H2O gerekir
    public void createH2SO4() {
        try {
            so3Ready.acquire();        // SO3 beklenir
            h2oReady.acquire();        // H2O beklenir

            // Sadece H2SO4 için özel çıktı formatı
            System.out.println("Molecule " + currentMoleculeId + ": H2SO4 Created");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
