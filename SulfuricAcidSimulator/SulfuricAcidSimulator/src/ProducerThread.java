public class ProducerThread extends Thread {

    // Üretilen molekülün ID'si (kaçıncı üretim)
    private final int moleculeId;

    // Ortak MoleculeFactory nesnesi, tüm üretim işlemleri buradan yürütülüyor
    private final MoleculeFactory factory;

    // Hangi element veya molekülün üretileceği (örneğin: "S", "O2-1", "H2SO4")
    private final String type;

    // Yapıcı metod, üretilecek molekül tipi ve bağlı olduğu fabrikayı alır
    public ProducerThread(int moleculeId, MoleculeFactory factory, String type) {
        this.moleculeId = moleculeId;
        this.factory = factory;
        this.type = type;
    }

    // Thread çalıştırıldığında, üretim tipine göre ilgili üretim metodu çağrılır
    @Override
    public void run() {
        switch (type) {

            // S atomu üretimi
            case "S":
                factory.createSulfur();
                break;

            // SO2 molekülü üretimi
            case "SO2":
                factory.createSO2();
                break;

            // SO3 molekülü üretimi
            case "SO3":
                factory.createSO3();
                break;

            // H2O molekülü üretimi
            case "H2O":
                factory.createWater();
                break;

            // H2SO4 son molekülün üretimi
            case "H2SO4":
                factory.createH2SO4();
                break;

            default:
                // O2 üretimi için: "O2-1", "O2-2" gibi adlandırmalarla çağrılır
                if (type.startsWith("O2")) {
                    int id = Integer.parseInt(type.split("-")[1]);
                    factory.createOxygen(id);
                }

                // H atomları için: "H-1", "H-2" gibi çağrılar yapılır
                else if (type.startsWith("H")) {
                    int id = Integer.parseInt(type.split("-")[1]);
                    factory.createHydrogen(id);
                }
                break;
        }
    }
}
