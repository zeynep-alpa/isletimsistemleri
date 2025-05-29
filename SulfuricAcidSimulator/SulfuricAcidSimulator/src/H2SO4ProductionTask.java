public class H2SO4ProductionTask implements Runnable {

    private final int id; // Bu üretim döngüsüne ait molekül numarası
    private final MoleculeFactory factory; // Ortak üretim kaynağı

    public H2SO4ProductionTask(int id, MoleculeFactory factory) {
        this.id = id;
        this.factory = factory;
    }

    @Override
    public void run() {

        // Bu üretime ait ID’yi fabrikaya bildir ve başlık yazdır
        factory.setCurrentMoleculeId(id);
        factory.logHeader(id);

        try {
            // S üretimi
            ProducerThread s = new ProducerThread(id, factory, "S");
            s.start(); s.join();

            // O2 (1) üretimi
            ProducerThread o2_1 = new ProducerThread(id, factory, "O2-1");
            o2_1.start(); o2_1.join();

            // SO2 üretimi (S + O2)
            ProducerThread so2 = new ProducerThread(id, factory, "SO2");
            so2.start(); so2.join();

            // O2 (2) üretimi
            ProducerThread o2_2 = new ProducerThread(id, factory, "O2-2");
            o2_2.start(); o2_2.join();

            // SO3 üretimi (SO2 + O2)
            ProducerThread so3 = new ProducerThread(id, factory, "SO3");
            so3.start(); so3.join();

            // H (1) üretimi
            ProducerThread h1 = new ProducerThread(id, factory, "H-1");
            h1.start(); h1.join();

            // H (2) üretimi
            ProducerThread h2 = new ProducerThread(id, factory, "H-2");
            h2.start(); h2.join();

            // O2 (3) üretimi (tek oksijen atomu olarak düşünülüyor)
            ProducerThread o2_3 = new ProducerThread(id, factory, "O2-3");
            o2_3.start(); o2_3.join();

            // H2O üretimi (2H + O)
            ProducerThread h2o = new ProducerThread(id, factory, "H2O");
            h2o.start(); h2o.join();

            // Son adım: H2SO4 üretimi (SO3 + H2O)
            ProducerThread h2so4 = new ProducerThread(id, factory, "H2SO4");
            h2so4.start(); h2so4.join();

        } catch (InterruptedException e) {
            // Thread kesintisi durumunda hata çıktısı
            e.printStackTrace();
        }
    }
}
