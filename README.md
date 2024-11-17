Bu repo, Hepsiburada, Pazarama ve LC Waikiki gibi üç farklı e-ticaret sitesi için temel ürün arama, sepete ekleme ve sepetten silme testlerini içermektedir. Testler, DataProvider kullanılarak Excel dosyalarından alınan verilere dayalı olarak çalıştırılır. Kullanıcı dostu bir yaklaşım sağlamak için aşağıda repo yapısı, testlerin çalışma şekli ve kullanım yönergeleri detaylandırılmıştır.


## 1. Testlerin Yapısı
Testler, üç farklı e-ticaret sitesinde ortak işlevleri hedef alır:

Ürün Arama: Girilen anahtar kelimeye uygun ürünlerin aranması.
Sepete Ekleme: Bulunan ürünlerin sepete eklenmesi.
Sepetten Silme: Sepetteki ürünlerin kaldırılması.

## 2. Excel Veri Yönetimi
Excel Dosyası: Test verileri bir Excel dosyasından alınır. İlk satırda başlıklar bulunur; başlık hariç her satır, ürün bilgilerini içerir.
Boş Satırlar: Excel dosyasındaki boş satırlar otomatik olarak atlanır. Sadece dolu satırlar işlenir.
DataProvider Kullanımı: Testler, DataProvider aracılığıyla Excel'den verileri alır ve ilgili testleri çalıştırır.

## 3. HB Class Yapısı
HepsiBurada e-ticaret sitesi için kullanılan class yapısı şu şekildedir:

Base Class (a):

Tüm testlerin temelini oluşturur.
Her bir !b classının çalıştırılabilmesi için gerekli altyapıyı sağlar.
Her e-ticaret sitesi için zorunludur.

Opsiyonel Class (b):

Base Class üzerine eklenir ve alternatif test senaryolarını çalıştırır.
Dikkat: Birden fazla !b class aynı anda çalıştırılabilir, ancak çakışma riski taşır. Önceden kontrol edilmeden çalıştırılmamalıdır.

Çalıştırılabilir Class (c):

Hem Base Class işlevlerini hem de !b class özelliklerini işler.
Sadece !c class çalıştırılarak tüm testlerin tamamlanması sağlanabilir.
Test Çalıştırma: Testler !c class kullanılarak başlatılır.

## 4. Test Sonuçları ve Raporlama
Extent Report: Test sonuçları, görselleştirilmiş bir şekilde Extent Report kullanılarak raporlanır.
Her bir e-ticaret sitesi için ayrı test senaryoları sonuçları detaylı olarak incelenebilir.

## 5. Testlerin Çalıştırılması

Hepsiburada Testleri:
a (Base Class) ile başlar.
İsteğe bağlı b class'lar eklenebilir.
c class çalıştırılarak sonuç alınır.

Pazarama ve LC Waikiki Testleri:
Daha basit bir yapıya sahiptir.
Tek bir sayfa üzerinden çalıştırılabilir ve sonuçlar raporlanır.

## 6. Notlar
Class Önceliği: !c class kullanılarak diğer class'ların manuel olarak başlatılmasına gerek kalmadan testler tamamlanabilir.
Veri Doğruluğu: Excel dosyasındaki verilerin doğru formatta olduğundan emin olun.
Çakışma Riski: Aynı anda birden fazla !b class çalıştırılmadan önce gerekli testler yapılmalıdır.

## 7. Kurulum ve Çalıştırma
Adım 1: Gerekli bağımlılıkları yükleyin. (Örneğin: Maven, TestNG, Extent Report vb.)
Adım 2: Excel dosyasını uygun bir dizine yerleştirin.
Adım 3: Testleri başlatmak için ilgili !c class'ı çalıştırın.
Adım 4: Test sonuçlarını Extent Report ile inceleyin.

## 8. Geliştirme ve Katkı
Bu repo açık kaynaklıdır ve katkıya açıktır. Yeni özellikler eklemek veya hata raporları bildirmek için Pull Request gönderebilirsiniz.
