public class Main {
    public static void main(String[] args) {

        // Simülasyon başlangıcını konsola yazdır
        System.out.println("Simülasyon başlatılıyor...\n");

        // Ortak Molekül Fabrikası nesnesi oluşturuluyor
        MoleculeFactory factory = new MoleculeFactory();

        // 1000 adet H2SO4 molekülü üretilecek
        for (int i = 1; i <= 1000; i++) {
            // Her molekül üretimi için ayrı bir üretim görevi (thread) başlatılıyor
            Thread t = new Thread(new H2SO4ProductionTask(i, factory));
            t.start();
            try {
                // Thread'in tamamlanması bekleniyor, sıralı üretim sağlanıyor
                t.join();
            } catch (InterruptedException e) {
                // Her ihtimale karşı hata kontrolü
                e.printStackTrace();
            }
        }

        // Simülasyon bitişini konsola yazdır
        System.out.println("\nSimülasyon tamamlandı.");
    }
}
